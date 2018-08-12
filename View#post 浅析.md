## View#post 浅析

之前的文章里写到过，我们在 onCreate() 和 onResume() 方法中无法获取 View 的宽高信息，但在平时开发中，我们经常会用到 View#post 来进行宽高信息的获取，具体代码如下：

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    View view = findViewById(R.id.test);
    view.post(new Runnable() {
        @Override
        public void run() {
            // 可以正常获取到 View 的宽高信息
            Log.e("Test", "view.post ---- > " + view.getHeight());
        }
    });
}
```

具体的实现原理是怎样的呢？这里我们以 API 26 为例，来尝试解答一下这个问题。

实际上，Android 系统以 API 24 为界，之前之后的版本，对此处的实现有细微的差别，具体的改动以及原因在后文会一一给出分析。

先来看 View#post 源码，重点注意注释：

```java
public boolean post(Runnable action) {
    // AttachInfo 是 View 的内部类，用来存储一些基本信息
    // 此处可以暂时认为 mAttachInfo 为 null
    final AttachInfo attachInfo = mAttachInfo;
    if (attachInfo != null) {
        // attachInfo 不为空时，转而使用其内部的 Handler 对象操作
        return attachInfo.mHandler.post(action);
    }

    // Postpone the runnable until we know on which thread it needs to run.
    // Assume that the runnable will be successfully placed after attach.
    // 在我们确定当前 Runnable 的目标运行线程之前，先将其推迟执行
    // 假设在 attach 完成之后，此 Runnable 对象会被成功的「placed」（暂且翻译成「放置」）
    // 好好理解一下这个注释，我们继续往下走
    getRunQueue().post(action);
    return true;
}
```

首先，我们来看一下这个看上去很重要的 **mAttachInfo** 是在哪里赋值的：

```java
void dispatchAttachedToWindow(AttachInfo info, int visibility) {
    mAttachInfo = info;
    // Transfer all pending runnables. 转移所有待办任务
    if (mRunQueue != null) {
        mRunQueue.executeActions(info.mHandler);
        mRunQueue = null;
    }
    // 回调方法
    onAttachedToWindow();
}
```

先不在意除了赋值以外的其他操作，我们继续追踪 dispatchAttachedToWindow 方法，发现其最初调用是在 ViewRootImpl#performTraversals 方法。好了，记住这个结论，我们先把它放在一旁。



接下来，我们来看一看这个 **getRunQueue().post()** 又做了什么：

```java
/**
 * 获取一个 RunQueue 对象，用来进行 post 操作
 * Returns the queue of runnable for this view.
 * 注释是：为当前 View 对象返回一个执行队列，记住这个「当前 View 对象」
 */
private HandlerActionQueue getRunQueue() {
    if (mRunQueue == null) {
        mRunQueue = new HandlerActionQueue();
    }
    return mRunQueue;
}
```

很明显，执行 post 方法的是 HandlerActionQueue 对象，那这又是个什么东西：

```java
/**
 * Class used to enqueue pending work from Views when no Handler is attached.
 * 此类用于在当前 View 没有 Handler 依附的时候，将其待完成的任务入队
 */
public class HandlerActionQueue {
    private HandlerAction[] mActions;
    private int mCount;

    // 这个就是我们在外边调用的 post 方法，最终会调用到 postDelayed 方法
    public void post(Runnable action) {
        postDelayed(action, 0);
    }

    // 将传入的 Runnable 对象存入数组中，等待调用
    public void postDelayed(Runnable action, long delayMillis) {
        final HandlerAction handlerAction = new HandlerAction(action, delayMillis);

        synchronized (this) {
            if (mActions == null) {
                mActions = new HandlerAction[4];
            }
            mActions = GrowingArrayUtils.append(mActions, mCount, handlerAction);
            mCount++;
        }
    }

    // 这里才是真的执行方法
    public void executeActions(Handler handler) {
        synchronized (this) {
            final HandlerAction[] actions = mActions;
            for (int i = 0, count = mCount; i < count; i++) {
                final HandlerAction handlerAction = actions[i];
                handler.postDelayed(handlerAction.action, handlerAction.delay);
            }

            mActions = null;
            mCount = 0;
        }
    }
}
```

通过查看 HandlerActionQueue 的源码，我们发现了一个问题：不同于在 onCreate() 直接获取 View 的宽高，我们调用 post 方法，其中的 run 方法并没有被马上执行。

这样就不难解释为什么用这种方式可以获取到宽高了，我们可以猜测一下，这种情况下，一定是 View 完成测量后才执行了这个方法，所以我们才可以拿到宽高信息。

事实上也正是这样的，那么这个方法到底是在什么时候执行的呢？很明显，HandlerActionQueue#executeActions 才是真正完成调用的方法，那这个方法又做了些什么工作呢？

**根据代码可知，该方法接收一个 Handler，然后使用这个 Handler 对当前队列中的所有 Runnable 进行处理，即 post 到该 Handler 的线程中，按照优先级对这些 Runnable 依次进行处理。**

**简单来说，就是传入的 Handler 决定着这些 Runnable 的执行线程。**

接下来，我们来追踪这个方法的调用情况。

![executeActions() 的调用情况](http://p5zd0id9p.bkt.clouddn.com/18-8-12/46072598.jpg)

我们注意到，对于该方法出现了两次调用，一次在 View#dispatchAttachToWindow（就是我们最开始找到的那个方法），另一次是在 ViewRootImpl#performTraversals。

很明显，所有的证据都指向了 performTraversals ，那么下面我们就来重点分析一下这个方法。

如果你了解过 View 的测绘流程，那你对这个方法一定不会陌生，因为这个方法就是 View 绘制流程的起点。

```java
private void performTraversals() {
    
    // host 是根布局 DecorView，用递归的方式一层一层的调用 dispatchAttachedToWindow
    // mAttachInfo 是不是很眼熟，就是最开始 View#post 的第一层判断
    // 这个 mAttachInfo 在 ViewRootImpl 的构造器中初始化的
    // mAttachInfo = new View.AttachInfo(mWindowSession, mWindow, display, this, mHandler, this, context);
    host.dispatchAttachedToWindow(mAttachInfo, 0);
    getRunQueue().executeActions(mAttachInfo.mHandler);
    
}
```

