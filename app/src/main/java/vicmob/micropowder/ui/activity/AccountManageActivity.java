package vicmob.micropowder.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.ui.adapter.AttentionAdapter;


/**
 * Created by Eren on 2017/6/23.
 * <p>
 * 账号管理
 */
public class AccountManageActivity extends BaseActivity {
    @BindView(R.id.attention_rclv)
    RecyclerView attentionRclv;
    @BindView(R.id.attention_back) ImageView attentionBack;
    private List<String> mDatasContent;
    private AttentionAdapter mAttentionAdapter;
    private List<String> mDatasTitle;
    //


    //    @BindView(R.id.btn_add_account)
    //    Button mBtnAddAccount;
    //    @BindView(R.id.btn_manage_account)
    //    Button mBtnManageAccount;
    //    @BindView(R.id.username)
    //    EditText mUsername;
    //    @BindView(R.id.password)
    //    EditText mPassword;
    //    @BindView(R.id.iv_back_account)
    //    ImageView ivBackAccount;
    //
    //
    //    /**
    //     * 用户名
    //     */
    //    private String Username;
    //    /**
    //     * 密码
    //     */
    //    private String Password;
    //    private AccountBean mAccountBean;
    //    private List<AccountBean> mAccountList;
    //    private AccountRecyclerAdapter mAccountRecyclerAdapter;
    //
    //
    //
    //    @Override
    //    public void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_login);
    //        ButterKnife.bind(this);
    //        mAccountList = new ArrayList<>();
    //
    //    }
    //
    //
    //    @OnClick({R.id.iv_back_account, R.id.btn_add_account, R.id.btn_manage_account})
    //    public void onViewClicked(View view) {
    //        switch (view.getId()) {
    //            case R.id.iv_back_account:
    //                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    //                outAnimation();
    //                break;
    //            case R.id.btn_add_account:
    //                Username = mUsername.getText().toString().trim();
    //                Password = mPassword.getText().toString().trim();
    //                if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Password)) {
    //                    MyToast.show(AccountManageActivity.this, "用户名和密码不能为空");
    //                    return;
    //                } else {
    //                    //添加数据库
    //                    AccountDao accountDao = new AccountDao(AccountManageActivity.this);
    ////                    //获取储存的内容
    ////                    String mContentUserName = PrefUtils.getString(AccountManageActivity.this, "ContentUserName", null);
    ////                    String mContentPassWord = PrefUtils.getString(AccountManageActivity.this, "ContentPassWord", null);
    //                    if (accountDao.queryOne(Username)) {
    //                        MyToast.show(AccountManageActivity.this, "输入内容重复");
    //                        return;
    //                    } else {
    //
    //                        //添加内容到实例中
    //                        mAccountBean = new AccountBean();
    //                        mAccountBean.setUserName(Username);
    //                        mAccountBean.setPassWord(Password);
    //                        //添加数据库
    //                        accountDao.add(mAccountBean);
    //                        //数据添加到集合并置顶选中，并刷新界面
    ////                        mAccountList.add(0, mAccountBean);
    //                        mUsername.setText("");
    //                        mPassword.setText("");
    //                    }
    //                }
    //                break;
    //            case R.id.btn_manage_account:
    //                inAnimation(LoginActivity.class);
    //                break;
    //        }
    //
    //    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        initDate();
        mAttentionAdapter = new AttentionAdapter(AccountManageActivity.this, mDatasTitle, mDatasContent);
        attentionRclv.setLayoutManager(new LinearLayoutManager(this));
        attentionRclv.setAdapter(mAttentionAdapter);
    }

    private void initDate() {
        mDatasTitle = new ArrayList<>();
        mDatasContent = new ArrayList<>();
        String attentionTitle[] = {
                getResources().getString(R.string.wechat_nearby),getResources().getString(R.string.wechat_phone_list),
                getResources().getString(R.string.wechat_add_friend), getResources().getString(R.string.wechat_add_group_friend),
                getResources().getString(R.string.wechat_public_num), getResources().getString(R.string.wechat_drift_bottle),
                getResources().getString(R.string.wechat_send_message), getResources().getString(R.string.wechat_whitelist), getResources().getString(R.string.wechat_circle_friends)};
        String attentionContent[] = {
                getResources().getString(R.string.wechat_nearby_content),
                getResources().getString(R.string.wechat_phone_list_content),

                getResources().getString(R.string.wechat_add_friend_content), getResources().getString(R.string.wechat_add_group_friend_content),
                getResources().getString(R.string.wechat_public_num_content), getResources().getString(R.string.wechat_drift_bottle_content),
                getResources().getString(R.string.wechat_send_message_content), getResources().getString(R.string.wechat_whitelist_content),getResources().getString(R.string.wechat_circle_friends_content)};
        for (int i = 0; i < attentionContent.length; i++) {
            mDatasContent.add(attentionContent[i]);
            mDatasTitle.add(attentionTitle[i]);
        }
    }

    @OnClick(R.id.attention_back)
    public void onViewClicked() {
        outAnimation();
    }


}
