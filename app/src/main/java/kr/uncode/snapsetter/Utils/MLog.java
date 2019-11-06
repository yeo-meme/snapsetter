package kr.uncode.snapsetter.Utils;

import android.util.Log;

import kr.uncode.snapsetter.BuildConfig;

public class MLog {
    private static final String TAG = "meme";
    private static boolean debug = BuildConfig.DEBUG;

    /**
     * Log Level Error
     **/
    public static void e(String message) {
        if (debug) Log.e(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Information
     **/
    public static void i(String message) {
        if (debug) Log.i(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Debug
     **/
    public static void d(String message) {
        if (debug) Log.d(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Error
     **/
    public static void e() {
        if (debug) Log.e(TAG, buildLogMsg(""));
    }

    /**
     * Log Level Information
     **/
    public static void i() {
        if (debug) Log.i(TAG, buildLogMsg(""));
    }

    /**
     * Log Level Debug
     **/
    public static void d() {
        if (debug) Log.d(TAG, buildLogMsg(""));
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(" " + message);
        return sb.toString();
    }

}
