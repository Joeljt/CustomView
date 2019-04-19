package com.joeljt.day_15

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.jar.Attributes

/**
 * Created by JoeLjt on 2019/4/19.
 * Email: lijiateng1219@gmail.com
 * Description: 九宫格解锁 View
 *
 */
class LockPatternView : View {

    private var mIsInit = false

    // 二维数组 int[3][3]
    private var mPoints: Array<Array<Point?>> = Array(3) {
        Array<Point?>(3, { null })
    }

    // 外圆的半径
    private var mDotRadius = 0

    // 画笔
    private lateinit var mLinePaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint

    // 颜色
    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        if (!mIsInit) {
            initDots(canvas)
            initPaint()
            mIsInit = true
        }

        drawShow(canvas)

    }

    /**
     * 绘制九宫格显示
     */
    private fun drawShow(canvas: Canvas){
        for (i in 0..2) {
            for (point in mPoints[i]) {

                // 先绘制外圆
                mNormalPaint.color = mOuterNormalColor
                canvas.drawCircle(point!!.centerX.toFloat(),
                        point.centerY.toFloat(), mDotRadius.toFloat(), mNormalPaint)

                // 后绘制内圆
                mNormalPaint.color = mInnerNormalColor
                canvas.drawCircle(point!!.centerX.toFloat(),
                        point.centerY.toFloat(), mDotRadius/6.toFloat(), mNormalPaint)


            }
        }
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {

        // 线的画笔
        mLinePaint = Paint()
        mLinePaint.color = mInnerPressedColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.isAntiAlias = true
        mLinePaint.isDither = true
        mLinePaint.strokeWidth = (mDotRadius / 9).toFloat()

        // 按下的画笔
        mPressedPaint = Paint()
//        mPressedPaint.color = mInnerPressedColor
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.isDither = true
        mPressedPaint.strokeWidth = (mDotRadius / 6).toFloat()

        mErrorPaint = Paint()
//        mErrorPaint.color = mEr
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.isDither = true
        mErrorPaint.strokeWidth = (mDotRadius / 6).toFloat()

        mNormalPaint = Paint()
//        mNormalPaint.color = mInnerPressedColor
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.isDither = true
        mNormalPaint.strokeWidth = (mDotRadius / 9).toFloat()

        // 箭头的画笔
        mArrowPaint = Paint()
//        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        mArrowPaint.isAntiAlias = true
        mArrowPaint.isDither = true
        mArrowPaint.strokeWidth = (mDotRadius / 9).toFloat()

    }

    /**
     * 初始化九个点  Point[3][3]
     *
     */
    private fun initDots(canvas: Canvas?) {
        // 九个宫格，存到二维数组中
        // 不断绘制的时候这几个点都有状态，而且后面肯定要回到到每个点，所以每个点都应该有下标，点肯定是一个对象
        // 计算中心位置

        var width = this.width;
        var height = this.height;

        // 兼容横竖屏
        var offsetX = 0
        var offsetY = 0
        if (height > width) {
            offsetY = (height - width) / 2
            height = width
        } else {
            offsetY = (width - height) / 2
            width = height
        }

        var squareWidth = width / 3

        // 外圆的大小，根据宽度来指定
        mDotRadius = width / 3

        mPoints[0][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + squareWidth / 2 * 3, offsetY + squareWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + squareWidth / 2 * 5, offsetY + squareWidth / 2, 2)
        mPoints[1][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2 * 3, 3)
        mPoints[1][1] = Point(offsetX + squareWidth / 2 * 3, offsetY + squareWidth / 2 * 3, 4)
        mPoints[1][2] = Point(offsetX + squareWidth / 2 * 5, offsetY + squareWidth / 2 * 3, 5)
        mPoints[2][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2 * 5, 6)
        mPoints[2][1] = Point(offsetX + squareWidth / 2 * 3, offsetY + squareWidth / 2 * 5, 7)
        mPoints[2][2] = Point(offsetX + squareWidth / 2 * 5, offsetY + squareWidth / 2 * 5, 8)


    }

    /**
     * 宫格的类
     */
    class Point(var centerX: Int, var centerY: Int, var index: Int) {
        companion object {
            private val STATUS_NORMAL = 1
            private val STATUS_PRESSED = 2
            private val STATUS_ERROR = 3
        }

        // 当前点的状态，有三种状态
        private var status = STATUS_NORMAL

    }


}