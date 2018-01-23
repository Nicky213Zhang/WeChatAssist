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
import vicmob.micropowder.daoman.bean.PublicNumBean;
import vicmob.micropowder.daoman.dao.PublicNumDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;

/**
 * author: Twisted
 * created on: 2017/12/6 11:02
 * description:公众号适配器
 */

public class PublicNumAdapter extends RecyclerView.Adapter {
    private List<PublicNumBean> mPublicNumBeanList = new ArrayList<>();

    private Context mContext;

    private String mPublicNumName;
    /**
     * 初始化第一条内容数据
     */
    private String mFristPublicNumText;

    public PublicNumAdapter(Context context, List<PublicNumBean> PublicNumBeanList) {
        mPublicNumBeanList = PublicNumBeanList;
        mContext = context;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.public_item, parent, false);
        PublicNumViewHolder publicNumViewHolder = new PublicNumViewHolder(view);
        publicNumViewHolder.setIsRecyclable(false);
        return publicNumViewHolder;
    }

    /**
     *  绑定并给ViewHolder赋值
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final  PublicNumViewHolder publicNumViewHolder = (PublicNumViewHolder) holder;
        //设置内容
        publicNumViewHolder.tvContent.setText(mPublicNumBeanList.get(position).getPublicNumName());
        /**
         * 点击删除
         */
        publicNumViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除数据库
                PublicNumDao friendCircleDao = new PublicNumDao(mContext);
                friendCircleDao.delete(mPublicNumBeanList.get(position));

                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mPublicNumBeanList.remove(position);
                notifyDataSetChanged();
                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPublicNumBeanList != null ? mPublicNumBeanList.size() : 0;
    }

    static class PublicNumViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_content) TextView tvContent;
        @BindView(R.id.iv_select_one) ImageView ivSelectOne;
        @BindView(R.id.ll_item) RelativeLayout llItem;
        @BindView(R.id.tv_delete) TextView tvDelete;

        PublicNumViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
