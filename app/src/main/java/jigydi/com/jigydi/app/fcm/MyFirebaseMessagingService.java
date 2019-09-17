package jigydi.com.jigydi.app.fcm;

/**
 * Created by Venu on 1/6/2016.
 */

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.activities.SplashScreen;
import jigydi.com.jigydi.app.utils.MyLog;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";
    private int mNotificationType;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> params = remoteMessage.getData();

        if (params.size() > 0) {

            //Calling method to generate notification
                JSONObject obj=new JSONObject(params);
            try {
                String message = obj.getString("title");
                String title=obj.getString("message");
                sendNotification(title,message);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            MyLog.e(TAG, "Push Failure:: ");
        }
    }


    private void sendNotification(String title, String messageBody) {
        PendingIntent pendingIntent = null;



            Intent intent = new Intent(this, SplashScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        initChannels(this);
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder= new NotificationCompat.Builder(this,"default")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());

     /*   if (mNotificationType == 5) {
            String current_session_id = CoreCustomPreferenceManager.getInstance(getApplication()).getSharedStringValue(CoreCustomPreferenceManager.PreferenceKeys.CURRENT_SESSION_ID);
            if (!current_session_id.equalsIgnoreCase(mSessionCode)) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        } else {
            notificationManager.notify(0, notificationBuilder.build());
        }*/
    }
    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

}