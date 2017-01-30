package com.westwood24.connect.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMyriadRegular extends TextView {
    public TextViewMyriadRegular(Context context) {
        super(context);
        setFont();
    }

    public TextViewMyriadRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public TextViewMyriadRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Myriad-Pro-regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
