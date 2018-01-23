package vicmob.micropowder.ui.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseLoadNetDataOperator;
import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.ApkBean;
import vicmob.micropowder.ui.adapter.MenuAdapter;
import vicmob.micropowder.ui.adapter.TabVpAdapter;
import vicmob.micropowder.ui.fragment.HomeUpdateFragment;
import vicmob.micropowder.ui.fragment.MapFragment;
import vicmob.micropowder.ui.fragment.SettingFragment;
import vicmob.micropowder.ui.views.HintDialog;
import vicmob.micropowder.ui.views.LoadingDialog;
import vicmob.micropowder.ui.views.NoScrollViewPager;
import vicmob.micropowder.utils.DownLoadAppUtils;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;


/**
 * 主界面
 */

public class MainActivity extends AppCompatActivity implements BaseLoadNetDataOperator, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_main)
    NoScrollViewPager mVpMain;


    public SlidingMenu mSlidingMenu;
    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.rl_home)
    RelativeLayout mRlHome;
    @BindView(R.id.iv_address)
    ImageView mIvAddress;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.rg_tab)
    LinearLayout mRgTab;
    private ListView mLvMenu;
    //当前版本号
    private int mCurrentCode;

    // apk更新地址
    private String path = Url.APK_URL;
    private ApkBean mApkBean;
    private int currentVersionCode;
    //设备匹配地址
    private String DevicePath = Url.DeviceUrl;
    //设备匹配代号
    private String DeviceMatch = Url.DeviceMatch;
    /**
     * 二次退出
     */
    private long mKeyTime;
    // 识别序列号和ip线程
    private Thread thr;
    // 获取外网ip线程
    private Thread ipThr;
    // 获取外网IP详情
    private Thread ipThr1;
    // ip字符串
    private String ipDetail;
    // 国家字符串
    private String country;
    // 大区
    private String area;
    // 省份
    private String region;
    // 城市
    private String city;
    // ip归属
    private String isp;
    // IP
    private String IP;
    //屏幕dpi
    private float dpi;

    public static final int LOADING_MSG = 12;
    private static final int POSTVALUE = 8;
    private static final int IPSUCCESS = 9;
    private static final int IPSUCCESS1 = 10;
    private static final int IPFALSE = 11;
    public static final int LOAD_SUCCESS = 13;
    public static final int LOAD_FAIL = 14;

    private HintDialog mHintDialog;
    //    public static final int ISCLICK = 12;
