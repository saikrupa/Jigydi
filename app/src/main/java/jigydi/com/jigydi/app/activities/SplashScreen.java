package jigydi.com.jigydi.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import jigydi.com.jigydi.app.utils.GPSTracker;
import jigydi.com.jigydi.app.utils.SendGPSDataService;

/**
 * Created by click2clinic on 07-06-2017.
 */

public class SplashScreen extends Activity{

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        FirebaseApp.initializeApp(this);
        getDeviceID();
        new Handler().postDelayed(new Runnable() {

            /*
                    * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
               // checkIsUserLoggedIn();
                //startService(new Intent(SplashScreen.this, GPSTracker.class));

                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void getDeviceID() {
        if (FirebaseInstanceId.getInstance().getToken() != null){
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            Log.v("GCMID", deviceToken);
        }
    }



    private void checkIsUserLoggedIn() {

        boolean is_login= CoreCustomPreferenceManager.getInstance(SplashScreen.this).getSharedBooleanValue(
                CoreCustomPreferenceManager.PreferenceKeys.IS_USER_LOGIN);
        if(is_login) {
            Intent i = new Intent(getApplicationContext(), WorksActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }


}
