package vicmob.micropowder.config;

import android.app.Application;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.bean.AccountUpdateBean;
import vicmob.micropowder.daoman.bean.CityNumberBean;
import vicmob.micropowder.daoman.bean.LoadLocationBean;
import vicmob.micropowder.daoman.bean.LoadWhiteBean;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.ui.activity.MainActivity;
import vicmob.micropowder.utils.MyLogger;

/**
 * Created by Eren on 2017/6/22.
 * 全局变量 （每一个辅助服务模块的开启状态）
 */
public class MyApplication extends Application {
    /**
     * 显示提示
     */
    public static boolean isDeBug = true;
    /**
     * 屏幕dpi
     */
    //public float DPI = getResources().getDisplayMetrics().density;
    /**
     * 一键发消息
     */
    private boolean AKeySendMessage = false;
    /**
     * 允许一键发消息
     */
    private boolean AllowAKeySendMessage = false;
    /**
     * qq一键发消息
     */
    private boolean QQAKeySendMessage = false;
    /**
     * QQ允许一键发消息
     */
    private boolean QQAllowAKeySendMessage = false;
    /**
     * 一键加群友
     */
    private boolean GroupFriendChat = false;


    public boolean getQQAKeySendMessage() {
        return QQAKeySendMessage;
    }

    public void setQQAKeySendMessage(boolean QQAKeySendMessage) {
        this.QQAKeySendMessage = QQAKeySendMessage;
    }

    public boolean getQQAllowAKeySendMessage() {
        return QQAllowAKeySendMessage;
    }

    public void setQQAllowAKeySendMessage(boolean QQAllowAKeySendMessage) {
        this.QQAllowAKeySendMessage = QQAllowAKeySendMessage;
    }

    public boolean getAKeySendMessage() {
        return AKeySendMessage;
    }

    public void setAKeySendMessage(boolean AKeySendMessage) {
        this.AKeySendMessage = AKeySendMessage;
    }

    public boolean getAllowAKeySendMessage() {
        return AllowAKeySendMessage;
    }

    public void setAllowAKeySendMessage(boolean allowAKeySendMessage) {
        AllowAKeySendMessage = allowAKeySendMessage;
    }

    /**
     * 附近人模块
     */
    private boolean NearbyPeople = false;
    /**
     * 允许附近的人打招呼
     */
    private boolean AllowGreeting = false;
    /**
     * 允许添加群好友
     */
    private boolean AllowAddGroup = false;
    /**
     * 允许漂流瓶
     */
    private boolean AllowDriftBottle = false;
    /**
     * 允许一键加好友
     */
    private boolean AllowAddFriends = false;
    /**
     * 允许公众号
     */
    private boolean AllowPublicNum = false;
    /**
     * 允许朋友圈
     */
    private boolean AllowFriendCircle = false;
    /**
     * 允许白名单
     */
    private boolean AllowWhite = false;
    /**
     * 朋友圈模块
     */
    private boolean FriendCircle = false;
    /**
     * 一键加好友模块
     */
    private boolean AddFriends = false;
    /**
     * 公众号模块
     */
    private boolean PublicNumber = false;
    /**
     * 漂流瓶
     */
    private boolean DriftBottle = false;
    /**
     * 一键加群友
     */
    private boolean GroupFriend = false;
    /**
     * 自动回复
     */
    private boolean AutoReply = false;
    /**
     * 白名单模块
     */
    private boolean WhiteList = false;
    //一键开始
    private boolean AllowOneStart = false;
    /***qq附近人****/

    private boolean QQNearbyPeople = false;
    private boolean AllowQQNearbyPeople = false;
    private boolean AllowQQDianZan = false;
    private boolean AllowQQDialogCancel = false;

    /**
     * qq一键加群友
     */
    private boolean QQFriendStream = false;
    private boolean AllowQQFriendStream = false;
    //微信发朋友圈
    private boolean startFriendMoments = false;
    //qq白名单
    private boolean AllowQQWhite = false;
    private boolean start = false;
    /**
     * 设备号ID
     */
    private static String deviceID;
    /**
     * 地址管理
     */
    public static List<MapSearchBean> mNetworkAddressList = new ArrayList<>();
    public static List<MapSearchBean> mAllAddressList = new ArrayList<>();
    public static List<LoadWhiteBean.AutoDevicesBean> wList = new ArrayList<>();


