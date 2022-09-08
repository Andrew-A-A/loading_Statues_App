package com.udacity

import android.graphics.RectF
import kotlin.math.cos
import kotlin.math.sin


class AnimatableRect(i: Float, i1: Float, widthSize: Float, heightSize: Float) : RectF(i, i1, widthSize, heightSize) {

    fun setTop(top: Float) {
        this.top = top
    }

    fun setBottom(bottom: Float) {
        this.bottom = bottom
    }

    fun setRight(right: Float) {
        this.right = right
    }

    fun setLeft(left: Float) {
        this.left = left
    }
    fun rotateRect(centerX: Float,centerY: Float,angle: Float) {
        val sinA = sin(Math.toRadians(angle.toDouble()))
        val cosA = cos(Math.toRadians(angle.toDouble()))
        val newX = centerX + (centerX - centerX) * cosA - (centerY - centerY) * sinA
        val newY = centerY + (centerY - centerY) * cosA + (centerX - centerX) * sinA
        val dx = newX - centerX
        val dy = newY - centerY

        offset(dx.toFloat(), dy.toFloat())
    }
}