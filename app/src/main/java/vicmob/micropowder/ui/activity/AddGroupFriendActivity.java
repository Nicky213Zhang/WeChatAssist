package vicmob.micropowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 添加群好友
 */
public class AddGroupFriendActivity extends BaseActivity {
    @BindView(R.id.iv_back_agf)
    ImageView ivBackAgf;
    @BindView(R.id.iv_begin_agf)
    ShSwitchView ivBeginAgf;
    @BindView(R.id.rl_open_agf)
    RelativeLayout rlOpenAgf;
    @BindView(R.id.et_content_agf)
    EditText etContentAgf;
    @BindView(R.id.but_begin_agf)
    Button butBeginAgf;

    /**
     * 全局变量
     */
    private MyApplication app;
    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 输入框输入的内容
     */
    private String gText;
    /**
     * 群默认个数
     */
    private String GRNum = "1";
    /**
     * 添加附近人的人数
     */
    private int mGroupText;

    private BaseAccessibilityService mAccessibilityService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_friend);
        ButterKnife.bind(this);
        String wx_groupfriend_num = PrefUtils.getString(AddGroupFriendActivity.this, Constant.wxFunction[3], "0");
        if (TextUtils.isEmpty(wx_groupfriend_num) || wx_groupfriend_num.equals("0")) {
            etContentAgf.setText(GRNum);
        } else {
            etContentAgf.setText(wx_groupfriend_num);
        }
        etContentAgf.setSelection(etContentAgf.getText().toString().trim().length());
        etContentAgf.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    gText = etContentAgf.getText().toString().trim();     //输入个数
                    try {
                        mGroupText = Integer.parseInt(gText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    PrefUtils.putInt(getApplication(), "mGroupText", mGroupText); //存储输入人数
                    if (isServiceOpening(AddGroupFriendActivity.this)) {
                        app = (MyApplication) getApplication();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                mAccessibilityService.execShellCmd("input keyevent 3");

                                app.setGroupFriend(true);  //开启一键加群友模块
                                app.setAllowAddGroup(true);
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                intentWechat(); //跳转微信主界面
                            }
                        }).start();

                    } else {

                        //跳转服务界面对话框
                        mConfirmDialog = new ConfirmDialog(AddGroupFriendActivity.this, new Callback() {
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
                    return false;
                }
                return false;
            }
        });

    }

    /**
     * 布局可见时调用
     */
    @Override
    protected void onStart() {
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


    @OnClick({R.id.iv_back_agf, R.id.rl_open_agf, R.id.but_begin_agf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_agf:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;

            case R.id.but_begin_agf:

                gText = etContentAgf.getText().toString().trim();     //输入个数
                try {
                    mGroupText = Integer.parseInt(gText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                PrefUtils.putInt(getApplication(), "mGroupText", mGroupText); //存储输入人数
                if (isServiceOpening(AddGroupFriendActivity.this)) {
                    app = (MyApplication) getApplication();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先执行杀掉微信后台操作
                            mAccessibilityService = new BaseAccessibilityService();
                            mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                            mAccessibilityService.execShellCmd("input keyevent 3");

                            app.setGroupFriend(true);  //开启一键加群友模块
                            app.setAllowAddGroup(true);
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            intentWechat(); //跳转微信主界面
                        }
                    }).start();

                } else {

                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(AddGroupFriendActivity.this, new Callback() {
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
            case R.id.rl_open_agf:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;

        }
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginAgf.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(AddGroupFriendActivity.this)) {

            ivBeginAgf.setOn(true);   //打开

        } else {

            ivBeginAgf.setOn(false);  //关闭
        }
    }
}
