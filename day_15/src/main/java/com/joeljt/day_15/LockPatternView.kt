package com.joeljt.day_15

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.MathUtils
import android.view.MotionEvent
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

    // 按下的时候是否按在一个点上
    private var mIsTouchPoint = false
    // 按下的点需要存起来
    private var mSelectPoints = ArrayList<Point>()

    private lateinit var mFinishListener: OnLockPatternFinishedListener

    public fun setOnLockPatternFinishedListener(l: OnLockPatternFinishedListener){
        this.mFinishListener = l
    }

    // 记录错误状态
    private var mIsErrorStatus = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        if (!mIsInit) {
            initDots()
            initPaint()
            mIsInit = true
        }

        drawShow(canvas)

    }

    private var mMovingX = 0f
    private var mMovingY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {

        mMovingX = event.x
        mMovingY = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                // 如何判断手指按下的位置是不是在圆内
                // 按下的位置到圆心的距离 < 半径
                var point = point
                if (point != null) {
                    mIsTouchPoint = true
                    mSelectPoints.add(point)
                    // 改变当前点的状态
                    point.setStatusPressed()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (mIsTouchPoint) {
                    // 按下的时候如果是在不同的点，不断滑动的时候要去绘制新点
                    var point = point
                    if (point != null) {
                        if (!mSelectPoints.contains(point)) {
                            mSelectPoints.add(point)
                        }
                        // 改变当前点的状态
                        point.setStatusPressed()
                    }
                }
            }

            MotionEvent.ACTION_UP -> {

                if (mSelectPoints.size == 1) {
                    resetPointStatus()
                } else if (mSelectPoints.size < 4) {
                    showSelectError()
                } else {
                    // 正常情况，处理回调
                    handleSuccessCallback()

                }

                // 抬起的时候回调监听
                mIsTouchPoint = false

            }

        }
        invalidate()
        return true
    }

    private fun handleSuccessCallback() {
        mFinishListener.onLockPatternFinished(getResult(mSelectPoints))
        postDelayed({
            resetPointStatus()
        },300)
    }

    private fun getResult(mSelectPoints: ArrayList<Point>): String {
        var resultStr = StringBuilder()
        mSelectPoints.forEach {
            resultStr.append(it.index + 1)
        }
        return resultStr.toString()
    }

    private fun showSelectError() {
        // 显示错误信息
        mIsErrorStatus = true
        mSelectPoints.forEach {
            it.setStatusError()
        }

        resetPointStatus()

    }

    private fun resetPointStatus() {
        if (mSelectPoints.size > 0) {
            postDelayed({
                mSelectPoints.forEach {
                    it.setStatusNormal()
                }
                mIsErrorStatus = false
                mSelectPoints.clear()
                postInvalidate()
            }, 200)
        }

    }

    /**
     * 绘制九宫格显示
     */
    private fun drawShow(canvas: Canvas) {
        for (i in 0..2) {
            for (point in mPoints[i]) {

                // 正常状态
                if (point!!.statusIsNormal()) {

                    // 先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius.toFloat(), mNormalPaint)

                    // 后绘制内圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius / 6.toFloat(), mNormalPaint)

                }

                // 按下状态
                if (point.statusIsPressed()) {

                    // 先绘制外圆
                    mPressedPaint.color = mOuterPressedColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius.toFloat(), mPressedPaint)

                    // 后绘制内圆
                    mPressedPaint.color = mInnerPressedColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius / 6.toFloat(), mPressedPaint)

                }

                // 错误状态
                if (point.statusIsError()) {

                    // 先绘制外圆
                    mErrorPaint.color = mOuterErrorColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius.toFloat(), mErrorPaint)

                    // 后绘制内圆
                    mErrorPaint.color = mInnerErrorColor
                    canvas.drawCircle(point.centerX.toFloat(),
                            point.centerY.toFloat(), mDotRadius / 6.toFloat(), mErrorPaint)

                }

            }
        }

        // 绘制两个点之间连线以及箭头
        drawLineToCanvas(canvas)

    }

    /**
     * 画线
     * @param canvas
     */
    private fun drawLineToCanvas(canvas: Canvas) {
        if (mSelectPoints.size >= 1) {
            if (mIsErrorStatus) {
                mLinePaint!!.color = mInnerErrorColor
                mArrowPaint!!.color = mInnerErrorColor
            } else {
                mLinePaint!!.color = mInnerPressedColor
                mArrowPaint!!.color = mInnerPressedColor
            }

            var lastPoint = mSelectPoints[0]
            for (i in 1..mSelectPoints.size - 1) {
                val point = mSelectPoints[i]
                // 不断的画线
                drawLine(lastPoint, point, canvas, mLinePaint!!)
                drawArrow(canvas, mArrowPaint!!, lastPoint, point, (mDotRadius / 4).toFloat(), 38)
                lastPoint = point
            }

            // 如果手指在内圆里就不再绘制了
            val isInnerPoint = MathUtil.checkInRound(lastPoint.centerX.toFloat(), lastPoint.centerY.toFloat(), mDotRadius.toFloat(), mMovingX, mMovingY)
            if (mIsTouchPoint && !isInnerPoint) {
                drawLine(lastPoint, Point(mMovingX.toInt(), mMovingY.toInt(), -1), canvas, mLinePaint!!)
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
    private fun initDots() {
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
            offsetX = (width - height) / 2
            width = height
        }

        var squareWidth = width / 3

        // 外圆的大小，根据宽度来指定
        mDotRadius = width / 12

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

        fun setStatusNormal() {
            status = STATUS_NORMAL
        }

        fun setStatusPressed() {
            status = STATUS_PRESSED
        }

        fun setStatusError() {
            status = STATUS_ERROR
        }

        fun statusIsNormal(): Boolean {
            return status == STATUS_NORMAL
        }

        fun statusIsPressed(): Boolean {
            return status == STATUS_PRESSED
        }

        fun statusIsError(): Boolean {
            return status == STATUS_ERROR
        }

    }


    private val point: Point?
        get() {
            for (i in mPoints.indices) {
                (0 until mPoints[i].size)
                        .map { mPoints[i][it] }
                        .filter {
                            MathUtil.checkInRound(it!!.centerX.toFloat(), it.centerY.toFloat(),
                                    mDotRadius.toFloat(), mMovingX, mMovingY)
                        }
                        .forEach { return it }
            }
            return null
        }

    /**
     * 画线
     */
    private fun drawLine(start: Point, end: Point, canvas: Canvas, paint: Paint) {
        val d = MathUtil.distance(start.centerX.toDouble(), start.centerY.toDouble(), end.centerX.toDouble(), end.centerY.toDouble())
        val rx = (((end.centerX - start.centerX) * mDotRadius).toDouble() / 5.0 / d).toFloat()
        val ry = (((end.centerY - start.centerY) * mDotRadius).toDouble() / 5.0 / d).toFloat()
        canvas.drawLine(start.centerX + rx, start.centerY + ry, end.centerX - rx, end.centerY - ry, paint)
    }

    /**
     * 画箭头
     */
    private fun drawArrow(canvas: Canvas, paint: Paint, start: Point, end: Point, arrowHeight: Float, angle: Int) {
        val d = MathUtil.distance(start.centerX.toDouble(), start.centerY.toDouble(), end.centerX.toDouble(), end.centerY.toDouble())
        val sin_B = ((end.centerX - start.centerX) / d).toFloat()
        val cos_B = ((end.centerY - start.centerY) / d).toFloat()
        val tan_A = Math.tan(Math.toRadians(angle.toDouble())).toFloat()
        val h = (d - arrowHeight.toDouble() - mDotRadius * 1.1).toFloat()
        val l = arrowHeight * tan_A
        val a = l * sin_B
        val b = l * cos_B
        val x0 = h * sin_B
        val y0 = h * cos_B
        val x1 = start.centerX + (h + arrowHeight) * sin_B
        val y1 = start.centerY + (h + arrowHeight) * cos_B
        val x2 = start.centerX + x0 - b
        val y2 = start.centerY.toFloat() + y0 + a
        val x3 = start.centerX.toFloat() + x0 + b
        val y3 = start.centerY + y0 - a
        val path = Path()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        path.lineTo(x3, y3)
        path.close()
        canvas.drawPath(path, paint)
    }

}