    /****
     * 城市和运营商
     */
    public static List<CityNumberBean.AutoCityBean> mCityNumberList = new ArrayList<>();
    /****
     * 微信账户
     */
    public static List<AccountUpdateBean.AutoAccountBean> mAccountList = new ArrayList<>();
    /**
     * 网络获取实例
     */
    private LoadLocationBean mLoadLocationBean;
    private MapSearchDao mMapSearchDao;

    /**
     * QQ一键加好友
     */
    public boolean QQAddFriends = false;
    /*
    微信自动换号
     */
    private boolean AutoSwitchNumber = false;
    private boolean AllowAutoSwitchNumber = false;

    public boolean isAutoSwitchNumber() {
        return AutoSwitchNumber;
    }

    public void setAutoSwitchNumber(boolean autoSwitchNumber) {
        AutoSwitchNumber = autoSwitchNumber;
    }

    public boolean isAllowAutoSwitchNumber() {
        return AllowAutoSwitchNumber;
    }

    public void setAllowAutoSwitchNumber(boolean allowAutoSwitchNumber) {
        AllowAutoSwitchNumber = allowAutoSwitchNumber;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //获取设备号
        String tm = MainActivity.getSerialNumber();
        deviceID = tm;
        MyLogger.d("Device", "设备号为1：" + deviceID);

        //初始化全局变量
        setNearbyPeople(false);
        setFriendCircle(false);
        setAddFriends(false);
        setPublicNumber(false);
        setDriftBottle(false);
        setGroupFriend(false);
        setAutoReply(false);
        setWhiteList(false);
        setAllowGreeting(false);
        setAllowAddGroup(false);
        setAllowDriftBottle(false);
        setAllowAddGroup(false);
        setAllowQQNearbyPeople(false);
        setQQNearbyPeople(false);
        setQQFriendStream(false);
        setAllowQQFriendStream(false);
        setAllowQQDianZan(false);
        setAllowQQDialogCancel(false);
        setAKeySendMessage(false);
        setAllowAKeySendMessage(false);
    }


    public boolean getAllowAddFriends() {
        return AllowAddFriends;
    }

    public void setAllowAddFriends(boolean allowAddFriends) {
        AllowAddFriends = allowAddFriends;
    }

    /**
     * 是否开启朋友圈功能
     *
     * @return
     */
    public boolean getStartFriend() {
        return startFriendMoments;
    }

    public void setStartFriend(boolean startFriendMoments) {
        this.startFriendMoments = startFriendMoments;
    }

    public boolean getAllowPublicNum() {
        return AllowPublicNum;
    }

    public void setAllowPublicNum(boolean allowPublicNum) {
        AllowPublicNum = allowPublicNum;
    }

    public boolean getAllowFriendCircle() {
        return AllowFriendCircle;
    }

    public void setAllowFriendCircle(boolean allowFriendCircle) {
        AllowFriendCircle = allowFriendCircle;
    }

    public boolean getWxDeleteDeadFriends() {
        return WxDeleteDeadFriends;
    }

    public void setWxDeleteDeadFriends(boolean wxDeleteDeadFriends) {
        WxDeleteDeadFriends = wxDeleteDeadFriends;
    }

    /**
     * 清除死粉
     */
    public boolean WxDeleteDeadFriends = false;

    public boolean getAllowWxDeleteDeadFriends() {
        return AllowWxDeleteDeadFriends;
    }

    public void setAllowWxDeleteDeadFriends(boolean allowWxDeleteDeadFriends) {
        AllowWxDeleteDeadFriends = allowWxDeleteDeadFriends;
    }

    /**
     * 允许清除死粉
     */
    public boolean AllowWxDeleteDeadFriends = false;

    /**
     * 获取附近人模块开启状态
     *
     * @return
     */
    public boolean getNearbyPeople() {
        return NearbyPeople;
    }

