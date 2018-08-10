### 为什么 onCreate() 和 onResume() 里获取不到 View 的高度



```java
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_test)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTv.getHeight(); // 0
        
        mTv.post(new Runnable() {
            @Override
            public void run() {
                 mTv.getHeight(); // 可以正常获取到宽高
            }
        });
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        mTv.getHeight(); // 0
    }
    
}
```

要弄清这个问题，首先需要知道代码中涉及到的方法具体做了什么工作，以及具体 View 是在什么时候完成测量的。

### onCreate()

很明显，我们在 onCreate() 方法中调用了 setContentView() 方法，而**设置布局**这个动作会给你一种可以获取到宽高的错觉；那么我们从源码的角度来看看，setContentView() 到底干了点什么。

```java
// 1. AppCompatDelegate 的抽象方法，根据注释，会调用到 Activity 的实现方法中
public abstract void setContentView(@LayoutRes int resId);

// 2. Activity 的实现方法
public void setContentView(@LayoutRes int layoutResID) {
    // Window 是一个抽象类，其唯一实现类是 PhoneWindow
    getWindow().setContentView(layoutResID);
    initWindowDecorActionBar();
}

@Override
public void setContentView(int layoutResID) {
    if (mContentParent == null) {
        // 3. 初始化 DecorView
        installDecor();
    } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        mContentParent.removeAllViews();
    }
    ... ...
}

private void installDecor() {
    mForceDecorInstall = false;
    if (mDecor == null) {
        // 4. 第一次加载窗口，mDecor 为空时，生成一个 DecorView 对象
        // generateDecor(-1) : return new DecorView()
        mDecor = generateDecor(-1);
        ... ...
    } else {
        mDecor.setWindow(this);
    }

    if (mContentParent == null) {
        // 5. 初始化父布局
        mContentParent = generateLayout(mDecor);
    }       
}

// 继续跟踪到 generateLayout(mDecor) 方法内部
protected ViewGroup generateLayout(DecorView decor) {
    // 此处根据设置的主题进行一些基础设置，没什么决定性作用
    TypedArray a = getWindowStyle();
    ... ... 

        // 接下来的一大段代码是根据各种主题设置默认布局，篇幅原因，此处有大量源码删减
        int layoutResource;
    int features = getLocalFeatures();
    if ((features & (1 << FEATURE_SWIPE_TO_DISMISS)) != 0) {
        layoutResource = R.layout.screen_swipe_dismiss;
        setCloseOnSwipeEnabled(true);
    } else if ((features & (1 << FEATURE_NO_TITLE)) == 0) {
        if (mIsFloating) {
            TypedValue res = new TypedValue();
            getContext().getTheme().resolveAttribute(
                R.attr.dialogTitleDecorLayout, res, true);
            layoutResource = res.resourceId;
        } else if ((features & (1 << FEATURE_ACTION_BAR)) != 0) {
            layoutResource = a.getResourceId(
                R.styleable.Window_windowActionBarFullscreenDecorLayout,
                R.layout.screen_action_bar);
        } else {
            layoutResource = R.layout.screen_title;
        }
    } else {
        // 默认布局样式
        layoutResource = R.layout.screen_simple;
    }

    // 6. 重点来了：将对应的布局加载到 DecorView 中
    mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);
    return contentParent;
}

void onResourcesLoaded(LayoutInflater inflater, int layoutResource) {
	// 加载资源文件
    final View root = inflater.inflate(layoutResource, null); 
	... ...
    // 7. 将 View 加载到当前 DecorView 中
    addView(root, 0, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
}

public void addView(View child, int index, LayoutParams params) {
	// 页面发生变化的话，请求重新摆放布局以及重新绘制
    requestLayout();
    invalidate(true);
    addViewInner(child, index, params, false);
}
```

说出来你可能不信，但是 setContentView() 到这里就差不多结束了











