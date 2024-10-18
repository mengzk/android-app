package com.dxm.aimodel.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dxm.aimodel.R;

/**
 * Author: Meng
 * Date: 2024/03/30
 * Modify: 2024/03/30
 * Desc:
 */
public class HeaderBar extends FrameLayout {

    public HeaderBar(@NonNull Context context) {
        this(context, null);
    }

    public HeaderBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HeaderBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.bar_header,this,false);
        addView(layout);
        getParams(context, attrs);
    }

    private void getParams(Context context, AttributeSet attrs){
        //获取参数
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.HeaderBar);

        boolean isBack = a.getBoolean(R.styleable.HeaderBar_back, true);
        String title = a.getString(R.styleable.HeaderBar_title);
        String rightText = a.getString(R.styleable.HeaderBar_rightText);
        int rightIcon = a.getResourceId(R.styleable.HeaderBar_rightIocn, R.drawable.ic_launcher_background);

        ImageView backBtn = findViewById(R.id.header_back_btn);
        TextView titleView = findViewById(R.id.header_title);
        TextView rightView = findViewById(R.id.header_right_text);
        ImageView rightBtn = findViewById(R.id.header_right_btn);

        if(!isBack) {
            backBtn.setVisibility(GONE);
        }
        if(title != null) {
            titleView.setText(title);
        }

        if(rightText != null) {
            rightView.setText(rightText);
            rightView.setVisibility(VISIBLE);
        }

        if(rightIcon != R.drawable.ic_launcher_background) {
            rightBtn.setImageResource(rightIcon);
            rightBtn.setVisibility(VISIBLE);
        }
//        int num = a.getInt(R.styleable.HeaderBar_num, 0);
//        int num2 = a.getFloat(R.styleable.HeaderBar_num, 0);
//        float num3 = a.getDimension(R.styleable.HeaderBar_num, 0);
//        int color = a.getColor(R.styleable.HeaderBar_color, "#000000");

        a.recycle();
    }


}
