package jigydi.com.jigydi.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.reflect.Field;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.utils.CustomProgressBar;
import jigydi.com.jigydi.app.utils.MyLog;

import static jigydi.com.jigydi.app.utils.AppConstants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static jigydi.com.jigydi.app.utils.AppConstants.REQUEST_CHECK_SETTINGS;
import static jigydi.com.jigydi.app.utils.AppConstants.UPDATE_INTERVAL_IN_MILLISECONDS;

/**
 * Created by click2clinic on 07-06-2017.
 */

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private CustomProgressBar progressDialog;
    private BaseActivity.KillReceiver mKillReceiver;

    private FirebaseAnalytics mFirebaseAnalytics;

    //For Location

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;



    private int KEY_REQUEST_PERMISSION = 1345;
    private String mobileNumber;
    private Context context;

    private ProgressDialog simpleDialog;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mKillReceiver = new BaseActivity.KillReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_KILL");
        registerReceiver(mKillReceiver,
                filter);
        context = BaseActivity.this;
        mTypeface = Typeface.createFromAsset(context.getAssets(), "CANDARA.ttf");

    }

    public void showSimpleProgressBar() {
        simpleDialog = new ProgressDialog(this, R.style.MyTheme);
        simpleDialog.setCancelable(true);
        simpleDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        simpleDialog.show();
    }

    public void GetLocation(Context context) {
        this.context = context;
       // service = ServiceGenerator.GetBaseUrl(FileUploadService.class);
       /* mobileNumber = CoreCustomPreferenceManager.getInstance(context).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MOBILE_NO);*/
        mRequestingLocationUpdates = false;
        //Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        mGoogleApiClient.connect();
    }

    public void setCustomFont(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(mTypeface, Typeface.NORMAL);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(mTypeface, Typeface.NORMAL);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(mTypeface, Typeface.NORMAL);
        }

    }

    public void setToolbarFont(Toolbar toolbar) {
        try {
            final Field staticField = Toolbar.class
                    .getDeclaredField("mTitleTextView");
            staticField.setAccessible(true);
            TextView toolbarTitle = (TextView) staticField.get(toolbar);
            setCustomFont(toolbarTitle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mKillReceiver);
    }

    private final class KillReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    public void showProgressBar() {
        String title = "";
        progressDialog = new CustomProgressBar(BaseActivity.this, title);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (simpleDialog != null && simpleDialog.isShowing()) {
            simpleDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                //logHomeClickedEvent(this.getClass().getName());
                /*AppUtility.clearNotificationActivities(BaseActivity.this);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));*/
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    protected synchronized void buildGoogleApiClient() {
        MyLog.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        MyLog.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            getPermissions();
        }
    }

    public void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, KEY_REQUEST_PERMISSION);
        } else {
            checkLocationSettings();
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                MyLog.i(TAG, "All location settings are satisfied.");
                //startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                MyLog.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(BaseActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    MyLog.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                MyLog.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        MyLog.i(TAG, "User agreed to make required location settings changes.");
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        MyLog.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*protected void startLocationUpdates() {
        Intent intent = new Intent(BaseActivity.this, SendGPSDataService.class);
        //if (!isMyServiceRunning(SendGPSDataService.class)) {
        stopService(intent);
        startService(intent);
        //}else {
        //    startService(intent);
        //}
    }
*/
  /*  @Override
    public void onLocationChanged(Location location) {
    }
*/

   /* protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == KEY_REQUEST_PERMISSION) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                checkLocationSettings();
            } else {
                //getPermissions();
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}