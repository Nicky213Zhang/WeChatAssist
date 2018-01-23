package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vicmob.earn.R;
import vicmob.micropowder.config.Delete;
import vicmob.micropowder.daoman.bean.AccountBean;
import vicmob.micropowder.daoman.bean.AccountUpdateBean;
import vicmob.micropowder.daoman.dao.AccountDao;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Twisted on 2017/7/3.
 * <p/>
 * 账号管理适配器
 */

public class AccountRecyclerAdapter extends RecyclerView.Adapter {
    private List<AccountUpdateBean.AutoAccountBean> mAccountList = new ArrayList<>();

    private Context mContext;

    private Delete mDelete;

    /**
     * 初始化第一条内容数据
     */
    private String mFristAccountText;
    private String mAccountUserName;

    public AccountRecyclerAdapter(Context context, List<AccountUpdateBean.AutoAccountBean> accountBean, Delete delete) {
        mAccountList = accountBean;
        mContext = context;
        mDelete = delete;
        for (int i=0;i<accountBean.size();i++){
            String currentaccount=PrefUtils.getString(context,"currentaccount","0");
            Log.i("123",currentaccount);
            if (!currentaccount.equals("0")&&currentaccount.equals(mAccountList.get(i).getAccount())){
                AccountUpdateBean.AutoAccountBean autoAccountBean=mAccountList.get(i);
                accountBean.remove(i);
                mAccountList.add(0,autoAccountBean);
            }
        }

        Log.i("123",new Gson().toJson(mAccountList));
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
        AccountViewHolder accountViewHolder = new AccountViewHolder(view);
        accountViewHolder.setIsRecyclable(false);
        return accountViewHolder;
    }

    /**
     * 绑定并给ViewHolder赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AccountViewHolder accountViewHolder = (AccountViewHolder) holder;
        //设置内容
        accountViewHolder.mTvContent.setText(mAccountList.get(position).getAccount());
        if (position == 0) {
            //设置第一个条目置顶选中
            accountViewHolder.mTvContent.setTextColor(Color.parseColor("#F58505"));
            accountViewHolder.mIvSelectOne.setVisibility(View.VISIBLE);
        }

        /**
         * 点击应用
         */
        accountViewHolder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //更新适配器，与数据库同步
                AccountUpdateBean.AutoAccountBean remove = mAccountList.remove(position);
                mAccountList.add(0, remove);
                notifyDataSetChanged();

                //内容储存到SP
                mAccountUserName = accountViewHolder.mTvContent.getText().toString().trim();
                PrefUtils.putString(mContext, "currentaccount", mAccountUserName);

            }
        });

        /**
         * 点击删除
         */
        accountViewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDelete.Delete(position);
//                //删除数据库
//                AccountDao accountDao = new AccountDao(mContext);
//                accountDao.delete(mAccountList.get(position));
                //刷新界面（不能够刷新，用移除删除项，在重新加载）
                mAccountList.remove(position);
                notifyDataSetChanged();
                //关闭滑动
                SwipeLayoutManager.create().closeLayout();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountList != null ? mAccountList.size() : 0;
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
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


        AccountViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
