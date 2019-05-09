package com.crossapps.petpal.Util.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;


/**
 * Created by CREATION on 11/28/2016.
 */

public class EditTextBold extends EditText {
    private static final String TAG = "CustomeditText";

    public EditTextBold(Context context) {
        super(context);
        init(context);
    }
    public EditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public EditTextBold(Context context, AttributeSet attrs, int defStyle) {
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
                Typeface myTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpanSans-Regular.ttf");
                setTypeface(myTypeface);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
