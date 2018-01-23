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
import vicmob.micropowder.daoman.bean.FriendCircleContentBean;
import vicmob.micropowder.daoman.dao.FriendCircleDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 朋友圈适配器
 */
public class FriendCircleRecyclerAdapter extends RecyclerView.Adapter {

    private List<FriendCircleContentBean> mFriendCircleContentList = new ArrayList<>();

    private Context mContext;

    private String mFriendText;
    /**
     * 初始化第一条内容数据
     */
    private String mFristFriendText;

    public FriendCircleRecyclerAdapter(Context context, List<FriendCircleContentBean> friendCircleContentBean) {
        mFriendCircleContentList = friendCircleContentBean;
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
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        friendViewHolder.setIsRecyclable(false);
        return friendViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FriendViewHolder friendViewHolder = (FriendViewHolder) holder;

        //设置内容
        friendViewHolder.mTvContent.setText(mFriendCircleContentList.get(position).getFriendContent());
        if (position == 0) {
            //设置第一个条目置顶选中
            friendViewHolder.mTvContent.setTextColor(Color.parseColor("#F58505"));
            friendViewHolder.mIvSelectOne.setVisibility(View.VISIBLE);

            mFristFriendText = friendViewHolder.mTvContent.getText().toString().trim();
            PrefUtils.putString(mContext, "mFriendText", mFristFriendText);
        }
        /**
         * 点击应用
         */
        friendViewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //先删除选择条目，再添加条目
                FriendCircleDao friendCircleDao = new FriendCircleDao(mContext);
                friendCircleDao.delete(mFriendCircleContentList.get(position));
                friendCircleDao.add(mFriendCircleContentList.get(position));

                //更新适配器，与数据库同步
                FriendCircleContentBean remove = mFriendCircleContentList.remove(position);
                mFriendCircleContentList.add(0, remove);
                notifyDataSetChanged();

                //内容储存到SP
                mFriendText = friendViewHolder.mTvContent.getText().toString().trim();
                PrefUtils.putString(mContext, "mFriendText", mFriendText);

            }
        });

        /**
         * 点击删除
         */
        friendViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                FriendCircleDao friendCircleDao = new FriendCircleDao(mContext);
                friendCircleDao.delete(mFriendCircleContentList.get(position));

                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mFriendCircleContentList.remove(position);
                notifyDataSetChanged();

                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendCircleContentList != null ? mFriendCircleContentList.size() : 0;

    }


    static class FriendViewHolder extends RecyclerView.ViewHolder {
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

        FriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
