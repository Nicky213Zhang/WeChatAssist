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
 * author: Twisted
 * created on: 2017/10/17 15:36
 * description:
 */

public class DeleteDeadFriendActivity extends BaseActivity {
    @BindView(R.id.iv_back_deletefriends) ImageView ivBackDeletefriends;
    @BindView(R.id.rl_title_deletefriends) RelativeLayout rlTitleDeletefriends;
    @BindView(R.id.iv_begin_deletefriends) ShSwitchView ivBeginDeletefriends;
    @BindView(R.id.rl_open_deletefriends) RelativeLayout rlOpenDeletefriends;
    @BindView(R.id.but_begin_deletefriends) Button butBeginDeletefriends;
    private MyApplication app;
    private ConfirmDialog mConfirmDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletedeadfriend);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back_deletefriends, R.id.rl_open_deletefriends, R.id.but_begin_deletefriends})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_deletefriends:
                outAnimation();
                break;
            case R.id.rl_open_deletefriends:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;
            case R.id.but_begin_deletefriends:
                if (isServiceOpening(DeleteDeadFriendActivity.this)) {
                    app = (MyApplication) getApplication();
                    app.setWxDeleteDeadFriends(true);  //开启一键清粉模块
                    app.setAllowWxDeleteDeadFriends(true);
//                    app.setAllowAddFriends(true);
                    intentWechat(); //跳转微信主界面
                } else {

                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(DeleteDeadFriendActivity.this, new Callback() {
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


    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginDeletefriends.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(DeleteDeadFriendActivity.this)) {

            ivBeginDeletefriends.setOn(true);   //打开

        } else {

            ivBeginDeletefriends.setOn(false);  //关闭
        }
    }
}
