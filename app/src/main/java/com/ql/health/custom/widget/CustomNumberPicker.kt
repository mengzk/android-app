package com.ql.health.custom.widget

/**
 * Author: Meng
 * Date: 2025/01/10
 * Modify: 2025/01/10
 * Desc:
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.NumberPicker

class CustomNumberPicker(context: Context, attrs: AttributeSet?) : NumberPicker(context, attrs) {
    init {
        // Customize the NumberPicker here if needed
    }

    override fun addView(child: android.view.View?) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: android.view.View?, index: Int, params: android.view.ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: android.view.View?, params: android.view.ViewGroup.LayoutParams?) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: android.view.View?) {
        if (view is EditText) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            view.setTypeface(null, Typeface.BOLD)
            view.setTextColor(resources.getColor(android.R.color.white, null))
        }
    }

}