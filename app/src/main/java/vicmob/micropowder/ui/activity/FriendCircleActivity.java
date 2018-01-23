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
import vicmob.micropowder.daoman.bean.FriendCircleContentBean;
import vicmob.micropowder.daoman.dao.FriendCircleDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.FriendCircleRecyclerAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.ContentDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p>
 * 朋友圈
 */
public class FriendCircleActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.but_begin)
    Button mButBegin;
    @BindView(R.id.but_edit)
    Button mButEdit;
    @BindView(R.id.iv_begin)
    ShSwitchView mIvBegin;
    @BindView(R.id.rl_open)
    RelativeLayout mRlOpen;
    @BindView(R.id.et_content)
    EditText mEtContent;

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
     * 添加附近人的人数
     */
    private int mFriendTextNum;
    /**
     * 输入框输入的内容
     */
    private String FCNum = "30";
    private FriendCircleRecyclerAdapter mFriendCircleAdapter;

    private ContentDialog mContentDialog;

    private List<FriendCircleContentBean> mFriendCircleContentList;

    private FriendCircleContentBean mFriendCircleContentBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_circle);
        ButterKnife.bind(this);
        String wx_circle_num = PrefUtils.getString(FriendCircleActivity.this, Constant.wxFunction[9], "0");
        if (wx_circle_num.equals("0") || TextUtils.isEmpty(wx_circle_num)) {
            mEtContent.setText(FCNum);
        } else {
            mEtContent.setText(wx_circle_num);
        }
        mEtContent.setSelection(mEtContent.getText().toString().trim().length());
        initDB();

        initRecyclerView();
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询数据库
        FriendCircleDao friendCircleDao = new FriendCircleDao(FriendCircleActivity.this);
        mFriendCircleContentList = friendCircleDao.queryForAll();
        //初始化数据库倒叙显示！！！
        Collections.reverse(mFriendCircleContentList);

        //设置软键盘回车键
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    mText = mEtContent.getText().toString().trim();     //输入人数
                    try {
                        mFriendTextNum = Integer.parseInt(mText);    //String转化成Int
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (mFriendTextNum <= 0) {
                        MyToast.show(FriendCircleActivity.this, "评论条数不能为零");
                    } else if (mFriendTextNum > 60) {
                        MyToast.show(FriendCircleActivity.this, "评论条数不能大于60条");
                    } else {
                        PrefUtils.putInt(getApplication(), "mFriendTextNum", mFriendTextNum); //存储输入人数

                        if (isServiceOpening(FriendCircleActivity.this)) {

                            app = (MyApplication) getApplication();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //先执行杀掉微信后台操作
                                    BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                                    mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                    mAccessibilityService.execShellCmd("input keyevent 3");
                                    app.setFriendCircle(true);  //开启朋友圈模块
                                    app.setAllowFriendCircle(true);
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
                            mConfirmDialog = new ConfirmDialog(FriendCircleActivity.this, new Callback() {
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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器

        mRvContent.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式

        mFriendCircleAdapter = new FriendCircleRecyclerAdapter(FriendCircleActivity.this, mFriendCircleContentList);   //添加RecyclerView适配器
        mRvContent.setAdapter(mFriendCircleAdapter);
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

    @OnClick({R.id.iv_back, R.id.but_begin, R.id.but_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;

            case R.id.rl_open: //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;

            case R.id.but_begin:    //开始按钮

                mText = mEtContent.getText().toString().trim();     //输入人数
                try {
                    mFriendTextNum = Integer.parseInt(mText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (mFriendTextNum <= 0) {
                    MyToast.show(FriendCircleActivity.this, "评论条数不能为零");
                } else if (mFriendTextNum > 60) {
                    MyToast.show(FriendCircleActivity.this, "评论条数不能大于60条");
                } else {
                    PrefUtils.putInt(getApplication(), "mFriendTextNum", mFriendTextNum); //存储输入人数

                    if (isServiceOpening(FriendCircleActivity.this)) {

                        app = (MyApplication) getApplication();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                                mAccessibilityService.execShellCmd("input keyevent 3");
                                app.setFriendCircle(true);  //开启朋友圈模块
                                app.setAllowFriendCircle(true);
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
                        mConfirmDialog = new ConfirmDialog(FriendCircleActivity.this, new Callback() {
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

            case R.id.but_edit:     //添加按钮
                mContentDialog = new ContentDialog(FriendCircleActivity.this, new Callback() {
                    @Override
                    /**
                     * 点击添加内容
                     */
                    public void Positive() {
                        String contentDialogText = PrefUtils.getString(FriendCircleActivity.this, "ContentDialogText", null);
                        if (contentDialogText == "" || TextUtils.isEmpty(contentDialogText) || "null".equals(contentDialogText)) {
                            MyToast.show(FriendCircleActivity.this, "内容设置不能为空");

                        } else {

                            //添加数据库
                            FriendCircleDao friendCircleDao = new FriendCircleDao(FriendCircleActivity.this);
                            //获取储存的内容
                            String mFriendText = PrefUtils.getString(FriendCircleActivity.this, "ContentDialogText", null);

                            if (friendCircleDao.queryOne(mFriendText)) {
                                MyToast.show(FriendCircleActivity.this, "输入内容重复");
                            } else {

                                //添加内容到实例中
                                mFriendCircleContentBean = new FriendCircleContentBean();
                                mFriendCircleContentBean.setFriendContent(mFriendText);
                                //添加数据库
                                friendCircleDao.add(mFriendCircleContentBean);
                                //数据添加到集合并置顶选中，并刷新界面
                                mFriendCircleContentList.add(0, mFriendCircleContentBean);

                                mFriendCircleAdapter.notifyDataSetChanged();
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
                }

                );

                mContentDialog.setCancelable(true);
                mContentDialog.show();
                break;
        }
    }

    /**
     * 开关状态发生改变
     */

    private void SwitchChanged() {
        mIvBegin.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
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
        if (isServiceOpening(FriendCircleActivity.this)) {

            mIvBegin.setOn(true);   //打开

        } else {

            mIvBegin.setOn(false);  //关闭
        }
    }
}
