package com.udacity.ui.customView

import android.animation.*
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.withStyledAttributes
import com.udacity.ButtonState
import com.udacity.R
import com.udacity.utils.downloadOperations
import kotlin.math.log
import kotlin.properties.Delegates



class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DownloadVIew(context, attrs, defStyleAttr), downloadOperations {

    private var downloadText = resources.getString(R.string.button_name)
    private var downloadBackground = 0
    private var downloadTextColor = 0
    private var isAnimate = false

    private val valueAnimator = ValueAnimator()
    private var rectanglePath = Path()
    private var circlePath = Path()

    // This delegates observable notify when it changed depend on Button state
    var buttonState: ButtonState? by Delegates.observable(null) { proberty, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                //when it clicked set animate to true
                isAnimate = true
                //change drawing text
                super.setText(resources.getString(R.string.button_loading))
                //change state of the buttin state
                buttonState = ButtonState.Loading
            }
            ButtonState.Loading -> {
                //add DecelerateInterpolator to value Animator
                valueAnimator.interpolator = DecelerateInterpolator(2f)
                //start animtaion
                startRectangleAnim()
            }
            ButtonState.Completed -> {
                //when it completed cancel this animation
                valueAnimator.cancel()
            }
        }
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.downloadButton) {
            downloadBackground = getColor(R.styleable.downloadButton_buttonColor, 0)
            downloadTextColor = getColor(R.styleable.downloadButton_textColor, 0)
        }
        //Initialize view attrs
        super.InitializeAtrrs(
            downloadText,
            downloadTextColor,
            downloadBackground,
            getColor(context, R.color.colorPrimaryDark),
            getColor(context, R.color.colorAccent)
        )

    }

    override fun onDraw(canvas: Canvas) {
        if (!isAnimate) {
            //if it doesn't animate draw standard one
            super.drawStandardButton(canvas)
        } else {
            //else draw Animator button
            super.drawAnimationButton(canvas, rectanglePath, circlePath)
        }
    }

    /**
     * This function generate and initialize animation
     */
    override fun startRectangleAnim() {
        // sets the range of our value
        valueAnimator.setIntValues(0, width)
        // set animation duration
        valueAnimator.duration = 2000
        // call update listener for that value animator
        valueAnimator.rectangleAnimation()
        // call start and end listener for that value animator
        valueAnimator.display(this)
        // then start animation
        valueAnimator.start()
    }

    /**
     * Handle on click listener
     */
    fun handleClick() {
        //change button state
        buttonState = ButtonState.Clicked
    }

    /**
     * This extension function listen to the animation statue if it start
     * or end and each one has it's operation
     */
    override fun ValueAnimator.display(view: View) {
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
                //lock animator
                isAnimate = false
                //change text value
                setText(resources.getString(R.string.button_name))
                //invalidate to redraw
                invalidate()
            }
        })
    }

    /**
     * addUpdateListener to redraw rectangle and circle
     */
    fun ValueAnimator.rectangleAnimation() =
        addUpdateListener {
            //Get current animation value
            var animationValues = (it?.animatedValue as Int)
            //Get dimension of drawing rect
            val left = 0f
            val right = animationValues.toFloat()
            val top = 0f
            val bottom = height.toFloat()
            //Create a RectF with above dimensions
            val rectF = RectF(left, top, right, bottom)
            //Rewind path to creating new path
            rectanglePath.rewind()
            //Add rect to path
            rectanglePath.addRect(rectF, Path.Direction.CCW)
            circleAnimation(animationValues)
            invalidate()
        }

    private fun circleAnimation(value: Int) {
        val frontRect = RectF()
        val textRect = super.textBounds()
        val left = (width.toFloat() / 2) + (textRect.width() / 2f) + 10f
        val right = (width.toFloat() / 2) + (textRect.width() / 2f) + 60f
        val bottom = (height.toFloat() / 2) + 25f
        val top = (height.toFloat() / 2) - 25f
        //makes sure the path is empty
        circlePath.rewind()
        frontRect.set(left, top, right, bottom)
        circlePath.moveTo((right - 30f), (bottom - 25f))
        circlePath.arcTo(frontRect, 0f, value.toFloat() * (360f / width.toFloat()))
    }

}