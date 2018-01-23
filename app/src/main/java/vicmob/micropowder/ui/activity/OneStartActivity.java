package vicmob.micropowder.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.bean.NearbyPeopleBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.daoman.dao.NearbyPeopleDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.utils.MyToast;

/**
 * Created by admin on 2017/10/19.
 */
public class OneStartActivity extends BaseActivity {
    @BindView(R.id.iv_back_onestart)
    ImageView ivBackOnestart;
    @BindView(R.id.rl_title_onestart)
    RelativeLayout rlTitleOnestart;
    @BindView(R.id.iv_begin_onestart)
    ShSwitchView ivBeginOnestart;
    @BindView(R.id.rl_open_onestart)
    RelativeLayout rlOpenOnestart;
    @BindView(R.id.btn_begin_onestart)
    Button btnBeginOnestart;

    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 全局变量
     */
    private MyApplication app;
    private BaseAccessibilityService mAccessibilityService;
    private NearbyPeopleBean mNearbyPeopleBean;
    private MapSearchDao mMapSearchDao;
    private List<NearbyPeopleBean> mNearbyPeopleBeanList;
    /**
     * 数据库
     */
    public static SQLiteDatabase mSQLiteDatabase;
    /**
     * 网络获取实例
     */

    private List<MapSearchBean> mMapSearchBeanList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onestart);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        initDB();
    }
    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询打招呼数据库
        NearbyPeopleDao nearbyPeopleDao = new NearbyPeopleDao(OneStartActivity.this);
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
        mMapSearchDao = new MapSearchDao(OneStartActivity.this);
        List<MapSearchBean> mMapSearchBeanCurrentList = mMapSearchDao.queryForAll();
        if (mMapSearchBeanCurrentList.size() > 0) {
            mMapSearchBeanList.addAll(mMapSearchBeanCurrentList);
        }
        if (app.mAllAddressList.size() > 0) {
            app.mAllAddressList.clear();
        }
        app.mAllAddressList = mMapSearchBeanList;
        Log.i("123",  app.mAllAddressList.size() + ":" + app.mNetworkAddressList.size());

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

    @OnClick({R.id.iv_back_onestart, R.id.rl_open_onestart, R.id.btn_begin_onestart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_onestart:
                outAnimation();
                break;
            case R.id.rl_open_onestart:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.btn_begin_onestart:
                if (isServiceOpening(OneStartActivity.this)) {
                    app.setNearbyPeople(true);  //开启附近人模块
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                            if (mMapSearchBeanList!=null&&mMapSearchBeanList.size()>0) {
                            //先执行杀掉微信后台操作
//                            mAccessibilityService = new BaseAccessibilityService();
//                            mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
//                            mAccessibilityService.execShellCmd("input keyevent 3");

                                startMock(mMapSearchBeanList.get(0).getLatitudes(), mMapSearchBeanList.get(0).getLongtitudes(), mMapSearchBeanList.get(0).getLac(), mMapSearchBeanList.get(0).getCid(), mMapSearchBeanList.get(0).getMnc());
                                app.setNearbyPeople(true);
                                app.setAllowGreeting(true);
                                app.setAllowOneStart(true);
//                                try {
//                                    Thread.sleep(2500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                intentWechat(); //跳转微信主界面
                            }else {
                               handler.sendEmptyMessage(1);
                            }
//                        }
//                    }).start();
                } else {

                    //跳转服务界面对话框
                    mConfirmDialog = new ConfirmDialog(OneStartActivity.this, new Callback() {
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
        if (isServiceOpening(OneStartActivity.this)) {
            ivBeginOnestart.setOn(true);   //打开

        } else {
            ivBeginOnestart.setOn(false);  //关闭
        }
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        ivBeginOnestart.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
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
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    MyToast.show(OneStartActivity.this,"请添加地点");
                    break;
            }
        }
    };
}
