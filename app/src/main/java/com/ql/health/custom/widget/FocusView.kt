package com.ql.health.custom.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Author: Meng
 * Date: 2025/02/11
 * Modify: 2025/02/11
 * Desc:
 */

class FocusView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint: Paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var focusRect: Rect? = null

    fun setFocusArea(rect: Rect) {
        focusRect = rect
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        focusRect?.let {
            canvas.drawRect(it, paint)
        }
    }
}