package jigydi.com.jigydi.app.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.adapters.AvailableWorksAdapter;
import jigydi.com.jigydi.app.adapters.CommentsAdapter;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.AvilableWorksList;
import jigydi.com.jigydi.app.model.Comments;
import jigydi.com.jigydi.app.model.CommentsResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saikrupa on 8/1/2017.
 */

public class CommentsActivity extends AppCompatActivity{



    public Toolbar toolbar;
    RecyclerView recyclerView;
    ApiInterface apiService;
    LinearLayoutManager linearLayoutManager;
    boolean is_supervisor;
    ArrayList<String> comments;
    List<Comments> record;
    String orderId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyelarview_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
       // comments=getIntent().getExtras().getStringArrayList("list");
        orderId=getIntent().getStringExtra("order_id");
        record=new ArrayList<>();
        //  String workedID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
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
        String userID = CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        String mem_type = CoreCustomPreferenceManager.getInstance(getApplicationContext()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
        is_supervisor = mem_type.equals("1");

        loadData(orderId);

       setUpToolBar();



    }


    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Comments");
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


    private void loadData(final String orderId) {
        final ProgressDialog loading = ProgressDialog.show(CommentsActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getApplicationContext(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<CommentsResponse> call;
                if(is_supervisor)
                    call = apiService.getCommentsList(orderId);
                else
                    call = apiService.getCommentsList(orderId);
                call.enqueue(new Callback<CommentsResponse>() {
                    @Override
                    public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
                        loading.dismiss();
                        CommentsResponse response1=response.body();
                        if(response1.getSuccess()) {
                            record=response1.getRecord();
                            CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, record);
                            recyclerView.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<CommentsResponse> call, Throwable t) {
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
