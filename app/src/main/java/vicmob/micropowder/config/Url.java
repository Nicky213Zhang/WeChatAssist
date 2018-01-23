package vicmob.micropowder.config;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 网络地址
 */
public class Url {

    /**
     * 主机
     */
    public final static String HOST = "http://101.132.129.64/";

    //http://www.vicmob.com/dapp/v_1.0.20/
    public final static String UpdateHOST = "http://www.vicmob.com/dapp/v_3.0.0/";
    public final static String DeviceUrl = "http://www.vicmob.com/dapp/deviceapi.php?";
    public final static String DeviceMatch = "1";
//    http://www.vicmob.com/dapp/v_1.0.10
//    "http://www.vicmob.com/dapp/api.php?";

    /**
     * apk更新地址
     */
    public final static String APK_URL = UpdateHOST + "MobileWeChatAssist.apk";

    /**
     * 更新配置文件地址
     */
    public final static String VERSION_URL = UpdateHOST + "MobileVersion.xml";

    /**
     * 获取添加的总的地点
     */
    public final static String SELECT_ADD = HOST + "VicmobLazyEarn/lazyearn-api/autoData/selectDevicesAddress";

    /**
     * 删除添加的总的地点
     */
    public final static String deleteAdd = HOST + "VicmobLazyEarn/lazyearn-api/autoData/deleteAddress";

    /**
     * 城市运营商号段
     */
    public final static String CITY_NUMBER = HOST + "weifen/selectCity.php";
    /**
     * 城市运营商号段
     */
    public final static String CITY_NUMBER_DEViCES = HOST + "VicmobLazyEarn/lazyearn-api/autoCity/earnSelectCityOperators";

    //更新账户

    public final static String updateAccount = HOST + "weifen/updateAccount.php";

    //更新地址
    public final static String updateAddress = HOST + "weifen/updateAddress.php";

    //上传添加的总的地点
    public final static String insertAdd = HOST + "weifen/insertAdd.php";

    //获取账号
    public final static String selectAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/findAccountByDevices";

    //插入账号地址
    public final static String insertAccountUrl = HOST + "weifen/insertAccount.php";

    //删除账号
    public final static String deleteAccountUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoAccount/deleteAccount";
    //获取所有默认值
    public final static String selectDefaultDataUrl = HOST + "VicmobLazyEarn/lazyearn-api/autoDefaultData/selectDefaultData";
    //获取所有白名单
    public final static String getAllWhite = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/findDevices";
    //删除白名单
    public static String deleteBaiMingDan = HOST + "VicmobLazyEarn/lazyearn-api/autoDevices/deleteDevices";
    //获取主设备号
    public final static String getMainByPartdevices = HOST + "VicmobLazyEarn/lazyearn-api/autoControl/findMainByPartdevices";
    /**
     * 上传地址信息
     */
    //public final static String postAddress = HOST + "VicmobLazyEarn/lazyearn-api/autoRegion/addRegion";

}
