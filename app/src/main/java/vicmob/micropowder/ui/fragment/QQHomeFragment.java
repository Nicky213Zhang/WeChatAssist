package vicmob.micropowder.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.umeng.analytics.MobclickAgent;

import vicmob.earn.R;
import vicmob.micropowder.base.BaseQQFragment;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.activity.LocationManageActivity;
import vicmob.micropowder.ui.activity.PhoneActivity;
import vicmob.micropowder.ui.activity.QQAKeySendMessageActivity;
import vicmob.micropowder.ui.activity.QQAddFriendsActivity;
import vicmob.micropowder.ui.activity.QQFriendStreamActivity;
import vicmob.micropowder.ui.activity.QQNearbyPeopleActivity;
import vicmob.micropowder.ui.activity.QQUserAttentionActivity;
import vicmob.micropowder.ui.adapter.MainAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;

/**
 * author: Twisted
 * created on: 2017/8/5 8:32
 * description:
 */

public class QQHomeFragment extends BaseQQFragment implements AdapterView.OnItemClickListener {
    private String current_channel;
    private GridView mQQGvContent;
    private MainAdapter mMainAdapter;
    private ConfirmDialog mConfirmDialog;
    private MyApplication app;
    private BaseAccessibilityService mAccessibilityService;

    @Override
    protected int getLayoutId() {
        return R.layout.qq_fragment_home;
    }

    public static QQHomeFragment newInstance(String title) {
        QQHomeFragment fragment = new QQHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", title);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(inflater, container, savedInstanceState);
        app = (MyApplication) getActivity().getApplication();
        initView();
    }

    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            current_channel = bundle.getString("activity");
            //fabClick(current_channel);
        }
        mQQGvContent = (GridView) findViewById(R.id.qq_gv_content);

        //GridView填充适配器
        mMainAdapter = new MainAdapter(mActivity, Constant.QQMAINNAMES, Constant.QQMAINIMAGES);

        mQQGvContent.setAdapter(mMainAdapter);

        mQQGvContent.setOnItemClickListener(QQHomeFragment.this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                inAnimation(QQUserAttentionActivity.class);//账号管理
                break;
            case 1:
                inAnimation(LocationManageActivity.class);//添加地点
                break;
            case 2:
                //重置
                mConfirmDialog = new ConfirmDialog(mActivity, new Callback() {
                    @Override
                    public void Positive() {
                        app.setWhiteList(false);
                        app.setQQAKeySendMessage(false);  //开启一键发消息// 模块
                        app.setQQNearbyPeople(false);
                        app.setQQFriendStream(false);
                        app.setAllowQQFriendStream(false);
                        //先执行杀掉qq后台操作
                        mAccessibilityService = new BaseAccessibilityService();
                        mAccessibilityService.execShellCmd("am force-stop com.tencent.mobileqq");
                    }

                    @Override
                    public void Negative() {
                        mConfirmDialog.dismiss();

                    }
                });
                mConfirmDialog.setContent("提示：" + "\n是否确定重置服务");
                mConfirmDialog.setCancelable(true);
                mConfirmDialog.show();
                break;
            case 3:
                inAnimation(PhoneActivity.class);//通讯录
                break;
            case 4:
                inAnimation(QQAddFriendsActivity.class);//加好友
                break;
            case 5:
                inAnimation(QQFriendStreamActivity.class);//加群友
                break;
            case 6:
                inAnimation(QQAKeySendMessageActivity.class);//一键发消息
                break;
            case 7:
                inAnimation(QQNearbyPeopleActivity.class);//附近人
                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;
            case 11:

                break;
            case 12:

                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("QQHomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("QQHomeFragment");
    }
}