    /**
     * 设置附近人模块开启状态
     *
     * @param nearbyPeople
     */
    public void setNearbyPeople(boolean nearbyPeople) {
        NearbyPeople = nearbyPeople;
    }

    /**
     * 获取朋友圈模块开启状态
     *
     * @return
     */
    public boolean getFriendCircle() {
        return FriendCircle;
    }

    /**
     * 设置朋友圈模块开启状态
     *
     * @param friendCircle
     */
    public void setFriendCircle(boolean friendCircle) {
        FriendCircle = friendCircle;
    }

    public boolean getAddFriends() {
        return AddFriends;
    }

    public void setAddFriends(boolean addFriends) {
        AddFriends = addFriends;
    }

    public boolean getPublicNumber() {
        return PublicNumber;
    }

    public void setPublicNumber(boolean publicNumber) {
        PublicNumber = publicNumber;
    }

    public boolean getDriftBottle() {
        return DriftBottle;
    }

    public void setDriftBottle(boolean driftBottle) {
        DriftBottle = driftBottle;
    }

    public boolean getGroupFriend() {
        return GroupFriend;
    }

    public void setGroupFriend(boolean groupFriend) {
        GroupFriend = groupFriend;
    }

    public boolean getAutoReply() {
        return AutoReply;
    }

    public void setAutoReply(boolean autoReply) {
        AutoReply = autoReply;
    }

    public boolean getWhiteList() {
        return WhiteList;
    }

    public void setWhiteList(boolean whiteList) {
        WhiteList = whiteList;
    }

    public boolean getAllowGreeting() {
        return AllowGreeting;
    }

    public void setAllowGreeting(boolean allowGreeting) {
        this.AllowGreeting = allowGreeting;
    }

    public boolean getAllowAddGroup() {
        return AllowAddGroup;
    }

    public void setAllowAddGroup(boolean allowAddGroup) {
        this.AllowAddGroup = allowAddGroup;
    }

    public boolean getAllowDriftBottle() {
        return AllowDriftBottle;
    }

