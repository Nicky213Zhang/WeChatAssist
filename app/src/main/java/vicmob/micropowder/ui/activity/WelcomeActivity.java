package vicmob.micropowder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.config.LoadData;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.AccountBean;
import vicmob.micropowder.daoman.bean.AccountUpdateBean;
import vicmob.micropowder.daoman.bean.CityNumberBean;
import vicmob.micropowder.daoman.bean.LoadLocationBean;
import vicmob.micropowder.daoman.bean.LoadWhiteBean;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.utils.MyJsonUtil;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;

/**
 * Created by sunjing on 2017/7/11.
 * <p/>
 * 欢迎界面
 */

public class WelcomeActivity extends Activity{
    private Handler mHandle = new Handler();
    SharedPreferences preferences;
    int count = 0;
    private MyApplication mApp;
    private LoadLocationBean mLoadLocationBean;
    private AccountUpdateBean accountUpdateBean;
    private List<MapSearchBean> mMapSearchBeanList = new ArrayList<>();
    private CityNumberBean mCityNumberBean = new CityNumberBean();
    private LoadWhiteBean mLoadWhiteBean;
    public List<LoadWhiteBean.AutoDevicesBean> mWhiteListContentList = new ArrayList<>();
    private AccountBean accountBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome); // 读取SharedPreferences中需要的数据
        mApp = (MyApplication) getApplication();
        preferences = getSharedPreferences("count", MODE_PRIVATE);
        count = preferences.getInt("count", 0);
        //时间
        mHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count == 0) {
                    startAndFinishThis1();
                    SharedPreferences.Editor editor = preferences.edit();
                    // 存入数据
                    editor.putInt("count", ++count);
                    // 提交修改
                    editor.commit();
                } else {
                    startAndFinishThis();
                }
            }
        }, 2500);
    }

    //启动主activity
    private void startAndFinishThis() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
//        overridePendingTransition(R.anim.zoom_enter,
//                R.anim.zoom_exit);

    }

    //启动引导页
    private void startAndFinishThis1() {
        Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }


     ////////////////////////////////////////////////网络接口无用////////////////////////////////////////

    /*****
     * 获取设备地址
     */
/*    @Override
    public void loadAddressData() {
        String url = Url.SELECT_ADD;
        MyLogger.d("Device", "设备号为：" + new MyApplication().getDeviceID().trim());
        OkHttpUtils
                .post()
                .url(url)
                .addParams("devices", new MyApplication().getDeviceID().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(WelcomeActivity.this, "网络获取地址失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("123", response);
                        mLoadLocationBean = MyJsonUtil.getBeanByJson(response, LoadLocationBean.class);
                        initDB();

//                        if (mLoadLocationBean.getcityNumberBeen().size() > 0) {
//                            mApp.mCityNumberList=mLoadLocationBean.getcityNumberBeen();
//                        }
                    }
                });
    }
    private void loadAccountData() {
        String url = Url.selectAccountUrl;
        MyLogger.d("Device", "设备号为：" + new MyApplication().getDeviceID().trim());
        OkHttpUtils
                .post()
                .url(url)
                .addParams("devices", new MyApplication().getDeviceID().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(WelcomeActivity.this, "网络获取地址失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("123", response);
                        accountUpdateBean = MyJsonUtil.getBeanByJson(response, AccountUpdateBean.class);
                        if (mApp.mAccountList.size() > 0) {
                            mApp.mAccountList.clear();
                        }
                        mApp.mAccountList = accountUpdateBean.getAuto_account();

                    }
                });
    }*/
    /**
     * 保存数据
     */
    /*private void initDB() {
        //网络获取到的数据添加到本地数据库中
        if (mLoadLocationBean != null && mLoadLocationBean.getAuto_data() != null && mLoadLocationBean.getAuto_data().size() > 0) {
            for (int i = 0; i < mLoadLocationBean.getAuto_data().size(); i++) {
                MapSearchBean mapSearchBean = new MapSearchBean();
                mapSearchBean.setSearchAddresses(mLoadLocationBean.getAuto_data().get(i).getAddress());
                mapSearchBean.setLatitudes(Double.parseDouble(mLoadLocationBean.getAuto_data().get(i).getLatitude()));
                mapSearchBean.setLongtitudes(Double.parseDouble(mLoadLocationBean.getAuto_data().get(i).getLongitude()));
                mapSearchBean.setMnc(mLoadLocationBean.getAuto_data().get(i).getMnc());
                mapSearchBean.setLac(mLoadLocationBean.getAuto_data().get(i).getLac());
                mapSearchBean.setCid(mLoadLocationBean.getAuto_data().get(i).getCid());
                mapSearchBean.setIsLocal(1);
                mapSearchBean.setDataId(mLoadLocationBean.getAuto_data().get(i).getDataId() + "");
                mMapSearchBeanList.add(mapSearchBean);
            }
        }
        if (mApp.mNetworkAddressList.size() > 0) {
            mApp.mNetworkAddressList.clear();
        }
        mApp.mNetworkAddressList = mMapSearchBeanList;
    }*/

    /****
     * 获取运营商和城市
     */
   /* @Override
    public void loadCityData() {
        String url = Url.CITY_NUMBER_DEViCES;
        OkHttpUtils
                .post()
                .url(url)
                .addParams("devices", MainActivity.getSerialNumber())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(WelcomeActivity.this, "网络获取城市失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null && !TextUtils.isEmpty(response) && !response.equals("false")) {
                            MyLogger.d("MMMM", response);
                           *//* Gson gson = new Gson();
                            Log.i("zmm", response);
                            List<CityNumberBean> cityNumberBeen = CityNumberBean.arrayCityNumber1FromData(response);
                            mApp.mCityNumberList.clear();
                            if (cityNumberBeen.size() > 0) {
                                mApp.mCityNumberList = cityNumberBeen;
                                Log.i("zmm", gson.toJson(cityNumberBeen));
                            }*//*
//                            Type type = new TypeToken<List<CityNumberBean>>(){}.getType();
                            CityNumberBean cityNumberBeen = MyJsonUtil.getBeanByJson(response, CityNumberBean.class);
                            mApp.mCityNumberList.clear();

                            if (cityNumberBeen.getcityNumberBeen().size() > 0) {
                                mApp.mCityNumberList = cityNumberBeen.getcityNumberBeen();
                            }
                        }
                    }
                });
    }*/

    /****
     * 获取白名单
     */
    /*public void loadWhiteData() {
        String url = Url.getAllWhite;
        MyLogger.d("123", "设备号为：" + new MyApplication().getDeviceID().trim());
        OkHttpUtils
                .get()
                .url(url)
                .addParams("devices", new MyApplication().getDeviceID().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(WelcomeActivity.this, "网络获取白名单失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("123", response);
                        mLoadWhiteBean = MyJsonUtil.getBeanByJson(response, LoadWhiteBean.class);
                        //网络获取到的数据添加到本地数据库中
                        if (mLoadWhiteBean != null && mLoadWhiteBean.getAuto_devices() != null && mLoadWhiteBean.getAuto_devices().size() > 0) {
                            mWhiteListContentList.clear();
                            mWhiteListContentList.addAll(mLoadWhiteBean.getAuto_devices());
                        }
                        if (mApp.wList.size() > 0) {
                            mApp.wList.clear();
                        }
                        mApp.wList = mWhiteListContentList;
                    }
                });
    }*/
}
