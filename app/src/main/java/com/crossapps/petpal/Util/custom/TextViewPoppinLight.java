package com.crossapps.petpal.Util.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by CREATION on 11/28/2016.
 */

public class TextViewPoppinLight  extends TextView {
    private static final String TAG = "CustomeditText";

    public TextViewPoppinLight (Context context) {
        super(context);
        init(context);
    }
    public TextViewPoppinLight (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public TextViewPoppinLight (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    /**
     * To set the custom attribute to the View
     * @param
     */
    private void init(Context mContext) {
        try {
            if (mContext!=null) {
                Typeface myTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Poppins-Regular.otf");
                setTypeface(myTypeface);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
