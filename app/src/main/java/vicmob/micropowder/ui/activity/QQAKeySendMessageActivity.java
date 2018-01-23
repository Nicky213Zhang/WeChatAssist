package vicmob.micropowder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.bean.SendMessageBean;
import vicmob.micropowder.daoman.dao.SendMessageDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.SendMessageAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.ui.views.OneKeyContentDialog;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * author: Twisted
 * created on: 2017/7/19 17:54
 * description:
 */

public class QQAKeySendMessageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back_akeychat)
    ImageView mIvBackAkeychat;
    @BindView(R.id.iv_begin_akeychat)
    ShSwitchView mIvBeginAkeychat;
    @BindView(R.id.rl_open_akeychat)
    RelativeLayout mRlOpenAkeychat;
    @BindView(R.id.rlv_akeychat)
    RecyclerView mRlvAkeychat;
    @BindView(R.id.btn_akeychat)
    Button mBtnAkeychat;
    @BindView(R.id.btn_edit_akeychat)
    Button mBtnEditAkeychat;

    private MyApplication app;
    private ConfirmDialog mConfirmDialog;
    private OneKeyContentDialog mOneKeyContentDialog;
    private SendMessageBean mSendMessageBean;
    private List<SendMessageBean> mSendMessageList = new ArrayList<>();
    private SendMessageAdapter mSendMessageAdapter;
    private BaseAccessibilityService mAccessibilityService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqakeysendmessage);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        initDB();
        initRecyclerView();
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询数据库
        SendMessageDao mSendMessageDao = new SendMessageDao(QQAKeySendMessageActivity.this);
        mSendMessageList = mSendMessageDao.queryForAll();
        //初始化数据库倒叙显示！！！
        Collections.reverse(mSendMessageList);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRlvAkeychat.setLayoutManager(new LinearLayoutManager(QQAKeySendMessageActivity.this));         //设置布局管理器

        mRlvAkeychat.addItemDecoration(new DividerItemDecoration(QQAKeySendMessageActivity.this));      //设置分割线样式

        mSendMessageAdapter = new SendMessageAdapter(QQAKeySendMessageActivity.this, mSendMessageList);   //添加RecyclerView适配器
        mRlvAkeychat.setAdapter(mSendMessageAdapter);
    }

    /**
     * 布局可见时调用
     */
    @Override
    public void onStart() {
        super.onStart();
        initOpenState();
    }

    /**
     * 布局交互时调用
     */
    @Override
    public void onResume() {
        super.onResume();
        SwitchChanged();
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        mIvBeginAkeychat.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isQQServiceOpening(QQAKeySendMessageActivity.this)) {
            mIvBeginAkeychat.setOn(true);   //打开
        } else {
            mIvBeginAkeychat.setOn(false);  //关闭
        }
    }

    @OnClick({R.id.iv_back_akeychat, R.id.rl_open_akeychat, R.id.btn_akeychat, R.id.btn_edit_akeychat})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_akeychat:
                outAnimation();
                break;
            case R.id.rl_open_akeychat:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;
            case R.id.btn_akeychat:
                if (isQQServiceOpening(QQAKeySendMessageActivity.this)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先执行杀掉QQ后台操作
                            mAccessibilityService = new BaseAccessibilityService();
                            mAccessibilityService.execShellCmd("am force-stop com.tencent.mobileqq");
                            mAccessibilityService.execShellCmd("input keyevent 3");
                            app.setQQAKeySendMessage(true);  //开启一键发消息// 模块
                            app.setQQAllowAKeySendMessage(true);
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //跳转QQ主界面
                            intentQQ();
                        }
                    }).start();

                } else {
                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(QQAKeySendMessageActivity.this, new Callback() {
                        @Override
                        public void Positive() {
                            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                        }

                        @Override
                        public void Negative() {
                            mConfirmDialog.dismiss();   //关闭
                        }
                    });
                    mConfirmDialog.setContent("提示：" + "\n服务没有开启不能进行下一步");
                    mConfirmDialog.setCancelable(true);
                    mConfirmDialog.show();
                }
                break;
            case R.id.btn_edit_akeychat:
                mOneKeyContentDialog = new OneKeyContentDialog(QQAKeySendMessageActivity.this, new Callback() {
                    @Override
                    /**
                     * 点击添加内容
                     */
                    public void Positive() {
                        //获取储存的内容
                        String mSendMessageText = PrefUtils.getString(QQAKeySendMessageActivity.this, "OneKeyDialogText", null);
                        if (mSendMessageText == "" || TextUtils.isEmpty(mSendMessageText) || "null".equals(mSendMessageText)) {
                            MyToast.show(QQAKeySendMessageActivity.this, "内容设置不能为空");

                        } else {
                            //添加数据库
                            SendMessageDao mSendMessageDao = new SendMessageDao(QQAKeySendMessageActivity.this);
                            if (mSendMessageDao.queryOne(mSendMessageText)) {
                                MyToast.show(QQAKeySendMessageActivity.this, "输入内容重复");
                            } else {
                                //添加内容到实例中
                                mSendMessageBean = new SendMessageBean();
                                mSendMessageBean.setMessageText(mSendMessageText);
                                //添加数据库
                                mSendMessageDao.add(mSendMessageBean);
                                //数据添加到集合并置顶选中，并刷新界面
                                mSendMessageList.add(0, mSendMessageBean);
                                mSendMessageAdapter.notifyDataSetChanged();
                                mOneKeyContentDialog.dismiss();
                            }
                        }
                    }

                    /**
                     * 点击小黄叉
                     */
                    @Override
                    public void Negative() {
                        mOneKeyContentDialog.dismiss();
                    }
                });

                mOneKeyContentDialog.setCancelable(true);
                mOneKeyContentDialog.show();
                break;
        }
    }
}

