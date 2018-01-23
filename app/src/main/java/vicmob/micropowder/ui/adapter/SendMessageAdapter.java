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
import vicmob.micropowder.daoman.bean.SendMessageBean;
import vicmob.micropowder.daoman.dao.SendMessageDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * author: Twisted
 * created on: 2017/7/20 16:04
 * description:一键发消息适配器
 */

public class SendMessageAdapter extends RecyclerView.Adapter{
    private List<SendMessageBean> mSendMessageBeenList = new ArrayList<>();
    private Context mContext;
    private String mMessage;
    /**
     * 初始化第一条内容数据
     */
    private String mFristText;
    public SendMessageAdapter(Context context, List<SendMessageBean> mSendMessageBeanList) {
        this.mSendMessageBeenList = mSendMessageBeanList;
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
        SendMessageViewHolder mSendMessageViewHolder = new SendMessageViewHolder(view);
        mSendMessageViewHolder.setIsRecyclable(false);
        return mSendMessageViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SendMessageViewHolder mSendMessageViewHolder = (SendMessageViewHolder) holder;
        //设置内容
        mSendMessageViewHolder.mTvContent.setText(mSendMessageBeenList.get(position).getMessageText());
        if (position == 0) {
            //设置第一个条目置顶选中
            mSendMessageViewHolder.mTvContent.setTextColor(Color.parseColor("#F58505"));
            mSendMessageViewHolder.mIvSelectOne.setVisibility(View.VISIBLE);

            mFristText = mSendMessageViewHolder.mTvContent.getText().toString().trim();
            PrefUtils.putString(mContext, "OneKeyDialogText", mFristText);
        }

        /**
         * 点击应用
         */
        mSendMessageViewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先删除选择条目，再添加条目
                SendMessageDao mSendMessageDao = new SendMessageDao(mContext);
                mSendMessageDao.delete(mSendMessageBeenList.get(position));
                mSendMessageDao.add(mSendMessageBeenList.get(position));

                //更新适配器，与数据库同步
                SendMessageBean remove = mSendMessageBeenList.remove(position);
                mSendMessageBeenList.add(0, remove);
                notifyDataSetChanged();

                //内容储存到SP
                mMessage = mSendMessageViewHolder.mTvContent.getText().toString().trim();
                PrefUtils.putString(mContext, "OneKeyDialogText", mMessage);

            }
        });

        /**
         * 点击删除
         */
        mSendMessageViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                SendMessageDao mSendMessageDao = new SendMessageDao(mContext);
                mSendMessageDao.delete(mSendMessageBeenList.get(position));

                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mSendMessageBeenList.remove(position);
                notifyDataSetChanged();
                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
                if(mSendMessageBeenList.size()==0){
                    PrefUtils.putString(mContext, "OneKeyDialogText", "");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSendMessageBeenList != null ? mSendMessageBeenList.size() : 0;
    }


    static class SendMessageViewHolder extends RecyclerView.ViewHolder {
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

        SendMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
