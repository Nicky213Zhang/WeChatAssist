package vicmob.micropowder.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.IDConstant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.bean.PublicNumBean;
import vicmob.micropowder.daoman.bean.WhiteListContentBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.daoman.dao.PublicNumDao;
import vicmob.micropowder.daoman.dao.WhiteListDao;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/24.
 * <p/>
 * 辅助服务
 */
public class EarnAccessibilityService extends BaseAccessibilityService {

    /**
     * 全局
     */
    private MyApplication app;
    /**
     * 当前界面状态
     */
    private int pageStatus = 0;
    private int pageStatus1 = 0;
    /**
     * 主界面
     */
    public static final int LAUNCHER_UI = 1;
    /**
     * 附近人界面
     */
    public static final int NEARBY_FRIENDS_UI = 2;
    /**
     * 打招呼页面
     */
    public static final int SAY_HI_EDIT_UI = 3;
    /**
     * 详细资料页面
     */
    public static final int CONTACT_INFO_UI = 4;
    /**
     * 当前附近人访问的位置
     */
    private int mNearbyNumber = 0;
    /**
     * 当前附近的访问总人数
     */
    private int mNearbySumNumber = 0;
    /**
     * 新的朋友界面
     */
    public static final int NEW_FRIENDS_UI = 5;
    /**
     * 一键加好友详细资料界面
     */
    public static final int NEW_FRIEND_DATA = 6;
    /**
     * 当前新的朋友访问的位置
     */
    private int mAddFriendsNumber = 0;
    /**
     * 验证申请页面
     */
    public static final int VERITICATION_REQUEST_UI = 6;
    //公众号
    /**
     * 当前公众号访问的位置
     */
    private int mPublicNumNumber = 0;
    /**
     * 获取设置公众号个数
     */
    private int mGetPublicText = 0;
    /**
     * 点击公众号推送总人数
     */
    private int mPublicNumTotalPeople = 0;
    /**
     * 当前选择联系人的位置
     */
    private int mChoosePeopleNum = 0;
    /**
     * 获取推送联系人数
     */
    private int mGetPeopleNumText = 0;
    /**
     * 点击推送公众号总个数
     */
    private int mPublicNumTotalNumber = 0;
    /**
     * 获取设置公众号开始数
     */
    private int mGetPublicNumStartText;
    /**
     * 群聊
     */
    public static final int GF_CHAT = 2;
    /**
     * 详细信息
     */
    public static final int GF_INFO = 3;
    /**
     * 提示
     */
    public static final int GF_PROMPT = 8;

    /**
     * 验证申请
     */
    public static final int GF_TEST = 5;
    /**
     * 选择联系人
     */
    public static final int GF_LAST = 6;
    /**
     * 选择联系人
     */
    public static final int GF_MCHAT = 7;
    /**
     * 群list计数
     */
    private int out = 0;
    /**
     * 外循环停止标记
     */
    private int stopi = 0;
    /**
     * 群成员计数
     */
    private int inside = 0;
    private int inside1 = 0;
    private int inside2 = 31;
    private boolean isMore = true;

    private int mClickIndex = 0;
    /**
     * 当前白名单位置
     */
    private int mWhiteNumber = 0;
    public static final int LAUNCHER_END = 5;
    /**
     * 搜索
     */
    public static final int SEARCH = 7;
    /**
     *
     */
    private List<WhiteListContentBean> mWhiteListContentList;

    private MapSearchDao mMapSearchDao;

    private List<MapSearchBean> mMapSearchBeanList;
    /**
     * 当前第几个地点
     */
    public static int index = 0;

    /**
     * 数据库
     */
    public static SQLiteDatabase mSQLiteDatabase;

