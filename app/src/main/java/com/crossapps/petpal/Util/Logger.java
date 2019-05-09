package com.crossapps.petpal.Util;

import android.util.Log;

/**
 * Created by web-shuttle-2 on 10-Jun-16.
 */
public class Logger {

    public static void e(String tag, String message) {
       Log.e(tag, message);
    }public static void e(String tag, String message, Throwable throwable) {
       Log.e(tag, message,throwable);
    }

    public static void d(String tag, String message) {
       Log.d(tag, message);
    }

    public static void w(String tag, String message) {
      //  Log.w(tag, message);
    }

}
