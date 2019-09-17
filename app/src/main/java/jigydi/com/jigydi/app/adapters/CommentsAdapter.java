package jigydi.com.jigydi.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.model.Comments;

/**
 * Created by Saikrupa on 8/1/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    Context mContext;
    List<Comments> mData;
    public CommentsAdapter(Context context, List<Comments> data){
        mContext=context;
        mData=data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.individual_comments, null);

        CommentsAdapter.MyViewHolder myViewHolder = new CommentsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comments singleRecord=mData.get(position);
        holder.txtComments.setText(singleRecord.getNotification());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtComments;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtComments=(TextView)itemView.findViewById(R.id.txtCommentText);
        }
    }
}
