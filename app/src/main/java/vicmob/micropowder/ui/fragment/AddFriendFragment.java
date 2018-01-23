package vicmob.micropowder.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.sevenheaven.iosswitch.ShSwitchView;

import vicmob.earn.R;
import vicmob.micropowder.base.BaseQQFragment;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by qq944 on 2017/7/27.
 */

public class AddFriendFragment extends BaseQQFragment{
    private RelativeLayout qq_rl_open_addfriends;
    private ShSwitchView qq_iv_begin_addfriends;
    private EditText qq_et_content_addfriends;
    private Button qq_but_begin_addfriends;
    private String mText;
    private int maddfriendsText;
    private MyApplication app;
    private ConfirmDialog mConfirmDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_friend;
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
    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        initData();
        super.init(inflater, container, savedInstanceState);
    }

    public void initView(){
        qq_rl_open_addfriends = ((RelativeLayout)findViewById(R.id.qq_rl_open_addfriends));
        qq_iv_begin_addfriends = ((ShSwitchView) findViewById(R.id.qq_iv_begin_addfriends));
        qq_et_content_addfriends = ((EditText) findViewById(R.id.qq_et_content_addfriends));
        qq_but_begin_addfriends = ((Button) findViewById(R.id.qq_but_begin_addfriends));
    }
    public void initData(){
        qq_rl_open_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
        qq_but_begin_addfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText =qq_et_content_addfriends.getText().toString().trim();     //输入人数
                if (TextUtils.isEmpty(mText) || mText == "" ){
                    mText ="30";
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
                if (maddfriendsText <= 0){
                    MyToast.show(getContext(), "添加人数请大于0");
                }
                else if (maddfriendsText > 80) {

                    MyToast.show(getContext(), "每次添加上限30人哦~");
                } else {
                    PrefUtils.putInt(getContext(), "mQQAddFriendsText", maddfriendsText); //存储输入人数
                    if (isQQServiceOpening(getActivity())) {
                        app = (MyApplication) getActivity().getApplication();
                        app.setQQAddFriends(true);  //开启一键加好友模块
                        //                        app.setAllowAddFriends(true);
                        intentQq(); //跳转QQ主界面
                    } else {

                        //跳转服务界面对话框
                        mConfirmDialog = new ConfirmDialog(getActivity(), new Callback() {
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
    public void init(){

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
        if (isQQServiceOpening(getActivity())) {

            qq_iv_begin_addfriends.setOn(true);   //打开

        } else {

            qq_iv_begin_addfriends.setOn(false);  //关闭
        }
    }
}
