package jigydi.com.jigydi.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.adapters.CommissionReportAdapter;
import jigydi.com.jigydi.app.model.CommissionReportModel;
import jigydi.com.jigydi.app.model.CommissionReportRecords;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saikrupa on 6/30/2018.
 */

public class CommissionReportActivity extends AppCompatActivity{


    public Toolbar toolbar;
    RecyclerView recyclerView;
    ApiInterface apiService;
    LinearLayoutManager linearLayoutManager;
    String worker_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commission_report_recyclarview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        // comments=getIntent().getExtras().getStringArrayList("list");
        worker_id=getIntent().getStringExtra("worker_id");
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext())
        {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        loadData(worker_id);
        setUpToolBar();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Commission Report");
        toolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleApperance);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData(final String worker_id) {
        final ProgressDialog loading = ProgressDialog.show(CommissionReportActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<CommissionReportModel> call;
                    call = apiService.getCommissionReport(worker_id);
                call.enqueue(new Callback<CommissionReportModel>() {
                    @Override
                    public void onResponse(Call<CommissionReportModel> call, Response<CommissionReportModel> response) {
                        loading.dismiss();
                        CommissionReportModel response1=response.body();
                        if(response1.getSuccess()) {
                            List<CommissionReportRecords> record=response1.getRecord();
                            CommissionReportAdapter adapter = new CommissionReportAdapter(CommissionReportActivity.this, record);
                            recyclerView.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<CommissionReportModel> call, Throwable t) {
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