//    public static boolean isclick;
    private LoadingDialog mLoadingDialog;
    public Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POSTVALUE:
                    Log.i("123", "POSTVALUE");
                    if (msg.obj.equals(DeviceMatch)) {
                        // 连接服务器，检查更新
                        loadNetData();
                        MyToast.show(getApplicationContext(),"您好！");

                    } else {
                        showUpdateDialog1();
                    }
                    break;
                case IPSUCCESS:
                    Log.i("123", "IPSUCCESS");
                    ipDetail = (String) msg.obj;
                    getLocalIpDetail(ipDetail);
                    break;
                case IPSUCCESS1:
                    // 开启code检测、ip获取的线程
                    Log.i("123", "IPSUCCESS1");
                    startThread1();
                    break;
                case IPFALSE:
                    // Toast.makeText(MainActivity.this, "未获取IP地址！",
                    // Toast.LENGTH_SHORT).show();
                    break;
                case LOADING_MSG:
                    //加载进度显示
                    //加载进度
                    mLoadingDialog = new LoadingDialog(MainActivity.this);
                    mLoadingDialog.setContent("正在玩命加载...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case LOAD_SUCCESS:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    MyToast.show(MainActivity.this, "获取成功", MyApplication.isDeBug);
                    break;
                case LOAD_FAIL:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    MyToast.show(MainActivity.this, "网络获取失败",MyApplication.isDeBug);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        initVP();

        initSlidingMenu();
//测试
        initData();
//        MyToast.show(MainActivity.this,getDeviceID(getApplicationContext()));
//        MyToast.show(MainActivity.this, getSerialNumber());
    }

    //初始化数据
    private void initData() {

        if (isNetworkAvailable(getApplicationContext())) {

            getLocalIp();
        } else {
            MyToast.show(this,"无法连接网络...",MyApplication.isDeBug);
        }


    }


    //初始化控件
    private void initView() {


    }


    //初始化ViewPager
    private void initVP() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeUpdateFragment());
        fragments.add(new MapFragment());
        fragments.add(new SettingFragment());
        //给Viewpager设置滑动选择监听
        mVpMain.setAdapter(new TabVpAdapter(getSupportFragmentManager(), fragments));
        mVpMain.addOnPageChangeListener(this);
        mVpMain.setCurrentItem(0);

    }


    //初始化侧滑菜单
    private void initSlidingMenu() {
        //创建侧滑菜单对象
        mSlidingMenu = new SlidingMenu(this);
        //设置侧滑菜单的滑出方向
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //设置侧滑菜单全屏可以滑出
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置侧滑菜单的宽度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        dpi = metrics.density;
        Log.i("zw_dpi_0", dpi + "");
        if (dpi < 1.9) {
            mSlidingMenu.setBehindOffset(600);
            Log.i("zw_dpi_1", dpi + "");
        } else if (dpi < 2.1) {
            mSlidingMenu.setBehindOffset(200);
            Log.i("zw_dpi_2", dpi + "");
        } else if (dpi < 2.6) {
            Log.i("zw_dpi_3", dpi + "");
        } else if (dpi < 3.1) {
            Log.i("zw_dpi_4", dpi + "");
        }
        //把侧滑菜单附加给Activity
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置淡入淡出的效果
        mSlidingMenu.setFadeDegree(0.4f);
        //设置侧滑菜单的布局
        mSlidingMenu.setMenu(R.layout.main_menu);

        mLvMenu = (ListView) mSlidingMenu.findViewById(R.id.lv_menu);
        //SlidingMenu添加适配器
        mLvMenu.setAdapter(new MenuAdapter(this));
        //SlidingMenu添加条目点击事件
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:     //白名单
                        startActivity(new Intent(MainActivity.this, WhiteListActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;

                    case 1:     //自动回复
                        startActivity(new Intent(MainActivity.this, VoluntarilyReplyActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;

                    case 2:     //设置
                        mVpMain.setCurrentItem(2, false);
                        mSlidingMenu.toggle();
                        break;

                    case 3:     //地点管理
                        startActivity(new Intent(MainActivity.this, LocationManageActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;

                    case 4:     //一键养号
                        startActivity(new Intent(MainActivity.this, OneKeyActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;
                    case 5:     //一键清粉
                        startActivity(new Intent(MainActivity.this, DeleteDeadFriendActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;
                    case 6:     //微信自动换号
                        startActivity(new Intent(MainActivity.this, AutoSwitchNumberActivity.class));
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;
                }
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void loadNetData() {

        String url = Url.VERSION_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            DownLoadAppUtils downLoadAppUtils = new DownLoadAppUtils(path, MainActivity.this);
                            mCurrentCode = downLoadAppUtils.setVersionCode();
                            Gson gson = new Gson();
                            mApkBean = new ApkBean();
                            if (response != null && !TextUtils.isEmpty(response)) {
                                mApkBean = gson.fromJson(response, ApkBean.class);
                                if (mApkBean.getWxversion().getVersionCode() > mCurrentCode) {
                                    downLoadAppUtils.updateApp(MainActivity.this, mApkBean.getWxversion().getVersionName());
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.rl_home, R.id.rl_address, R.id.rl_setting})
    public void onViewClicked(View view) {
        int item = 0;
        switch (view.getId()) {
            case R.id.rl_home:
                item = 0;
                mIvHome.setImageResource(R.drawable.mainbtn1);
                //mTvHome.setTextColor(getColor(R.color.main_tab_color));
                mTvHome.setTextColor(ContextCompat.getColor(this,R.color.main_tab_color));
                mIvAddress.setImageResource(R.drawable.addressbtn);
                mTvAddress.setTextColor(ContextCompat.getColor(this,R.color.white));
                mIvSetting.setImageResource(R.drawable.settingbtn);
                mTvSetting.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case R.id.rl_address:
                item = 1;
                mIvHome.setImageResource(R.drawable.mainbtn);
                mTvHome.setTextColor(ContextCompat.getColor(this,R.color.white));
                mIvAddress.setImageResource(R.drawable.addressbtn1);
                mTvAddress.setTextColor(ContextCompat.getColor(this,R.color.main_tab_color));
                mIvSetting.setImageResource(R.drawable.settingbtn);
                mTvSetting.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case R.id.rl_setting:
                item = 2;
                mIvHome.setImageResource(R.drawable.mainbtn);
                mTvHome.setTextColor(ContextCompat.getColor(this,R.color.white));
                mIvAddress.setImageResource(R.drawable.addressbtn);
                mTvAddress.setTextColor(ContextCompat.getColor(this,R.color.white));
                mIvSetting.setImageResource(R.drawable.settingbtn1);
                mTvSetting.setTextColor(ContextCompat.getColor(this,R.color.main_tab_color));
                break;
        }
        mVpMain.setCurrentItem(item, false);
    }

    /**
     * 2次退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSlidingMenu.isMenuShowing()) {
                mSlidingMenu.toggle();
            } else if ((System.currentTimeMillis() - mKeyTime) > 2000) {
                mKeyTime = System.currentTimeMillis();
                MyToast.show(this, "再按一次退出程序");
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 检查网络连接状态
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isConnected()) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * 获取本机外网IP地址
     *
     * @return IP地址
     */
    public void getLocalIp() {
        ipThr = new Thread() {
            @Override
            public void run() {
                URL infoUrl = null;
                InputStream inStream = null;
                String line = "";
                String line1 = "";
                try {
                    infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;

                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        while ((line = reader.readLine()) != null)
                            strber.append(line + "\n");
                        inStream.close();
                        // 从反馈的结果中提取出IP地址
                        int start = strber.indexOf("{");
                        int end = strber.indexOf("}");
                        String json = strber.substring(start, end + 3);

                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                line = jsonObject.optString("cip");
                                line1 = jsonObject.optString("cname");
                                Message message = new Message();
                                message.what = IPSUCCESS;
                                message.obj = line.toString();
                                myHandler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ipThr.start();

    }

    /**
     * 获取外网IP详情
     */
    public void getLocalIpDetail(final String str) {
        ipThr1 = new Thread() {
            @Override
            public void run() {

                try {
                    String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=" + str;
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setUseCaches(false);// 默认不使用缓存
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = connection.getInputStream();

                        // 将流转化为字符串
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String tmpString = "";
                        StringBuilder retJSON = new StringBuilder();
                        while ((tmpString = reader.readLine()) != null) {
                            retJSON.append(tmpString + "\n");
                        }

                        JSONObject jsonObject = new JSONObject(retJSON.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            IP = data.getString("ip");
                            country = data.getString("country");
                            area = data.getString("area") + "区";
                            region = data.getString("region");
                            city = data.getString("city");
                            isp = data.getString("isp");
                            Message message = new Message();
                            message.what = IPSUCCESS1;
                            myHandler.sendMessage(message);
                            Log.e("提示", "您的IP地址是：" + IP);
                            Log.e("提示", "country：" + country);
                            Log.e("提示", "area：" + area);
                            Log.e("提示", "region：" + region);
                            Log.e("提示", "city：" + city);
                        } else {
                            Message message = new Message();
                            message.what = IPFALSE;
                            myHandler.sendMessage(message);
                            Log.e("提示", "IP接口异常，无法获取IP地址！");
                        }
                    } else {
                        IP = "";
                        Log.e("提示", "网络连接异常，无法获取IP地址！");
                    }
                } catch (Exception e) {
                    IP = "";
                    Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
                }
            }
        };
        ipThr1.start();

    }

    // 连接服务器，匹配序列号、ip和区域
    public void startThread1() {
        thr = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(
//                            DevicePath + "code=" + getDeviceID(getApplicationContext()) + "&"
//                                    + "ip=" + ipDetail + "&" + "country=" + country + "&" + "area=" + area + "&"
//                                    + "region=" + region + "&" + "city=" + city + "&" + "isp=" + isp);
                            DevicePath + "code=" + getSerialNumber() + "&"
                                    + "ip=" + ipDetail + "&" + "country=" + country + "&" + "area=" + area + "&"
                                    + "region=" + region + "&" + "city=" + city + "&" + "isp=" + isp);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("charset", "UTF-8");
                    connection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder s = new StringBuilder();
                        String line;
                        // 读取服务器返回的数据
                        while ((line = reader.readLine()) != null) {
                            s.append(line);
                        }
                        Log.d("TAG", s.toString());
                        Message message = new Message();
                        message.what = POSTVALUE;
                        message.obj = s.toString();
                        myHandler.sendMessage(message);
                        reader.close();
                    }
                    out.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thr.start();
    }

    // 设备序列号
    public String getDeviceID(Context ctx) {
        String strResult = null;
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            strResult = telephonyManager.getDeviceId();
        }
        if (strResult == null) {
            strResult = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Log.i("123",strResult);
        return strResult;
    }

    public static String getSerialNumber(){

        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

/*    //传地址信息
    public void postRegion(){

        String url = Url.postAddress;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("device",getSerialNumber())
                .addParams("localIp",IP)
                .addParams("region",region)
                .addParams("type","singleEarn")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("123333333", "222222222222222222222222"+ e );
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e("123333333", "111111111111111111111111111111111111" );

                    }
                });

    }*/

    // 服务提示对话框
    public void showUpdateDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("警示:您的设备连接异常!");
        builder.setMessage("可能因素：\n1、网络未连接或网络不工作\n2、软件与设备不匹配");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    finish();
                    System.exit(0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.create().show();
    }


}
