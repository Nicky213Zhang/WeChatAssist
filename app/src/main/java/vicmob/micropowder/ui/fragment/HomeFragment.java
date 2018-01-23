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
import vicmob.micropowder.ui.activity.AKeySendMessageActivity;
import vicmob.micropowder.ui.activity.AccountManageActivity;
import vicmob.micropowder.ui.activity.AddFriendActivity;
import vicmob.micropowder.ui.activity.AddGroupFriendActivity;
import vicmob.micropowder.ui.activity.DriftBottleActivity;
import vicmob.micropowder.ui.activity.FriendCircleActivity;
import vicmob.micropowder.ui.activity.LocationManageActivity;
import vicmob.micropowder.ui.activity.NearbyPeopleActivity;
import vicmob.micropowder.ui.activity.OneStartActivity;
import vicmob.micropowder.ui.activity.PhoneActivity;
import vicmob.micropowder.ui.activity.PublicNumberActivity;
import vicmob.micropowder.ui.activity.VoluntarilyReplyActivity;
import vicmob.micropowder.ui.adapter.MainAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 首页
 */
public class HomeFragment extends BaseQQFragment implements AdapterView.OnItemClickListener {

    private GridView mGvContent;

    private MainAdapter mMainAdapter;
    private MyApplication app;
    private ConfirmDialog mConfirmDialog;
    private String current_channel;
    private BaseAccessibilityService mAccessibilityService;

    public static HomeFragment newInstance(String title) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(inflater, container, savedInstanceState);
        initView();
    }

    public void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            current_channel = bundle.getString("activity");
            //fabClick(current_channel);
        }
        mGvContent = (GridView) findViewById(R.id.gv_content);

        //GridView填充适配器
        mMainAdapter = new MainAdapter(mActivity, Constant.MAINNAMES, Constant.MAINIMAGES);

        mGvContent.setAdapter(mMainAdapter);

        mGvContent.setOnItemClickListener(HomeFragment.this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                inAnimation(AccountManageActivity.class);//操作指南
                break;

            case 1:
                inAnimation(LocationManageActivity.class);//添加地点
                break;

            case 2:
                inAnimation(NearbyPeopleActivity.class);//附近人
                break;

            case 3:
                //重置
                mConfirmDialog = new ConfirmDialog(mActivity, new Callback() {
                    @Override
                    public void Positive() {
                        app = new MyApplication();
                        app.setNearbyPeople(false);     //初始化全局变量
                        app.setFriendCircle(false);
                        app.setAddFriends(false);
                        app.setPublicNumber(false);
                        app.setDriftBottle(false);
                        app.setGroupFriend(false);
                        app.setAutoReply(false);
                        app.setWhiteList(false);
                        app.setAKeySendMessage(false);  //开启一键发消息// 模块
                        //先执行杀掉微信后台操作
                        mAccessibilityService = new BaseAccessibilityService();
                        mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
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

            case 4:
                inAnimation(OneStartActivity.class);//一键开始
                break;

            case 5:
                inAnimation(PhoneActivity.class);//通讯录
                break;

            case 6:
                inAnimation(AddFriendActivity.class);//一键加好友
                break;

            case 7:
                inAnimation(AddGroupFriendActivity.class);//一键加群好友
                break;

            case 8:
                inAnimation(PublicNumberActivity.class);//公众号
                break;

            case 9:
                inAnimation(DriftBottleActivity.class);//漂流瓶
                break;

            case 10:
                inAnimation(FriendCircleActivity.class);//朋友圈
                break;

            case 11:
                inAnimation(AKeySendMessageActivity.class);//一键发消息
                break;
            case 12:
                inAnimation(VoluntarilyReplyActivity.class);//自动回复
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");
    }

}
