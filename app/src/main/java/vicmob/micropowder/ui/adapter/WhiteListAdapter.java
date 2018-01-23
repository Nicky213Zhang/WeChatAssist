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
import vicmob.micropowder.daoman.bean.WhiteListContentBean;
import vicmob.micropowder.daoman.dao.WhiteListDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 白名单适配器
 */
public class WhiteListAdapter extends RecyclerView.Adapter {

    private List<WhiteListContentBean> mWhiteListContentList = new ArrayList<>();

    private Context mContext;

    /**
     * 初始化第一条内容数据
     */
    private String mText;

    public WhiteListAdapter(Context context, List<WhiteListContentBean> whiteListContentBeen) {
        mWhiteListContentList = whiteListContentBeen;
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
        View view = getLayoutInflater().inflate(R.layout.white_list_item, parent, false);
        WhiteViewHolder whiteViewHolder = new WhiteViewHolder(view);
        return whiteViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final WhiteViewHolder whiteViewHolder = (WhiteViewHolder) holder;

        //设置内容
        whiteViewHolder.mTvContent.setText(mWhiteListContentList.get(position).getWhiteContent());
        mText = whiteViewHolder.mTvContent.getText().toString().trim();
        PrefUtils.putString(mContext, "mText", mText);

        /**
         * 点击删除
         */
        whiteViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                WhiteListDao whiteListDao = new WhiteListDao(mContext);
                whiteListDao.delete(mWhiteListContentList.get(position));
                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mWhiteListContentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mWhiteListContentList.size());

                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWhiteListContentList != null ? mWhiteListContentList.size() : 0;

    }


    static class WhiteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.iv_select_one)
        ImageView mIvSelectOne;
        @BindView(R.id.ll_item)
        RelativeLayout mLlItem;
        @BindView(R.id.tv_delete)
        TextView mTvDelete;

        WhiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
