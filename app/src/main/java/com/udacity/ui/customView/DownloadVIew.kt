package com.udacity.ui.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

abstract class DownloadVIew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var downloadText: String = ""
    private var downloadTextColor: Int = 0
    private var downloadBackgroundColor: Int = 0
    private var downloadRectAnimatorColor: Int = 0
    private var downloadCircleAnimatorColor: Int = 0

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 30f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    /**
     * Draw Rectangle with custom paint and canvas
     */
    fun drawRectangle(canvas: Canvas, paint: Paint) {
        val rectDimensions = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rectDimensions, paint)
    }

    /**
     * Draw text at the center of canvas with custom paint
     */
    fun drawText(canvas: Canvas, paint: Paint) {
        canvas.drawText(
            downloadText,
            width.toFloat() / 2,
            (height.toFloat() / 2) - ((paint.descent() + paint.ascent()) / 2),
            paint
        )
    }

    /**
     * Draw specific path with custom paint
     */
    fun drawPath(canvas: Canvas, path: Path, paint: Paint) {
        canvas.drawPath(path, paint)
    }

    /**
     * Draw standard button with text and rectangle
     */
    fun drawStandardButton(canvas: Canvas) {
        drawRectangle(canvas, paint.apply { color = downloadBackgroundColor })
        drawText(canvas, paint.apply { color = downloadTextColor })
    }

    /**
     * This special function used to draw Animation Button with specific path for circle and rect
     * and rectangle and text
     */
    fun drawAnimationButton(canvas: Canvas, rectPath: Path, circlePath: Path) {
        drawRectangle(canvas, paint.apply { color = downloadBackgroundColor })
        drawPath(canvas, rectPath, paint.apply { color = downloadRectAnimatorColor })
        drawText(canvas, paint.apply { color = downloadTextColor })
        drawPath(canvas, circlePath, paint.apply { color = downloadCircleAnimatorColor })
    }

    /**
     * This function get the text dimension into rect variable contains the height and
     * width
     */
    fun textBounds(): Rect {
        val textRect = Rect()
        paint.getTextBounds(downloadText, 0, downloadText.length, textRect)
        return textRect
    }

    /**
     * initialize Attrs
     */
    fun InitializeAtrrs(
        downloadText: String = "",
        downloadTextColor: Int = 0,
        downloadBackgroundColor: Int = 0,
        downloadRectAnimatorColor: Int = 0,
        downloadCircleAnimatorColor: Int = 0
    ) {
        this.downloadText = downloadText
        this.downloadTextColor = downloadTextColor
        this.downloadBackgroundColor = downloadBackgroundColor
        this.downloadRectAnimatorColor = downloadRectAnimatorColor
        this.downloadCircleAnimatorColor = downloadCircleAnimatorColor
    }

    /**
     * initialize Attrs
     */
    fun InitializeAtrrs(
        downloadText: String,
        downloadTextColor: Int,
        downloadBackgroundColor: Int
    ) {
        this.downloadText = downloadText
        this.downloadTextColor = downloadTextColor
        this.downloadBackgroundColor = downloadBackgroundColor
    }

    /**
     * Text Setter
     */
    fun setText(text: String) {
        downloadText = text
    }
}