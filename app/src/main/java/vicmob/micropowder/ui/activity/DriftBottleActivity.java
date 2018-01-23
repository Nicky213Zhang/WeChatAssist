package vicmob.micropowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.bean.DriftBottleContentBean;
import vicmob.micropowder.daoman.dao.DriftBottleDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.DriftBottleRecyclerAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.ContentDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 漂流瓶
 */
public class DriftBottleActivity extends BaseActivity {
    @BindView(R.id.iv_back_bottle)
    ImageView ivBackBottle;
    @BindView(R.id.iv_begin_bottle)
    ShSwitchView ivBeginBottle;
    @BindView(R.id.rl_open_bottle)
    RelativeLayout rlOpenBottle;
    @BindView(R.id.et_content_bottle)
    EditText etContentBottle;
    @BindView(R.id.rv_content_bottle)
    RecyclerView rvContentBottle;
    @BindView(R.id.but_begin_bottle)
    Button butBeginBottle;
    @BindView(R.id.but_edit_bottle)
    Button butEditBottle;

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
    private String mText;
    /**
     * 添加漂流瓶个数
     */
    private int mBottleTextNum;
    /**
     * 添加漂流瓶个数
     */
    private String DriftBottleNum = "2";
    private DriftBottleRecyclerAdapter mDriftBottleAdapter;
    private ContentDialog mContentDialog;
    private List<DriftBottleContentBean> mDriftBottleContentList;
    private DriftBottleContentBean mDriftBottleContentBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);
        ButterKnife.bind(this);

        initDB();

        initRecyclerView();

    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询数据库
        DriftBottleDao driftBottleDao = new DriftBottleDao(DriftBottleActivity.this);
        mDriftBottleContentList = driftBottleDao.queryForAll();
        //初始化数据库倒叙显示！！！
        Collections.reverse(mDriftBottleContentList);
        etContentBottle.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mText = etContentBottle.getText().toString().trim();     //输入人数
                    try {
                        mBottleTextNum = Integer.parseInt(mText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    if (mBottleTextNum > 20) {

                        MyToast.show(DriftBottleActivity.this, "瓶子个数最大为20");

                        return false;
                    }

                    PrefUtils.putInt(getApplication(), "mBottleTextNum", mBottleTextNum); //存储输入人数

                    if (isServiceOpening(DriftBottleActivity.this)) {

                        app = (MyApplication) getApplication();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                mAccessibilityService.execShellCmd("input keyevent 3");
                                app.setDriftBottle(true);  //开启朋友圈模块
                                app.setAllowDriftBottle(true);
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
                        mConfirmDialog = new ConfirmDialog(DriftBottleActivity.this, new Callback() {
                            @Override
                            public void Positive() {
                                mText = etContentBottle.getText().toString().trim();     //输入人数
                                if (TextUtils.isEmpty(mText)){
                                    MyToast.show(DriftBottleActivity.this, "瓶子个数不能为空");
                                    return;
                                }
                                try {
                                    mBottleTextNum = Integer.parseInt(mText);    //String转化成Int
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        rvContentBottle.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器

        rvContentBottle.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式

        mDriftBottleAdapter = new DriftBottleRecyclerAdapter(DriftBottleActivity.this, mDriftBottleContentList);   //添加RecyclerView适配器
        rvContentBottle.setAdapter(mDriftBottleAdapter);
    }

    /**
     * 布局可见时调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        String wx_driftbottle_num = PrefUtils.getString(DriftBottleActivity.this, Constant.wxFunction[7], "0");
        if (wx_driftbottle_num.equals("0") || TextUtils.isEmpty(wx_driftbottle_num)) {
            etContentBottle.setText(DriftBottleNum);
        } else {
            etContentBottle.setText(wx_driftbottle_num);
        }
        etContentBottle.setSelection(etContentBottle.getText().toString().trim().length());
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

    @OnClick({R.id.iv_back_bottle, R.id.rl_open_bottle, R.id.but_begin_bottle, R.id.but_edit_bottle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_bottle:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();  //返回按钮
                break;
            case R.id.rl_open_bottle:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;
            case R.id.but_begin_bottle:

                mText = etContentBottle.getText().toString().trim();     //输入人数
                if (TextUtils.isEmpty(mText)){
                    MyToast.show(DriftBottleActivity.this, "瓶子个数不能为空");

                    return;
                }
                try {
                    mBottleTextNum = Integer.parseInt(mText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (mBottleTextNum > 20) {

                    MyToast.show(DriftBottleActivity.this, "瓶子个数最大为20");

                    return;
                }

                PrefUtils.putInt(getApplication(), "mBottleTextNum", mBottleTextNum); //存储输入人数

                if (isServiceOpening(DriftBottleActivity.this)) {

                    app = (MyApplication) getApplication();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //先执行杀掉微信后台操作
                            BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                            mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                            mAccessibilityService.execShellCmd("input keyevent 3");
                            app.setDriftBottle(true);  //开启朋友圈模块
                            app.setAllowDriftBottle(true);
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
                    mConfirmDialog = new ConfirmDialog(DriftBottleActivity.this, new Callback() {
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
            case R.id.but_edit_bottle:
                mContentDialog = new ContentDialog(DriftBottleActivity.this, new Callback() {
                    @Override
                    /**
                     * 点击添加内容
                     */
                    public void Positive() {

                        String contentDialogText = PrefUtils.getString(DriftBottleActivity.this, "ContentDialogText", null);
                        if (contentDialogText == "" || TextUtils.isEmpty(contentDialogText) || "null".equals(contentDialogText)) {
                            MyToast.show(DriftBottleActivity.this, "内容设置不能为空");

                        } else if (contentDialogText.length() < 5) {
                            MyToast.show(DriftBottleActivity.this, "字数不能少于5个字");

                        } else {

                            //添加数据库
                            DriftBottleDao driftBottleDao = new DriftBottleDao(DriftBottleActivity.this);
                            //获取储存的内容
                            String mBottleText = PrefUtils.getString(DriftBottleActivity.this, "ContentDialogText", null);

                            if (driftBottleDao.queryOne(mBottleText)) {
                                MyToast.show(DriftBottleActivity.this, "输入内容重复");

                            } else {
                                //添加内容到实例中
                                mDriftBottleContentBean = new DriftBottleContentBean();
                                mDriftBottleContentBean.setBottleContent(mBottleText);
                                //添加数据库
                                driftBottleDao.add(mDriftBottleContentBean);
                                //数据添加到集合并置顶选中，并刷新界面
                                mDriftBottleContentList.add(0, mDriftBottleContentBean);

                                mDriftBottleAdapter.notifyDataSetChanged();

                                mContentDialog.dismiss();
                            }
                        }
                    }

                    /**
                     * 点击小黄叉
                     */
                    @Override
                    public void Negative() {
                        mContentDialog.dismiss();
                    }
                });

                mContentDialog.setCancelable(true);
                mContentDialog.show();
                break;
        }
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginBottle.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(DriftBottleActivity.this)) {

            ivBeginBottle.setOn(true);   //打开

        } else {

            ivBeginBottle.setOn(false);  //关闭
        }
    }
}