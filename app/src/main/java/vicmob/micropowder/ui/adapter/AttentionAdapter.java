package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vicmob.earn.R;


/**
 * author: Twisted
 * created on: 2017/9/11 13:18
 * description:
 */

public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.MyViewHolder> {
    private List<String> mDataContent;
    private Context mContext;
    private List<String> mDataTitle;

    public AttentionAdapter(Context mContext, List<String> mDataTitle, List<String> mDataContent) {
        this.mContext = mContext;
        this.mDataTitle=mDataTitle;
        this.mDataContent = mDataContent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AttentionAdapter.MyViewHolder holder = new AttentionAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_attention_view, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.AttentionContentTv.setText(mDataContent.get(position));
        holder.AttentionTitleTv.setText(mDataTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataTitle.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView AttentionTitleTv;
        TextView AttentionContentTv;
        public MyViewHolder(View view) {
            super(view);
            AttentionContentTv = (TextView) view.findViewById(R.id.attention_content_tv);
            AttentionTitleTv = (TextView) view.findViewById(R.id.attention_title_tv);
        }
    }
}
