package jigydi.com.jigydi.app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.activities.WorkDetailedActivity;
import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.AvilableWorksList;
import jigydi.com.jigydi.app.model.GenericResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saikrupa on 6/28/2017.
 */

public class AvailableWorksAdapter extends RecyclerView.Adapter<AvailableWorksAdapter.MyViewHolder>{


    List<AvilableWorksList> list;
    Context mContext;
    ApiInterface apiService;
    String userID,workerID;
    RefreshPage mListener;
    boolean is_from_available;
    boolean is_supervisor;


    public AvailableWorksAdapter(Context _context,boolean is_from_available,List<AvilableWorksList> _list,RefreshPage mListener){
        mContext = _context;
        list = _list;
        this.mListener = mListener;
        this.is_from_available = is_from_available;
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        userID = CoreCustomPreferenceManager.getInstance(mContext).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.USER_ID);
        workerID = CoreCustomPreferenceManager.getInstance(mContext).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID);
        String mem_type = CoreCustomPreferenceManager.getInstance(mContext).getSharedStringValue(
                CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE);
        is_supervisor = mem_type.equals("1");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.works_list_item, null);

        AvailableWorksAdapter.MyViewHolder myViewHolder = new AvailableWorksAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AvilableWorksList item = list.get(position);
        holder.txtName.setText(item.getOrderNumber());
        holder.txtWork.setText(item.getServiceName());
        holder.txtDate.setText(item.getAvailability());
        holder.txtTime.setText(item.getReminderTime());
        holder.txtAddress.setText(item.getZone_center());
        if (!is_from_available) {
            // holder.icAccept.setVisibility(View.GONE);
            holder.txtWorkStatus.setVisibility(View.VISIBLE);
            if (item.getOrderStatus() == 1)
                holder.txtWorkStatus.setText("Processing");
            else if (item.getOrderStatus() == 2)
                holder.txtWorkStatus.setText("Work in progress");
            else if (item.getOrderStatus() == 3)
                holder.txtWorkStatus.setText("Completed");
            else if (item.getOrderStatus() == 180)
                holder.txtWorkStatus.setText("Hold");
            else if (item.getOrderStatus() == 194) {
                holder.txtWorkStatus.setText("Complaint");
                holder.liWorkDetails.setBackgroundColor(mContext.getResources().getColor(R.color.liteRed));
                holder.cvDetailsItem.setBackgroundColor(mContext.getResources().getColor(R.color.liteRed));
            }
        } else
            holder.txtWorkStatus.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName, txtWork, txtAddress, txtDate,txtTime, txtWorkStatus;
        // ImageView icAccept;
        LinearLayout liWorkDetails;
        CardView cvDetailsItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.tvUserName);
            txtWork = (TextView) itemView.findViewById(R.id.txtWork);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtWorkStatus = (TextView) itemView.findViewById(R.id.txtWorkStatus);
            liWorkDetails = (LinearLayout) itemView.findViewById(R.id.liWorkDetails);
            cvDetailsItem = (CardView) itemView.findViewById(R.id.cvDetailsItem);
            //  icAccept=(ImageView) itemView.findViewById(R.id.ivAccept);

            // icAccept.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            final AvilableWorksList item = list.get(getAdapterPosition());

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(mContext);
                }
                builder.setTitle("Alert")
                        .setMessage("Are you sure you want to assign this work")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // continue with delete
                                final ProgressDialog loading = ProgressDialog.show(mContext, "", mContext.getString(R.string.please_wait), false, false);
                                loading.show();
                                NetworkUtils.checkInternetConnection(mContext, new NetworkUtils.NetworkStatusListener() {
                                    @Override
                                    public void onNetworkAvailable() {
                                        Call<GenericResponse> call;
                                        if(is_supervisor)
                                            call = apiService.assignWorkSupervisor(userID,item.getOrderId());
                                        else
                                            call = apiService.assignWorkIfAvailable(item.getOrderId(), workerID, userID);
                                        call.enqueue(new Callback<GenericResponse>() {
                                            @Override
                                            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                                                loading.dismiss();
                                                GenericResponse responseData = response.body();
                                                Log.v("Hello", responseData.getSuccess() + "");
                                                if (responseData.getSuccess()) {
                                                   /* AvilableWorksList workdata = list.get(getAdapterPosition());
                                                    Intent i = new Intent(mContext, WorkDetailedActivity.class);
                                                    i.putExtra("order_id", item.getOrderId());
                                                    mContext.startActivity(i);
                                                    ((Activity)mContext).finish();*/
                                                    Toast.makeText(mContext, "Order allocated successfully", Toast.LENGTH_SHORT).show();
                                                    mListener.refreshPage(true);
                                                } else {
                                                    Toast.makeText(mContext,responseData.getError(), Toast.LENGTH_LONG).show();
                                                    mListener.refreshPage(false);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GenericResponse> call, Throwable t) {
                                                // Log error here since request failed
                                                Log.e("LoginActivity", t.toString());
                                                loading.dismiss();
                                                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onNetworkNotAvailable() {
                                        loading.dismiss();
                                        Toast.makeText(mContext, "Internet not available", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

        }
    }
    public interface RefreshPage{
        void refreshPage(boolean isAvailable);
    }
}