    /**
     * 一键加好友添加的人数
     */
    private int AddPeople = 0;
    /**
     * 附近的人默认内容
     */
    private String NearByContent = "hello 你好";
    /**
     * 漂流瓶默认内容
     */
    private String DriftBottleContent = "hello 你好";
    /**
     * 朋友圈默认内容
     */
    private String FCContent = "腻害了";
    /**
     * 一键发消息默认内容
     */
    private String AKeyContent = "hello!!!";
    private int mGroupText;
    private int mClickFinish = 0;
    private final int AKEYCHATPEOPLEUI = 10;
    private int mContactlistCurrent = 0;
    private int page = 1;
    //文件传输/微信团队
    private boolean isnotsend;
    private boolean iskeymessage;
    private int pubIndex = 0;
    private int zuobiao = 0;
    String friendText;//朋友圈内容
    //一键清粉
    private int weishanchu = 0;
    private int lianxuweishanchu = 0;
    private boolean isfirstclearfriends;
    //默认值
    private String FCNum = "30";
    /**
     * 附近的人默认个数
     */
    private String mNrearByNum = "30";
    /**
     * 默认个数
     */
    private String mAddFriendNum = "30";
    private String mwx_public_index = "1";
    private String mwx_publicfriend_num = "30";
    private String mwx_public_num = "1";
    private String mwx_driftbottle_num = "2";
    private String mwx_groupfriend_num = "1";
    private boolean isAlreadyReply = false;
    private int nearbyGreetNum = 0;
    private final int FRIENDS_VERIFY = 13;
    private final int NEARBY_DETAIL_UI = 14;
    private int clickYESNum = 0;
    private boolean isFrequently = false;
    private boolean isfirstEnter = false;
    private boolean isfirstpush = true;
    //推送公众号数
    private int mClickPublicNumList = 0;
    private static int PUBLIC_NUM_SEARCH = 15;
    private int PUBLIC_NUM_DETAILS_UI = 16;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        app = (MyApplication) getApplication();
        int eventType = event.getEventType();
        if (event.getSource() != null) {
            try {
                /**
                 * 附近人功能 01
                 */
                if (app.getNearbyPeople()) {
                    /**
                     * 主界面
                     */
                    //获取需要打招呼的人数
                    int mNearTextNum = PrefUtils.getInt(EarnAccessibilityService.this, "mNearTextNum", 0);
                    if (app.getAllowOneStart()) {
                        String wxNearby_Num = PrefUtils.getString(this, Constant.wxFunction[0], "");
                        if (TextUtils.isEmpty(wxNearby_Num) || wxNearby_Num.equals("0")) {
                            wxNearby_Num = mNrearByNum;
                        }
                        try {
                            mNearTextNum = Integer.parseInt(wxNearby_Num);    //String转化成Int
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e(TAG, "mNearTextNum: " + mNearTextNum);
                    if (app.getAllowGreeting()) {
                        sleepTime(1000);
                        //复制需要输入的评论内容
                        String mNearText = PrefUtils.getString(getApplicationContext(), Constant.wxFunction[1], "");//网络获取的默认值
                        Log.e(TAG, "mNearText wan: " + mNearText);
                        String mNearText1 = PrefUtils.getString(getApplicationContext(), "mNearText", null);//本地
                        Log.e(TAG, "mNearText1 ben: " + mNearText1);
                        if (mNearText.equals("null") || TextUtils.isEmpty(mNearText) || mNearText == null) {
                            if (mNearText1 == "" || TextUtils.isEmpty(mNearText1)) {
                                mNearText = NearByContent;
                            } else {
                                mNearText = mNearText1;
                            }
                        }
                        copyToBoard(mNearText);

                        //如果是主界面，并找到发现按钮
                        if (isFindText("发现") && isFindId(IDConstant.MAIN_BUTTON)) {
                            sleepTime(1000);
                            //Log.i("890", "node");
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "发现", 2);
                            //findTextAndClick("发现", 0);  //点击发现
                        }
                        if (isFindText("附近的人")) {
                            sleepTime(1000);
                            findTextAndClick("附近的人", 0);  //点击附近的人
                            sleepTime(1000);
                            if (isFindId(IDConstant.WX_NO_NEXT)) {
                                // 下次不提示
                                findIdAndClick(IDConstant.WX_NO_NEXT, 0);
                                sleepTime(1000);
                                // 确定
                                findIdAndClick(IDConstant.WX_NO_NEXT_YES, 0);
                            }
                            mNearbyNumber = 0;
                            app.setAllowGreeting(false);
                        }

                    }
                    /**
                     * 附近de人界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (!isAlreadyReply) {
                            findContentAndClick(IDConstant.WX_HEAD, 0, "更多", 2);
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_GREET_NEARBY, 3);
                            isAlreadyReply = true;
                        } else {
                            pageStatus = NEARBY_FRIENDS_UI;     //当前在附近人界面
                            //当一页添加9人时，就进行翻页操作
                            if (mNearbyNumber >= 9) {
                                sleepTime(1500);
                                execSlipCmd("input swipe 400 1200 400 520");
                                sleepTime(5000);
                                mNearbyNumber = 0;

                            }
                            sleepTime(1000);
                            mNearbySumNumber++;
                            if (mNearbySumNumber > mNearTextNum) {
                                index++;
                                //切换地点
                                ChangeLocation();
                                //TODO 切换账号
                                //                            MyToast.show(EarnAccessibilityService.this, "服务已完结");
                                app.setAllowGreeting(true);
                                mNearbyNumber = 0;
                                mNearbySumNumber = 0;
                            } else {
                                findIdAndClick(IDConstant.NEARBY_LIST_VIEW, mNearbyNumber);
                                mNearbyNumber++;
                            }
                            //当浏览完毕退出，并将附近人模块开启状态变成false
                        }
                    }

                    //附近打招呼的人界面
                    else if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbySayHiListUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        clickYESNum = 0;
                        //是否是频繁过来的
                        clickYESNum = 0;
                        if (isFrequently) {
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_GREET_NEARBY_BACK, 0);
                            isFrequently = false;
                        } else {

                            //有查看更多按钮时
                            if (isFindId(IDConstant.WX_LOOK_MORE)) {
                                sleepTime(1000);
                                findIdAndClick(IDConstant.WX_LOOK_MORE, 0);
                            }
                            if (nearbyGreetNum >= 9) {
                                nearbyGreetNum = 0;
                                sleepTime(1500);
                                execSlipCmd("input swipe 400 1200 400 520");
                                sleepTime(5000);
                            }
                            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.WX_GREET_NEARBY_LIST);
                            Log.i("zzz1", list.size() + "");
                            if (list.size() == 0) {
                                sleepTime(1000);
                                findIdAndClick(IDConstant.WX_GREET_NEARBY_BACK, 0);
                            } else if (list.size() < 9 && list.size() > 0) {
                                if (nearbyGreetNum >= list.size()) {
                                    sleepTime(1000);
                                    findIdAndClick(IDConstant.WX_WX_GREET_NEARBY_CLEAR, 0);
                                    sleepTime(1000);
                                    findIdAndClick(IDConstant.WX_WX_GREET_NEARBY_CLEAR_YES, 0);
                                    sleepTime(1000);
                                    //                                    findIdAndClick(IDConstant.WX_GREET_NEARBY_BACK, 0);
                                }
                            } else {
                                Log.i("zzz1", isFindWXBouns(IDConstant.WX_GREET_NEARBY_LIST, nearbyGreetNum) + "");
                                if (isFindWXBouns(IDConstant.WX_GREET_NEARBY_LIST, nearbyGreetNum)) {
                                    sleepTime(1000);
                                    findIdAndClick(IDConstant.WX_WX_GREET_NEARBY_CLEAR, 0);
                                    sleepTime(1000);
                                    findIdAndClick(IDConstant.WX_WX_GREET_NEARBY_CLEAR_YES, 0);
                                    sleepTime(1000);
                                    //                                    findIdAndClick(IDConstant.WX_GREET_NEARBY_BACK, 0);
                                }
                            }
                            Log.i("zzz1", "mm");
                            findIdAndClick(IDConstant.WX_GREET_NEARBY_LIST, nearbyGreetNum);
                            nearbyGreetNum++;
                        }
                    }

                    //朋友验证界面
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = FRIENDS_VERIFY;
                        sleepTime(1000);
                        findIdAndClick(IDConstant.WX_FRIENDS_VERIFY, 0);
                        clickYESNum++;
                        Log.i("zzz2", clickYESNum + "");
                        if (clickYESNum >= 3) {
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_FRIENDS_SURE_BACK, 0);
                            clickYESNum = 0;
                            isFrequently = false;
                            sleepTime(1000);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsIntroUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {//第一次进入附近的人
                        //第一次打开微信附近的人会出现开始查看
                        findTextAndClick("开始查看", 0);
                        sleepTime(1000);
                        // 下次不提示
                        findIdAndClick(IDConstant.WX_NO_NEXT, 0);
                        sleepTime(1000);
                        // 确定
                        findIdAndClick(IDConstant.WX_NO_NEXT_YES, 0);

                    } else if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindText("查看附近的人")) {
                            WXfindIdTextAndClick(IDConstant.NEARBY_CHECK_BUTTON, "查看附近的人", 0);
                            //findTextAndClick("查看附近的人", 0);
                        }

                        /**
                         * 详细资料界面
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        //附近人打招呼详细资料界面
                        sleepTime(1000);
                        if (pageStatus == FRIENDS_VERIFY || pageStatus == SAY_HI_EDIT_UI || isFindText("发消息")) {
                            pageStatus = NEARBY_DETAIL_UI;
                            findIdAndClick(IDConstant.WX_GREET_NEARBY_BACK, 0);
                        } else {
                            pageStatus = NEARBY_DETAIL_UI;
                            if (isFindText("通过验证")) {
                                findTextAndClick("通过验证", 0);
                            } else if (isFindText("打招呼")) {
                                WXfindIdTextAndClick(IDConstant.WX_HELLO_BUTTON, "打招呼", 0);
                            } else {
                                execSlipCmd("input swipe 400 1100 400 500");
                                sleepTime(2000);
                                if (isFindText("通过验证")) {
                                    findTextAndClick("通过验证", 0);
                                } else if (isFindText("打招呼")) {
                                    WXfindIdTextAndClick(IDConstant.WX_HELLO_BUTTON, "打招呼", 0);
                                }
                            }
                        }
                    }
                    /**
                     * 打招呼界面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.ui.contact.SayHiEditUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = SAY_HI_EDIT_UI;    //当前在打招呼界面
                        sleepTime(1000);
                        paste(IDConstant.SAY_HELLO_EDIT_TEXT, 0);   // 粘贴进剪切板
                        sleepTime(1000);
                        //findIdAndClick(IDConstant.WX_HELLO_SEND_BUTTON, 0); // 点击发送
                        WXfindIdTextAndClick(IDConstant.WX_HELLO_SEND_BUTTON, "发送", 0);
                    }
                }


                /**
                 * 朋友圈服务 02
                 */
                Log.i("123", event.getClassName() + "aaa");
                if (app.getFriendCircle()) {
                    //获取需要评论的人数
                    int friendTextNum = PrefUtils.getInt(getApplication(), "mFriendTextNum", 0);
                    if (app.getAllowOneStart()) {
                        Log.e(TAG, "getAllowOneStart 0000000000000");
                        String wx_circle_num = PrefUtils.getString(EarnAccessibilityService.this, Constant.wxFunction[9], "0");
                        if (wx_circle_num.equals("0") || TextUtils.isEmpty(wx_circle_num)) {
                            wx_circle_num = FCNum;
                        }
                        try {
                            friendTextNum = Integer.parseInt(wx_circle_num);    //String转化成Int
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.e(TAG, "friendTextNum : " + friendTextNum);
                    if (app.getAllowFriendCircle()) {
                        sleepTime(1000);
                        //复制要输入的评论内容
                        friendText = PrefUtils.getString(getApplicationContext(), Constant.wxFunction[10], "");//网络获取
                        Log.e(TAG, "friendText wan : " + friendText);
                        String friendText1 = PrefUtils.getString(getApplicationContext(), "mFriendText", null);//本地
                        Log.e(TAG, "friendText ben : " + friendText1);
                        if (friendText.equals("0") || TextUtils.isEmpty(friendText) || friendText.equals("null")) {
                            if (friendText1 == null || friendText1 == "" || TextUtils.isEmpty(friendText1) || friendText1.equals("null")) {
                                friendText = "";
                            } else {
                                friendText = friendText1;
                            }
                        }
                        Log.i("123", friendText + "AAAAAAAAAAAAAAAAAA");
                        if (!TextUtils.isEmpty(friendText) && !friendText.equals("null")) {
                            copyToBoard(friendText);
                        }
                        /**
                         * 主界面
                         */
                        //如果是主界面，并找到发现按钮
                        if (isFindText("发现") && isFindId(IDConstant.MAIN_BUTTON)) {
                            sleepTime(1000);
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "发现", 2);
                            //findTextAndClick("发现", 0);  //点击发现
                        }
                        if (isFindText("朋友圈")) {
                            sleepTime(1000);
                            findTextAndClick("朋友圈", 0);  //点击朋友圈
                            app.setAllowFriendCircle(false);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        for (int i = 0; i < friendTextNum; i++) {
                            sleepTime(1000);
                            Log.i("123", isFindIdlistnum(IDConstant.THUMBS_UP) + ":1");
                            //判定当前有几个评论只有一评论点击第一个，超过两个点击第二个，防止评论被listhead挡住。
                            if (isFindIdlistnum(IDConstant.THUMBS_UP) > 0) {
                                if (isFindIdlistnum(IDConstant.THUMBS_UP) >= 2) {
                                    findIdAndClick(IDConstant.THUMBS_UP, 1);
                                    sleepTime(1500);
                                    //判断是否已经被点赞
                                    if (!isFindText("取消") && isFindId(IDConstant.WX_DIANZAN)) {
                                        //findIdAndClick(IDConstant.WX_DIANZAN, 0);
                                        //findTextAndClick("赞", 0);
                                        WXfindIdTextAndClick(IDConstant.WX_DIANZAN, "赞", 0);
                                        sleepTime(1500);
                                        //点赞操作
                                        findIdAndClick(IDConstant.THUMBS_UP, 1);
                                    }
                                    sleepTime(2000);
                                    if (!TextUtils.isEmpty(friendText) && !friendText.equals("null")) {
                                        WXfindIdTextAndClick(IDConstant.COMMENT, "评论", 0);
                                        sleepTime(1500);
                                        //评论操作
                                        paste(IDConstant.COMMENT_CONTENT, 0);
                                        sleepTime(1500);
                                        findIdAndClick(IDConstant.COMMENT_SEND, 0);
                                        //findTextAndClick("发送", 0);
                                        sleepTime(1500);
                                    }
                                } else {
                                    findIdAndClick(IDConstant.THUMBS_UP, 0);
                                    sleepTime(1500);
                                    //判断是否已经被点赞
                                    if (!isFindText("取消") && isFindId(IDConstant.WX_DIANZAN)) {
                                        //findIdAndClick(IDConstant.WX_DIANZAN, 0);
                                        //findTextAndClick("赞", 0);
                                        WXfindIdTextAndClick(IDConstant.WX_DIANZAN, "赞", 0);
                                        sleepTime(1500);
                                        //点赞操作
                                        findIdAndClick(IDConstant.THUMBS_UP, 0);
                                    }
                                    sleepTime(2000);
                                    if (!TextUtils.isEmpty(friendText) && !friendText.equals("null")) {
                                        WXfindIdTextAndClick(IDConstant.COMMENT, "评论", 0);
                                        sleepTime(1500);
                                        //评论操作
                                        paste(IDConstant.COMMENT_CONTENT, 0);
                                        sleepTime(1500);
                                        findIdAndClick(IDConstant.COMMENT_SEND, 0);
                                        //findTextAndClick("发送", 0);
                                        sleepTime(1500);
                                    }
                                }
                                Log.i("123", "滑动");
                                //翻页操作
                                execSlipCmd("input swipe 400 1100 400 700");
                                sleepTime(2000);

                            } else {
                                //翻页操作
                                execSlipCmd("input swipe 400 1100 400 700");
                                sleepTime(2000);
                            }
                        }

                        app.setFriendCircle(false);     //将朋友圈模块开启状态变成false
                        if (app.getAllowOneStart()) {
                            //                            app.setAddFriends(true);  //开启一键加好友模块
                            //                            new Thread(new Runnable() {
                            //                                @Override
                            //                                public void run() {
                            //先执行杀掉微信后台操作
                            performBackClick(); //退出
                            Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                            execShellCmd("am force-stop com.tencent.mm");
                            //                                    execShellCmd("am force-stop com.tencent.mm");
                            //                                    execShellCmd("input keyevent 3");
                            sleepTime(2000);
                            app.setAddFriends(true);  //开启一键加好友模块
                            app.setAllowAddFriends(true);
                            sleepTime(1000);
                            openThread(); //跳转微信主界面
                            //                        app.setAllowOneStart(true);
                            //                                    try {
                            //                                        Thread.sleep(2500);
                            //                                    } catch (InterruptedException e) {
                            //                                        e.printStackTrace();
                            //                                    }
                            //                                    intentWechat(); //跳转微信主界面

                            //                                }
                            //                            }).start();

                        } else {
                            performBackClick();     //退出
                            //TODO 切换账号
                            MyToast.show(EarnAccessibilityService.this, "服务已完结");
                        }

                    }
                }
                /**
                 * 一键加好友功能  03
                 */
                if (app.getAddFriends()) {
                    //获取设置的人数
                    int mAddfriendsText = PrefUtils.getInt(EarnAccessibilityService.this, "mAddFriendsText", 0);
                    if (app.getAllowOneStart()) {
                        Log.e(TAG, "getAllowOneStart 0000000000000");
                        String wx_friend_num = PrefUtils.getString(EarnAccessibilityService.this, Constant.wxFunction[2], "0");
                        if (wx_friend_num.equals("0") || TextUtils.isEmpty(wx_friend_num)) {
                            wx_friend_num = mAddFriendNum;
                        }
                        try {
                            mAddfriendsText = Integer.parseInt(wx_friend_num);    //String转化成Int
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.e(TAG, "mAddfriendsText : " + mAddfriendsText);

                    if (app.getAllowAddFriends()) {
                        /**
                         * 主界面
                         */
                        //如果是主界面，并找到通讯录按钮
                        if (isFindText("通讯录") && isFindId(IDConstant.MAIN_BUTTON)) {
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "通讯录", 1);
                            //findTextAndClick("通讯录", 0);
                            sleepTime(1000);
                        }
                        if (isFindId(IDConstant.WX_NEW_FRIENDS)) {
                            findIdAndClick(IDConstant.WX_NEW_FRIENDS, 0);
                            sleepTime(1000);
                            app.setAllowAddFriends(false);
                        }
                    }
                    /**
                     * 新的朋友界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindId(IDConstant.NEW_FRIENDS_LIST_ICON)) {
                            pageStatus = NEW_FRIENDS_UI;
                            //获取一页的公众号数
                            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.NEW_FRIENDS_LIST_ICON);
                            if (list.size() < 7 && list.size() < mAddfriendsText) {
                                findIdAndClick(IDConstant.NEW_FRIENDS_LIST_ICON, mAddFriendsNumber);
                                sleepTime(1000);
                                mAddFriendsNumber++;
                                AddPeople++;
                                mClickIndex = 0;
                                mClickFinish = 0;
                                if (list.size() == mAddFriendsNumber) {
                                    mAddFriendsNumber = 0;
                                    AddPeople = 0;
                                    app.setAddFriends(false);     //将一键加好友模块开启状态变成false
                                    app.setAllowAddFriends(false);
                                    if (app.getAllowOneStart()) {
                                        //                                        app.setPublicNumber(true);  //开启公众号模块
                                        Log.i("123", "开启。。。1");
                                        //                                        new Thread(new Runnable() {
                                        //                                            @Override
                                        //                                            public void run() {
                                        //                                                //先执行杀掉微信后台操作
                                        //                                                execShellCmd("am force-stop com.tencent.mm");
                                        //                                                execShellCmd("input keyevent 3");
                                        performBackClick(); //退出
                                        Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                                        execShellCmd("am force-stop com.tencent.mm");
                                        sleepTime(2000);
                                        app.setPublicNumber(true);  //开启公众号模块
                                        app.setAllowPublicNum(true);
                                        sleepTime(1000);
                                        openThread(); //跳转微信主界面
                                        //                                                try {
                                        //                                                    Thread.sleep(2500);
                                        //                                                } catch (InterruptedException e) {
                                        //                                                    e.printStackTrace();
                                        //                                                }
                                        //                                                intentWechat(); //跳转微信主界面
                                        //
                                        //                                            }
                                        //                                        }).start();
                                    } else {
                                        performBackClick();     //退出
                                        MyToast.show(EarnAccessibilityService.this, "服务已完结");
                                        return;
                                    }
                                }
                            } else {
                                if (AddPeople >= mAddfriendsText) {
                                    mAddFriendsNumber = 0;
                                    AddPeople = 0;
                                    app.setAddFriends(false);     //将一键加好友模块开启状态变成false
                                    app.setAllowAddFriends(false);
                                    if (app.getAllowOneStart()) {
                                        //                                        app.setPublicNumber(true);  //开启公众号模块
                                        Log.i("123", "开启。。。2");
                                        //                                        new Thread(new Runnable() {
                                        //                                            @Override
                                        //                                            public void run() {
                                        //                                                //先执行杀掉微信后台操作
                                        //                                                execShellCmd("am force-stop com.tencent.mm");
                                        //                                                execShellCmd("input keyevent 3");
                                        performBackClick(); //退出
                                        Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                                        execShellCmd("am force-stop com.tencent.mm");
                                        sleepTime(2000);
                                        app.setPublicNumber(true);  //开启公众号模块
                                        app.setAllowPublicNum(true);
                                        sleepTime(1000);
                                        openThread(); //跳转微信主界面
                                        //                                                try {
                                        //                                                    Thread.sleep(2500);
                                        //                                                } catch (InterruptedException e) {
                                        //                                                    e.printStackTrace();
                                        //                                                }
                                        //                                                intentWechat(); //跳转微信主界面
                                        //
                                        //                                            }
                                        //                                        }).start();
                                    } else {
                                        performBackClick();     //退出
                                        MyToast.show(EarnAccessibilityService.this, "服务已完结");
                                        return;
                                    }
                                }
                                //当一页添加7人时，就进行翻页操作
                                if (mAddFriendsNumber >= 7) {
                                    sleepTime(1500);
                                    execSlipCmd("input swipe 400 1200 400 480");
                                    sleepTime(5000);
                                    mAddFriendsNumber = 0;
                                }
                                findIdAndClick(IDConstant.NEW_FRIENDS_LIST_ICON, mAddFriendsNumber);
                                sleepTime(1000);
                                mAddFriendsNumber++;
                                AddPeople++;
                                mClickIndex = 0;
                                mClickFinish = 0;
                            }
                        } else {
                            mAddFriendsNumber = 0;
                            AddPeople = 0;
                            app.setAddFriends(false);     //将一键加好友模块开启状态变成false
                            app.setAllowAddFriends(false);
                            if (app.getAllowOneStart()) {
                                //                                app.setPublicNumber(true);  //开启公众号模块
                                Log.i("123", "开启。。。3");
                                //                                new Thread(new Runnable() {
                                //                                    @Override
                                //                                    public void run() {
                                //                                        //先执行杀掉微信后台操作
                                //                                        execShellCmd("am force-stop com.tencent.mm");
                                //                                        execShellCmd("input keyevent 3");
                                performBackClick(); //退出
                                Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                                execShellCmd("am force-stop com.tencent.mm");
                                sleepTime(2000);
                                app.setPublicNumber(true);  //开启公众号模块
                                app.setAllowPublicNum(true);
                                sleepTime(1000);
                                openThread(); //跳转微信主界面
                                //                                        try {
                                //                                            Thread.sleep(2500);
                                //                                        } catch (InterruptedException e) {
                                //                                            e.printStackTrace();
                                //                                        }
                                //                                        intentWechat(); //跳转微信主界面
                                //
                                //                                    }
                                //                                }).start();
                            } else {
                                performBackClick();     //退出
                                MyToast.show(EarnAccessibilityService.this, "服务已完结");
                                return;
                            }
                        }
                    }
                    /**
                     * 详细资料页面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus == NEW_FRIENDS_UI) {   //从新的朋友跳转来的，则点击
                            if (isFindText("发消息") && isFindId(IDConstant.WX_SEND_MESSAGE)) {
                                findIdAndClick(IDConstant.WX_BACK, 0);
                            } else if (isFindText("通过验证") && isFindId(IDConstant.WX_PASS_VALIDATION)) {
                                WXfindIdTextAndClick(IDConstant.WX_PASS_VALIDATION, "通过验证", 0);
                                //findTextAndClick("通过验证", 0);
                            } else if (isFindText("添加到通讯录") && isFindId(IDConstant.WX_ADD_FRIEND)) {
                                WXfindIdTextAndClick(IDConstant.WX_ADD_FRIEND, "添加到通讯录", 0);
                                //findTextAndClick("添加到通讯录", 0);
                                mClickIndex++;
                                sleepTime(1500);
                                //出现禁止通过通讯录加好友情况
                                if (isFindId(IDConstant.GR_AFFIRM)) {
                                    sleepTime(1500);
                                    findIdAndClick(IDConstant.GR_AFFIRM, 0);
                                    sleepTime(1500);
                                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                    return;
                                }
                                //出现添加黑名单时
                                if (mClickIndex == 3) {
                                    sleepTime(1500);
                                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                    return;
                                }
                            } else {
                                findIdAndClick(IDConstant.WX_BACK, 0);
                            }
                        } else if (pageStatus == VERITICATION_REQUEST_UI) {
                            sleepTime(1500);
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            return;
                        }
                    }
                    /**
                     * 验证申请页面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = VERITICATION_REQUEST_UI;
                        sleepTime(1500);
                        findIdAndClick(IDConstant.WX_HELLO_SEND_BUTTON, 0);
                        mClickFinish++;
                        if (mClickFinish == 3) {
                            sleepTime(1500);
                            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                            return;
                        }
                    }
                }
                /**
                 * 公众号推广04
                 */
                if (app.getPublicNumber()) {
                    //推送公众号
                    PublicNumDao mPublicNumDao = new PublicNumDao(getApplication());
                    List<PublicNumBean> publicNumBeanList = mPublicNumDao.queryForAll();
                    Collections.reverse(publicNumBeanList);
                    //推送人数
                    String publicNumPeopleNumText = PrefUtils.getString(EarnAccessibilityService.this, "publicNumPeopleNum", null);
                    int pushPeopleNum = Integer.parseInt(publicNumPeopleNumText);
                    Log.i("123",pushPeopleNum+"DDDD");
                    //从第几人开始推送
                    String publicNumStartPeopleText = PrefUtils.getString(EarnAccessibilityService.this, "PublicNumStartPeople", null);
                    int pushStartPeople = (Integer.parseInt(publicNumStartPeopleText)-1)>0?(Integer.parseInt(publicNumStartPeopleText)-1):0;
                    /**
                     * 主界面
                     */
                    if (app.getAllowPublicNum()) {
                        sleepTime(1000);
                        pageStatus = LAUNCHER_UI;   //当前在主界面
                        //如果是主界面，并找到通讯录按钮
                        if (isFindText("通讯录") && isFindId(IDConstant.MAIN_BUTTON)) {
                            sleepTime(1000);
                            //findTextAndClick("通讯录", 0);  //点击通讯录
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "通讯录", 1);
                        }
                        if (isFindText("公众号")) {
                            sleepTime(1000);
                            findTextAndClick("公众号", 0);  //点击公众号
                            app.setAllowPublicNum(false);
                        }
                    }
                    //公众号界面
                    else if (event.getClassName().equals("com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1500);
                        //点击搜索按钮
                        findContentAndClick(IDConstant.WX_SEARCH_PUBLICNUM, 0, "搜索", 2);
                    }
                    /**
                     * 公众号搜索页面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.brandservice.ui.BrandServiceLocalSearchUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = PUBLIC_NUM_SEARCH;
                        isfirstpush = true;
                        findIdAndClick(IDConstant.WX_PUBLICNUM_SOUSUOKUANG, 0);
                        sleepTime(500);
                        if (isFindId(IDConstant.WX_publicNumName)) {
                            findIdAndClick(IDConstant.WX_publicNumName, 0);
                        }
                        sleepTime(500);
                        if (mClickPublicNumList >= publicNumBeanList.size()) {
                            mClickPublicNumList = 0;
                            mChoosePeopleNum = 0;
                            mPublicNumTotalPeople = 0;
                            isfirstpush = true;
                            app.setPublicNumber(false);  //关闭公众号模块
                            app.setAllowPublicNum(false);
                            PrefUtils.putString(EarnAccessibilityService.this, "publicNumPeopleNum", "30");
                            PrefUtils.putString(EarnAccessibilityService.this, "PublicNumStartPeople", "0");
                            if (app.getAllowOneStart()) {
                                execShellCmd("am force-stop com.tencent.mm");
                                sleepTime(2000);
                                Log.i("123", "开启。。。4");
                                app.setDriftBottle(true);
                                app.setAllowDriftBottle(true);
                                sleepTime(1000);
                                openThread(); //跳转微信主界面
                            } else {
                                performBackClick();
                                Toast.makeText(EarnAccessibilityService.this, "服务已结束", Toast.LENGTH_SHORT).show();
                            }
                        }
                        String text = publicNumBeanList.get(mClickPublicNumList).getPublicNumName();
                        Log.i("1207", text);
                        mClickPublicNumList++;
                        inputHello(text);
                        sleepTime(1500);
                        if (isFindId(IDConstant.WX_SEARCH_RESULT)) {
                            findIdAndClick(IDConstant.WX_SEARCH_RESULT, 0);
                        } else {
                            findIdAndClick(IDConstant.WX_NO_SEARCH_RESULT_BACK, 0);
                        }

                    }
                    /**
                     * 公众号聊天界面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.ui.chatting.ChattingUI") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus == PUBLIC_NUM_DETAILS_UI) {
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_PUBLIC_NUM_CHAT_BACK, 0);
                        } else {
                            sleepTime(1000);
                            findContentAndClick(IDConstant.WX_PUBLIC_NUM_HEAD, 0, "聊天信息", 2);
                        }
                    }
                    /**
                     * 公众号详细界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = PUBLIC_NUM_DETAILS_UI;
                        if (mPublicNumTotalPeople >= pushPeopleNum) {
                            mPublicNumTotalPeople = 0;
                            sleepTime(1500);
                            findIdAndClick(IDConstant.WX_PUBLIC_NUM_DETAILS_BACK, 0);
                        } else {
                            sleepTime(1000);
                            findContentAndClick(IDConstant.WX_PUBLIC_NUM_HEAD, 0, "更多", 2);
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_PUBLIC_NUM_FRIEND, 0);
                        }
                    }
                    /**
                     * 选择界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.ui.transmit.SelectConversationUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1000);
                        findTextAndClick("更多联系人", 0);
                    }
                    /**
                     * 选择联系人界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.ui.contact.SelectContactUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        //如果一页添加11人就进行翻页操作
                        pubIndex = isFindIdlistnum(IDConstant.CHOOSE_PEOPLE);
                        if ((pushStartPeople + mPublicNumTotalPeople) / pubIndex > 0) {
                            for (int i = 0; i < (pushStartPeople + mPublicNumTotalPeople) / pubIndex; i++) {
                                normalMode(1000);
                                execShellCmd("input swipe 400 1100 400 700");
                                normalMode(5000);
                            }
                        }
                        if (mChoosePeopleNum >= pubIndex) {
                            //置零
                            mChoosePeopleNum = 0;
                        }
                        normalMode(1000);
                        if (isfirstpush) {
                            mChoosePeopleNum = pushStartPeople % 9;
                            isfirstpush = false;
                        }
                        findIdAndClick(IDConstant.WX_ContactListProject, mChoosePeopleNum);
                        //当前页面推送人数加1
                        mChoosePeopleNum++;
                        //总的 已推送公众号人数加1
                        mPublicNumTotalPeople++;
                        findIdAndClick(IDConstant.WX_HELLO_SEND_BUTTON, 0);
                        normalMode(1000);
                        findIdAndClick(IDConstant.WX_SendProject, 0);
                    }

                }


                /**
                 * 漂流瓶服务    05
                 */
                if (app.getDriftBottle()) {

                    //获取漂流瓶个数
                    int bottleTextNum = PrefUtils.getInt(getApplication(), "mBottleTextNum", 0);
                    //复制要输入的评论内容
                    if (app.getAllowOneStart()) {
                        Log.e(TAG, "getAllowOneStart 0000000000000");
                        String wx_driftbottle_num = PrefUtils.getString(EarnAccessibilityService.this, Constant.wxFunction[7], "0");
                        if (wx_driftbottle_num.equals("0") || TextUtils.isEmpty(wx_driftbottle_num)) {
                            wx_driftbottle_num = mwx_driftbottle_num;
                        }
                        try {
                            bottleTextNum = Integer.parseInt(wx_driftbottle_num);    //String转化成Int
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e(TAG, "bottleTextNum " + bottleTextNum);
                    if (app.getAllowDriftBottle()) {
                        sleepTime(1000);
                        String bottleText = PrefUtils.getString(getApplicationContext(), Constant.wxFunction[8], "");
                        String bottleText1 = PrefUtils.getString(getApplicationContext(), "mDriftBottle", null);
                        if (bottleText.equals("null") || TextUtils.isEmpty(bottleText) || bottleText == null) {
                            if (bottleText1 == "" || TextUtils.isEmpty(bottleText1)) {
                                bottleText = DriftBottleContent;
                            } else {
                                bottleText = bottleText1;
                            }
                        }
                        copyToBoard(bottleText);
                        //如果是主界面，并找到发现按钮
                        if (isFindText("发现") && isFindId(IDConstant.MAIN_BUTTON)) {
                            normalMode(800);
                            findTextAndClick("发现", 0);  //点击发现

                            if (isFindText("漂流瓶")) {
                                normalMode(800);
                                findTextAndClick("漂流瓶", 0);  //点击朋友圈
                            } else {
                                normalMode(2000);
                                Log.e(TAG, "not find");
                                if (isFindText("我")) {
                                    normalMode(800);
                                    findTextAndClick("我", 0);  //点击发现
                                }
                                if (isFindText("设置")) {
                                    normalMode(800);
                                    findTextAndClick("设置", 0);  //点击发现
                                }

                                if (isFindId(IDConstant.BOTTLE_GLOBAL)) {
                                    normalMode(800);
                                    findIdAndClick(IDConstant.BOTTLE_GLOBAL, 4);  //点击通用
                                }
                                if (isFindId(IDConstant.BOTTLE_GLOBAL)) {
                                    normalMode(800);
                                    findIdAndClick(IDConstant.BOTTLE_GLOBAL, 5);  //点击功能
                                }
                                Log.e(TAG, "11111111111111111111111");
                                normalMode(1500);
                                if (isFindText("漂流瓶")) {
                                    Log.e(TAG, "22222222333333333333");
                                    normalMode(1500);
                                    findTextAndClick("漂流瓶", 0);  //点击发现
                                }
                                normalMode(1500);
                                if (isFindText("启用该功能")) {
                                    normalMode(1500);
                                    findTextAndClick("启用该功能", 0);  //点击发现
                                }
                                normalMode(3000);
                                execShellCmd("input keyevent 4");
                                normalMode(1000);
                                execShellCmd("input keyevent 4");
                                normalMode(1000);
                                execShellCmd("input keyevent 4");
                                normalMode(1000);
                                execShellCmd("input keyevent 4");
                                normalMode(1000);
                                if (isFindText("发现")) {
                                    normalMode(800);
                                    findTextAndClick("发现", 0);  //点击发现
                                }
                                if (isFindText("漂流瓶")) {
                                    normalMode(800);
                                    findTextAndClick("漂流瓶", 0);  //点击朋友圈
                                    app.setAllowDriftBottle(false);
                                }

                            }
                        }
                    }

                    if (event.getClassName().equals("com.tencent.mm.plugin.bottle.ui.BottleBeachUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        int m = 0;
                        while (m < bottleTextNum) {
                            sleepTime(1500);
                            if (isFindText("扔一个") || isFindId(IDConstant.BOTTLE_THROW)) {
                                sleepTime(1000);
                                openNext("扔一个");
                            }
                            if (isFindId(IDConstant.BOTTLE_TURE)) {
                                execSlipCmd("input keyevent 4");
                                sleepTime(1000);
                                execSlipCmd("input keyevent 4");
                                m = bottleTextNum;
                            } else {
                                sleepTime(1500);
                                findIdAndClick(IDConstant.WX_ChangeText, 0);//键盘
                                //execSlipCmd("input tap 70 1780");
                                sleepTime(1500);
                                paste(IDConstant.BOTTLE_CONTENT, 0);
                                sleepTime(1500);
                                execSlipCmd("input keyevent 4");
                                //execSlipCmd("input tap 1100 1250");
                                sleepTime(1500);
                                execSlipCmd("input tap 430 1230");
                                sleepTime(6500);
                            }
                            m++;
                        }
                        app.setDriftBottle(false);
                        Toast.makeText(this, "漂流瓶服务已完结", Toast.LENGTH_SHORT).show();
                        if (app.getAllowOneStart()) {
                            //                            app.setGroupFriend(true);  //开启一键加群友模块
                            //                            new Thread(new Runnable() {
                            //                                @Override
                            //                                public void run() {
                            //                                    //先执行杀掉微信后台操作
                            //
                            //                                    execShellCmd("am force-stop com.tencent.mm");
                            //                                    execShellCmd("input keyevent 3");
                            performBackClick(); //退出
                            Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                            execShellCmd("am force-stop com.tencent.mm");
                            sleepTime(2000);
                            app.setGroupFriend(true);  //开启一键加群友模块
                            app.setAllowAddGroup(true);
                            sleepTime(1000);
                            intentWechat(); //跳转微信主界面
                            //                                    try {
                            //                                        Thread.sleep(2500);
                            //                                    } catch (InterruptedException e) {
                            //                                        e.printStackTrace();
                            //                                    }
                            //                                    intentWechat(); //跳转微信主界面
                            //
                            //                                }
                            //                            }).start();

                        } else {
                            performBackClick();
                        }

                    }
                }

                /**
                 * 一键加群友 06
                 */
                if (app.getGroupFriend()) {
                    Log.i("123", event.getClassName().toString() + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


                    /**
                     * 主界面
                     */
                    if (app.getAllowAddGroup()) {

                        sleepTime(1000);
                        pageStatus = LAUNCHER_UI;
                        pageStatus1 = LAUNCHER_UI;
                        //如果是主界面，并找到发现按钮
                        while (!isFindText("通讯录")) {
                            sleepTime(500);
                        }
                        if (isFindText("通讯录") && isFindId(IDConstant.MAIN_BUTTON)) {
                            sleepTime(1000);
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "通讯录", 1);
                            //findTextAndClick("通讯录", 0);  //点击发现
                        }
                        while (!isFindText("群聊")) {
                            sleepTime(500);
                        }
                        if (isFindText("群聊")) {
                            sleepTime(1000);
                            findTextAndClick("群聊", 0);  //点击附近的人
                            app.setAllowAddGroup(false);
                        }
                    }

                    /**
                     * 群list界面
                     */
                    if (event.getClassName().equals("com.tencent.mm.ui.contact.ChatroomContactUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        mGroupText = PrefUtils.getInt(EarnAccessibilityService.this, "mGroupText", 0);
                        if (app.getAllowOneStart()) {
                            Log.e(TAG, "getAllowOneStart 0000000000000");
                            //wxAddFriendNum
                            String wx_groupfriend_num = PrefUtils.getString(EarnAccessibilityService.this, Constant.wxFunction[3], "0");
                            if (TextUtils.isEmpty(wx_groupfriend_num) || wx_groupfriend_num.equals("0")) {
                                wx_groupfriend_num = mwx_groupfriend_num;
                            }
                            try {
                                mGroupText = Integer.parseInt(wx_groupfriend_num);    //String转化成Int
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e(TAG, "mGroupText " + mGroupText);
                        //获取设置的起始群数
                        if (mGroupText > out) {
                            out = mGroupText - 1;
                        }
                        //获取群list个数并点击
                        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.GR_LiST_IMG);
                        Log.i("list", list.size() + "kk");
                        if (mGroupText > list.size()) {
                            out = list.size() - 1;
                        }
                        if (isFindId(IDConstant.GR_NULL) && isFindText("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")) {
                            app.setGroupFriend(false);     //将附近人模块开启状态变成false
                            if (app.getAllowOneStart()) {
                                //                                app.setAKeySendMessage(true);  //开启一键发消息// 模块
                                //                                new Thread(new Runnable() {
                                //                                    @Override
                                //                                    public void run() {
                                //                                        //先执行杀掉微信后台操作
                                //
                                //                                        execShellCmd("am force-stop com.tencent.mm");
                                //                                        execShellCmd("input keyevent 3");
                                performBackClick(); //退出
                                Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                                execShellCmd("am force-stop com.tencent.mm");
                                sleepTime(2000);
                                app.setAKeySendMessage(true);  //开启一键发消息// 模块
                                app.setAllowAKeySendMessage(true);
                                sleepTime(1000);
                                openThread(); //跳转微信主界面
                                //                                        try {
                                //                                            Thread.sleep(2500);
                                //                                        } catch (InterruptedException e) {
                                //                                            e.printStackTrace();
                                //                                        }
                                //                                        intentWechat(); //跳转微信主界面
                                //                                    }
                                //                                }).start();
                            } else {
                                performBackClick();     //退出
                                //TODO 切换账号
                                MyToast.show(EarnAccessibilityService.this, "服务已完结");
                                return;
                            }
                        } else {
                            stopi = list.size() - 1;
                            if (out < list.size()) {
                                list.get(out).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                list.get(out).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                sleepTime(1500);
                            }
                        }

                        /**
                         * 群聊界面
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.ui.chatting.ChattingUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        Log.i("123", pageStatus + ":" + LAUNCHER_UI);
                        if (pageStatus == LAUNCHER_UI) {  //向内执行
                            sleepTime(1000);
                            findContentAndClick(IDConstant.WX_HEAD, 0, "聊天信息", 2);
                            sleepTime(1500);
                            //execShellCmd("input tap 1135 95");
                        }//右上角图标
                        if (pageStatus1 == GF_LAST) {   //向外执行
                            execShellCmd("input keyevent 4");
                            app.setAllowAddGroup(true);
                            sleepTime(1500);
                        }
                        /**
                         * 聊天信息界面
                         * 如果成员超过35人，先下滑一页，有‘查看全部群成员’，点击，没有就滑回原位从第一个开始加
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        mClickIndex = 0;
                        pageStatus = GF_INFO;//页面赋值

                        if (pageStatus1 == LAUNCHER_UI) {  //好友遍历并点击
                            if (inside == 35) {
                                execSlipCmd("input swipe 400 1200 400 450");
                                sleepTime(2000);
                                inside = 0;
                            }
                            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(
                                    "com.tencent.mm:id/" + IDConstant.GR_MEMBER_IMG);
                            Log.i("123", list.size() + "");
                            if (list.size() == 35) {
                                Log.i("123", list.size() + "ll");
                                if (isMore) {
                                    execSlipCmd("input swipe 400 1200 400 480");
                                }
                                sleepTime(2000);
                                if (isFindText("查看全部群成员")) {
                                    isMore = true;
                                    Log.i("123", list.size() + "ll");
                                    findTextAndClick("查看全部群成员", 0);
                                    sleepTime(2000);
                                } else {
                                    if (isMore) {
                                        execSlipCmd("input swipe 400 480 400 1200");
                                        isMore = false;
                                    }
                                    sleepTime(2000);
                                    if (inside < list.size()) {
                                        list.get(inside).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        list.get(inside).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        sleepTime(1500);
                                    }
                                }
                            } else {
                                //                                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                                //                                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.GR_MEMBER_IMG);
                                if (inside < list.size()) {
                                    list.get(inside).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    list.get(inside).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    sleepTime(1500);
                                }
                            }
                        } else if (pageStatus1 == GF_LAST) {  //第一个群执行结束初始化下一个
                            out++;
                            inside = 0;
                            inside1 = 0;
                            execShellCmd("input keyevent 4");
                            sleepTime(1500);
                        }


                        /**
                         * 聊天成员
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.chatroom.ui.SeeRoomMemberUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = GF_MCHAT;
                        Log.i("123", "pageStatus1" + pageStatus1);
                        sleepTime(1500);
                        if (pageStatus1 == GF_LAST) {
                            execShellCmd("input keyevent 4");
                        } else {
                            if (isMore) {
                                Log.i("123", "l3");
                                execSlipCmd("input swipe 680 420 680 1200");
                                isMore = false;
                            }
                            sleepTime(2000);
                            if (inside1 == 30) {
                                Log.i("123", "l2");
                                execSlipCmd("input swipe 400 1200 400 530");
                                inside1 = 0;
                                sleepTime(2000);
                            }
                            findIdAndClick(IDConstant.GR_MEMBER_IMG1, inside1);
                            inside1++;
                            sleepTime(1500);
                        }
                    }
                    /**
                     * 详细资料界面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus == GF_TEST || pageStatus == GF_PROMPT) {    //向外执行
                            Log.i("123", "GF_TEST");
                            sleepTime(1500);
                            findIdAndClick(IDConstant.GR_Back, 0);
                        } else if (pageStatus == GF_INFO || pageStatus == GF_MCHAT) {
                            Log.i("123", "GF_INFO");
                            if (isFindText("发消息") && isFindId(IDConstant.WX_SEND_MESSAGE)) {
                                sleepTime(1500);
                                execShellCmd("input keyevent 4");
                                inside++;
                            } else if (isFindId(IDConstant.WX_ADD_FRIEND) && isFindText("添加到通讯录")) {
                                sleepTime(1500);
                                WXfindIdTextAndClick(IDConstant.WX_ADD_FRIEND, "添加到通讯录", 0);

                                // findTextAndClick("添加到通讯录", 0);
                                mClickIndex++;
                                //出现添加黑名单时
                                if (mClickIndex >= 3) {
                                    sleepTime(1500);
                                    isfirstEnter = true;
                                    findIdAndClick(IDConstant.GR_Back, 0);
                                    sleepTime(1500);
                                    inside++;
                                    pageStatus1 = LAUNCHER_UI;
                                    mClickIndex = 0;
                                }
                            } else {
                                if (!isfirstEnter) {
                                    Log.i("123", "keyevent");
                                    sleepTime(1500);
                                    execShellCmd("input keyevent 4");
                                    inside++;
                                } else {
                                    isfirstEnter = false;
                                }

                            }

                        }
                        /**
                         * 提示
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.ui.base.i")) {
                        Log.i("zw1008", "拒绝*********************************************************************************");
                        Log.i("123", isFindId(IDConstant.GR_AFFIRM) + "----" + isFindText("确定"));
                        sleepTime(1000);
                        if (isFindId(IDConstant.GR_AFFIRM) && isFindText("确定")) {
                            Log.i("123", isFindId(IDConstant.GR_AFFIRM) + "----" + isFindText("确定"));
                            sleepTime(1500);
                            findIdAndClick(IDConstant.GR_AFFIRM, 0);
                            pageStatus = GF_PROMPT;
                            inside++;
                        }
                        /**
                         * 选择联系人
                         */
                    } else if (event.getClassName().equals("com.tencent.mm.ui.contact.SelectContactUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus1 = GF_LAST;
                        if (isFindId(IDConstant.GR_CONTACTS)) {
                            execShellCmd("input keyevent 4");
                        }
                        if (out == stopi) {
                            //                            if(out==list.size()-1){
                            app.setGroupFriend(false);     //将附近人模块开启状态变成false

                            if (app.getAllowOneStart()) {
                                //                                app.setAKeySendMessage(true);  //开启一键发消息// 模块
                                //                                new Thread(new Runnable() {
                                //                                    @Override
                                //                                    public void run() {
                                //                                        //先执行杀掉微信后台操作
                                //
                                //                                        execShellCmd("am force-stop com.tencent.mm");
                                //                                        execShellCmd("input keyevent 3");
                                performBackClick(); //退出
                                Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                                execShellCmd("am force-stop com.tencent.mm");
                                sleepTime(2000);
                                app.setAKeySendMessage(true);  //开启一键发消息// 模块
                                app.setAllowAKeySendMessage(true);
                                sleepTime(1000);
                                intentWechat(); //跳转微信主界面
                                //                                        try {
                                //                                            Thread.sleep(2500);
                                //                                        } catch (InterruptedException e) {
                                //                                            e.printStackTrace();
                                //                                        }
                                //                                        intentWechat(); //跳转微信主界面
                                //                                    }
                                //                                }).start();
                            } else {
                                performBackClick();     //退出
                                //TODO 切换账号
                                MyToast.show(EarnAccessibilityService.this, "服务已完结");
                            }
                        }
                    }
                    /**
                     * 验证申请界面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindId(IDConstant.GR_SEND) && isFindText("发送")) {
                            sleepTime(1500);
                            WXfindIdTextAndClick(IDConstant.GR_SEND, "发送", 0);
                            //findIdAndClick(IDConstant.GR_SEND, 0);
                            inside++;
                            pageStatus = GF_TEST;
                        }
                    }

                }
                /**
                 * 白名单  07
                 */
                if (app.getWhiteList()) {
                    /**
                     * 主界面
                     */
                    if (app.getAllowWhite()) {
                        sleepTime(1000);

                        mWhiteListContentList = new WhiteListDao(EarnAccessibilityService.this).queryForAll();
                        Collections.reverse(mWhiteListContentList);
                        pageStatus = LAUNCHER_UI;
                        sleepTime(1000);
                        findContentAndClick(IDConstant.WX_HEAD, 0, "更多功能按钮", 2);
                        //execShellCmd("input tap 1120 95");
                        if (isFindId(IDConstant.SLIDING)) {
                            sleepTime(1000);
                            WXfindIdTextAndClick(IDConstant.SLIDING, "添加朋友", 1);
                            //findIdAndClick(IDConstant.SLIDING, 1);
                            app.setAllowWhite(false);
                        }
                    }
                    if (event.getClassName().equals("com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {     //新的朋友界面
                        pageStatus = SEARCH;
                        if (mWhiteNumber == mWhiteListContentList.size()) {
                            app.setWhiteList(false);     //将朋友圈模块开启状态变成false
                            performBackClick();     //退出
                            MyToast.show(EarnAccessibilityService.this, "服务已完结");
                        } else {
                            execSlipCmd("input tap 400 230");//点击输入框
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.search.ui.FTSAddFriendUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {    //搜索界面

                        if (pageStatus == SEARCH) {
                            String WhiteText = mWhiteListContentList.get(mWhiteNumber).getWhiteContent();//搜索内容赋值
                            sleepTime(1500);
                            if (isFindId(IDConstant.WHITE_CONTENT2)) {
                                WXfindIdTextAndClick(IDConstant.WHITE_CONTENT2, "搜索", 0);
                            }
                            sleepTime(1500);
                            inputHello(WhiteText);//输入搜索内容
                            sleepTime(1500);
                            findIdAndClick(IDConstant.WHITE_CONTENT3, 0);
                            //execSlipCmd("input tap 650 215");//点击搜索
                            sleepTime(3000);
                        }
                        if (isFindText("该用户不存在") || pageStatus == LAUNCHER_END || pageStatus == NEARBY_FRIENDS_UI) {
                            //  pageStatus=CONTACT_INFO_UI;
                            mWhiteNumber++;
                            mClickIndex = 0;
                            execSlipCmd("input keyevent 4");
                        }
                        if (isFindText("操作过于频繁，请稍后再试")) {
                            mWhiteNumber = mWhiteListContentList.size();
                            mClickIndex = 0;
                            execSlipCmd("input keyevent 4");
                        }

                    } else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) { //详细资料界面

                        if (pageStatus == LAUNCHER_END) {
                            execSlipCmd("input keyevent 4");
                        } else {
                            pageStatus = NEARBY_FRIENDS_UI;
                            if (isFindText("发消息") && isFindId(IDConstant.WX_SEND_MESSAGE)) {
                                sleepTime(1000);
                                execSlipCmd("input keyevent 4");//如果是发消息点击返回
                            }
                            if (isFindText("添加到通讯录") && isFindId(IDConstant.WX_ADD_FRIEND)) {
                                WXfindIdTextAndClick(IDConstant.WX_ADD_FRIEND, "添加到通讯录", 0);
                                //findTextAndClick("添加到通讯录", 0);  //否则添加点添加到通讯录
                                mClickIndex++;
                                sleepTime(2500);
                            } else {
                                sleepTime(1000);
                                execSlipCmd("input keyevent 4");//如果是发消息点击返回
                            }
                            //出现添加黑名单时
                            if (mClickIndex >= 3) {
                                sleepTime(1500);
                                findIdAndClick(IDConstant.GR_Back, 0);
                                pageStatus = LAUNCHER_END;
                                mClickIndex = 0;
                            }
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {  //验证申请界面
                        pageStatus = LAUNCHER_END;
                        sleepTime(1000);
                        findIdAndClick(IDConstant.WHITE_SEND, 0);
                        //     execSlipCmd("input tap 1115 100");

                    }

                }
                /**
                 * 一键发消息模块08
                 */
                if (app.getAKeySendMessage()) {
                    if (app.getAllowAKeySendMessage()) {
                        Log.i("123", isnotsend + "");
                        if (!isnotsend) {
                            isnotsend = false;
                            sleepTime(1000);
                            while (!isFindText("通讯录")) {
                                sleepTime(500);
                            }
                            if (isFindId(IDConstant.MAIN_BUTTON) && isFindText("通讯录")) {
                                WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "通讯录", 1);
                                //findTextAndClick("通讯录", 0);
                            }
                        } else {
                            isnotsend = false;
                        }
                        Log.i("123", mContactlistCurrent + ":" + isFindIdlistnum(IDConstant.WX_CONTACT_LIST));
                        if (mContactlistCurrent == isFindIdlistnum(IDConstant.WX_CONTACT_LIST)) {
                            Log.i("123", isFindIdlistnum(IDConstant.WX_CONTACT_LIST) + "");
                            if (isFindId(IDConstant.WX_CONTACT_agp)) {
                                app.setAllowAKeySendMessage(false);
                                app.setAKeySendMessage(false);
                                mContactlistCurrent = 0;
                                execShellCmd("input keyevent 4");
                                MyToast.show(EarnAccessibilityService.this, "服务已完结");
                            } else {
                                sleepTime(1500);
                                execSlipCmd("input swipe 400 1100 400 400");
                                isnotsend = true;
                                sleepTime(5000);
                                Log.i("123", mContactlistCurrent + isFindIdlistnum(IDConstant.WX_CONTACT_LIST) + "");
                                if (isFindId(IDConstant.WX_CONTACT_agp) && isFindIdlistnum(IDConstant.WX_CONTACT_LIST) < 8 && mContactlistCurrent < 8) {
                                    mContactlistCurrent = isFindIdlistnum(IDConstant.WX_CONTACT_LIST) - mContactlistCurrent;
                                } else {
                                    mContactlistCurrent = 0;
                                }
                                Log.i("123", app.getAllowAKeySendMessage() + "1");
                                findIdAndClick(IDConstant.WX_CONTACT_LIST, mContactlistCurrent);
                                app.setAllowAKeySendMessage(false);
                                mContactlistCurrent++;

                            }

                        } else {
                            sleepTime(1500);
                            Log.i("123", app.getAllowAKeySendMessage() + "1");
                            while (!isFindId(IDConstant.WX_CONTACT_LIST, mContactlistCurrent)) {
                                sleepTime(500);
                            }
                            findIdAndClick(IDConstant.WX_CONTACT_LIST, mContactlistCurrent);
                            app.setAllowAKeySendMessage(false);
                            mContactlistCurrent++;
                        }

                    }/**
                     * 详细资料页面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        while (!isFindText("发消息")) {
                            sleepTime(500);
                        }
                        if (isFindText("微信团队") || isFindText("文件传输助手")) {
                            sleepTime(1000);
                            isnotsend = true;
                            app.setAllowAKeySendMessage(true);
                            findIdAndClick(IDConstant.WX_BACK, 0);
                        } else {
                            if (isFindText("发消息") && isFindId(IDConstant.WX_SEND_MESSAGE)) {
                                sleepTime(1000);
                                WXfindIdTextAndClick(IDConstant.WX_SEND_MESSAGE, "发消息", 0);
                                //findTextAndClick("发消息", 0);
                            }
                        }
                    }
                    /**
                     * 聊天界面
                     */
                    else if (event.getClassName().equals("com.tencent.mm.ui.chatting.ChattingUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        while (!isFindId(IDConstant.WX_AKEYCHAT_EDIT)) {
                            sleepTime(500);
                        }
                        if (isFindId(IDConstant.WX_AKEYCHAT_EDIT)) {
                            sleepTime(1000);
                            findIdAndClick(IDConstant.WX_AKEYCHAT_EDIT, 0);
                            String str = PrefUtils.getString(getApplicationContext(), Constant.wxFunction[11], "");
                            String str1 = PrefUtils.getString(getApplicationContext(), "OneKeyDialogText", null);
                            if (str.equals("null") || TextUtils.isEmpty(str) || str == null) {
                                if (str1 == "" || TextUtils.isEmpty(str1)) {
                                    str = AKeyContent;
                                } else {
                                    str = str1;
                                }
                            }
                            sleepTime(1000);
                            inputHello(str);
                            sleepTime(1500);
                            findIdAndClick(IDConstant.WX_AKEYCHAT_SEND, 0);
                            sleepTime(1000);
                            //返回
                            findIdAndClick(IDConstant.WX_CHATTING_BACK, 0);
                            app.setAllowAKeySendMessage(true);
                            Log.i("123", app.getAllowAKeySendMessage() + "2");
                        }
                    }
                }
                //一键清粉
                /**
                 * 清除死粉
                 */
                Log.i("123", event.getClassName().toString());
                if (app.getWxDeleteDeadFriends()) {
                    if (app.getAllowWxDeleteDeadFriends()) {
                        app.setAllowWxDeleteDeadFriends(false);
                        if (!isfirstclearfriends) {
                            findIdAndClick(IDConstant.WX_DELETE_DEAD_TONGXUNLU, 1);
                            isfirstclearfriends = true;
                        }
                        sleepTime(1000);
                        findIdAndClick(IDConstant.WX_TONGXUNLU_LIST, weishanchu);
                    }
                    if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindText("A-A")) {
                            app.setAllowWxDeleteDeadFriends(true);
                            lianxuweishanchu = 0;
                            sleepTime(1000);
                            findContentAndClick(IDConstant.WX_HEAD, 0, "更多", 2);
                            sleepTime(1000);
                            WXfindIdTextAndClick(IDConstant.WX_XIANXIZILIAO_SHANCHU, "删除", 6);
                            sleepTime(1000);
                            WXfindIdTextAndClick(IDConstant.WX_XIANXIZILIAO_QUEDING_SHANCHU, "删除", 0);
                        } else {
                            app.setAllowWxDeleteDeadFriends(true);
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_BACK, 0);
                            //未删除计数加一
                            weishanchu++;
                            //连续3个未删除加一
                            lianxuweishanchu++;
                        }
                        if (lianxuweishanchu == 3) {
                            weishanchu = 0;
                            lianxuweishanchu = 0;
                            app.setWxDeleteDeadFriends(false);
                            app.setAllowWxDeleteDeadFriends(false);
                            performBackClick();     //退出
                            MyToast.show(EarnAccessibilityService.this, "服务已完结");
                        }
                    }

                }
                if (app.isAllowAutoSwitchNumber()) {
                    String mobile = PrefUtils.getString(getApplicationContext(), "mAutoswitchmobile", "");
                    String password = PrefUtils.getString(getApplicationContext(), "mAutoswitchpassword", "");
                    if (app.isAutoSwitchNumber()) {

                        sleepTime(1000);
                        while (!isFindText("我")) {
                            sleepTime(500);
                        }
                        if (isFindId(IDConstant.MAIN_BUTTON) && isFindText("我")) {
                            WXfindIdTextAndClick(IDConstant.MAIN_BUTTON, "我", 3);
                        }
                        while (!isFindText("设置")) {
                            sleepTime(500);
                        }
                        if (isFindText("设置")) {
                            findTextAndClick("设置", 0);
                            app.setAutoSwitchNumber(false);
                            sleepTime(1000);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.plugin.setting.ui.setting.SettingsUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        Log.i("123", "退出");
                        while (!isFindText("退出")) {

                            sleepTime(500);
                        }
                        if (isFindText("退出")) {
                            Log.i("123", "find退出");
                            findTextAndClick("退出", 0);
                        }
                        sleepTime(2000);
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_bm3)) {
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_bm3, 0);
                        }
                        sleepTime(2000);
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_aga)) {
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_aga, 0);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.ui.account.LoginPasswordUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1000);
                        findContentAndClick(IDConstant.WX_HEAD, 0, "更多", 1);
                        sleepTime(2000);
                        Log.i("123", "切换");
                        //                        if (isFindText("切换账号")){
                        //                            Log.i("123","切换over");
                        //                            findTextAndClick("切换账号",0);
                        //                        }
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_hl)) {
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_hl, 0);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.ui.account.mobile.MobileInputUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1000);
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_blj)) {
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_blj, 0);
                        }
                    } else if (event.getClassName().equals("com.tencent.mm.ui.account.LoginUI")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1000);
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_hb)) {
                            copyToBoard(mobile);
                            paste(IDConstant.WX_XIANXIZILIAO_hb, 0);
                            copyToBoard(password);
                            paste(IDConstant.WX_XIANXIZILIAO_hb, 1);
                            findIdAndClick(IDConstant.WX_XIANXIZILIAO_blk, 0);
                        }

                        if (isFindId(IDConstant.WX_XIANXIZILIAO_bxr)) {
                            if (isFindText("确定")) {
                                findTextAndClick("确定", 0);
                                app.setAutoSwitchNumber(false);
                            }
                        }
                        if (isFindId(IDConstant.WX_XIANXIZILIAO_bxn)) {
                            if (isFindText("是")) {
                                findTextAndClick("是", 0);
                                app.setAutoSwitchNumber(false);
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 切换地点
     */

    private void ChangeLocation() {
        //        mMapSearchDao = new MapSearchDao(getApplicationContext());
        //        mMapSearchBeanList = mMapSearchDao.queryForAll();
        mMapSearchBeanList = new MyApplication().mAllAddressList;
        Log.i("123", mMapSearchBeanList.size() + "AA" + index + app.getAllowOneStart());
        if (mMapSearchBeanList.size() <= index) {
            index = 0;
            app.setNearbyPeople(false);
            performBackClick();     //退出
            //将附近人模块开启状态变成false
            if (app.getAllowOneStart()) {
                app.setFriendCircle(true);  //开启朋友圈模块
                isAlreadyReply = false;
                performBackClick(); //退出
                Toast.makeText(EarnAccessibilityService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                execShellCmd("am force-stop com.tencent.mm");
                sleepTime(2000);
                app.setFriendCircle(true);  //开启朋友圈模块
                app.setAllowFriendCircle(true);
                sleepTime(1000);
                openThread(); //跳转微信主界面
                //                new Thread(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        //先执行杀掉微信后台操作
                //
                //                        execShellCmd("am force-stop com.tencent.mm");
                //                        sleepTime(1500);
                //                        execShellCmd("input keyevent 3");
                //
                //                        app.setFriendCircle(true);  //开启朋友圈模块
                //                        app.setAllowFriendCircle(true);
                //                        //                        app.setAllowOneStart(true);
                //                        try {
                //                            Thread.sleep(2500);
                //                        } catch (InterruptedException e) {
                //                            e.printStackTrace();
                //                        }
                //                        intentWechat(); //跳转微信主界面
                //
                //                    }
                //                }).start();

            }
            //            BackClick();

            //            //TODO 切换账号
            //            MyToast.show(EarnAccessibilityService.this, "服务已完结");

        } else {
            sleepTime(800);
            execShellCmd("input keyevent 3");
            sleepTime(800);
            app.setNearbyPeople(false);     //将附近人模块开启状态变成false
            startThread();
        }


    }

    /**
     * 线程跳转微信
     */
    private void startThread() {
        //先执行杀掉微信后台操作
        execShellCmd("am force-stop com.tencent.mm");

        startMock(mMapSearchBeanList.get(index).getLatitudes(), mMapSearchBeanList.get(index).getLongtitudes(), mMapSearchBeanList.get(index).getLac(), mMapSearchBeanList.get(index).getCid(), mMapSearchBeanList.get(index).getMnc());

        app.setNearbyPeople(true);  //开启附近人模块

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intentWechat(); //跳转微信主界面

    }

    private void openThread() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intentWechat(); //跳转微信主界面

    }
    //    private final Timer timer = new Timer();
    //    private TimerTask task;
    //    Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            // TODO Auto-generated method stub
    //            Log.e("98765", "985699851111111111" );
    //            execShellCmd("input keyevent 4");
    //            // 要做的事情
    //            super.handleMessage(msg);
    //        }
    //    };


    @Override
    public void onInterrupt() {
        super.onInterrupt();
    }

    /**
     * 跳转微信主界面
     */
    public void intentWechat() {
        //        Intent wxIntent = new Intent();
        //        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        //        wxIntent.setAction(Intent.ACTION_MAIN);
        //        wxIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //        wxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        wxIntent.setComponent(cmp);
        //        startActivity(wxIntent);

        try {
            PackageManager packageManager = getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent viewIntent = new
                    Intent("android.intent.action.VIEW", Uri.parse("http://weixin.qq.com/"));
            startActivity(viewIntent);
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
