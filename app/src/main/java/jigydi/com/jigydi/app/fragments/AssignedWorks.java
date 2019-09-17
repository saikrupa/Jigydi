package jigydi.com.jigydi.app.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.activities.LoginActivity;
import jigydi.com.jigydi.app.activities.SplashScreen;
import jigydi.com.jigydi.app.activities.WorkDetailedActivity;
import jigydi.com.jigydi.app.activities.WorksActivity;
import jigydi.com.jigydi.app.adapters.AssignedWorksAdapter;
import jigydi.com.jigydi.app.adapters.AvailableWorksAdapter;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.AvilableWorksList;
import jigydi.com.jigydi.app.model.LoginResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saikrupa on 6/14/2017.
 */

public class AssignedWorks extends Fragment{


    RecyclerView recyclerView;
    Button txtFilter;
    ApiInterface apiService;
    LinearLayoutManager linearLayoutManager;
    boolean is_supervisor;
    int filterValue=1;
    List<AvilableWorksList> completeList;
    boolean isViewShowing=false;
    SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.assigned_works_layout,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        txtFilter=(Button)view.findViewById(R.id.txtFilter);
        completeList=new ArrayList<>();
        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        srl=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        srl.setRefreshing(false);
                    }
                }, 2000);
                if(!is_supervisor) {
                    assert ((WorksActivity)getActivity()) != null;
                    ((WorksActivity)getActivity()).getWorkerDepositAmountDetails();
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private void applyFilter() {

        RadioGroup rgFilter;

        final Dialog dialog = new Dialog(getActivity());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.CENTER;
        dialog.setContentView(R.layout.filter_elements);
        rgFilter=(RadioGroup)dialog.findViewById(R.id.rgFilter);
        rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int radioId = group.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) group.getChildAt(radioId);
                //String selection = (String) btn.getText();
                filterValue=Integer.parseInt(btn.getTag().toString());
                filterListValues();
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Filter");
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            String userID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
            String workedID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
            String mem_type=CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
            is_supervisor = mem_type.equals("1");
            if(is_supervisor)
                filterValue=3;
            if(getView()!=null) {
                isViewShowing=true;
                loadData(userID, workedID);
            }else{
                isViewShowing=false;
            }
        } else {

        }
    }


    private void loadData(final String userId, final String workerID) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getActivity(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<AvailableWorksResponse> call;
                if(is_supervisor)
                    call = apiService.allocatedWorksSupervisor(userId);
                else
                    call = apiService.allocatedWorks(userId,workerID);
                call.enqueue(new Callback<AvailableWorksResponse>() {
                    @Override
                    public void onResponse(Call<AvailableWorksResponse>call, Response<AvailableWorksResponse> response) {
                        loading.dismiss();
                        AvailableWorksResponse movies = response.body();
                        completeList=movies.getRecord();
                        if(completeList!=null && completeList.size()>0) {
                            int complaintsCount =0;
                            for(int i=0;i<completeList.size();i++){
                                AvilableWorksList data=completeList.get(i);
                                // this is for checking the complaints count
                                if(data.getOrderStatus()==194)
                                    complaintsCount++;

                            }
                            if(complaintsCount>0)
                                filterValue=194;
                            filterListValues();
                            if(!is_supervisor) {
                                assert ((WorksActivity)getActivity()) != null;
                                ((WorksActivity)getActivity()).getWorkerDepositAmountDetails();
                            }

                            txtFilter.setVisibility(View.VISIBLE);
                        }
                        else
                            txtFilter.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<AvailableWorksResponse>call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });

            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getActivity(),"Internet not available",Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isViewShowing){
            String userID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
            String workedID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
            String mem_type=CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                    CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
            is_supervisor = mem_type.equals("1");
            if(is_supervisor)
                filterValue=3;
                loadData(userID, workedID);
        }
    }

    private List<AvilableWorksList> filterListValues() {
        List<AvilableWorksList> sortedList=new ArrayList<>();
        int complaintsCount=0;
        for(int i=0;i<completeList.size();i++){
            AvilableWorksList data=completeList.get(i);
            // this is for checking the complaints count
            if(data.getOrderStatus()==194)
                complaintsCount++;

            if(data.getOrderStatus()==filterValue){
                sortedList.add(data);
            }
        }
        if(sortedList.size()>0) {
            AssignedWorksAdapter adapter = new AssignedWorksAdapter(getActivity(),false ,sortedList, new AssignedWorksAdapter.RefreshPage() {
                @Override
                public void refreshPage() {
                }
            });
            recyclerView.setAdapter(adapter);
        }else{
            recyclerView.setAdapter(null);
            Toast.makeText(getActivity(),"no data",Toast.LENGTH_SHORT).show();
        }
      //  complaintsCount++;
        if(complaintsCount>0)
        {
            WorksActivity.tvComplaints.setTextSize(20);
            WorksActivity.tvComplaints.setTypeface(null, Typeface.BOLD);
            WorksActivity.tvComplaints.setTextColor(getResources().getColor(R.color.liteRed));
        }
        WorksActivity.tvComplaints.setText("Complaints: "+complaintsCount);
        return sortedList;
    }

}
