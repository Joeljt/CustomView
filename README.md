## day_01_MyTextView

### 构造方法

- 一个参数

  在代码中初始化时使用

- 两个参数

  在布局文件中使用时，会经过这个方法；第二个参数 **attrs** 就是传入的自定义属性

- 三个参数

  同样是在布局文件中使用，但是当文件中使用到 style 文件时才会使用，第三个参数是 style 文件

### 测量规格

MeasureSpec 是一个 32 位的 int 值，前 2 位表示 SpecMode，后 30 位表示 SpecSize。

MeasureSpec 通过将 SpecMode 和 SpecSize 打包成一个 int 值来避免过多的对象内存分配，同样在使用到具体的属性时，可以通过解包的方式来获取原始值。

```
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 具体在测量控件大小时，宽高的 MeasureSpec 都是由父布局一层层传递下来的
        // MeasureSpec 可以理解为是父 View 对子 View 的的测量要求
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

```

- MeasureSpec 的三种情况

  - AT_MOST

    父容器指定了一个可用大小，即 SpecSize，当前子 View 大小不能超过这个值

    对应布局文件中的 wrap_content

  - EXACTLY

    父容器已经测量出 View 所需要的精确大小，子 View 最终的大小就是测量到的值

    对应布局文件中的 match_parent 或者固定数值

  - UNSECIFIED

    一般系统的控件才会使用到这个，自己自定义 View 的话，很少用到

### ScrollView 嵌套 ListView 的解决方法的原理

* AT_MOST 是个什么鬼

  ```
  public class ListViewForScrollView extends ListView {
      public ListViewForScrollView(Context context) {
          super(context);
      }
      public ListViewForScrollView(Context context, AttributeSet attrs) {
          super(context, attrs);
      }
      public ListViewForScrollView(Context context, AttributeSet attrs,
          int defStyle) {
          super(context, attrs, defStyle);
      }

      @Override
      /**
       * 重写该方法，达到使ListView适应ScrollView的效果
       */
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
          // 打包方法，重新构造 heightMeasureSpec
          int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
          MeasureSpec.AT_MOST);
          super.onMeasure(widthMeasureSpec, expandSpec);
      }
  }

  ```

  我们知道，Android 的测绘机制是一个递归的流程，从最顶层的开始，依次递归向下测量子 View ，即调用 measureChild() 方法，一层层测量后，最后再测量最外层的 ViewGroup .

  查看 ScrollView 的源码：

  ```
  @Override
      protected void measureChild(View child, int parentWidthMeasureSpec,
              int parentHeightMeasureSpec) {
          ViewGroup.LayoutParams lp = child.getLayoutParams();

          int childWidthMeasureSpec;
          int childHeightMeasureSpec;

          childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft
                  + mPaddingRight, lp.width);
          final int verticalPadding = mPaddingTop + mPaddingBottom;

          // ScrollView 在具体测量子 View 时，向下传递的测量规格为 MeasureSpec.UNSPECIFIED
          childHeightMeasureSpec = MeasureSpec.makeSafeMeasureSpec(
                  Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding),
                  MeasureSpec.UNSPECIFIED);

          child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
      }

  ```

  理论上讲，这时候代码会走到 ListView 的 onMeasure() 方法中：

  ```
  @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
          super.onMeasure(widthMeasureSpec, heightMeasureSpec);

          final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
          int heightSize = MeasureSpec.getSize(heightMeasureSpec);

          int childHeight = 0;

          ... ...

          // 获取 ListView 的高度，此时应该只有一个条目的高度
          childHeight = child.getMeasuredHeight();

       	... ...

          // 重点就在这里
          // 如果测量模式为 MeasureSpec.UNSPECIFIED，则最终的高度就是已测量的高度 + padding
          if (heightMode == MeasureSpec.UNSPECIFIED) {
              heightSize = mListPadding.top + mListPadding.bottom + childHeight +
                      getVerticalFadingEdgeLength() * 2;
          }

          // 如果为 AT_MOST ，则会调用 measureHeightOfChildren() 方法，重新计算 View 高度
          if (heightMode == MeasureSpec.AT_MOST) {
              heightSize = measureHeightOfChildren(widthMeasureSpec, 0, NO_POSITION, heightSize, -1);
          }

          setMeasuredDimension(widthSize, heightSize);
      }

  ```



