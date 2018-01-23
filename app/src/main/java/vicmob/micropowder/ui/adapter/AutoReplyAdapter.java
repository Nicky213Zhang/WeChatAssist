package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vicmob.earn.R;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.daoman.bean.AutoReplyBean;
import vicmob.micropowder.daoman.dao.AutoReplyDao;
import vicmob.micropowder.ui.views.AutoContentDialog;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * 自动回复适配器
 * Created by Twisted on 2017/7/4.
 */

public class AutoReplyAdapter extends RecyclerView.Adapter {


    private List<AutoReplyBean> mAutoReplyBeanList = new ArrayList<>();
    private Context context;
    private AutoContentDialog mAutoContentDialog;
    private String autoKeyword;
    private String autoContent;

    public AutoReplyAdapter(List<AutoReplyBean> autoReplyBeanList, Context context) {
        mAutoReplyBeanList = autoReplyBeanList;
        this.context = context;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    /**
     * 创建viewholder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.auto_reply_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        //设置关键字
        mViewHolder.mTvAutoreplyKeyword.setText(mAutoReplyBeanList.get(position).getKeyWord());
        //设置内容
        mViewHolder.mTvAutoreplyContent.setText(mAutoReplyBeanList.get(position).getContent());
        /**
         * 点击删除
         */
        mViewHolder.mTvAutoreplyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除数据库
                AutoReplyDao mAutoReplyDao = new AutoReplyDao(context);
                mAutoReplyDao.delete(mAutoReplyBeanList.get(position));
                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mAutoReplyBeanList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mAutoReplyBeanList.size());
                //关闭滑动
                SwipeLayoutManager.create().closeLayout();
            }
        });

        /**
         * 点击编辑
         */
        mViewHolder.mTvAutoreplyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strKeyWord = mAutoReplyBeanList.get(position).getKeyWord().toString().trim();
                String strContent = mAutoReplyBeanList.get(position).getContent().toString().trim();
                PrefUtils.putString(context, "strKeyword", strKeyWord);
                PrefUtils.putString(context, "strContent", strContent);

                mAutoContentDialog = new AutoContentDialog(context, new Callback() {
                    @Override
                    /**
                     * 点击添加内容
                     */
                    public void Positive() {
                        autoKeyword = PrefUtils.getString(context, "autoKeyword", null);
                        autoContent = PrefUtils.getString(context, "autoContent", null);
                        if (TextUtils.isEmpty(autoContent) || TextUtils.isEmpty(autoKeyword)) {
                            MyToast.show(context, "关键词和内容不能为空");
                        }
                        else {
                            //先删除选中条目,在添加条目
                            AutoReplyDao mAutoReplyDao = new AutoReplyDao(context);
                            mAutoReplyDao.delete(mAutoReplyBeanList.get(position));
                            //刷新界面（不能够刷新，用移除删除项，在重新加载）
                            mAutoReplyBeanList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mAutoReplyBeanList.size());
                            //添加数据库
                            AutoReplyDao autoReplyDao = new AutoReplyDao(context);
                            AutoReplyBean autoReplyBean = new AutoReplyBean();
                            autoReplyBean.setKeyWord(autoKeyword);
                            autoReplyBean.setContent(autoContent);
                            autoReplyDao.add(autoReplyBean);
                            //数据添加到集合并置顶选中，并刷新界面
                            mAutoReplyBeanList.add(0, autoReplyBean);
                            notifyDataSetChanged();
                        }
                        //关闭滑动
                        SwipeLayoutManager.create().closeLayout();
                        mAutoContentDialog.dismiss();
                    }

                    @Override
                    public void Negative() {
                        mAutoContentDialog.dismiss();
                    }
                });

                mAutoContentDialog.setCancelable(true);
                mAutoContentDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                mAutoContentDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAutoReplyBeanList != null ? mAutoReplyBeanList.size() : 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_autoreply_keyword)
        TextView mTvAutoreplyKeyword;
        @BindView(R.id.tv_autoreply_content)
        TextView mTvAutoreplyContent;
        @BindView(R.id.tv_autoreply_edit)
        TextView mTvAutoreplyEdit;
        @BindView(R.id.tv_autoreply_delete)
        TextView mTvAutoreplyDelete;
        @BindView(R.id.ll_auto_item)
        LinearLayout mLlAutoItem;
        @BindView(R.id.ll_autoreply_content_in)
        LinearLayout mLlAutoreplyContentIn;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
