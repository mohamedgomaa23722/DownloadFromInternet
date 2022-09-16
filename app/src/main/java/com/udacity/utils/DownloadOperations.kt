package com.udacity.utils

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View

interface downloadOperations {
    fun ValueAnimator.display(view: View)
    //animation functions
    fun startRectangleAnim()
}