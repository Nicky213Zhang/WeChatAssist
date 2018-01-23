package vicmob.micropowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sevenheaven.iosswitch.ShSwitchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Delete;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.AccountBean;
import vicmob.micropowder.daoman.bean.AccountUpdateBean;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.AccountRecyclerAdapter;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyJsonUtil;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;


public class AutoSwitchNumberActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.but_begin)
    Button mButBegin;
    @BindView(R.id.rl_open)
    RelativeLayout mRlOpen;
    @BindView(R.id.iv_begin)
    ShSwitchView mIvBegin;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    AccountRecyclerAdapter AccountRecyclerAdapter;

    private BaseAccessibilityService mAccessibilityService;
    /**
     * 全局变量
     */
    private MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_switch_number);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        initRecyclerView();
    }
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器

        mRvContent.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式
        if (app.mAccountList.size() > 0 && app.mAccountList != null) { //如果网络获取成功
            AccountRecyclerAdapter = new AccountRecyclerAdapter(AutoSwitchNumberActivity.this, app.mAccountList, new Delete() {
                @Override
                public void Delete(int position) {
                    deleteThread(app.mAccountList.get(position).getAccountId()+"");
                }
            });   //添加RecyclerView适配器
            mRvContent.setAdapter(AccountRecyclerAdapter);
        }
    }
    /**
     * 布局可见时调用按钮的开启状态
     */
    @Override
    protected void onStart() {
        super.onStart();

        initOpenState();
    }
    /**
     * 布局交互时调用按钮的监听
     */
    @Override
    public void onResume() {
        super.onResume();

        SwitchChanged();
    }
    @OnClick({R.id.iv_back, R.id.but_begin, R.id.rl_open})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;

            case R.id.but_begin:    //

                if (isServiceOpening(AutoSwitchNumberActivity.this)) {
                    if (app.mAccountList.size()>0){
                        PrefUtils.putString(getApplication(), "mAutoswitchmobile", app.mAccountList.get(0).getAccount()); //存储输入账户
                        PrefUtils.putString(getApplication(), "mAutoswitchpassword", app.mAccountList.get(0).getPassword()); //存储输入密码
                        app.setAutoSwitchNumber(true);  //开启自动换号
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //先执行杀掉微信后台操作
//                                mAccessibilityService = new BaseAccessibilityService();
//                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
//                                mAccessibilityService.execShellCmd("input keyevent 3");

                                app.setAutoSwitchNumber(true);
                                app.setAllowAutoSwitchNumber(true);
//                                try {
//                                    Thread.sleep(2500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                intentWechat(); //跳转微信主界面
//                            }
//                        }).start();
                    }else {
                        MyToast.show(AutoSwitchNumberActivity.this,"请添加微信号");
                    }

                }
                break;

            case R.id.rl_open: //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;


        }
    }


    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        mIvBegin.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
    }


    /**
     * 初始化服务按钮开启的状态
     */
    private void initOpenState() {
        if (isServiceOpening(AutoSwitchNumberActivity.this)) {

            mIvBegin.setOn(true);   //打开

        } else {

            mIvBegin.setOn(false);  //关闭
        }
    }
    /**
     * 删除数据
     *
     * @param
     */
    public void deleteThread(final String id) {
        Log.i("zw",id);
        Thread th = new Thread() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Url.deleteAccountUrl)
                        .addParams("accountId", id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Boolean success = MyJsonUtil.getBeanByJson(response);
                                if (success) {
                                    MyToast.show(AutoSwitchNumberActivity.this, "删除成功");
                                }
                            }
                        });
            }
        };
        th.start();
    }
}
