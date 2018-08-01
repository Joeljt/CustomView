## day_01_MyTextView

### 构造方法

* 一个参数

  在代码中初始化时使用

* 两个参数

  在布局文件中使用时，会经过这个方法；第二个参数 **attrs** 就是传入的自定义属性

* 三个参数

  同样是在布局文件中使用，但是当文件中使用到 style 文件时才会使用，第三个参数是 style 文件

### 测量规格

MeasureSpec 是一个 32 位的 int 值，前 2 位表示 SpecMode，后 30 位表示 SpecSize。

MeasureSpec 通过将 SpecMode 和 SpecSize 打包成一个 int 值来避免过多的对象内存分配，同样在使用到具体的属性时，可以通过解包的方式来获取原始值。

```java
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 具体在测量控件大小时，宽高的 MeasureSpec 都是由父布局一层层传递下来的
        // MeasureSpec 可以理解为是父 View 对子 View 的的测量要求
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
```



* MeasureSpec 的三种情况

    * AT_MOST

        父容器指定了一个可用大小，即 SpecSize，当前子 View 大小不能超过这个值

        对应布局文件中的 wrap_content

    * EXACTLY

        父容器已经测量出 View 所需要的精确大小，子 View 最终的大小就是测量到的值

        对应布局文件中的 match_parent 或者固定数值

    * UNSECIFIED

        一般系统的控件才会使用到这个，自己自定义 View 的话，很少用到；

        具体事例，可参考 ScrollView 嵌套 ListView 的解决方法的原理

