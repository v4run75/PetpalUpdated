package com.crossapps.petpal.Util.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by web-shuttle-2 on 10-May-16.
 */
public class TextViewBold extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TextViewBold";

    public TextViewBold(Context context) {
        super(context);
        init(null);
    }
    public TextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
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
                Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa-Bold.otf");
                setTypeface(myTypeface);
            }
        }
        catch (Exception e) {

            Log.e(TAG, e.getMessage(), e);
        }
    }
}
