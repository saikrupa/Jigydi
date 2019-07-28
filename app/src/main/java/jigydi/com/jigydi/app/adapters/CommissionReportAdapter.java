package jigydi.com.jigydi.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.model.CommissionReportModel;
import jigydi.com.jigydi.app.model.CommissionReportRecords;

/**
 * Created by Saikrupa on 6/30/2018.
 */

public class CommissionReportAdapter extends RecyclerView.Adapter<CommissionReportAdapter.MyViewHolder> {

    Context mContext;
    List<CommissionReportRecords> mList;

    public CommissionReportAdapter(Context _context,List<CommissionReportRecords> _list){
        mContext=_context;
        mList=_list;
    }

    @NonNull
    @Override
    public CommissionReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.deposite_deductions_item,null);

        return new CommissionReportAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionReportAdapter.MyViewHolder holder, int position) {

        CommissionReportRecords singleRecord=mList.get(position);
        holder.tvDate.setText(singleRecord.getCreatedDate());
        holder.tvJigydiAmount.setText(singleRecord.getJigydiPayment());
        holder.tvWorkAmount.setText(singleRecord.getTotalPayment());
        holder.tvWorkId.setText(singleRecord.getOrderNumber());
        holder.tvCustomer.setText(singleRecord.getCusName());
        if(singleRecord.getLookupValue()!=null && !singleRecord.getLookupValue().equals(""))
            holder.tvRemarks.setText(singleRecord.getLookupValue());
        else
            holder.tvRemarks.setText("Commission");


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvWorkId;
        TextView tvCustomer;
        TextView tvWorkAmount;
        TextView tvJigydiAmount;
        TextView tvRemarks;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvCustomer=(TextView)itemView.findViewById(R.id.tvCustomer);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvWorkId=(TextView)itemView.findViewById(R.id.tvWorkId);
            tvWorkAmount=(TextView)itemView.findViewById(R.id.tvWorkValue);
            tvJigydiAmount=(TextView)itemView.findViewById(R.id.tvJigydiValue);
            tvRemarks=(TextView)itemView.findViewById(R.id.tvRemarks);
        }
    }
}
