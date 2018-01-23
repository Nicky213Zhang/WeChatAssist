package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vicmob.earn.R;

/**
 * Created by qq944 on 2017/7/26.
 */

public class MyQqHeadAdapter extends RecyclerView.Adapter<MyQqHeadAdapter.MyViewHolder> implements View.OnClickListener{
    public MyQqHeadAdapter(Context context, List<String> rlvList) {
        this.context = context;
        this.rlvList = rlvList;
    }

    private Context context;
    private List<String> rlvList = new ArrayList<String>();
    private OnItemClickListener mOnItemClickListener = null;
    private int num;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gridview_assistant, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //添加点击事件
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position==num){
            holder.tv_rlv_item.setSelected(true);
            holder.v_fenge.setSelected(true);
        }else {
            holder.tv_rlv_item.setSelected(false);
            holder.v_fenge.setSelected(false);
        }

        holder.tv_rlv_item.setText(rlvList.get(position));

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return rlvList.size();
    }

    public void refresh(int num){
        this.num=num;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private final View v_fenge;
        private TextView tv_rlv_item;

        public MyViewHolder(View view)
        {
            super(view);
            tv_rlv_item = (TextView) view.findViewById(R.id.tv_gv_classify);
            v_fenge = ((View) view.findViewById(R.id.v_fenge));
        }
    }
    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
