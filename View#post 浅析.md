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



### post() 里做了什么

先来看 View#post 源码，重点注意注释：

```java
/**
 * Causes the Runnable to be added to the message queue.
 * The runnable will be run on the user interface thread.
 * 将 Runnable 添加到执行队列中，其最终会在 UI 线程中执行
 */
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

首先，明确一点：**Runnable 会在 UI 线程中执行**；

然后，我们来看一下这个看上去很重要的 **mAttachInfo** 是在哪里赋值的：

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



### HandlerActionQueue 是什么

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

这样就不难解释为什么用这种方式可以获取到宽高了。那我们可以猜测一下，这种情况下，一定是 View 完成测量后才执行了这个方法，所以我们才可以拿到宽高信息。

事实上也正是这样的，那么这个方法到底是在什么时候执行的呢？很明显，HandlerActionQueue#executeActions 才是真正完成调用的方法，那这个方法又做了些什么工作呢？

**根据代码可知，该方法接收一个 Handler，然后使用这个 Handler 对当前队列中的所有 Runnable 进行处理，即 post 到该 Handler 的线程中，按照优先级对这些 Runnable 依次进行处理。**

**简单来说，就是传入的 Handler 决定着这些 Runnable 的执行线程。**

接下来，我们来追踪这个方法的调用情况。

![executeActions() 的调用情况](http://p5zd0id9p.bkt.clouddn.com/18-8-12/46072598.jpg)

我们注意到，对于该方法出现了两次调用，一次在 View#dispatchAttachToWindow（就是我们最开始找到的那个方法），另一次是在 ViewRootImpl#performTraversals。

### performTraversals()

很明显，所有的证据都指向了 performTraversals ，那么下面我们就来重点分析一下这个方法。

如果你了解过 View 的测绘流程，那你对这个方法一定不会陌生，因为这个方法就是 View 绘制流程的起点。

```java
private void performTraversals() {
    
    // 此处的 host 是根布局 DecorView，用递归的方式一层一层的调用 dispatchAttachedToWindow
    // mAttachInfo 是不是很眼熟，就是最开始 View#post 的第一层判断
    // 这个 mAttachInfo 在 ViewRootImpl 的构造器中初始化的，其持有 ViewRootImpl 的 Handler 对象
    host.dispatchAttachedToWindow(mAttachInfo, 0);
    getRunQueue().executeActions(mAttachInfo.mHandler);
    
    // 绘制流程就从这里开始
    performMeasure();
    performLayout();
    performDraw();
}
```

我们先从 dispatchAttachedToWindow 开始，我们之前已经看过这个方法的源码了：

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
**现在来进行分析：**

1. 我们已经知道了此方法是从根视图开始递归向下调用的，那么递归到最深处，就会轮到最开始我们调用 post 方法的 View 对象来执行该方法，也就是该方法内的所有属性，都是我们 findViewById 获得的那个 View 对象的属性；
2. 而且我们也知道，第一个参数 AttachInfo 就是 ViewRootImpl 中初始化的 AttachInfo，它持有当前 ViewRootImpl 的 Handler 对象引用，并将该引用传给了 executeActions()。此时，我们再来回顾一下 **executeActions()** 方法的作用，**传入的 Handler 决定着队列里这些 Runnable 的执行线程**。


很明显，此处的 mRunQueue 就是我们最开始调用 post() 时，调用 View#getRunQueue 返回的那个对象，这个对象中有准备获取View高度的 Runnable 对象，也就是说 **mRunQueue 通过调用 executeActions() 将当前 View 的所有 Runnable ，都会转由 ViewRootImpl 的 Handler 来处理！**而在完成这个工作之后，当前 View 也显示地将 mRunQueue 置空，因为所有的待办任务都已经交给 ViewRootImpl 去处理了。

现在再回过头看代码的注释，就差不多可以理解了：

```java
// Postpone the runnable until we know on which thread it needs to run.
// Assume that the runnable will be successfully placed after attach.
// 所有的 Runnable 都会在 attach 之后被正确的放到其应该运行的线程上去
getRunQueue().post(action);

