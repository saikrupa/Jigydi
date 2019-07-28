package jigydi.com.jigydi.app.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.adapters.AvailableWorksAdapter;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.AvilableWorksList;
import jigydi.com.jigydi.app.model.ComplaintsListModel;
import jigydi.com.jigydi.app.model.ComplaintsResponse;
import jigydi.com.jigydi.app.model.GenericResponse;
import jigydi.com.jigydi.app.model.WorkDetailModel;
import jigydi.com.jigydi.app.model.WorkDetaildResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.AppConstants;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Saikrupa on 6/16/2017.
 */

public class WorkDetailedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public Toolbar toolbar;
    TextView txtName,txtWork,txtAddress,txtTimings,txtLandMark,txtStartDate,txtEndDate,txtPhoneNumber;
    ImageView ivComment;
    EditText etWorkDetails;
    ApiInterface apiService;
    String userID,workedID,orderID,startDate,endDate,orderDescription,workDetailsText;
    WorkDetailModel workDetails;
    boolean isStartDatePressed;
    boolean isHoldPressed=false;
    List<String> complaintsID;
    List<String> complaintsValue;
    String selectedComplaintID;
    Button btnStart,btnComplete,btnInspect,btnSpComplaint;
    LinearLayout liWorkerLayout,liSupervisorLayout;
    boolean is_supervisor;
    Spinner spnTimings;
    ArrayList<String> commentsList;
    LinkedHashMap<Integer, String> timings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_detailed_layout);
        txtName=(TextView)findViewById(R.id.tvUserName);
        txtWork=(TextView)findViewById(R.id.txtWorkType);
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtTimings=(TextView)findViewById(R.id.txtAvailability);
        txtLandMark=(TextView)findViewById(R.id.txtLandMark);
        txtStartDate=(TextView)findViewById(R.id.txtStartDate);
        txtEndDate=(TextView)findViewById(R.id.txtEndDate);
        txtPhoneNumber=(TextView)findViewById(R.id.txtPhoneNumber);
        etWorkDetails=(EditText) findViewById(R.id.etDetails);
        ivComment=(ImageView) findViewById(R.id.ivComment);
        btnStart=(Button) findViewById(R.id.btnStart);
        btnComplete=(Button) findViewById(R.id.btnComplete);
        liWorkerLayout=(LinearLayout)findViewById(R.id.liWorkerLayout);
        liSupervisorLayout=(LinearLayout)findViewById(R.id.liSupervisorLayout);
        userID= CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        workedID= CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
        String mem_type=CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
        is_supervisor = mem_type.equals("1");
        complaintsID=new ArrayList<>();
        complaintsValue=new ArrayList<>();
        timings = new LinkedHashMap<>();

        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        orderID=getIntent().getStringExtra("order_id");

        setUpToolBar();

        loadData();
        if(is_supervisor) {
            loadSpinner(10 + "");
            liWorkerLayout.setVisibility(View.GONE);
            liSupervisorLayout.setVisibility(View.VISIBLE);
        }
        else {
            loadSpinner(11 + "");
            liWorkerLayout.setVisibility(View.VISIBLE);
            liSupervisorLayout.setVisibility(View.GONE);
        }

        txtPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPhoneNumber.getText().toString().length()>0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" +  txtPhoneNumber.getText().toString()));
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Number not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loadSpinner(final String code_id) {

        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
                    @Override
                    public void onNetworkAvailable() {
                        loading.dismiss();
                        Call < ComplaintsResponse > call = apiService.getComplaintsList(44 + "", code_id);
                        call.enqueue(new Callback<ComplaintsResponse>() {
                            @Override
                            public void onResponse(Call<ComplaintsResponse>call, Response<ComplaintsResponse> response) {
                                ComplaintsResponse movies = response.body();
                                if(movies.getSuccess())
                                {
                                    List<ComplaintsListModel> list=movies.getRecord();
                                    for (ComplaintsListModel data:list) {
                                        complaintsID.add(data.getOptionId());
                                        complaintsValue.add(data.getLookupValue());
                                    }
                                }
                                Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                            }

                            @Override
                            public void onFailure(Call<ComplaintsResponse>call, Throwable t) {
                                // Log error here since request failed
                                loading.dismiss();
                                Log.e("LoginActivity", t.toString());
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNetworkNotAvailable() {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadData() {
        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<WorkDetaildResponse> call;

                    call = apiService.getOderDetails(orderID, userID);

                call.enqueue(new Callback<WorkDetaildResponse>() {
                    @Override
                    public void onResponse(Call<WorkDetaildResponse>call, Response<WorkDetaildResponse> response) {
                        loading.dismiss();
                        WorkDetaildResponse movies = response.body();
                        if(movies.getSuccess())
                        {
                            workDetails=movies.getRecord();
                            assignDataToScreen();
                        }else {
                            Toast.makeText(getApplicationContext(),"problem loading data",Toast.LENGTH_SHORT).show();
                        }
                        Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<WorkDetaildResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ivComment(View view){
        String workComment=etWorkDetails.getText().toString();
        if(workComment.length()>0)
            commentRequest(workComment);
        else
            Toast.makeText(getApplicationContext(),"Enter some comment to send",Toast.LENGTH_SHORT).show();

    }

    public void btnHold(View view){

        /*Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        //int year=c.gety
        int thisMonth = c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);
        int day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, WorkDetailedActivity.this, year, thisMonth, day);
        isHoldPressed=true;
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();*/

        final Dialog dialog = new Dialog(WorkDetailedActivity.this,R.style.Dialog);
        // Include dialog.xml file
        dialog.setContentView(R.layout.reschedule_dialogue);
        // Set dialog title
        dialog.setTitle("Reschedule");
        LinearLayout llWorkDate=(LinearLayout)dialog.findViewById(R.id.llWorkDate);
        spnTimings=(Spinner)dialog.findViewById(R.id.spnPrefTime);
        final TextView txtStartDate = (TextView) dialog.findViewById(R.id.txtStartDate);
        getTimingsDropDown(false);

        llWorkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                //int year=c.gety
                int hours = c.get(Calendar.HOUR_OF_DAY);
                if (hours >= AppConstants.MaxHours) {
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }
                int thisMonth = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        WorkDetailedActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();

                        String selectedData=year + "-" + (month + 1) + "-" + dayOfMonth;
                        String currentDate=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
                        getTimingsDropDown(isGreater(selectedData,currentDate));
                        txtStartDate.setText(selectedData);
                    }
                }, year, thisMonth, day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        dialog.show();

        Button btnReschedule = (Button) dialog.findViewById(R.id.btnReschedule);
        // if decline button is clicked, close the custom dialog
        btnReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if(txtStartDate.getText().toString().length()>0) {
                    holdApiRequest(txtStartDate.getText().toString(),getSelectedTimeValue(spnTimings.getSelectedItem().toString())+"");
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"Select the reschedule date",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }

    public int getSelectedTimeValue(String time) {
        switch (time) {
            case "9:00 AM - 11:00 AM":
                return 0;
            case "11:00 AM - 01:00 PM":
                return 1;
            case "01:00 PM - 03:00 PM":
                return 2;
            case "03:00 PM - 05:00 PM":
                return 3;
            case "05:00 PM - 07:00 PM":
                return 4;
        }
        return -1;
    }
    public boolean isGreater(String selected,String actual){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date selectedDate = sdf.parse(selected);
            Date actualDate = sdf.parse(actual);
            if(selectedDate.after(actualDate))
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getTimingsDropDown(boolean validateCondition) {
        timings.put(9, "9:00 AM - 11:00 AM");
        timings.put(11, "11:00 AM - 01:00 PM");
        timings.put(13, "01:00 PM - 03:00 PM");
        timings.put(15, "03:00 PM - 05:00 PM");
        timings.put(17, "05:00 PM - 07:00 PM");
        List<String> timingsDropdown = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        //int year=c.gety
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if (hours >= AppConstants.MaxHours || validateCondition) {
            timingsDropdown = Arrays.asList(getResources().getStringArray(R.array.timings));
        } else {
            for (Integer entry : timings.keySet()) {
                if (hours < entry)
                    timingsDropdown.add(timings.get(entry));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timingsDropdown);
        spnTimings.setAdapter(adapter);
    }

    public void viewComments(View view){
        if(commentsList!=null && commentsList.size()>0) {
            Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
            intent.putExtra("order_id",orderID);
            //intent.putStringArrayListExtra("list",commentsList);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"No comments available",Toast.LENGTH_SHORT).show();
        }
    }

    private void assignDataToScreen() {
        String text = "<b>" + workDetails.getCusName() + "</b> "+"<br />"+workDetails.getCustomer_available_timing()+"<br />"+workDetails.getCusAddress()
                +"<br />"+workDetails.getCusAddLandMark()+"<br />"+workDetails.getCusContact1()+"<br />"+workDetails.getCusContact2()+"<br />"+workDetails.getOrderDescription();

        txtName.setText(Html.fromHtml(text));
        txtAddress.setVisibility(View.GONE);
        txtLandMark.setVisibility(View.GONE);
        txtWork.setVisibility(View.GONE);
        txtTimings.setVisibility(View.GONE);
       // etWorkDetails.setText(workDetails.getWorkDetails());
        commentsList=workDetails.getWorkDetails();
        txtStartDate.setText(workDetails.getStartDate());
        txtEndDate.setText(workDetails.getCompletedDate());
        txtPhoneNumber.setVisibility(View.GONE);
        if (workDetails.getWorkerStatus()!=null&&workDetails.getWorkerStatus().equals("2")){
            btnStart.setVisibility(View.GONE);
            btnComplete.setVisibility(View.VISIBLE);
        }else if(workDetails.getWorkerStatus()!=null&&workDetails.getWorkerStatus().equals("3")){
            btnStart.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }else if(workDetails.getWorkerStatus()!=null&&workDetails.getWorkerStatus().equals("194")){
            btnStart.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }else{
            btnStart.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.GONE);
        }
    }


    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Work info");
        toolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleApperance);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),WorksActivity.class);
        intent.putExtra("nav_to_allocated",true);
        startActivity(intent);
        finish();
    }

    public void btnComplete(View v){
        startDate=txtStartDate.getText().toString();
        endDate=txtEndDate.getText().toString();
        orderDescription=etWorkDetails.getText().toString();

        if(startDate.length()<=0)
            Toast.makeText(getApplicationContext(),"Select start date",Toast.LENGTH_SHORT).show();
        else if(endDate.length()<=0)
            Toast.makeText(getApplicationContext(),"Select end date",Toast.LENGTH_SHORT).show();
        else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.estimated_amout_dialogue);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final EditText etAmount = (EditText) dialog.findViewById(R.id.etAmount);
            final EditText etMaterialAmount = (EditText) dialog.findViewById(R.id.etMaterialAmount);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAmount.getText().toString().length() <= 0&& etMaterialAmount.getText().toString().length()<=0) {
                        Toast.makeText(getApplicationContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                    } else {
                        int amount = Integer.parseInt(etAmount.getText().toString());
                        int materialValue=Integer.parseInt(etMaterialAmount.getText().toString());
                        startWorkRequest(amount,materialValue,3);
                      //  String completeComment= "Work " + workDetails.getOrderDescription()+" has completed ";
                        dialog.dismiss();
                    }
                }
            });
            dialog.setTitle("Enter work amount");
            dialog.show();
        }
    }
    public void btnStart(View v){
        startDate=txtStartDate.getText().toString();
        endDate=txtEndDate.getText().toString();
        orderDescription=etWorkDetails.getText().toString();

        if(startDate.length()<=0)
            Toast.makeText(getApplicationContext(),"Select start date",Toast.LENGTH_SHORT).show();
        else if(endDate.length()<=0)
            Toast.makeText(getApplicationContext(),"Select end date",Toast.LENGTH_SHORT).show();
        else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.estimated_amout_dialogue);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final EditText etAmount = (EditText) dialog.findViewById(R.id.etAmount);
            final EditText etMaterialAmount = (EditText) dialog.findViewById(R.id.etMaterialAmount);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (etAmount.getText().toString().length() <= 0&& etMaterialAmount.getText().toString().length()<=0) {
                        Toast.makeText(getApplicationContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                    } else {
                        int amount = Integer.parseInt(etAmount.getText().toString());
                        int materialValue=Integer.parseInt(etMaterialAmount.getText().toString());
                        startWorkRequest(amount,materialValue,2);
                        dialog.dismiss();
                    }
                }
            });
            dialog.setTitle("Enter estimate amount");
            dialog.show();
        }
    }

    private void startWorkRequest(final int amount,final int materialValue,final int workStatus) {

        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
                    @Override
                    public void onNetworkAvailable() {
                        Call < GenericResponse > call = apiService.startWork(userID, orderID, workedID, startDate, endDate, materialValue+ "", amount+"", orderDescription,workStatus);
                        call.enqueue(new Callback<GenericResponse>() {
                            @Override
                            public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                                loading.dismiss();
                                GenericResponse movies = response.body();
                                if(movies.getSuccess())
                                {
                                    Toast.makeText(getApplicationContext(),"Status updated",Toast.LENGTH_SHORT).show();
                                   // finish();
                                    onBackPressed();

                                }
                                Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                            }

                            @Override
                            public void onFailure(Call<GenericResponse>call, Throwable t) {
                                // Log error here since request failed
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity", t.toString());
                            }
                        });
                    }

                    @Override
                    public void onNetworkNotAvailable() {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void commentRequest(final String comment) {

        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call < GenericResponse > call = apiService.commentOnWork(userID, orderID, workedID, comment);
                call.enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                        loading.dismiss();
                        GenericResponse movies = response.body();
                        if(movies.getSuccess())
                        {
                            Toast.makeText(getApplicationContext(),"Comment sent",Toast.LENGTH_SHORT).show();
                            etWorkDetails.setText("");
                            loadData();
                            //finish();
                        }
                        Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<GenericResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void btnComplaint(View v){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.complaint_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnSubmit=(Button)dialog.findViewById(R.id.btnSubmit);
        Spinner spnComplaints=(Spinner)dialog.findViewById(R.id.spnComplaints);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, complaintsValue);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnComplaints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedComplaintID=complaintsID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnComplaints.setAdapter(dataAdapter);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                raiseComplaint();
            }
        });
        dialog.setTitle("Complaint");
        dialog.show();
    }

    private void raiseComplaint() {
        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
                    @Override
                    public void onNetworkAvailable() {
                        Call < GenericResponse > call = apiService.raiseComplaint(userID,orderID,workedID, selectedComplaintID);
                        call.enqueue(new Callback<GenericResponse>() {
                            @Override
                            public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                                loading.dismiss();
                                GenericResponse movies = response.body();
                                if(movies.getSuccess())
                                {
                                    Toast.makeText(getApplicationContext(),"Complaint raised",Toast.LENGTH_SHORT).show();
                                    finish();
                                    onBackPressed();
                                }
                                Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                            }

                            @Override
                            public void onFailure(Call<GenericResponse>call, Throwable t) {
                                // Log error here since request failed
                                loading.dismiss();
                                Log.e("LoginActivity", t.toString());
                                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onNetworkNotAvailable() {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void tvStartDate(View v){


        Calendar c = Calendar.getInstance();
        //int year=c.gety
        int thisMonth = c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);
        int day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, WorkDetailedActivity.this, year, thisMonth, day);
        isStartDatePressed=true;
        datePickerDialog.show();
    }
    public void tvFinishDate(View v){
        Calendar c = Calendar.getInstance();
        //int year=c.gety
        int thisMonth = c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);
        int day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, WorkDetailedActivity.this, year, thisMonth, day);
        isStartDatePressed=false;
        datePickerDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WorkDetailedActivity.this);
                alertDialogBuilder.setMessage("Are you sure to want logout");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                CoreCustomPreferenceManager.getInstance(WorkDetailedActivity.this).setSharedBooleanValue(
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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(isStartDatePressed)
            txtStartDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
        else if(isHoldPressed){
            showTimeDialogue(dayOfMonth+"-"+(month+1)+"-"+year);
        }
        else
            txtEndDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
    }

    private void showTimeDialogue(final String date) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(WorkDetailedActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                holdApiRequest(date,selectedHour+":"+selectedMinute);
            }
        }, 0, 0, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


    }

    private void holdApiRequest(final String date, final String time) {
        //Toast.makeText(getApplicationContext(),time,Toast.LENGTH_SHORT).show();
        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call < GenericResponse > call = apiService.rescheduleWork(userID,orderID,workedID, date,time);
                call.enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                        loading.dismiss();
                        GenericResponse movies = response.body();
                        if(movies!=null && movies.getSuccess())
                        {
                            Toast.makeText(getApplicationContext(),"Rescheduled successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            onBackPressed();
                        }
                        //Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<GenericResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Log.e("LoginActivity", t.toString());
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void btnInspect(View view){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.inspect_dialouge);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final CheckBox cbInspectedWork = (CheckBox) dialog.findViewById(R.id.cbInspectedWork);
        final EditText etComments = (EditText) dialog.findViewById(R.id.etComments);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbInspectedWork.isChecked()){
                    String comments=etComments.getText().toString();
                    sendWorkInspectedRequest(comments,true);
                }else{
                    Toast.makeText(getApplicationContext(),"You can't submit until you inpect the place",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setTitle("Enter estimate amount");
        dialog.show();
    }

    private void sendWorkInspectedRequest(final String comments, final boolean isInspected) {

        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call < GenericResponse > call = apiService.inspectWorkSupervisor(userID, orderID,isInspected,comments);
                call.enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                        loading.dismiss();
                        GenericResponse movies = response.body();
                        if(movies.getSuccess())
                        {
                            Toast.makeText(getApplicationContext(),"Status updated",Toast.LENGTH_SHORT).show();
                            finish();
                            onBackPressed();
                        }
                        Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<GenericResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void btnSupervisorComplaints(View v){

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.supervisor_complaints_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btnSubmit=(Button)dialog.findViewById(R.id.btnSubmit);
        Spinner spnComplaints=(Spinner)dialog.findViewById(R.id.spnComplaints);
        final EditText etComments=(EditText) dialog.findViewById(R.id.etComments);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, complaintsValue);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnComplaints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedComplaintID=complaintsID.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnComplaints.setAdapter(dataAdapter);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String comments=etComments.getText().toString();
                raiseSupervisorComplaint(selectedComplaintID,comments);

            }
        });
        dialog.setTitle("Complaint");
        dialog.show();
    }

    private void raiseSupervisorComplaint(final String selectedComplaintID, final String comments) {

        final ProgressDialog loading = ProgressDialog.show(WorkDetailedActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call < GenericResponse > call = apiService.reportComplaintsSupervisor(userID, orderID,selectedComplaintID,comments);
                call.enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse>call, Response<GenericResponse> response) {
                        loading.dismiss();
                        GenericResponse movies = response.body();
                        if(movies.getSuccess())
                        {
                            Toast.makeText(getApplicationContext(),"Status updated",Toast.LENGTH_SHORT).show();
                            finish();
                            onBackPressed();
                        }
                        Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<GenericResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });


    }

}
