package jigydi.com.jigydi.app.utils;

/**
 * Created by click2clinic on 08-06-2017.
 */

public class AppConstants {


    //For location
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final boolean IS_DEVELOPMENT = BuildAppConfig.DEBUG;
    public static final int LOG_LEVEL = IS_DEVELOPMENT ? MyLog.DEBUG : MyLog.NO_LOGS;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    public static final int MaxHours = 17;
}
