package com.westwood24.connect.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontCustom {

    static public Typeface setFont(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Myriad-Pro-regular.ttf");
        return font;
    }

    static public Typeface setFontBold(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Myriad-Pro-bold.ttf");
        return font;
    }

    static public Typeface setFontCircleLight(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/circle-light.ttf");
        return font;
    }

    static public Typeface setFontThin(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLt.ttf");
        return font;
    }

    static public Typeface setFontBd(Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueBd.ttf");
        return font;
    }


}