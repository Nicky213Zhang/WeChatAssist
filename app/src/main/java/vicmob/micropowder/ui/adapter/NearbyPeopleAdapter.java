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
import vicmob.micropowder.daoman.bean.NearbyPeopleBean;
import vicmob.micropowder.daoman.dao.NearbyPeopleDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/29.
 * <p/>
 * 附近人适配器
 */
public class NearbyPeopleAdapter extends RecyclerView.Adapter {

    private List<NearbyPeopleBean> mNearbyPeopleBeanList = new ArrayList<>();

    private Context mContext;

    private String mNearbyPeopleText;
    /**
     * 初始化第一条内容数据
     */
    private String mFristNearbyPeopleText;

    public NearbyPeopleAdapter(Context context, List<NearbyPeopleBean> nearbyPeopleBean) {
        mNearbyPeopleBeanList = nearbyPeopleBean;
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
        NearbyViewHolder nearbyViewHolder = new NearbyViewHolder(view);
        nearbyViewHolder.setIsRecyclable(false);
        return nearbyViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NearbyViewHolder nearbyViewHolder = (NearbyViewHolder) holder;

        //设置内容
        nearbyViewHolder.mTvContent.setText(mNearbyPeopleBeanList.get(position).getNearbyContent());
        if (position == 0) {
            //设置第一个条目置顶选中
            nearbyViewHolder.mTvContent.setTextColor(Color.parseColor("#F58505"));
            nearbyViewHolder.mIvSelectOne.setVisibility(View.VISIBLE);

            mFristNearbyPeopleText = nearbyViewHolder.mTvContent.getText().toString().trim();
            PrefUtils.putString(mContext, "mNearText", mFristNearbyPeopleText);
        }

        /**
         * 点击应用
         */
        nearbyViewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //先删除选择条目，再添加条目
                NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(mContext);
                nearbyPeopleDao.delete(mNearbyPeopleBeanList.get(position));
                nearbyPeopleDao.add(mNearbyPeopleBeanList.get(position));

                //更新适配器，与数据库同步
                NearbyPeopleBean remove = mNearbyPeopleBeanList.remove(position);
                mNearbyPeopleBeanList.add(0, remove);
                notifyDataSetChanged();

                //内容储存到SP
                mNearbyPeopleText = nearbyViewHolder.mTvContent.getText().toString().trim();
                PrefUtils.putString(mContext, "mNearText", mNearbyPeopleText);

            }
        });

        /**
         * 点击删除
         */
        nearbyViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(mContext);
                nearbyPeopleDao.delete(mNearbyPeopleBeanList.get(position));

                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mNearbyPeopleBeanList.remove(position);
                notifyDataSetChanged();
                if (mNearbyPeopleBeanList.size() == 0 || mNearbyPeopleBeanList == null) {
                    PrefUtils.putString(mContext, "mNearText", "");
                }
                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearbyPeopleBeanList != null ? mNearbyPeopleBeanList.size() : 0;
    }

    static class NearbyViewHolder extends RecyclerView.ViewHolder {
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

        NearbyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
