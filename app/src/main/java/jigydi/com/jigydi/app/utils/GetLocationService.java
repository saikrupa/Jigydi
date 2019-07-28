package jigydi.com.jigydi.app.utils;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import java.text.SimpleDateFormat;
import java.util.Date;

import jigydi.com.jigydi.app.model.UpdateUserLatLongResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by rajeshkantipudi on 30/03/17.
 */

public class GetLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private String LOGSERVICE = GetLocationService.class.getSimpleName();
    private String mobileNumber;
    private ApiInterface service;

    @Override
    public void onCreate() {
        super.onCreate();
        service = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        mobileNumber = CoreCustomPreferenceManager.getInstance(this).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        buildGoogleApiClient();
        MyLog.i(LOGSERVICE, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.i(LOGSERVICE, "onStartCommand");

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return START_NOT_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        MyLog.i(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            stopSelf();
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            MyLog.i(LOGSERVICE, "lat " + l.getLatitude());
            MyLog.i(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        MyLog.i(LOGSERVICE, "onConnectionSuspended " + i);
    }

    @Override
    public void onLocationChanged(final Location location) {
        MyLog.i(LOGSERVICE, "lat " + location.getLatitude());
        MyLog.i(LOGSERVICE, "lng " + location.getLongitude());
        LatLng mLocation = (new LatLng(location.getLatitude(), location.getLongitude()));
        //Toast.makeText(this, "" + location.getLongitude() + "  " + location.getLatitude(), Toast.LENGTH_SHORT).show();
        //EventBus.getDefault().post(mLocation);

        // Storing the user location to preferences
        CoreCustomPreferenceManager.getInstance(GetLocationService.this).setSharedDoubleValue(
                CoreCustomPreferenceManager.PreferenceKeys.LATITUDE, location.getLatitude());
        CoreCustomPreferenceManager.getInstance(GetLocationService.this).setSharedDoubleValue(
                CoreCustomPreferenceManager.PreferenceKeys.LONGITUDE, location.getLongitude());


//        stopLocationUpdate();
//        stopSelf();

        NetworkUtils.checkInternetConnection(this, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());


                Call<UpdateUserLatLongResponse> latLongResponseCall = service.shareLocationUpdates(mobileNumber,location.getLatitude() +","+ location.getLongitude() + "&"+currentDateAndTime);
                latLongResponseCall.enqueue(new Callback<UpdateUserLatLongResponse>() {
                    @Override
                    public void onResponse(Call<UpdateUserLatLongResponse> call, retrofit2.Response<UpdateUserLatLongResponse> response) {
                        if (response != null) {
                            UpdateUserLatLongResponse latLongResponse = response.body();
                            if (latLongResponse != null && latLongResponse.getSuccess()) {
                              //  Toast.makeText(GetLocationService.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                        stopLocationUpdate();
                        stopSelf();
                    }

                    @Override
                    public void onFailure(Call<UpdateUserLatLongResponse> call, Throwable t) {
                        stopLocationUpdate();
                        Toast.makeText(GetLocationService.this, "failure", Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                stopLocationUpdate();
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i(LOGSERVICE, "onDestroy");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        MyLog.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            stopSelf();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }
}
