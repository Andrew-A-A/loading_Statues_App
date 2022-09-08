package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var state : ButtonState = ButtonState.Clicked
    var angle=0f
    private val LOADING_TIME=2000L
    private var widthSize = 0
    private var heightSize = 0
    private lateinit var rect:AnimatableRect
    private lateinit var rectLoading:AnimatableRect
    private var loadingColor=ResourcesCompat.getColor(resources,R.color.colorPrimaryDark,null)
    private var loadingPaint=Paint().apply {
        color=loadingColor
    }

    private var circleAnimator=ValueAnimator()
    private var paint= Paint().apply {
        color=ResourcesCompat.getColor(resources,R.color.colorPrimary,null)
        style=Paint.Style.FILL
        textSize=60f
    }
    private var circlePaint= Paint().apply {
        color=Color.YELLOW
        style=Paint.Style.FILL
    }


    init {
        isClickable=true

    }


    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        canvas.drawRect(rect,paint)
        canvas.drawRect(rectLoading,loadingPaint)
        paint.color=resources.getColor(R.color.white,null)
        if (state==ButtonState.Clicked)
          canvas.drawText("Download",widthSize/2.7f,heightSize/1.6f,paint)
        else {
            canvas.drawText("Loading..", widthSize / 2.7f, heightSize / 1.6f, paint)
            canvas.drawArc((widthSize.toFloat() - 300f),(heightSize.toFloat() / 2) - 25f, (widthSize.toFloat()-250f),
                (heightSize.toFloat() / 2) + 25f, 0F,angle, true,circlePaint)
        }
        paint.color=resources.getColor(R.color.colorPrimary,null)
    }

    override fun performClick(): Boolean {
        isClickable=false
        StartAnimation()
        return super.performClick()
    }

    private fun StartAnimation() {
        circleAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
            duration = LOADING_TIME
            addUpdateListener { valueAnimator ->
                angle = valueAnimator.animatedValue as Float
                invalidate()
            }
            start()
        }
        ObjectAnimator.ofFloat(rectLoading, "right", 0f, widthSize.toFloat()).apply {
            duration = LOADING_TIME
            interpolator = LinearInterpolator()
            state = ButtonState.Loading
            start()
            invalidate()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    state = ButtonState.Loading
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isClickable = true
                    state = ButtonState.Clicked
                    rectLoading.right = 0f
                    angle = 0f
                    invalidate()
                }
            })
            addUpdateListener {
                postInvalidate()
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h


        setMeasuredDimension(w, h)
        rect= AnimatableRect(0f,0f,widthSize.toFloat(),heightSize.toFloat())
        rectLoading=AnimatableRect(0f,0f,0f,heightSize.toFloat())

    }

}