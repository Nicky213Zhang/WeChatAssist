package vicmob.micropowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;

import vicmob.earn.R;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.service.QQAccessibilityService;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.PrefUtils;

public class QQFriendStreamActivity extends AppCompatActivity {


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

    private QQAccessibilityService mQQAccessibilityService;

    private ShSwitchView iv_begin_fsf;
    private EditText et_content_fsf;
    private Button but_begin_fsf;
    private ImageView iv_back_fsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_stream);
        initView();
        initData();
        init();
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
        //Editext默认获得焦点
        et_content_fsf.setFocusable(true);
        et_content_fsf.setFocusableInTouchMode(true);
        et_content_fsf.requestFocus();
        SwitchChanged();
    }

    public void initView() {
        iv_begin_fsf = ((ShSwitchView) findViewById(R.id.iv_begin_fsf));
        et_content_fsf = ((EditText) findViewById(R.id.et_content_fsf));
        but_begin_fsf = ((Button) findViewById(R.id.but_begin_fsf));
        iv_back_fsf = ((ImageView) findViewById(R.id.iv_back_fsf));
    }

    public void initData() {

    }

    public void init() {
        //设置软键盘回车键
        et_content_fsf.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    gText = et_content_fsf.getText().toString().trim();     //输入个数
                    try {
                        mGroupText = Integer.parseInt(gText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    PrefUtils.putInt(getApplication(), "mQQGroupText", mGroupText); //存储输入人数
                    if (isQQServiceOpening(getApplication())) {
                        app = (MyApplication) getApplication();
                        app.setQQFriendStream(true);  //开启一键加群友模块
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉QQ后台操作
                                mQQAccessibilityService = new QQAccessibilityService();
                                mQQAccessibilityService.execShellCmd("am force-stop com.tencent.mobileqq");
                                mQQAccessibilityService.execShellCmd("input keyevent 3");
                                app.setQQFriendStream(true);  //开启一键加群友模块
                                app.setAllowQQFriendStream(true);
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
                        mConfirmDialog = new ConfirmDialog(QQFriendStreamActivity.this, new Callback() {
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

        String qq_groupfriend_num = PrefUtils.getString(QQFriendStreamActivity.this, Constant.qqFunction[3], "0");
        if (qq_groupfriend_num.equals("0") || TextUtils.isEmpty(qq_groupfriend_num)) {
            et_content_fsf.setText(GRNum);
        } else {
            et_content_fsf.setText(qq_groupfriend_num);
        }
        et_content_fsf.setSelection(et_content_fsf.getText().length());
        but_begin_fsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gText = et_content_fsf.getText().toString().trim();     //输入个数
                try {
                    mGroupText = Integer.parseInt(gText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                PrefUtils.putInt(getApplication(), "mQQGroupText", mGroupText); //存储输入人数
                if (isQQServiceOpening(getApplication())) {
                    app = (MyApplication) getApplication();
                    app.setQQFriendStream(true);  //开启一键加群友模块
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先执行杀掉QQ后台操作
                            mQQAccessibilityService = new QQAccessibilityService();
                            mQQAccessibilityService.execShellCmd("am force-stop com.tencent.mobileqq");
                            mQQAccessibilityService.execShellCmd("input keyevent 3");
                            app.setQQFriendStream(true);  //开启一键加群友模块
                            app.setAllowQQFriendStream(true);
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
                    mConfirmDialog = new ConfirmDialog(QQFriendStreamActivity.this, new Callback() {
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
        });
        iv_back_fsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
            }
        });
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        iv_begin_fsf.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isQQServiceOpening(this)) {
            iv_begin_fsf.setOn(true);   //打开
        } else {
            iv_begin_fsf.setOn(false);  //关闭
        }
    }

    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public boolean isQQServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + QQAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 跳转QQ主界面
     */
    public void intentQQ() {
        try {
            PackageManager packageManager = this.getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent viewIntent = new
                    Intent("android.intent.action.VIEW", Uri.parse("http://www.qq.com/"));
            startActivity(viewIntent);
        }
    }
}
