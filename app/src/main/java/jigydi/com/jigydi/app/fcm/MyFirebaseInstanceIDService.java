package jigydi.com.jigydi.app.fcm;

/**
 * Created by SERVER on 10/1/2016.
 */


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import jigydi.com.jigydi.app.utils.MyLog;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        MyLog.d(TAG, "Refreshed token: " + refreshedToken);
        CoreCustomPreferenceManager.getInstance(this).setSharedBooleanValue(
                CoreCustomPreferenceManager.PreferenceKeys.PUSH_GCM_STATUS, false);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}