package vicmob.micropowder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sevenheaven.iosswitch.ShSwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.ui.views.ConfirmDialog;

/**
 * Created by admin on 2017/10/12.
 */
public class OneKeyActivity extends BaseActivity {
    @BindView(R.id.iv_back_onekey)
    ImageView ivBackOnekey;
    @BindView(R.id.rl_title_onekey)
    RelativeLayout rlTitleOnekey;
    @BindView(R.id.iv_begin_onekey)
    ShSwitchView ivBeginOnekey;
    @BindView(R.id.rl_open_onekey)
    RelativeLayout rlOpenOnekey;
    @BindView(R.id.btn_begin_onekey)
    Button btnBeginOnekey;

    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 全局变量
     */
    private MyApplication app;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onekey);

        ButterKnife.bind(this);
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

    @OnClick({R.id.iv_back_onekey, R.id.rl_open_onekey,R.id.btn_begin_onekey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_onekey:
                outAnimation();
                break;
            case R.id.rl_open_onekey:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.btn_begin_onekey:
                if (isOneServiceOpening(OneKeyActivity.this)) {
                    app = (MyApplication) getApplication();
                    app.setFriendCircle(true);  //开启一键加好友模块
                    app.setStart(true);
//                    app.setDriftBottle(true);
//                    app.setStart(true);
                    intentWechat(); //跳转微信主界面
                } else {

                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(OneKeyActivity.this, new Callback() {
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
        }
    }

    /**
     * 初始化服务按钮开启的状态
     */
    private void initOpenState() {
        if (isOneServiceOpening(OneKeyActivity.this)) {
            ivBeginOnekey.setOn(true);   //打开

        } else {
            ivBeginOnekey.setOn(false);  //关闭
        }
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginOnekey.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
    }



}
