package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vicmob.earn.R;
import vicmob.micropowder.config.Delete;
import vicmob.micropowder.daoman.bean.MapSearchBean;

/**
 * Created by Twisted on 2017/7/3.
 * <p/>
 * 地点管理适配器
 */

public class LocationRecyclerAdapter extends RecyclerView.Adapter {

    private List<MapSearchBean> mMapSearchList = new ArrayList<>();
    private Context mContext;
    private Delete delete;


    public LocationRecyclerAdapter(Context context, List<MapSearchBean> mMapSearchList, Delete delete) {
        this.mMapSearchList = mMapSearchList;
        mContext = context;
        this.delete = delete;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.white_list_item, parent, false);
        LocationViewHolder locationViewHolder = new LocationViewHolder(view);
        return locationViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
        //设置内容
        locationViewHolder.mTvContent.setText(mMapSearchList.get(position).getSearchAddresses());


        /**
         * 点击删除
         */
        locationViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.Delete(position);
                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mMapSearchList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mMapSearchList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMapSearchList != null ? mMapSearchList.size() : 0;
    }


    static class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.iv_select_one)
        ImageView mIvSelectOne;
        @BindView(R.id.tv_delete)
        TextView mTvDelete;
        @BindView(R.id.ll_item)
        RelativeLayout mLlItem;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
