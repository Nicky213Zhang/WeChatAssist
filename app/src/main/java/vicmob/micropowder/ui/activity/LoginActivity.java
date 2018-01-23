package vicmob.micropowder.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Delete;
import vicmob.micropowder.daoman.bean.AccountBean;
import vicmob.micropowder.daoman.dao.AccountDao;
import vicmob.micropowder.ui.adapter.AccountRecyclerAdapter;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;


/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 登录账号
 */
public class LoginActivity extends BaseActivity {
//    @BindView(R.id.iv_back_login_manage)
//    ImageView mIvBackLoginManage;
//    @BindView(R.id.rlv_login_manage)
//    RecyclerView mRlvLoginManage;
//    @BindView(R.id.iv_login_manage)
//    ImageView mIvLoginManage;
//    @BindView(R.id.tv_login_manage)
//    TextView mTvLoginManage;
//    @BindView(R.id.btn_add_account_login)
//    Button mBtnAddAccountLogin;
//    @BindView(R.id.rll_login)
//    RelativeLayout mRllLogin;
//
//
//    private AccountRecyclerAdapter accountRecyclerAdapter;
//
//    private List<AccountBean> mAccountBeanList;
//
//    public static final int DELETE = 0;
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DELETE:
//                    if (mAccountBeanList.size() == 0) {
//                        mIvLoginManage.setVisibility(View.VISIBLE);
//                        mTvLoginManage.setVisibility(View.VISIBLE);
//                    }
//                    accountRecyclerAdapter.notifyDataSetChanged();
//                    MyToast.show(LoginActivity.this, "删除成功");
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_manage);
//        ButterKnife.bind(this);
//    }
//
//    /**
//     * 初始化数据库数据
//     */
//    private void initDB() {
//        //查询数据库
//        AccountDao accountDao = new AccountDao(LoginActivity.this);
//        mAccountBeanList = accountDao.queryForAll();
//        //初始化数据库倒叙显示！！！
//        Collections.reverse(mAccountBeanList);
//        if (mAccountBeanList.size() != 0 && !mAccountBeanList.get(0).getPassWord().isEmpty()) {
//            mIvLoginManage.setVisibility(View.INVISIBLE);
//            mTvLoginManage.setVisibility(View.INVISIBLE);
//        } else {
//            mIvLoginManage.setVisibility(View.VISIBLE);
//            mTvLoginManage.setVisibility(View.VISIBLE);
//            this.onRestart();
//        }
//        initRecyclerView();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initDB();
//    }
//
//    @OnClick({R.id.iv_back_login_manage, R.id.btn_add_account_login})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_back_login_manage:
//                outAnimation();
//                break;
//            case R.id.btn_add_account_login:
//                outAnimation();
//                break;
//        }
//    }
//
//    /**
//     * 初始化RecyclerView
//     */
//    private void initRecyclerView() {
//        mRlvLoginManage.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器
//
//        mRlvLoginManage.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式
//        accountRecyclerAdapter = new AccountRecyclerAdapter(LoginActivity.this, mAccountBeanList, new Delete() {
//            @Override
//            public void Delete(int position) {
//                AccountDao accountDao = new AccountDao(LoginActivity.this);
//                accountDao.delete(mAccountBeanList.get(position));
//                mHandler.sendEmptyMessage(DELETE);
//            }
//        });
//        //添加RecyclerView适配器
//        mRlvLoginManage.setAdapter(accountRecyclerAdapter);
//        accountRecyclerAdapter.notifyDataSetChanged();
//    }


}