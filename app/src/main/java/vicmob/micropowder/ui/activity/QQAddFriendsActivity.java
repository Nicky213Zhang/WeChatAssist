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

import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * author: Twisted
 * created on: 2017/8/5 9:45
 * description:
 */

public class QQAddFriendsActivity extends BaseActivity {
    private RelativeLayout qq_rl_open_addfriends;
    private ShSwitchView qq_iv_begin_addfriends;
    private EditText qq_et_content_addfriends;
    private Button qq_but_begin_addfriends;
    private String mText = "30";
    private int maddfriendsText;
    private MyApplication app;
    private ConfirmDialog mConfirmDialog;
    private ImageView qq_iv_back_add_friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_friend);
        initView();
        initdata();
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

    public void initView() {
        qq_rl_open_addfriends = (RelativeLayout) findViewById(R.id.qq_rl_open_addfriends);
        qq_iv_begin_addfriends = (ShSwitchView) findViewById(R.id.qq_iv_begin_addfriends);
        qq_et_content_addfriends = (EditText) findViewById(R.id.qq_et_content_addfriends);
        qq_but_begin_addfriends = (Button) findViewById(R.id.qq_but_begin_addfriends);
        qq_iv_back_add_friends = (ImageView) findViewById(R.id.qq_iv_back_add_friends);
    }

    public void initdata() {
        //设置软键盘回车键
        qq_et_content_addfriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mText = qq_et_content_addfriends.getText().toString().trim();     //输入人数
                    try {
                        maddfriendsText = Integer.parseInt(mText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    if (maddfriendsText <= 0) {
                        MyToast.show(QQAddFriendsActivity.this, "添加人数请大于0");
                    } else if (maddfriendsText > 30) {

                        MyToast.show(QQAddFriendsActivity.this, "每次添加上限30人哦~");
                    } else {
                        PrefUtils.putInt(QQAddFriendsActivity.this, "mQQAddFriendsText", maddfriendsText); //存储输入人数
                        if (isQQServiceOpening(QQAddFriendsActivity.this)) {
                            app = (MyApplication) getApplication();
                            app.setQQAddFriends(true);  //开启一键加好友模块
                            //                        app.setAllowAddFriends(true);
                            intentQq(); //跳转QQ主界面
                        } else {

                            //跳转服务界面对话框
                            mConfirmDialog = new ConfirmDialog(QQAddFriendsActivity.this, new Callback() {
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


        String qq_friend_num = PrefUtils.getString(QQAddFriendsActivity.this, Constant.qqFunction[2], "0");
        if (qq_friend_num.equals("0") || TextUtils.isEmpty(qq_friend_num)) {
            qq_et_content_addfriends.setText(mText);
        } else {
            qq_et_content_addfriends.setText(qq_friend_num);
        }
        qq_et_content_addfriends.setSelection(qq_et_content_addfriends.getText().toString().trim().length());
        qq_iv_back_add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();
            }
        });
        qq_rl_open_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
        qq_but_begin_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = qq_et_content_addfriends.getText().toString().trim();     //输入人数
                try {
                    maddfriendsText = Integer.parseInt(mText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (maddfriendsText <= 0) {
                    MyToast.show(QQAddFriendsActivity.this, "添加人数请大于0");
                } else if (maddfriendsText > 30) {

                    MyToast.show(QQAddFriendsActivity.this, "每次添加上限30人哦~");
                } else {
                    PrefUtils.putInt(QQAddFriendsActivity.this, "mQQAddFriendsText", maddfriendsText); //存储输入人数
                    if (isQQServiceOpening(QQAddFriendsActivity.this)) {
                        app = (MyApplication) getApplication();
                        app.setQQAddFriends(true);  //开启一键加好友模块
                        //                        app.setAllowAddFriends(true);
                        intentQq(); //跳转QQ主界面
                    } else {

                        //跳转服务界面对话框
                        mConfirmDialog = new ConfirmDialog(QQAddFriendsActivity.this, new Callback() {
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


            }
        });
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        qq_iv_begin_addfriends.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isQQServiceOpening(QQAddFriendsActivity.this)) {

            qq_iv_begin_addfriends.setOn(true);   //打开

        } else {

            qq_iv_begin_addfriends.setOn(false);  //关闭
        }
    }
}
