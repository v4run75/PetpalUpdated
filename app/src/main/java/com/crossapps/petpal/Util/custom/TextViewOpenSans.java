package com.crossapps.petpal.Util.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by web-shuttle-2 on 10-May-16.
 */
public class TextViewOpenSans extends TextView {
    private static final String TAG = "TextViewLight";

    public TextViewOpenSans(Context context) {
        super(context);
        init(null);
    }
    public TextViewOpenSans(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public TextViewOpenSans(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * To set the custom attribute to the View
     * @param context
     */
    private void init(Context context) {
        try {
            if (context!=null) {
                Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                setTypeface(myTypeface);
            }
        }
        catch (Exception e) {

            Log.e(TAG, e.getMessage(), e);
        }
    }
}
