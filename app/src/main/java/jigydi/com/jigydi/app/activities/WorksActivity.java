package jigydi.com.jigydi.app.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;

import java.math.BigDecimal;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.adapters.WorksPager;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.DepositRecords;
import jigydi.com.jigydi.app.model.FeedbackResponse;
import jigydi.com.jigydi.app.model.GenericResponse;
import jigydi.com.jigydi.app.model.WorkerDepositModel;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import jigydi.com.jigydi.app.utils.SendGPSDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saikrupa on 6/13/2017.
 */

public class WorksActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    public static ViewPager viewPager;

    public static String usersTypes = "1";


    public Toolbar toolbar;
    int possition = 0;
    boolean navToAssigned=false;
    ApiInterface apiService;
    boolean is_supervisor;
    String worker_id;
    LinearLayout llInfo;
    TextView tvCredit,tvLimit;
    public static TextView tvComplaints;
    RatingBar rbWorkerRatings;
    TextView ratingsText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workers_pager_layout);
        //Initializing the tablayout
        llInfo=(LinearLayout)findViewById(R.id.llInfo);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tvCredit =(TextView) findViewById(R.id.tvCredit);
        tvLimit =(TextView) findViewById(R.id.tvLimit);
        tvComplaints =(TextView) findViewById(R.id.tvComplaints);
        rbWorkerRatings=(RatingBar) findViewById(R.id.rbWorkerRatings);
        ratingsText=(TextView) findViewById(R.id.ratingText);
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Available Works"));
        tabLayout.addTab(tabLayout.newTab().setText("Assigned works"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        navToAssigned=getIntent().getBooleanExtra("nav_to_allocated",false);

        final WorksPager adapter = new WorksPager(getSupportFragmentManager(), tabLayout.getTabCount(),navToAssigned);
        getDeviceID();
        String mem_type=CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
        worker_id=CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
        is_supervisor = mem_type.equals("1");

        if(is_supervisor)
            llInfo.setVisibility(View.GONE);
        else {
            getWorkerDepositAmountDetails();
            getWorkerRatings();
        }


        checkLocationPermission();

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        if(navToAssigned)
            possition=1;
        viewPager.setCurrentItem(possition);
        setUpToolBar();
    }

    private void getWorkerRatings() {

        NetworkUtils.checkInternetConnection(WorksActivity.this, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<FeedbackResponse> call;

                call = apiService.getWorkerFeedback(worker_id);

                call.enqueue(new Callback<FeedbackResponse>() {
                    @Override
                    public void onResponse(Call<FeedbackResponse>call, Response<FeedbackResponse> response) {
                        //loading.dismiss();
                        FeedbackResponse movies = response.body();

                        if(movies!=null && movies.getSuccess()) {
                           //movies.getRecord();
                            rbWorkerRatings.setRating(round(Float.parseFloat(movies.getRecord()),2));
                            ratingsText.setText(" ("+round(Float.parseFloat(movies.getRecord()),2)+" / 5)");
                        }else {
                            rbWorkerRatings.setRating(0);
                            ratingsText.setText(" ("+0 + " / 5)");
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedbackResponse>call, Throwable t) {

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });

            }

            @Override
            public void onNetworkNotAvailable() {
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public void getWorkerDepositAmountDetails() {

        NetworkUtils.checkInternetConnection(WorksActivity.this, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<WorkerDepositModel> call;

                    call = apiService.getWorkerDepositAmount(worker_id);

                call.enqueue(new Callback<WorkerDepositModel>() {
                    @Override
                    public void onResponse(Call<WorkerDepositModel>call, Response<WorkerDepositModel> response) {
                        //loading.dismiss();
                        WorkerDepositModel movies = response.body();

                        if(movies!=null && movies.getSuccess()) {
                            DepositRecords depositRecords=movies.getRecord();
                            tvCredit.setText("Credit: "+depositRecords.getFloatingAmount());
                            tvLimit.setText("Limit: "+movies.getLimit());

                        }
                    }

                    @Override
                    public void onFailure(Call<WorkerDepositModel>call, Throwable t) {

                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });

            }

            @Override
            public void onNetworkNotAvailable() {
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getDeviceID() {
        if (FirebaseInstanceId.getInstance().getToken() != null){
            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            Log.v("GCMID", deviceToken);
            sendDeviceToken(deviceToken);
        }
    }

    private void sendDeviceToken(final String deviceToken) {
        final  String userID= CoreCustomPreferenceManager.getInstance(WorksActivity.this).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        NetworkUtils.checkInternetConnection(WorksActivity.this, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<GenericResponse> call= apiService.saveDeviceToken(userID,"ANDROID",deviceToken);

                call.enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                        Log.e("Testing","");
                    }

                    @Override
                    public void onFailure(Call<GenericResponse>call, Throwable t) {
                        Log.e("Testing","");
                    }
                });
            }
            @Override
            public void onNetworkNotAvailable() {


            }
        });
    }


    private void checkLocationPermission() {
        if (is_supervisor) {

            if (ContextCompat.checkSelfPermission(WorksActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(WorksActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WorksActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);


            }else{
                startLocationUpdates();
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startLocationUpdates();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog.Builder dialogue=new AlertDialog.Builder(WorksActivity.this);
                    dialogue.setMessage("You have to enable the location permission to use this app");
                    dialogue.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                        }
                    });
                    dialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialogue.show();




                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    protected void startLocationUpdates() {
        Intent intent = new Intent(WorksActivity.this, SendGPSDataService.class);
        //if (!isMyServiceRunning(SendGPSDataService.class)) {
        stopService(intent);
        startService(intent);
        //}else {
        //    startService(intent);
        //}
    }


    private void setUpToolBar() {

        String WorkerNameStr=CoreCustomPreferenceManager.getInstance(WorksActivity.this).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.WORKER_NAME);
        String serviceName=CoreCustomPreferenceManager.getInstance(WorksActivity.this).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.SERVICE_NAME);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setTitle(WorkerName+">>"+serviceName);
        TextView workerName=(TextView)findViewById(R.id.tvWorkerName);
        TextView workType=(TextView)findViewById(R.id.tvWorkerType);
        workerName.setText(WorkerNameStr);
        workType.setText(serviceName);

        toolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleApperance);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        if(is_supervisor)
            menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WorksActivity.this);
                alertDialogBuilder.setMessage("Are you sure to want logout");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                CoreCustomPreferenceManager.getInstance(WorksActivity.this).setSharedBooleanValue(
                                        CoreCustomPreferenceManager.PreferenceKeys.IS_USER_LOGIN, false);
                                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        });
                alertDialogBuilder.setNegativeButton("No",null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                break;
            case R.id.action_report:
                Intent intent=new Intent(WorksActivity.this,CommissionReportActivity.class);
                intent.putExtra("worker_id",worker_id);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        possition = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        possition = tab.getPosition();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        possition = tab.getPosition();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Creating our pager adapter


    }



    @Override
    public void onClick(View v) {
        Intent i=new Intent(getApplicationContext(),WorkDetailedActivity.class);
        startActivity(i);
    }
}