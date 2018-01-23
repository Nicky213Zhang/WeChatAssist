package vicmob.micropowder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import vicmob.micropowder.daoman.bean.PublicNumBean;
import vicmob.micropowder.daoman.dao.PublicNumDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.PublicNumAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 公众号
 */
public class PublicNumberActivity extends BaseActivity {


    @BindView(R.id.iv_back_pn) ImageView ivBackPn;
    @BindView(R.id.rl_title_pn) RelativeLayout rlTitlePn;
    @BindView(R.id.iv_begin_pn) ShSwitchView ivBeginPn;
    @BindView(R.id.rl_open_pn) RelativeLayout rlOpenPn;
    @BindView(R.id.et_pushaddpublicname) EditText etPushaddpublicname;
    @BindView(R.id.btn_pushaddpublicname) Button btnPushaddpublicname;
    @BindView(R.id.ll_pushpublicname) LinearLayout llPushpublicname;
    @BindView(R.id.et_pushpeoplenumtwo) EditText etPushpeoplenumtwo;
    @BindView(R.id.btn_confirmpushpeople) Button btnConfirmpushpeople;
    @BindView(R.id.et_pushstartpeople) EditText etPushstartpeople;
    @BindView(R.id.btn_confirmstartpeople) Button btnConfirmstartpeople;
    @BindView(R.id.ll_pushpublicnumtwo) LinearLayout llPushpublicnumtwo;
    @BindView(R.id.rl_content_pn) RelativeLayout rlContentPn;

    @BindView(R.id.but_begin_pn) Button butBeginPn;
    @BindView(R.id.rclv_publicnum) RecyclerView rclvPublicnum;
    private PublicNumAdapter mPublicNumAdapter;
    private List<PublicNumBean> mPublicNumBeanList = new ArrayList<>();
    private PublicNumBean mPublicNumBean;
    private PublicNumDao mPublicNumDao;
    private MyApplication app;
    private ConfirmDialog mConfirmDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mPublicNumAdapter = new PublicNumAdapter(PublicNumberActivity.this, mPublicNumBeanList);
                    rclvPublicnum.setAdapter(mPublicNumAdapter);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_number);
        ButterKnife.bind(this);
        initView();
        initRecyclerView();
    }

    private void initView() {
        mPublicNumDao = new PublicNumDao(PublicNumberActivity.this);
        mPublicNumBeanList = mPublicNumDao.queryForAll();
        Collections.reverse(mPublicNumBeanList);
        mHandler.sendEmptyMessage(1);
    }


    /**
     * 布局可见时调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        initOpenState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        rclvPublicnum.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器

        rclvPublicnum.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式

        //        mPublicNumAdapter = new PublicNumAdapter(PublicNumberActivity.this, mPublicNumBeanList);   //添加RecyclerView适配器
        //        rclvPublicnum.setAdapter(mPublicNumAdapter);
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginPn.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(PublicNumberActivity.this)) {

            ivBeginPn.setOn(true);   //打开

        } else {

            ivBeginPn.setOn(false);  //关闭
        }
    }

    @OnClick({R.id.iv_back_pn, R.id.btn_pushaddpublicname, R.id.btn_confirmpushpeople, R.id.btn_confirmstartpeople, R.id.but_begin_pn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_pn:
                outAnimation();
                break;
            case R.id.btn_pushaddpublicname:
                //点击确定公众号
                String publicNumNameText = etPushaddpublicname.getText().toString().trim();
                if (publicNumNameText == "" || TextUtils.isEmpty(publicNumNameText)) {
                    Toast.makeText(PublicNumberActivity.this, "未输入推送公众号", Toast.LENGTH_SHORT).show();
                } else {
                    mPublicNumDao = new PublicNumDao(PublicNumberActivity.this);
                    //添加内容到实例中
                    mPublicNumBean = new PublicNumBean();
                    mPublicNumBean.setPublicNumName(publicNumNameText);
                    mPublicNumDao.add(mPublicNumBean); //添加数据库
                    //数据添加到集合并置顶选中，并刷新界面
                    mPublicNumBeanList = mPublicNumDao.queryForAll();
                    Collections.reverse(mPublicNumBeanList);
                    mHandler.sendEmptyMessage(1);
                    mPublicNumAdapter.notifyDataSetChanged();
                    etPushaddpublicname.setText("");
                }
                break;
            case R.id.btn_confirmpushpeople:
                String publicNumPeopleNumText = etPushpeoplenumtwo.getText().toString().trim();
                PrefUtils.putString(PublicNumberActivity.this, "publicNumPeopleNum", publicNumPeopleNumText);
                if (publicNumPeopleNumText == null || TextUtils.isEmpty(publicNumPeopleNumText)) {
                    MyToast.show(PublicNumberActivity.this, "没有添加推送人数" );
                } else
                {
                    MyToast.show(PublicNumberActivity.this, "推送人数为" + publicNumPeopleNumText);
                }
                etPushpeoplenumtwo.setText("");
                break;
            case R.id.btn_confirmstartpeople:
                String publicNumStartPeopleText = etPushstartpeople.getText().toString().trim();
                PrefUtils.putString(PublicNumberActivity.this, "PublicNumStartPeople", publicNumStartPeopleText);
                if (publicNumStartPeopleText == null || TextUtils.isEmpty(publicNumStartPeopleText)) {
                    MyToast.show(PublicNumberActivity.this, "从第0人开始" );
                } else {
                    MyToast.show(PublicNumberActivity.this, "从第" + publicNumStartPeopleText + "人开始");
                }
                etPushstartpeople.setText("");
                break;
            case R.id.but_begin_pn:
                String publicNumPeopleNum = PrefUtils.getString(PublicNumberActivity.this, "publicNumPeopleNum", null);
                String publicNumStartPeople = PrefUtils.getString(PublicNumberActivity.this, "PublicNumStartPeople", null);
                if (publicNumPeopleNum == null || TextUtils.isEmpty(publicNumPeopleNum)) {
                    PrefUtils.putString(PublicNumberActivity.this, "publicNumPeopleNum", "30");
                } else if (publicNumStartPeople == null || TextUtils.isEmpty(publicNumStartPeople)) {
                    PrefUtils.putString(PublicNumberActivity.this, "PublicNumStartPeople", "0");
                }
                if (isServiceOpening(PublicNumberActivity.this)) {
                    app = (MyApplication) getApplication();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先执行杀掉微信后台操作
                            BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                            mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                            mAccessibilityService.execShellCmd("input keyevent 3");
                            app.setPublicNumber(true);  //开启公众号模块
                            app.setAllowPublicNum(true);
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
                    mConfirmDialog = new ConfirmDialog(PublicNumberActivity.this, new Callback() {
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
}
