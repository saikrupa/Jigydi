package jigydi.com.jigydi.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.activities.WorkDetailedActivity;
import jigydi.com.jigydi.app.model.AvilableWorksList;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;

/**
 * Created by click2clinic on 28-07-2017.
 */

public class AssignedWorksAdapter extends RecyclerView.Adapter<AssignedWorksAdapter.MyViewHolder> {


    List<AvilableWorksList> list;
    Context mContext;
    ApiInterface apiService;
    String userID, workerID;
    AssignedWorksAdapter.RefreshPage mListener;
    boolean is_from_available;
    boolean is_supervisor;


    public AssignedWorksAdapter(Context _context, boolean is_from_available, List<AvilableWorksList> _list, AssignedWorksAdapter.RefreshPage mListener) {
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
    public AssignedWorksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.works_list_item, null);

        AssignedWorksAdapter.MyViewHolder myViewHolder = new AssignedWorksAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AssignedWorksAdapter.MyViewHolder holder, int position) {
        AvilableWorksList item = list.get(position);
        holder.txtName.setText(item.getOrderNumber());
        holder.txtWork.setText(item.getServiceName());
        holder.txtDate.setText(item.getAvailability());
        holder.txtTime.setText(item.getReminderTime());
        holder.txtAddress.setText(item.getCusAddress().replace("\n", ","));
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

    public interface RefreshPage {
        void refreshPage();
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


            Intent i = new Intent(mContext, WorkDetailedActivity.class);
            i.putExtra("order_id", item.getOrderId());
            mContext.startActivity(i);
            ((Activity) mContext).finish();

        }
    }
}