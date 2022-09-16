package com.udacity.ui.customView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.udacity.R
import com.udacity.utils.downloadOperations

class SimpleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DownloadVIew(context, attrs, defStyleAttr) {

    private var buttonText: String = ""
    private var buttonTextColor: Int = 0
    private var bckGround: Int = 0

    private var valueAnimator = ValueAnimator()
    private var rectPath = Path()
    private var isAnimate = false

    init {
        isClickable = true
        //Get styleable values and set it to each variable
        context.withStyledAttributes(attrs, R.styleable.downloadButton) {
            buttonText = getString(R.styleable.downloadButton_text)!!
            buttonTextColor =getColor(R.styleable.downloadButton_textColor, context.getColor(R.color.white))
            bckGround = getColor(R.styleable.downloadButton_buttonColor, context.getColor(R.color.colorAccent))
        }
        //Initialize Parent View parametars
        super.InitializeAtrrs(buttonText, buttonTextColor, bckGround)
    }

    //Declare Paint variable
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 30f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isAnimate) {
            //when it animate so at the first draw original rect
            super.drawRectangle(canvas, paint.apply { color = bckGround })
            //then draw current path
            super.drawPath(
                canvas,
                rectPath,
                paint.apply { color = resources.getColor(R.color.gray) })
            //Then draw text
            super.drawText(canvas, paint.apply { color = buttonTextColor })
        } else {
            //if it not draw standard one
            super.drawStandardButton(canvas)
        }
    }

    /**
     * This function helps us to generate animation on view
     */
    fun handleClick() {
        //set is animate to true
        isAnimate = true
        //generate animation
        Animate()
    }

    /**
     * This function for Initialize the values animator and start it
     */
    fun Animate() {
        //set from to values
        valueAnimator.setIntValues(0, (width / 2).toInt())
        //set animation duration
        valueAnimator.duration = 300
        // call the update extension function
        valueAnimator.animate()
        // call statues of animation extension function
        valueAnimator.animationStatue(this)
        //start animation
        valueAnimator.start()
    }

    /**
     * This extension function listen to the animation statue if it start
     * or end and each one has it's operation
     */
    fun ValueAnimator.animationStatue(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                //disallow click on the view
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //allow click on the view
                view.isEnabled = true
                //set animate to false
                isAnimate = false
                //invalidate to redraw canvas
                invalidate()
            }
        })
    }

    /**
     * This Extension function listen to the animation statue if it is
     * updates
     */
    fun ValueAnimator.animate() {
        addUpdateListener {
            //Get current animation value
            val value = it.animatedValue as Int
            //draw rectangle with this value
            drawRectanglePath(value)
        }
    }

    /**
     * This function draw Rectangle by path start from center and move to
     * each side to the end and to start
     */
    fun drawRectanglePath(animValue: Int) {
        //Initialize drawing point where it will be drawing
        val rightRect = RectF(
            (width.toFloat() / 2f) - animValue,
            0f,
            (width.toFloat() / 2f) + animValue,
            height.toFloat()
        )
        //rewind path
        rectPath.rewind()
        //add this rect to rect path
        rectPath.addRect(rightRect, Path.Direction.CCW)
        //invalidate to redraw
        invalidate()
    }
}