package com.example.miranda.monitoringpdam;

import android.util.Log;

/**
 * Error debug.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class Debug {
    public static void i(String tag, String message) {
        if (Cons.ENABLE_DEBUG) Log.i(tag, message);
    }

    public static void i(String message) {
        Debug.i(Cons.TAG, message);
    }

    public static void e(String tag, String message) {
        if (Cons.ENABLE_DEBUG) Log.e(tag, message);
    }

    public static void e(String message) {
        Debug.e(Cons.TAG, message);
    }

    public static void e(String tag, String message, Exception e) {
        if (Cons.ENABLE_DEBUG) {
            Log.e(tag, message);

            e.printStackTrace();
        }
    }
}