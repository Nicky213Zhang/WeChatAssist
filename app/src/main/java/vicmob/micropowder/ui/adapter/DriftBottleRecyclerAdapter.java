package vicmob.micropowder.ui.adapter;


import android.content.Context;
import android.graphics.Color;
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
import vicmob.micropowder.daoman.bean.DriftBottleContentBean;

import vicmob.micropowder.daoman.dao.DriftBottleDao;

import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 漂流瓶适配器
 */
public class DriftBottleRecyclerAdapter extends RecyclerView.Adapter {

    private List<DriftBottleContentBean> mDriftBottleContentList = new ArrayList<>();

    private Context mContext;

    private String mDriftBottle;
    /**
     * 初始化第一条内容数据
     */
    private String mFristBottleText;

    public DriftBottleRecyclerAdapter(Context context, List<DriftBottleContentBean> driftBottleContentBeen) {
        mDriftBottleContentList = driftBottleContentBeen;
        mContext = context;
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
        View view = getLayoutInflater().inflate(R.layout.friend_circle_item, parent, false);
        BottleViewHolder bottleViewHolder = new BottleViewHolder(view);
        bottleViewHolder.setIsRecyclable(false);
        return bottleViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BottleViewHolder bottleViewHolder = (BottleViewHolder) holder;

        //设置内容
        bottleViewHolder.mTvContent.setText(mDriftBottleContentList.get(position).getBottleContent());
        if (position == 0) {
            //设置第一个条目置顶选中
            bottleViewHolder.mTvContent.setTextColor(Color.parseColor("#F58505"));
            bottleViewHolder.mIvSelectOne.setVisibility(View.VISIBLE);

            mFristBottleText = bottleViewHolder.mTvContent.getText().toString().trim();
            PrefUtils.putString(mContext, "mDriftBottle", mFristBottleText);
        }
        /**
         * 点击应用
         */
        bottleViewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //先删除选择条目，再添加条目
                DriftBottleDao friendCircleDao = new DriftBottleDao(mContext);
                friendCircleDao.delete(mDriftBottleContentList.get(position));
                friendCircleDao.add(mDriftBottleContentList.get(position));

                //更新适配器，与数据库同步
                DriftBottleContentBean remove = mDriftBottleContentList.remove(position);
                mDriftBottleContentList.add(0, remove);
                notifyDataSetChanged();

                //内容储存到SP
                mDriftBottle = bottleViewHolder.mTvContent.getText().toString().trim();
                PrefUtils.putString(mContext, "mDriftBottle", mDriftBottle);

            }
        });

        /**
         * 点击删除
         */
        bottleViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                DriftBottleDao friendCircleDao = new DriftBottleDao(mContext);
                friendCircleDao.delete(mDriftBottleContentList.get(position));

                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mDriftBottleContentList.remove(position);
                notifyDataSetChanged();

                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDriftBottleContentList != null ? mDriftBottleContentList.size() : 0;

    }


    static class BottleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.iv_select_one)
        ImageView mIvSelectOne;
        @BindView(R.id.ll_item)
        RelativeLayout mLlItem;
        @BindView(R.id.tv_edit)
        TextView mTvEdit;
        @BindView(R.id.tv_delete)
        TextView mTvDelete;

        BottleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
