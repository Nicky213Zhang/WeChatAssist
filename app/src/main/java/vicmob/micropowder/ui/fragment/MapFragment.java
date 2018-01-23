package vicmob.micropowder.ui.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseFragment;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.GpsBean;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.receiver.SDKBroadCast;
import vicmob.micropowder.ui.adapter.MyAdapter;
import vicmob.micropowder.ui.views.EditTextWithDel;
import vicmob.micropowder.utils.GPSUtil;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 地图
 */
public class MapFragment extends BaseFragment implements View.OnClickListener, OnGetSuggestionResultListener, OnGetPoiSearchResultListener, BDLocationListener, OnGetGeoCoderResultListener, AdapterView.OnItemClickListener, BaiduMap.OnMarkerDragListener, BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMapClickListener {

    private static final String TAG = "MapFragment";
    private MyApplication app;
    private ImageView mMapMark;
    private ImageButton mMapRealLocation;
    private EditTextWithDel mMapKeyOrResult;
    private Button mMapSearchButton;
    private Button mMapAddressButton;
    private ListView mRvMapSearch;
    private TextureMapView mapView;
    /**
     * 显示当前模拟的位置的详细信息
     */
    private RelativeLayout detailMsg;
    private TextView detailMessage2;
    /**
     * 开始模拟按钮
     */
    private ImageButton startMock;
    /**
     * 停止模拟按钮
     */
    private ImageButton stopMock;

    private BaiduMap baiduMap;
    /**
     * 模糊搜索
     */
    private SuggestionSearch mSuggestionSearch;
    /**
     * 用于停止模拟时获取真实位置
     */
    private LatLng realLatlng;
    /**
     * 是否首次定位
     */
    private boolean isFirstLoc = true;
    /**
     * 当前经纬度
     */
    private LatLng curLatlng;
    /**
     * 转码搜索
     */
    private GeoCoder mSearch;
    /**
     * ListView列表状态是否是打开的
     */
    private boolean isListView = false;
    /**
     * 搜索地址集合
     */
    private List<String> district = new ArrayList<>();
    /**
     * 搜索地址关键词
     */
    private List<String> key = new ArrayList<>();
    /**
     * 经度集合
     */
    private List<Double> longitude = new ArrayList<>();
    /**
     * 纬度集合
     */
    private List<Double> latitude = new ArrayList<>();
    /**
     * poi检索
     */
    private PoiSearch poiSearch;
    /**
     * 列表适配器
     */
    private MyAdapter mMapRvAdapter;

    /**
     * 定位相关
     */
    private LocationClient mLocClient;  // 声明LocationClient类
    private MyLocationConfiguration.LocationMode mCurrentMode;  // 定位模式
    private BitmapDescriptor mCurrentMarker;    // 定位图标

    /**
     * 存放城市字符串
     */
    private String city = "";

    /**
     * 显示或者隐藏模拟按钮
     */
    private static final int showMockBtn_MSG = 1;
    private static final int hideMockBtn_MSG = 2;
    /**
     * 模拟定位
     */
    public static SQLiteDatabase mSQLiteDatabase;
    private String pacakgeName;
    private int lac = 0, cid = 0, mnc = 0;
    private double lat = 0.0, lon = 0.0;

    private MapSearchDao mMapSearchDao;

