package com.al.split_edit_text_view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @Date:2020/6/27 23:07
 * @Author:Chen-Al
 * @Email:646331658@qq.com
 * @Description:
 */
public class AEditText extends AppCompatEditText {
    public AEditText(Context context) {
        this(context ,null);
    }

    public AEditText(Context context, AttributeSet attrs) {
        //this(context, attrs,0);
        this(context, attrs,android.R.attr.editTextStyle);
    }

    public AEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //setFocusable(true);
        //setFocusableInTouchMode(true);
    }
}
