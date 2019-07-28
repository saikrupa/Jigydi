package jigydi.com.jigydi.app.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rajeshkantipudi on 30/03/17.
 */

public class SendGPSDataService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private Handler mHandler;
    private LocationManager locationManager;
    public static final long delay_time = 0;
    public static final long HOURS = 1;
    public static final long MINUTES = 10;
    public static final long MILLISECONDS = 60000; //1 minutes


    @Override
    public void onCreate() {
        mHandler = new Handler();
        timer = new Timer();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            startService(new Intent(SendGPSDataService.this, GetLocationService.class));
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay_time,HOURS*MINUTES*MILLISECONDS);
       // scheduleNotify();
    }

    private void scheduleNotify()
    {
        Intent notificationIntent = new Intent(this, GetLocationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,1000*10*1,pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //timerTask.cancel();
       // stopSelf();
    }
}