    public void setAllowDriftBottle(boolean allowDriftBottle) {
        this.AllowDriftBottle = allowDriftBottle;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public boolean getAllowWhite() {
        return AllowWhite;
    }

    public void setAllowWhite(boolean allowWhite) {
        AllowWhite = allowWhite;
    }

    public boolean getAllowQQNearbyPeople() {
        return AllowQQNearbyPeople;
    }

    public void setAllowQQNearbyPeople(boolean allowQQNearbyPeople) {
        AllowQQNearbyPeople = allowQQNearbyPeople;
    }

    public boolean getQQNearbyPeople() {
        return QQNearbyPeople;

    }

    public boolean getGroupFriendChat() {
        return GroupFriendChat;
    }

    public void setGroupFriendChat(boolean groupFriendChat) {
        GroupFriendChat = groupFriendChat;
    }

    public void setQQNearbyPeople(boolean QQNearbyPeople) {
        this.QQNearbyPeople = QQNearbyPeople;
    }

    public boolean isQQFriendStream() {
        return QQFriendStream;
    }

    public void setQQFriendStream(boolean QQFriendStream) {
        this.QQFriendStream = QQFriendStream;
    }

    public boolean isAllowQQFriendStream() {
        return AllowQQFriendStream;
    }

    public void setAllowQQFriendStream(boolean allowQQFriendStream) {
        AllowQQFriendStream = allowQQFriendStream;
    }

    public boolean getStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean getAllowOneStart() {
        return AllowOneStart;
    }

    public void setAllowOneStart(boolean allowOneStart) {
        AllowOneStart = allowOneStart;
    }

    public boolean getAllowQQWhite() {
        return AllowQQWhite;
    }

    public void setAllowQQWhite(boolean allowQQWhite) {
        AllowQQWhite = allowQQWhite;
    }

    public boolean getQQAddFriends() {
        return QQAddFriends;
    }

    public void setQQAddFriends(boolean QQAddFriends) {
        this.QQAddFriends = QQAddFriends;
    }

    public boolean getAllowQQDianZan() {
        return AllowQQDianZan;
    }

    public void setAllowQQDianZan(boolean allowQQDianZan) {
        AllowQQDianZan = allowQQDianZan;
    }

    public boolean getAllowQQDialogCancel() {
        return AllowQQDialogCancel;
    }

    public void setAllowQQDialogCancel(boolean allowQQDialogCancel) {
        AllowQQDialogCancel = allowQQDialogCancel;
    }
/*    public float getDPI() {
        return DPI;
    }

    public void setDPI(float DPI) {
        this.DPI = DPI;
    }*/

    /******
     * 获取自定义数据
     */
   /* public void getDefaultData(String mDevice) {
        String urlString = Url.selectDefaultDataUrl;
        OkHttpUtils.post()
                .url(urlString)
                .addParams("devices", mDevice)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(getApplicationContext(), "网络获取失败", isDeBug);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("456", response);
                        if (response != null && !response.equals("[]") && !TextUtils.isEmpty(response)) {

                            try {
                                DefaultDataBean defaultDataStrList = MyJsonUtil.getBeanByJson(response, DefaultDataBean.class);
                                if (defaultDataStrList != null && !defaultDataStrList.getAuto_default_data().equals("[]") && defaultDataStrList.getAuto_default_data().size() > 0) {
                                    MyToast.show(getApplicationContext(), "数据获取成功", isDeBug);
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[0], defaultDataStrList.getAuto_default_data().get(0).getWxNearbyNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxNearbyNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[1], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxNearbyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxNearbyContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[2], defaultDataStrList.getAuto_default_data().get(0).getWxFriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxFriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[3], defaultDataStrList.getAuto_default_data().get(0).getWxGroupfriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxGroupfriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[4], defaultDataStrList.getAuto_default_data().get(0).getWxPublicIndex() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicIndex() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[5], defaultDataStrList.getAuto_default_data().get(0).getWxPublicNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[6], defaultDataStrList.getAuto_default_data().get(0).getWxPublicfriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxPublicfriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[7], defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[8], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxDriftbottleContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[9], defaultDataStrList.getAuto_default_data().get(0).getWxCircleNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxCircleNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[10], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxCircleContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxCircleContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), Constant.wxFunction[11], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getWxAkeyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getWxAkeyContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), Constant.qqFunction[0], defaultDataStrList.getAuto_default_data().get(0).getQqNearbyNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqNearbyNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.qqFunction[1], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getQqNearbyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqNearbyContent().toString() : "", "UTF-8"));
                                    PrefUtils.putString(getApplicationContext(), Constant.qqFunction[2], defaultDataStrList.getAuto_default_data().get(0).getQqFriendNum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqFriendNum() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.qqFunction[3], defaultDataStrList.getAuto_default_data().get(0).getQqGroupfriendGum() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqGroupfriendGum().toString() : "");
                                    PrefUtils.putString(getApplicationContext(), Constant.qqFunction[11], URLDecoder.decode(defaultDataStrList.getAuto_default_data().get(0).getQqAkeyContent() != null ? defaultDataStrList.getAuto_default_data().get(0).getQqAkeyContent().toString() : "", "UTF-8"));
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                MyToast.show(getApplicationContext(), "网络获取失败", isDeBug);
                            }

                        } else {
                            MyToast.show(getApplicationContext(), "数据库无数据", isDeBug);
                        }
                    }
                });
    }*/

    /*****
     * 获取主设备
     */
   /* public void getMainDeviceData() {
        String urlString = Url.getMainByPartdevices;
        OkHttpUtils.post()
                .url(urlString)
                .addParams("partdevices", getSerialNumber())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(getApplicationContext(), "网络获取失败", isDeBug);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("456", response);
                        try {
                            MainDeviceBean mainDeviceBean = MyJsonUtil.getBeanByJson(response, MainDeviceBean.class);
                            if (mainDeviceBean != null && mainDeviceBean.getAuto_control().size() > 0 && !mainDeviceBean.getAuto_control().equals("[]")) {
                                getDefaultData(mainDeviceBean.getAuto_control().get(0).getMaindevices());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyToast.show(getApplicationContext(), "网络获取失败", isDeBug);
                        }
                    }
                });
    }
*/
    public static String getSerialNumber() {

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
}
