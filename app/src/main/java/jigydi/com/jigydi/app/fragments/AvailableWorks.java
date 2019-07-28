package jigydi.com.jigydi.app.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.activities.WorkDetailedActivity;
import jigydi.com.jigydi.app.activities.WorksActivity;
import jigydi.com.jigydi.app.adapters.AvailableWorksAdapter;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.AvilableWorksList;
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

public class AvailableWorks extends Fragment{


    RecyclerView recyclerView;
    ApiInterface apiService;
    LinearLayoutManager linearLayoutManager;
    boolean is_supervisor;
    boolean navToAssigned;
    SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_recyelarview, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        srl=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);

      //  String workedID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity())
        {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        String userID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        String mem_type=CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
        is_supervisor = mem_type.equals("1");
        savedInstanceState=getArguments();
        navToAssigned=savedInstanceState.getBoolean("nav_type");
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        srl.setRefreshing(false);
                    }
                }, 2000);
                if(!is_supervisor)
                    ((WorksActivity)getActivity()).getWorkerDepositAmountDetails();
            }
        });
        loadData(userID);
        return view;


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()) {

            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        String userID= CoreCustomPreferenceManager.getInstance(getActivity()).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        loadData(userID);
    }

    private void loadData(final String userId) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(getActivity(), new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<AvailableWorksResponse> call;
                if(is_supervisor)
                    call = apiService.availableWorksSupervisor(userId);
                else
                    call = apiService.categoryBasedWorks(userId);
                call.enqueue(new Callback<AvailableWorksResponse>() {
                    @Override
                    public void onResponse(Call<AvailableWorksResponse> call, Response<AvailableWorksResponse> response) {
                        loading.dismiss();
                        AvailableWorksResponse movies = response.body();
                        if(movies!=null) {
                            List<AvilableWorksList> list = movies.getRecord();
                            if (list != null && list.size() > 0) {

                                AvailableWorksAdapter adapter = new AvailableWorksAdapter(getActivity(), true, list, new AvailableWorksAdapter.RefreshPage() {
                                    @Override
                                    public void refreshPage(boolean isAssigned) {
                                        loadData(userId);
                                        if (isAssigned) {
                                            WorksActivity.viewPager.setCurrentItem(1);
                                        }
                                    }
                                });
                                if(!is_supervisor) {
                                    assert ((WorksActivity) getActivity()) != null;
                                    ((WorksActivity) getActivity()).getWorkerDepositAmountDetails();
                                }
                                recyclerView.setAdapter(adapter);

                            }
                        }else{
                            Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AvailableWorksResponse> call, Throwable t) {
                        // Log error here since request failed
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

}
