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
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 添加好友
 */
public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.iv_back_addfriends)
    ImageView mIvBackAddFriends;
    @BindView(R.id.iv_begin_addfriends)
    ShSwitchView mIvBeginAddFriends;
    @BindView(R.id.but_begin_addfriends)
    Button but_begin_addfriends;
    @BindView(R.id.rl_open_addfriends)
    RelativeLayout mRlOpenAddfriends;
    @BindView(R.id.et_content_addfriends)
    EditText mEtContentAddfriends;
    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 全局变量
     */
    private MyApplication app;
    /**
     * 输入框输入的内容
     */
    private String mText;
    /**
     * 添加好友的人数
     */
    private int maddfriendsText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        ButterKnife.bind(this);
        String wx_friend_num = PrefUtils.getString(AddFriendActivity.this, Constant.wxFunction[2], "0");
        if (wx_friend_num.equals("0") || TextUtils.isEmpty(wx_friend_num)) {
            wx_friend_num = "30";
        }
        mEtContentAddfriends.setText(wx_friend_num);
        mEtContentAddfriends.setSelection(mEtContentAddfriends.getText().toString().trim().length());
        mEtContentAddfriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    mText = mEtContentAddfriends.getText().toString().trim();     //输入人数
                    try {
                        maddfriendsText = Integer.parseInt(mText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (maddfriendsText <= 0) {
                        MyToast.show(AddFriendActivity.this, "添加人数请大于0");
                    } else if (maddfriendsText > 80) {

                        MyToast.show(AddFriendActivity.this, "每次添加上限80人哦~");
                    } else {

                        PrefUtils.putInt(getApplication(), "mAddFriendsText", maddfriendsText); //存储输入人数
                        if (isServiceOpening(AddFriendActivity.this)) {
                            app = (MyApplication) getApplication();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //先执行杀掉微信后台操作
                                    BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                                    mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                    mAccessibilityService.execShellCmd("input keyevent 3");
                                    app.setAddFriends(true);  //开启一键加好友模块
                                    app.setAllowAddFriends(true);
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
                            mConfirmDialog = new ConfirmDialog(AddFriendActivity.this, new Callback() {
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

    @OnClick({R.id.iv_back_addfriends, R.id.but_begin_addfriends, R.id.rl_open_addfriends})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_back_addfriends:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;


            case R.id.rl_open_addfriends: //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;
            case R.id.but_begin_addfriends://
                mText = mEtContentAddfriends.getText().toString().trim();     //输入人数
                if (TextUtils.isEmpty(mText) || mText.equals("0")) {
                    mText = "30";
                }
                try {
                    maddfriendsText = Integer.parseInt(mText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

//                if (TextUtils.isEmpty(mText) || mText == "" || maddfriendsText <= 0) {
//
//                    MyToast.show(AddFriendActivity.this, "人数不能为空");
//                }
                if (maddfriendsText <= 0) {
                    MyToast.show(AddFriendActivity.this, "添加人数请大于0");
                } else if (maddfriendsText > 80) {

                    MyToast.show(AddFriendActivity.this, "每次添加上限80人哦~");
                } else {

                    PrefUtils.putInt(getApplication(), "mAddFriendsText", maddfriendsText); //存储输入人数
                    if (isServiceOpening(AddFriendActivity.this)) {
                        app = (MyApplication) getApplication();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                mAccessibilityService.execShellCmd("input keyevent 3");
                                app.setAddFriends(true);  //开启一键加好友模块
                                app.setAllowAddFriends(true);
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
                        mConfirmDialog = new ConfirmDialog(AddFriendActivity.this, new Callback() {
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


                }
                break;
            default:
                break;
        }
    }


    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        mIvBeginAddFriends.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(AddFriendActivity.this)) {

            mIvBeginAddFriends.setOn(true);   //打开

        } else {

            mIvBeginAddFriends.setOn(false);  //关闭
        }
    }
}