* Integer.MAX_VALUE >> 2 又是个啥

故事讲到这里，还没有结束。

我们已经知道了在解决嵌套问题的时候为什么要使用 AT_MOST，但是好像还不理解第一个参数 Integer.MAX_VALUE >> 2 到底是个什么鬼？接下来我们来解决这个疑惑。

首先，理论基础是：

> MeasureSpec 是一个 32 位的 int 值，前 2 位表示 SpecMode，后 30 位表示 SpecSize。
>
> MeasureSpec 通过将 SpecMode 和 SpecSize 打包成一个 int 值来避免过多的对象内存分配，同样在使用到具体的属性时，可以通过解包的方式来获取原始值。

很明显，SpecMode 已经知道了，就是 AT_MOST；那么，由于 SpecSize 是一个 30 位的值，因此需要对此处传入的参数进行一个右移两位的操作，也就是这个 **>>2**  的动作；至于 Integer.MAX_VALUE , 则是希望这个值尽可能的大，从而不对 item 的高度造成约束。

具体查看源码：

  ```java
// 这里是接着上面的代码，测量规格设置为 AT_MOST 以后，进入的测量子 View 高度的方法
// 注意此处的 maxHeight，就是我们传入的 Integer.MAX_VALUE，顾名思义，最大高度
final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition,
            int maxHeight, int disallowPartialChildPosition) {
     	... ...
        for (i = startPosition; i <= endPosition; ++i) {
            child = obtainView(i, isScrap);
		   ... ...
            // 继续追踪代码，进入这个方法；同样将子 View，最大高度等参数传入
            measureScrapChild(child, i, widthMeasureSpec, maxHeight);
		   ... ...
        }

        // At this point, we went through the range of children, and they each
        // completely fit, so return the returnedHeight
        return returnedHeight;
    }
  ```

  ```java
private void measureScrapChild(View child, int position, int widthMeasureSpec, int heightHint) {
        // 获取子 View 的 LayoutParams, 如果没有，就初始化一个
        // new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT, 0);
    	LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }

        // 此处获取 LayoutParams 的高度值
        // 我们知道，MATCH_PARENT 的值为 -1，WRAP_CONTENT 的值为 -2
        // 所以，除非是开发者在布局文件或代码中指明了高度的确切值，否则 lpHeight > 0 就不会成立
        final int lpHeight = p.height;
        final int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            // 代码会走到这里
            // 将传下来的 Integer.MAX_VALUE >> 2 与 MeasureSpec.UNSPECIFIED 一起
            // 打包成当前子 View 的 测量规格
            childHeightSpec = MeasureSpec.makeSafeMeasureSpec(heightHint, MeasureSpec.UNSPECIFIED);
        }
        // 递归向下，调用子 View 的 measure() 方法，而在 measure() 方法中又会去实际调用当前 View 的
        // onMeasure() 方法来进行测量
        child.measure(childWidthSpec, childHeightSpec);

        // Since this view was measured directly aginst the parent measure
        // spec, we must measure it again before reuse.
        child.forceLayout();
    }
  ```

这里我们以 TextView 为例：

```java
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height;

        if (heightMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize;
            mDesiredHeightAtMeasure = -1;
        } else {
            // if 判断不成立，会进入 else 分支
            // 调用 getDesiredHeight() 方法得到当前 View 的高度
            // 在其中会将测量得到的，控件所需要的最小高度，与我们传入的高度做对比，然后取较小值
            // 这也就是为什么我们传入的值虽然很大，但是最终不会对控件高度造成影响
            // 相反，如果传入的值过小，就有可能因为这个值的限制，导致最终显示不全
            int desired = getDesiredHeight();

            height = desired;
            mDesiredHeightAtMeasure = desired;

            // 条件不满足
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, heightSize);
            }
        }

        int unpaddedHeight = height - getCompoundPaddingTop() - getCompoundPaddingBottom();
        if (mMaxMode == LINES && mLayout.getLineCount() > mMaximum) {
            unpaddedHeight = Math.min(unpaddedHeight, mLayout.getLineTop(mMaximum));
        }

        // 设置最终的宽高信息
        setMeasuredDimension(width, height);
    }
```