// Transfer all pending runnables.
// 转移所有待办任务(到 ViewRootImpl 中进行处理)
if (mRunQueue != null) {
    mRunQueue.executeActions(info.mHandler);
    mRunQueue = null;
}
```

dispatch 方法执行完了，我们继续回来走 performTraversals() ，接下来一句是：

```java
// 有之前的经验，我们知道这句话的意思是
// 使用 mAttachInfo.mHandler 来处理 getRunQueue() 中的 Runnable 任务
getRunQueue().executeActions(mAttachInfo.mHandler);
```

要明确的一点是，此时我们处在 ViewRootImpl 类中，此处的 getRunQueue() 方法有别于 View#post：

```java
// ViewRootImpl#getRunQueue
// 使用 ThreadLocal 来存储每个线程自身的执行队列 HandlerActionQueue
static HandlerActionQueue getRunQueue() {
    // sRunQueues 是 ThreadLocal<HandlerActionQueue> 对象
    HandlerActionQueue rq = sRunQueues.get();
    if (rq != null) {
        return rq;
    }
    rq = new HandlerActionQueue();
    sRunQueues.set(rq);
    return rq;
}

// View#post
// 为当前 View 返回一个执行队列，但是在 dispatchAttachToWindow 时转到 UI 线程去
private HandlerActionQueue getRunQueue() {
    if (mRunQueue == null) {
        mRunQueue = new HandlerActionQueue();
    }
    return mRunQueue;
}
```

说回 performTraversals() ，很明显 getRunQueue() 是 UI 线程执行队列的第一次初始化，也就是说当前这个任务队列里并没有待执行任务！

但是需要注意的是，**当前没有执行任务（**HandlerActionQueue**），不代表 Handler 消息队列中没有消息**，这是两个概念，需要注意区分开。

总结一下：

1. View#post 方法调用时，会为当前 View 对象初始化一个 HandlerActionQueue ，并将 Runnable 入队存储；
2. 等在 ViewRootImpl#performTraversals 中递归调用到 View#dispatchAttachedToWindow 时，会将 ViewRootImpl 的 Handler 对象传下来，然后通过这个 Handler 将最初的 Runnable 发送到 UI 线程（消息队列中）等待执行，并将 View 的 HandlerActionQueue 对象置空，方便回收；
3. ViewRootImpl#performTraversals 继续执行，才会为 UI 线程首次初始化 HandlerActionQueue 对象，并通过 ThreadLocal 进行存储，方便之后的复用，但需要注意的是，此处初始化的队列中是没有任何 Runnable 对象的；
4. 然后 ViewRootImpl#performTraversals 继续执行，开始 View 的测量流程。



### Runnable#run 执行的时机

但现在的问题是，无论怎么说，**HandlerActionQueue#executeActions 都是先于 View 测绘流程的**，为什么在还没有完成测量的时候，就可以拿到宽高信息？

我们都知道，Android 系统是基于消息机制运行的，所有的事件、行为，都是基于 Handler 消息机制在运行的。所以，当 ViewRootImpl#performTraversals 在执行的时候，也一定是基于某个消息的。而且，HandlerActionQueue#executeActions 执行的时候，也只是通过 Handler 将 Runnable post 到了 UI 线程等待执行（还记得 View#post 的注释吗？）。

不出意外的话，此时 UI 线程正忙着执行 ViewRootImpl#performTraversal ，等该方法执行完毕，View 已经完成了测量流程，此时再去执行 Runnable#run ，也就自然可以获取到 View 的宽高信息了。

下面用具体的实例佐证一下我们的猜想。

```JAVA
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();

    // 等待 Add 到父布局中
    view = new View(this) {
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);

            Log.e("LJT", "执行了onLayout()");

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            Log.e("LJT", "执行了onMeasure()");
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e("LJT", "执行了onDraw()");
        }
    };

    // 自己声明的 Handler 
    mHandler.post(new Runnable() {
        @Override
        public void run() {
            Log.e("LJT", "mHandler.post ---- > " + view.getHeight());
        }
    });

    // onCreate() 中 mAttachInfo 还未被赋值，这里会交给 ViewRootImpl 的 Handler 来处理
    // 即加入消息队列，等待执行
    view.post(new Runnable() {
        @Override
        public void run() {
            Log.e("LJT", "view.post ---- > " + view.getHeight());
        }
    });

    viewGroup.addView(view);

}
```

