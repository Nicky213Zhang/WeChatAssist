package vicmob.micropowder.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.bean.NearbyPeopleBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.daoman.dao.NearbyPeopleDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.NearbyPeopleAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.ContentDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p>
 * 附近人
 */
public class NearbyPeopleActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.but_begin)
    Button mButBegin;
    @BindView(R.id.rl_open)
    RelativeLayout mRlOpen;
    @BindView(R.id.iv_begin)
    ShSwitchView mIvBegin;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.but_edit)
    Button mButEdit;

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
     * 附近的人默认个数
     */
    private String mNrearByNum = "30";
    /**
     * 添加附近人的人数
     */
    private int mNearTextNum;
    /**
     * 附近人RecyclerView适配器
     */
    private NearbyPeopleAdapter mNearbyPeopleAdapter;
    /**
     * 自定义Dialog
     */
    private ContentDialog mContentDialog;
    /**
     * 数据库
     */
    public static SQLiteDatabase mSQLiteDatabase;

    private NearbyPeopleBean mNearbyPeopleBean;

    private List<NearbyPeopleBean> mNearbyPeopleBeanList;

    private BaseAccessibilityService mAccessibilityService;

    private MapSearchDao mMapSearchDao;
    /**
     * 网络获取实例
     */

    private List<MapSearchBean> mMapSearchBeanList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        ButterKnife.bind(this);
        String wxNearby_Num = PrefUtils.getString(this, Constant.wxFunction[0], "");
        if (TextUtils.isEmpty(wxNearby_Num) || wxNearby_Num.equals("0")) {
            mEtContent.setText(mNrearByNum);
        } else {
            mEtContent.setText(wxNearby_Num);
        }
        mEtContent.setSelection(mEtContent.getText().toString().trim().length());
        app = (MyApplication) getApplication();
        initDB();
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询打招呼数据库
        NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(NearbyPeopleActivity.this);
        mNearbyPeopleBeanList = nearbyPeopleDao.queryForAll();
        //初始化数据库倒叙显示！！！
        Collections.reverse(mNearbyPeopleBeanList);
        //查询本地地址数据库
        if (mMapSearchBeanList.size() > 0) {
            mMapSearchBeanList.clear();
        }
        if (app.mNetworkAddressList.size() > 0 && app.mNetworkAddressList != null) {
            mMapSearchBeanList.addAll(app.mNetworkAddressList);
        }
        mMapSearchDao = new MapSearchDao(NearbyPeopleActivity.this);
        List<MapSearchBean> mMapSearchBeanCurrentList = mMapSearchDao.queryForAll();
        if (mMapSearchBeanCurrentList.size() > 0) {
            mMapSearchBeanList.addAll(mMapSearchBeanCurrentList);
        }
        if (app.mAllAddressList.size() > 0) {
            app.mAllAddressList.clear();
        }
        app.mAllAddressList = mMapSearchBeanList;
        Log.i("123", app.mAllAddressList.size() + ":" + app.mNetworkAddressList.size());
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器

        mRvContent.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式

        mNearbyPeopleAdapter = new NearbyPeopleAdapter(NearbyPeopleActivity.this, mNearbyPeopleBeanList);   //添加RecyclerView适配器
        mRvContent.setAdapter(mNearbyPeopleAdapter);
    }

    /**
     * 布局可见时调用按钮的开启状态
     */
    @Override
    protected void onStart() {
        super.onStart();

        initOpenState();
    }

    /**
     * 布局交互时调用按钮的监听
     */
    @Override
    public void onResume() {
        super.onResume();

        SwitchChanged();
    }

    @OnClick({R.id.iv_back, R.id.but_begin, R.id.rl_open, R.id.but_edit})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                outAnimation();     //返回按钮
                break;

            case R.id.but_begin:    //

                mText = mEtContent.getText().toString().trim();     //输入人数
                try {
                    mNearTextNum = Integer.parseInt(mText);    //String转化成Int
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (mNearTextNum > 80) {

                    MyToast.show(NearbyPeopleActivity.this, "每次添加上限80人哦~");
                    return;
                }

                PrefUtils.putInt(getApplication(), "mNearTextNum", mNearTextNum); //存储输入人数

                if (isServiceOpening(NearbyPeopleActivity.this)) {
                    if (mMapSearchBeanList.size() != 0) {
                        app.setNearbyPeople(true);  //开启附近人模块
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
//                                mAccessibilityService.execShellCmd("input keyevent 3");

                                startMock(mMapSearchBeanList.get(0).getLatitudes(), mMapSearchBeanList.get(0).getLongtitudes(), mMapSearchBeanList.get(0).getLac(), mMapSearchBeanList.get(0).getCid(), mMapSearchBeanList.get(0).getMnc());
                                app.setNearbyPeople(true);
                                app.setAllowGreeting(true);
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                intentWechat(); //跳转微信主界面
                            }
                        }).start();
                    } else {

                        MyToast.show(NearbyPeopleActivity.this, "地点不能为空");
                    }

                } else {

                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(NearbyPeopleActivity.this, new Callback() {
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

            case R.id.rl_open: //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;

            case R.id.but_edit: //添加打招呼内容
                mContentDialog = new ContentDialog(NearbyPeopleActivity.this, new Callback() {
                    /**
                     * 点击添加内容
                     */
                    @Override
                    public void Positive() {
                        String contentDialogText = PrefUtils.getString(NearbyPeopleActivity.this, "ContentDialogText", "");
                        if (contentDialogText == "" || TextUtils.isEmpty(contentDialogText) || "null".equals(contentDialogText)) {
                            MyToast.show(NearbyPeopleActivity.this, "内容设置不能为空");
                        } else {
                            if (contentDialogText.length() > 50) {
                                MyToast.show(NearbyPeopleActivity.this, "打招呼内容不能大于50个字符!");
                            } else {
                                //添加数据库
                                NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(NearbyPeopleActivity.this);
                                //获取储存的内容
                                String mNearbyPeopleText = PrefUtils.getString(NearbyPeopleActivity.this, "ContentDialogText", null);

                                if (nearbyPeopleDao.queryOne(mNearbyPeopleText)) {
                                    MyToast.show(NearbyPeopleActivity.this, "输入内容重复");
                                } else {
                                    //添加内容到实例中
                                    mNearbyPeopleBean = new NearbyPeopleBean();
                                    mNearbyPeopleBean.setNearbyContent(mNearbyPeopleText);
                                    //添加数据库
                                    nearbyPeopleDao.add(mNearbyPeopleBean);
                                    //数据添加到集合并置顶选中，并刷新界面
                                    mNearbyPeopleBeanList.add(0, mNearbyPeopleBean);

                                    mNearbyPeopleAdapter.notifyDataSetChanged();
                                    mContentDialog.dismiss();

                                }
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
        if (isServiceOpening(NearbyPeopleActivity.this)) {

            mIvBegin.setOn(true);   //打开

        } else {

            mIvBegin.setOn(false);  //关闭
        }
    }

    /**
     * 调用开始模拟的方法
     *
     * @param latitude
     * @param longitude
     */
    private void startMock(Double latitude, Double longitude, int lac, int cid, int mnc) {

        mSQLiteDatabase = new PxDBHelper(this).getWritableDatabase();
        //创建一个集合
        ContentValues contentValues = new ContentValues();
        contentValues.put("package_name", "com.tencent.mm");
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("lac", lac);
        contentValues.put("cid", cid);
        contentValues.put("mnc", mnc);
        //将集合里的参数插入到数据库
        mSQLiteDatabase.insertWithOnConflict(PxDBHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
