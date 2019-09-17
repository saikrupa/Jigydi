package jigydi.com.jigydi.app.utils;

import android.util.Log;

/**
 * Created by click2clinic on 08-06-2017.
 */

public class MyLog {

    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    public static final int NO_LOGS = 5;

    private static final int LOG_LEVEL = AppConstants.LOG_LEVEL;

    /**Send a DEBUG log message.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if(LOG_LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }
    /**Send an INFO log message.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     */
    public static void i(String tag, String msg) {

        if(LOG_LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }
    /**Send an WARN log message.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     */
    public static void w(String tag, String msg) {

        if(LOG_LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }
    /**Send an ERROR log message.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     */
    public static void e(String tag, String msg) {

        if(LOG_LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }
    /**Send a DEBUG log message and log the exception.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     @param  e  An exception to log
     */
    //	@SuppressWarnings("unused")
    public static void d(String tag, String msg, Throwable e) {

        if(LOG_LEVEL <= DEBUG) {
            Log.d(tag, msg, e);
        }
    }
    /**Send a INFO log message and log the exception.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     @param  e  An exception to log
     */
    public static void i(String tag, String msg, Throwable e) {

        if(LOG_LEVEL <= INFO) {
            Log.i(tag, msg, e);
        }
    }
    /**Send a WARN log message and log the exception.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     @param  e  An exception to log
     */
    public static void w(String tag, String msg, Throwable e) {

        if(LOG_LEVEL <= WARN) {
            Log.w(tag, msg, e);
        }
    }
    /**Send a ERROR log message and log the exception.

     @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     @param msg  The message you would like logged.
     @param  e  An exception to log
     */
    public static void e(String tag, String msg, Throwable e) {
        if(LOG_LEVEL <= ERROR) {
            Log.e(tag, msg, e);
        }
    }
}