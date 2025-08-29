package com.ql.health.custom.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ql.health.R

@SuppressLint("SetTextI18n")
class CircularProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0
    private var max = 100
    private var strokeWidth = 39f
    private var progressColor = ContextCompat.getColor(context, R.color.green)
    private var backgroundColor = ContextCompat.getColor(context, R.color.gray)
    private val paint = Paint()
    private val rectF = RectF()
//    private val textView = TextView(context)

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircularProgress, 0, 0)
            progress = typedArray.getInt(R.styleable.CircularProgress_progress, progress)
            max = typedArray.getInt(R.styleable.CircularProgress_max, max)
//            strokeWidth = typedArray.getFloat(R.styleable.CircularProgressBar_strokeWidth, strokeWidth)
            progressColor =
                typedArray.getColor(R.styleable.CircularProgress_progressColor, progressColor)
            backgroundColor =
                typedArray.getColor(R.styleable.CircularProgress_backgroundColor, backgroundColor)
            typedArray.recycle()
        }

//        textView.layoutParams =
//            ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        textView.textSize = 38f
//        textView.setTextColor(ContextCompat.getColor(context, R.color.white))
//        textView.text = "$progress%"
//        textView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val diameter = Math.min(width, height)
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE

        // Draw background circle
        paint.color = backgroundColor
        rectF.set(
            strokeWidth / 2,
            strokeWidth / 2,
            diameter - strokeWidth / 2,
            diameter - strokeWidth / 2
        )
        canvas.drawOval(rectF, paint)

        // Draw progress arc
        paint.color = progressColor
        val sweepAngle = 360 * progress / max.toFloat()
        canvas.drawArc(rectF, -90f, sweepAngle, false, paint)
        canvas.save()

        // Draw progress text

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.white)
        paint.textSize = 120f
        paint.textAlign = Paint.Align.CENTER
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val fontMetrics = paint.fontMetrics
//        val w = paint.measureText("$progress%")
//        val h = fontMetrics.bottom - fontMetrics.top
        val textX = (width) / 2
        val textY = (height) / 2 -(fontMetrics.ascent + fontMetrics.descent) / 2
        canvas.drawText("$progress%", textX, textY, paint)

//        textView.text = "$progress%"
//        val textX = (width - textView.measuredWidth) / 2
//        val textY = (height + textView.measuredHeight) / 2
//        canvas.save()
//        canvas.translate(textX, textY)
//        textView.draw(canvas)
//        canvas.restore()
    }

    fun setProgress(progress: Int) {
        this.progress = progress
//        textView.text = "$progress%"
        invalidate()
    }

    fun setMax(max: Int) {
        this.max = max
        invalidate()
    }
}