    /**
     * 更新UI
     */
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case showMockBtn_MSG:
                    //坐标动画
                    Animation shake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake_y);
                    shake.reset();
                    shake.setFillAfter(true);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mMapMark.startAnimation(shake);
                    startMock.setVisibility(View.GONE);
                    stopMock.setVisibility(View.GONE);
                    startMock.startAnimation(shake);
                    break;
                case hideMockBtn_MSG:
                    startMock.setVisibility(View.GONE);
                    stopMock.setVisibility(View.GONE);
                    mMapKeyOrResult.setText("正在加载...");
                    detailMsg.setVisibility(View.GONE);
                    mRvMapSearch.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
    private MapSearchBean mMapSearchBean;
    private GpsBean mGpsBean;
    private List<GpsBean.LacCidBean> mGpsBeanList = new ArrayList<>();


    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= 23) {
            //动态获取权限
            showContacts();
        }

        initSDK();

        setTitle("地图");

        View view = createContent();

        addView(view);

    }

    @Override
    public View createContent() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_map, null, false);

        mapView = ((TextureMapView) view.findViewById(R.id.map_view));
        mMapMark = (ImageView) view.findViewById(R.id.map_mark);
        mMapRealLocation = (ImageButton) view.findViewById(R.id.map_real_location);
        mMapKeyOrResult = (EditTextWithDel) view.findViewById(R.id.map_keyOrResult);
        mMapSearchButton = (Button) view.findViewById(R.id.map_search_button);
        mMapAddressButton = (Button) view.findViewById(R.id.map_address_button);
        mRvMapSearch = (ListView) view.findViewById(R.id.map_search_listView);
        startMock = (ImageButton) view.findViewById(R.id.startmockLocation);
        stopMock = (ImageButton) view.findViewById(R.id.stopmockLocation);
        detailMsg = (RelativeLayout) view.findViewById(R.id.detailMsg);
        detailMessage2 = (TextView) view.findViewById(R.id.detailMessage2);
        startMock.setVisibility(View.GONE);
        stopMock.setVisibility(View.GONE);

        initMap();

        initListener();

        //初始化模拟定位数据库
        //pacakgeName = "com.tencent.mobileqq";
        app = (MyApplication) getActivity().getApplication();
        pacakgeName = "com.tencent";
        mSQLiteDatabase = new PxDBHelper(mActivity).getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.query(PxDBHelper.APP_TABLE_NAME, new String[]{"latitude,longitude"}, "package_name=?", new String[]{pacakgeName}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
            LatLng latLng1 = new LatLng(lat, lon);
            setCurrentMapLatLng(latLng1);
            cursor.close();
        }

        return view;
    }

    /**
     * 地图初始化
     */
    private void initMap() {

        //初始化百度SDK

        baiduMap = mapView.getMap();
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocClient = new LocationClient(mActivity);
        //设置地图类型
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //实例化PoiSearch对象,初始化搜索模块，注册搜索事件监听
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);


        //初始化模糊搜索，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        //初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        // 设置正常地图显示 , MyLocationConfiguration 的包
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 缩放比例控制的大一些（地图感官上更细腻）
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        baiduMap.setMapStatus(msu);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

    private void initSDK() {
        SDKInitializer.initialize(mActivity.getApplicationContext());// 必须传递全局Context
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        mActivity.registerReceiver(new SDKBroadCast(mActivity), filter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mMapSearchButton.setOnClickListener(this);
        mMapAddressButton.setOnClickListener(this);
        mMapRealLocation.setOnClickListener(this);
        startMock.setOnClickListener(null);
        stopMock.setOnClickListener(null);
        mLocClient.registerLocationListener(this);
        mRvMapSearch.setOnItemClickListener(this);
        baiduMap.setOnMarkerDragListener(this);
        baiduMap.setOnMapStatusChangeListener(this);
        //地图点击监听事件
        baiduMap.setOnMapClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_search_button:        //搜索地点
                // 点击时将本类内存中对象清空
                key.clear();
                district.clear();
                latitude.clear();
                longitude.clear();

                //城市搜索，需要城市名称和关键字
                String searchMapResult = mMapKeyOrResult.getText().toString().trim();
                // 先进行poi搜索
                poiSearch.searchInCity((new PoiCitySearchOption()).city(city)
                        .keyword(searchMapResult).pageNum(3).pageCapacity(20));
                // 搜索之后，若此参数还是空的，表示搜索没有成功
                if (key.size() == 0) {
                    // 进行模糊搜索
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(searchMapResult).city(city));
                }

                break;

            case R.id.map_address_button:       //添加地点
                String addMapResult = mMapKeyOrResult.getText().toString().trim();

                if (addMapResult.equals("正在加载...")) {

                    MyToast.show(mActivity, "正在加载中请稍后添加");
                } else {
                    if (addMapResult != null && !TextUtils.isEmpty(addMapResult)) {
                        //将地点名、经纬度添加到数据库实例
                        loadData(0, addMapResult);
                    }
                }

                break;

            case R.id.map_real_location:        //回到当前地址
                isFirstLoc = true;// 请求地图定位
                Snackbar.make(v, "回到当前位置", Snackbar.LENGTH_SHORT).setAction("sss", null).show();
                break;

            case R.id.startmockLocation:        //开始模拟
                Intent xPosedIntent = mActivity.getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
                if (xPosedIntent != null) {
                    String cc = mMapKeyOrResult.getText().toString().trim();
                    if (!cc.equals("正在加载...")) {
                        try {
                            Thread.sleep(800);
                            startMock.setVisibility(View.GONE);
                            stopMock.setVisibility(View.GONE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (cc != null && !TextUtils.isEmpty(cc)) {
                            //添加到地点管理数据库中
                            // loadData(1, cc);

                        }

                        detailMsg.setVisibility(View.VISIBLE);
                        detailMessage2.setText(mMapKeyOrResult.getText().toString().trim());


                    } else {
                        MyToast.show(mActivity, "请等待位置识别");
                    }
                } else {
                    MyToast.show(mActivity, "您还没有安装定位模拟插件");
                }
                break;

            case R.id.stopmockLocation:     //停止模拟
                try {
                    Thread.sleep(500);
                    startMock.setVisibility(View.GONE);
                    stopMock.setVisibility(View.GONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                detailMsg.setVisibility(View.GONE);
                //获取真实位置的经纬度的集合
                //loadData(2, "");

                MyToast.show(mActivity, "已停止模拟");
                break;
        }
    }

    /**
     * 调用模拟数据库
     */
    private void hookMock(Double latitude, Double longitude, int lac, int cid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("package_name", pacakgeName);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("lac", lac);
        contentValues.put("cid", cid);
        //将集合里的参数插入到数据库
        mSQLiteDatabase.insertWithOnConflict(PxDBHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }


    /**
     * 模糊查询
     *
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        try {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null || suggestionResult.equals("")) {
                return;

            } else {

                List<SuggestionResult.SuggestionInfo> list = suggestionResult.getAllSuggestions();
                mRvMapSearch.setVisibility(View.VISIBLE);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        latitude.add(list.get(i).pt.latitude);
                        longitude.add(list.get(i).pt.longitude);
                        district.add(list.get(i).district);
                        key.add(list.get(i).key);
                    }
                    mMapRvAdapter = new MyAdapter(district, key, mActivity);
                    mRvMapSearch.setAdapter(mMapRvAdapter);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
//            if (!isListV) {
//                ToastUtils.showShort(mActivity.getApplicationContext(), "没有更多结果，可以换个关键词");
//            }
            e.printStackTrace();
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        //获得POI的检索结果，一般检索数据都是在这里获取
        //如果搜索到的结果为空，并且有错误
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            List<PoiInfo> list = poiResult.getAllPoi();
            if (list != null && list.size() > 0) {
                mRvMapSearch.setVisibility(View.VISIBLE);
                isListView = true;
                for (int i = 0; i < list.size(); i++) {
                    // 添加数据
                    latitude.add(list.get(i).location.latitude);
                    longitude.add(list.get(i).location.longitude);
                    key.add(list.get(i).name.toString().trim());
                    district.add(list.get(i).address.toString().trim());
                }
                // RecyclerView数据绑定
                mMapRvAdapter = new MyAdapter(district, key, mActivity);
                mRvMapSearch.setAdapter(mMapRvAdapter);
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        MobclickAgent.onPageStart("HomeUpdateFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
        MobclickAgent.onPageEnd("HomeUpdateFragment");
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView = null;
        mSQLiteDatabase.close();
        super.onDestroy();
    }

    /**
     * 定位SDK监听函数
     *
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // map view 销毁后不在处理新接收的位置
        if (bdLocation == null || mapView == null) {
            return;
        }
        //定位真实位置
        realLatlng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        // 是否第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            setCurrentMapLatLng(ll);
        }
    }

    /**
     * setCurrentMapLatLng 设置当前坐标
     */
    private void setCurrentMapLatLng(LatLng arg0) {
        curLatlng = arg0;

        // 设置地图中心点为这是位置
        LatLng ll = new LatLng(arg0.latitude, arg0.longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(u);

        // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
    }

    /**
     * onGetGeoCodeResult 搜索（根据实地信息-->经纬坐标）
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
        }
        // 获取地理编码结果
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.coordinate)).anchor(0.3f,0.6f));
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f", geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);

        mMapKeyOrResult.setText(String.format("%s", geoCodeResult.getAddress()));
        MyToast.show(mActivity, strInfo);
    }

    /**
     * onGetReverseGeoCodeResult 搜索（根据坐标-->实地信息）
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }

        mMapKeyOrResult.setText(String.format(reverseGeoCodeResult.getAddress()));
    }


    /**
     * 百度地图动态权限申请
     */
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mRvMapSearch.setVisibility(View.GONE);
        isListView = false;
        mMapKeyOrResult.setText(key.get(position));

        // 定义Maker坐标点
        LatLng latLng = new LatLng(latitude.get(position), longitude.get(position));
        setCurrentMapLatLng(latLng);
        // 点击列表效果
        mRvMapSearch.setItemChecked(position, true);
    }

    /**
     * 地图上标记拖动结束
     *
     * @param marker
     */
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * 地图上标记拖动结束
     *
     * @param marker
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        setCurrentMapLatLng(marker.getPosition());
        myHandler.sendEmptyMessage(showMockBtn_MSG);
    }

    /**
     * 地图上标记拖动开始
     *
     * @param marker
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        myHandler.sendEmptyMessage(hideMockBtn_MSG);
    }


    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        myHandler.sendEmptyMessage(hideMockBtn_MSG);
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
        curLatlng = mapStatus.target;
        myHandler.sendEmptyMessage(showMockBtn_MSG);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        setCurrentMapLatLng(latLng);
        mRvMapSearch.setVisibility(View.GONE);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    public void loadData(final int index, final String result) {
//        double[] gps = GPSUtil.bd09_To_Gcj02(curLatlng.latitude, curLatlng.longitude);
        //String url = "http://api.cellocation.com/recell/?incoord=bd09ll&coord=bd09ll&lat=" + curLatlng.latitude + "&lon=" + curLatlng.longitude;
//        String url = "http://api.cellocation.com/recell/?incoord=gcj02&coord=gcj02&lat=" + gps[0] + "&lon=" + gps[1];
//        Log.i("123",url);
//        OkHttpUtils
//                .get()
//                .url(url)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        MyToast.show(getActivity(), "网络获取失败");
//                    }
//
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onResponse(String response, int id) {
//                        MyLogger.d("Data", "数据为：" + response);
//                        if (response != null && !TextUtils.isEmpty(response) && !response.equals("[]")) {
//                            try {
//                                if (mGpsBeanList.size() != 0 && mGpsBeanList != null) {
//                                    mGpsBeanList.clear();
//                                }
//                                JSONArray jsonArray = new JSONArray(response);
//                                mGpsBeanList = GpsBean.getJSONlist(jsonArray);
                                getLatCidInfo(result);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            MyToast.show(mActivity, "获取基站信息失败!");
//                        }
//
//                    }
//                });
    }

    public void getLatCidInfo(String result) {
        /******
         * mnc 中国移动系统使用00、02、04、07，
         * 中国联通GSM系统使用01、06、09，
         * 中国电信CDMA系统使用03、05、电信4G使用11，
         * 中国铁通系统使用20
         */
//        for (int i = 0; i < mGpsBeanList.size(); i++) {
//            if (mGpsBeanList.get(i).lac > mGpsBeanList.get(i).ci || mGpsBeanList.get(i).mnc == 11 || mGpsBeanList.get(i).mnc == 20) {
//            } else {
//                lac = mGpsBeanList.get(i).lac;
//                cid = mGpsBeanList.get(i).ci;
//                mnc = mGpsBeanList.get(i).mnc;
//                lat = mGpsBeanList.get(i).location.get(0).lat;
//                lon = mGpsBeanList.get(i).location.get(0).lon;
//                break;
//            }
//        }
//        if (lac == 0 || cid == 0) {
//            MyToast.show(mActivity, "添加地点失败");
//        } else {
            //double[] gps = GPSUtil.gcj02_To_Bd09(lat, lon);
            MapSearchBean mapSearchBean = new MapSearchBean();
            mapSearchBean.setSearchAddresses(result);
            mapSearchBean.setLongtitudes(curLatlng.longitude);
            mapSearchBean.setLatitudes(curLatlng.latitude);
            //mapSearchBean.setLongtitudes(gps[1]);
            //mapSearchBean.setLatitudes(gps[0]);
            mapSearchBean.setCid(cid);
            mapSearchBean.setLac(lac);
            mapSearchBean.setMnc(mnc);
            //将数据库实例添加到数据库
            MapSearchDao mapSearchDao = new MapSearchDao(mActivity);
            mapSearchDao.add(mapSearchBean);
            MyToast.show(mActivity, "添加地点成功");
//        }
    }

}
