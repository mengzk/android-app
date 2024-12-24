package com.ql.health.custom.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ql.health.R


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
class HeaderBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        initView(context, attrs)
    }

    fun initView(context: Context, attrs: AttributeSet?) {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.bar_header, this, false) as FrameLayout
        addView(layout)
        getParams(context, attrs)
    }

    private fun getParams(context: Context, attrs: AttributeSet?) {
        //获取参数
        val a = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar)

        val isBack = a.getBoolean(R.styleable.HeaderBar_back, true)
        val title = a.getString(R.styleable.HeaderBar_title)
        val rightText = a.getString(R.styleable.HeaderBar_rightText)
        val rightIcon =
            a.getResourceId(R.styleable.HeaderBar_rightIocn, R.drawable.ic_launcher_background)

        val backBtn = findViewById<ImageView>(R.id.header_back_btn)
        val titleView = findViewById<TextView>(R.id.header_title)
        val rightView = findViewById<TextView>(R.id.header_right_text)
        val rightBtn = findViewById<ImageView>(R.id.header_right_btn)

        if (!isBack) {
            backBtn.visibility = GONE
        }
        if (title != null) {
            titleView.text = title
        }

        if (rightText != null) {
            rightView.text = rightText
            rightView.visibility = VISIBLE
        }

        if (rightIcon != R.drawable.ic_launcher_background) {
            rightBtn.setImageResource(rightIcon)
            rightBtn.visibility = VISIBLE
        }

        //        int num = a.getInt(R.styleable.HeaderBar_num, 0);
//        int num2 = a.getFloat(R.styleable.HeaderBar_num, 0);
//        float num3 = a.getDimension(R.styleable.HeaderBar_num, 0);
//        int color = a.getColor(R.styleable.HeaderBar_color, "#000000");
        a.recycle()
    }
}