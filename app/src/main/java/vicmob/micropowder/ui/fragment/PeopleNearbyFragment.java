package vicmob.micropowder.ui.fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.sevenheaven.iosswitch.ShSwitchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseQQFragment;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.LoadData;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.LoadLocationBean;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.bean.NearbyPeopleBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.daoman.dao.NearbyPeopleDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.activity.LocationManageActivity;
import vicmob.micropowder.ui.activity.NearbyPeopleActivity;
import vicmob.micropowder.ui.adapter.NearbyPeopleAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.ContentDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by qq944 on 2017/7/25.
 */

public class PeopleNearbyFragment extends BaseQQFragment implements View.OnClickListener {
    private ImageView mIvBack;
    private EditText mEtContent;
    private Button mButBegin;
    private RelativeLayout mRlOpen;
    private ShSwitchView mIvBegin;
    private RecyclerView mRvContent;
    private Button mButEdit;

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
    private LoadLocationBean mLoadLocationBean;

    private List<MapSearchBean> mMapSearchBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_people_nearby;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(inflater, container, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        app = (MyApplication) getActivity().getApplication();
        mEtContent.setText(mNrearByNum + "");
        mEtContent.setSelection(mEtContent.getText().length());
        initDB();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mButBegin = (Button) findViewById(R.id.but_begin);
        mRlOpen = (RelativeLayout) findViewById(R.id.rl_open);
        mIvBegin = (ShSwitchView) findViewById(R.id.iv_begin);
        mRvContent = (RecyclerView) findViewById(R.id.rv_content);
        mButEdit = (Button) findViewById(R.id.but_edit);
        mIvBack.setOnClickListener(this);
        mButBegin.setOnClickListener(this);
        mButEdit.setOnClickListener(this);
        mRlOpen.setOnClickListener(this);
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询数据库
        NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(getActivity());
        mNearbyPeopleBeanList = nearbyPeopleDao.queryForAll();
        //初始化数据库倒叙显示！！！
//        Collections.reverse(mNearbyPeopleBeanList);

        if (mMapSearchBeanList.size() > 0) {
            mMapSearchBeanList.clear();
        }
        if (app.mNetworkAddressList.size() > 0 && app.mNetworkAddressList != null) {
            mMapSearchBeanList.addAll(app.mNetworkAddressList);
        }
        mMapSearchDao = new MapSearchDao(getActivity());
        List<MapSearchBean> mMapSearchBeanCurrentList = mMapSearchDao.queryForAll();
        if (mMapSearchBeanCurrentList.size() > 0) {
            mMapSearchBeanList.addAll(mMapSearchBeanCurrentList);
        }
        if (app.mAllAddressList.size() > 0) {
            app.mAllAddressList.clear();
        }
        app.mAllAddressList = mMapSearchBeanList;
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));         //设置布局管理器

        mRvContent.addItemDecoration(new DividerItemDecoration(getActivity()));      //设置分割线样式

        mNearbyPeopleAdapter = new NearbyPeopleAdapter(getActivity(), mNearbyPeopleBeanList);   //添加RecyclerView适配器
        mRvContent.setAdapter(mNearbyPeopleAdapter);
    }

    /**
     * 布局可见时调用按钮的开启状态
     */
    @Override
    public void onStart() {
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
        if (isQQServiceOpening(getActivity())) {

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
    private void startMock(Double latitude, Double longitude, int lac, int cid) {

        mSQLiteDatabase = new PxDBHelper(getActivity()).getWritableDatabase();
        //创建一个集合
        ContentValues contentValues = new ContentValues();
        contentValues.put("package_name", "com.tencent.mobileqq");
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("lac", lac);
        contentValues.put("cid", cid);
        //将集合里的参数插入到数据库
        mSQLiteDatabase.insertWithOnConflict(PxDBHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
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

                    MyToast.show(getActivity(), "每次添加上限80人哦~");
                    return;
                }

                PrefUtils.putInt(getActivity(), "mQQNearTextNum", mNearTextNum); //存储输入人数

                if (isQQServiceOpening(getActivity())) {
                    if (mMapSearchBeanList.size() != 0) {
                        //app.setNearbyPeople(true);  //开启附近人模块
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //先执行杀掉微信后台操作
                                mAccessibilityService = new BaseAccessibilityService();
                                mAccessibilityService.execShellCmd("am force-stop com.tencent.mobileqq");
                                mAccessibilityService.execShellCmd("input keyevent 3");

                                startMock(mMapSearchBeanList.get(0).getLatitudes(), mMapSearchBeanList.get(0).getLongtitudes(), mMapSearchBeanList.get(0).getLac(), mMapSearchBeanList.get(0).getCid());
                                Log.i("456", mMapSearchBeanList.get(0).getLatitudes() + "  " + mMapSearchBeanList.get(0).getLongtitudes() + "  " + mMapSearchBeanList.get(0).getLac() + "  " + mMapSearchBeanList.get(0).getCid());
                                app.setAllowQQNearbyPeople(true);
                                app.setQQNearbyPeople(true);
                                try {
                                    Thread.sleep(2500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                intentQq(); //跳转QQ主界面
                            }
                        }).start();
                    } else {

                        MyToast.show(getActivity(), "地点不能为空");
                    }

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
                break;

            case R.id.rl_open: //服务开关切换
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                break;

            case R.id.but_edit: //添加打招呼内容
                mContentDialog = new ContentDialog(getActivity(), new Callback() {
                    /**
                     * 点击添加内容
                     */
                    @Override
                    public void Positive() {
                        String contentDialogText = PrefUtils.getString(getActivity(), "ContentDialogText", null);
                        if (contentDialogText == "" || TextUtils.isEmpty(contentDialogText)) {
                            MyToast.show(getActivity(), "内容设置不能为空");
                        } else {

                            //添加数据库
                            NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(getActivity());
                            //获取储存的内容
                            String mNearbyPeopleText = PrefUtils.getString(getActivity(), "ContentDialogText", null);

                            if (nearbyPeopleDao.queryOne(mNearbyPeopleText)) {
                                MyToast.show(getActivity(), "输入内容重复");
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
}
