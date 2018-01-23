package vicmob.micropowder.service;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Collections;
import java.util.List;

import vicmob.micropowder.config.Constant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.QQIDConstant;
import vicmob.micropowder.daoman.PxDBHelper;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.bean.WhiteListContentBean;
import vicmob.micropowder.daoman.dao.WhiteListDao;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

import static vicmob.micropowder.config.QQIDConstant.QQ_NAME;

/**
 * Created by Eren on 2017/6/24.
 * <p>
 * 辅助服务
 */
public class QQAccessibilityService extends BaseAccessibilityService {

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
    public static final int QQ_LAUNCHER_UI = 1;
    /**
     * 附近人界面
     */
    public static final int QQ_NEARBY_FRIENDS_UI = 2;
    public static final int QQ_USER_INFO_UI = 3;

    /**
     * 打招呼页面
     */
    public static final int QQ_SAY_HI_EDIT_UI = 4;
    /**
     * 直播页面
     */
    public static final int QQ_INTER_VIDEO_UI = 5;

    /**
     * 当前QQ附近人访问的位置
     */
    private int mQQNearbyIndex = 0;
    /**
     * 当前QQ附近的访问总人数
     */
    private int mQQNearbySumNumber = 0;
    /**
     * QQ附近的人默认内容
     */
    private String QQNearByContent = "hello 你好";
    /**
     * 一键发消息默认内容
     */
    private String AKeyContent = "hello!!!";
    /***
     * 发消息点击次数
     * **/
    private int clickNum = 0;
    /**
     * 当前第几个地点
     */
    public static int index = 0;
    private List<MapSearchBean> mMapSearchBeanList;
    /***
     * 第一次进入附近界面
     */
    private int first = 0;
    /*****
     * 当前点击关注次数
     */
    private int focus = 0;
    /**
     * 数据库
     */
    public static SQLiteDatabase mSQLiteDatabase;
    private int frienditem = 0;//qq一键发送对item的计数
    private int friendlisttext = 0;//qq一键发送对id=text1的计数
    private boolean isfirstsendmessage;//qq一键发送刚开始进入点击联系人合并listview


    private int goupfriendi = 0;
    //qq白名单号码
    private List<WhiteListContentBean> mWhiteListContentList;
    //群好友的点击位置
    private String cmd = "input tap 1155 468";
    //群名称
    private String groupText = "初始";
    //群成员总数
    private Integer groupAllNum;
    //群成员位置
    private Integer goupMemberj = 0;
    //群成员列表可翻页次数
    private Integer groupOverTime;
    /**
     * 当前白名单位置
     */
    private int mWhiteNumber = 0;
    //每页群成员位置
    private int x = 0;

    private int whitexz = 0;
    private int WHITEBACK = 2;
    private int SENDBACK = 3;

    /**
     * QQ手机通讯录当前页面点击个数
     */
    private int mQQClickPhoneListNum = 0;
    /**
     * QQ一键加好友个数总和
     */
    private int mQQAddFriendsTotalNum = 0;
    /**
     * QQ一键加好友   加好友按钮点击计数
     */
    private int mQQClickAddFriendsBtnNum = 0;


    private boolean isaddfriend;
    private int groupitem;
    private int grouptext;
    private int groupfrienditem;
    private boolean isgroupsend;
    private boolean isgroupend;
    private boolean isgroupswipe;
    private boolean isdanger;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        app = (MyApplication) getApplication();
        int eventType = event.getEventType();
        if (event.getSource() != null) {
            try {
                //附近的人
                if (app.getQQNearbyPeople()) {
                    Log.i("123", event.getClassName().toString() + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    //获取需要打招呼的人数
                    int mQQNearTextNum = PrefUtils.getInt(QQAccessibilityService.this, "mQQNearTextNum", 0);
                    /***
                     * QQ附近人
                     * ***/

                    if (app.getAllowQQNearbyPeople()) {
                        pageStatus = QQ_LAUNCHER_UI;
                        sleepTime(1000);
                        //复制需要输入的评论内容
                        String mNearText = PrefUtils.getString(getApplicationContext(), Constant.qqFunction[1], "");
                        String mNearText1 = PrefUtils.getString(getApplicationContext(), "mNearText", null);
                        Log.i("789", mNearText + "aa");
                        if (mNearText.equals("null") || TextUtils.isEmpty(mNearText) || mNearText == null) {
                            if (mNearText1.equals("null") || TextUtils.isEmpty(mNearText1)) {
                                mNearText = QQNearByContent;
                            } else {
                                mNearText = mNearText1;
                            }
                        }
                        copyToBoard(mNearText);
                        if (isFindText("动态")) {
                            sleepTime(2000);
                            findTextAndClick("动态", 0);
                        }
                        if (isFindText("附近")) {
                            sleepTime(1000);
                            findTextAndClick("附近", 0);
                            mQQNearbyIndex = 1;
                            first = 0;
                            clickNum = 0;
                            app.setAllowQQNearbyPeople(false);
                        }

                    }
                    /***
                     * 附近界面*/
                    if (event.getClassName().equals("com.tencent.mobileqq.activity.NearbyActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = QQ_NEARBY_FRIENDS_UI;
                        if (pageStatus == QQ_NEARBY_FRIENDS_UI) {
                            sleepTime(1000);
                            if (first == 0) {//第一次进入界面
                                sleepTime(1000);
                                execSlipCmd("input swipe 360 500 360 1050");//下拉刷新
                                sleepTime(10000);
                                first++;
                            } else {
                                sleepTime(2000);
                            }
                            Log.i("aa", isFindQQIdlistnum(QQIDConstant.QQNEARBY_LIST_VIEW) + "---" + mQQNearbyIndex);
                            //每页等于或超过几个人时翻页
                            if (mQQNearbyIndex >= isFindQQIdlistnum(QQIDConstant.QQNEARBY_LIST_VIEW) && isFindQQIdlistnum(QQIDConstant.QQNEARBY_LIST_VIEW) != 0) {
                                sleepTime(1500);
                                execSlipCmd("input swipe 360 1000 360 300");
                                sleepTime(4000);
                                mQQNearbyIndex = 0;
                            }

                            mQQNearbySumNumber++;

                            if (mQQNearbySumNumber > mQQNearTextNum) {//当点击大于默认或输入的数据时服务结束
                                index++;
                                //切换地点
                                ChangeLocation();
                                mQQNearbyIndex = 0;
                                mQQNearbySumNumber = 0;
                                first = 0;

                            } else {
                                QQfindIdAndClick(QQIDConstant.QQNEARBY_LIST_VIEW, mQQNearbyIndex);
                                mQQNearbyIndex++;
                            }
                        }
                        /****
                         * 去领心弹框界面
                         */
                    } else if (event.getClassName().equals("android.app.Dialog")) {
                        sleepTime(1000);
                        Log.i("123", "-----" + isFindText("去领心") + "---" + isFindText("连续登录天天加心") + "---" + isFindId("name"));
                        if (isFindText("去领心")) {
                            execSlipCmd("input tap 360 1139");
                            sleepTime(1000);
                            mQQNearbyIndex = 1;
                            mQQNearbySumNumber = 0;
                        }
                        /****
                         * 普通详细信息界面
                         */
                    } else if (event.getClassName().equals("com.tencent.mobileqq.nearby.profilecard.NearbyPeopleProfileActivity") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = QQ_USER_INFO_UI;
                        focus = PrefUtils.getInt(QQAccessibilityService.this, "focusNum", 0);
                        Log.i("789", focus + "aa");
                        if (pageStatus == QQ_USER_INFO_UI) {
                            if (isFindQQIdText(QQ_NAME, "编辑交友资料")) {
                                sleepTime(1000);
                                BackClick();
                            }

                            if (focus >= 5) {

                            } else {
                                if (isFindQQIdText("txt", "关注") && !isFindText("已关注")) {
                                    sleepTime(1000);
                                    QQfindIdTextAndClick("txt", "关注", 0);
                                    focus++;
                                    PrefUtils.putInt(QQAccessibilityService.this, "focusNum", focus);
                                }
                            }
                            sleepTime(2000);
                            if (isFindQQIdText("txt", "发消息")) {
                                sleepTime(1000);
                                QQfindIdTextAndClick("txt", "发消息", 0);
                                clickNum++;
                                first++;
                                sleepTime(2000);
                                if (clickNum > 0) {
                                    QQfindIdAndClick(QQIDConstant.QQ_BACK, 0);
                                    return;
                                }
                            }
                        }
                        /****
                         *账号第一次登入第一次打招呼出现的弹框
                         */
                    } else if (event.getClassName().equals("com.tencent.mobileqq.nearby.widget.NearbyCustomDialog")) {
                        sleepTime(1000);
                        Log.i("123", "-----" + isFindText("知道了") + "---" + isFindText("附近点赞升级啦") + "---" + isFindId("name"));
                        if (isFindText("知道了") || isFindText("附近点赞升级啦")) {
                            execSlipCmd("input tap 360 1017");
                            sleepTime(1000);
                        }

                        /****
                         * 直播界面
                         */
                    } else if (event.getClassName().equals("com.tencent.mobileqq.intervideo.now.NowGestureProxyActivity") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = QQ_INTER_VIDEO_UI;
                        sleepTime(2000);
                        BackClick();
                        sleepTime(1500);
                        Log.i("aa", "...." + mQQNearbyIndex);


                        /****
                         * 广告界面(进不了)
                         */
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.QQBrowserActivity") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(2000);
                        BackClick();

                        /****
                         * 发消息界面
                         */
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.ChatActivity") &&
                            eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = QQ_SAY_HI_EDIT_UI;
                        sleepTime(1000);
                        QQpaste(QQIDConstant.QQ_SAY_HELLO_EDIT_TEXT, 0);   // 粘贴进剪切板
                        sleepTime(1000);
                        QQfindIdAndClick(QQIDConstant.QQ_HELLO_SEND_BUTTON, 0); // 点击发送
                        sleepTime(1000);
                        //execSlipCmd("input tap 50 100");//坐标返回
                        performChatBackClick("rlCommenTitle", 0);
                        clickNum = 0;
                    }
                }
                /**
                 * 发消息
                 */
                if (app.getQQAKeySendMessage()) {
                    if (app.getQQAllowAKeySendMessage()) {

                        if (isfirstsendmessage) {
                            Log.i("123", "1" + isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES));
                            if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES) > 0) {
                                Log.i("123", friendlisttext + ":" + isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES));

                                if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES) == friendlisttext) {
                                    Log.i("123", isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES) + ":" + friendlisttext);
                                    if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES) < 5) {
                                        Log.i("123", "3");
                                        app.setQQAKeySendMessage(false);
                                        app.setQQAllowAKeySendMessage(false);
                                        isfirstsendmessage = false;
                                        friendlisttext = 0;
                                        frienditem = 0;
                                        //杀死QQ后台
                                        execShellCmd("am force-stop com.tencent.mobileqq");
                                        sleepTime(1000);
                                        execShellCmd("input keyevent 3");
                                        sleepTime(1000);
                                    } else {
                                        if (isFindQQBouns(QQIDConstant.QQ_FRIEND_TEXT, friendlisttext - 1,
                                                QQIDConstant.QQ_ELV_BUDDIES).bottom == 1074 && isFindQQBouns(QQIDConstant.QQ_FRIEND_TEXT, friendlisttext - 1, QQIDConstant.QQ_ELV_BUDDIES).top == 1118) {
                                            Log.i("123", "4");
                                            app.setQQAKeySendMessage(false);
                                            app.setQQAllowAKeySendMessage(false);
                                            isfirstsendmessage = false;
                                            friendlisttext = 0;
                                            frienditem = 0;
                                            //杀死QQ后台
                                            execShellCmd("am force-stop com.tencent.mobileqq");
                                            sleepTime(1000);
                                            execShellCmd("input keyevent 3");
                                            sleepTime(1000);
                                        } else {
                                            Log.i("123", "5");
                                            sleepTime(1500);
                                            friendlisttext = 0;
                                            execSlipCmd("input swipe 360 1000 360 365");
                                            //                                            SWIPE_DOWN_AND_UPClick();
                                            sleepTime(4000);
                                        }
                                    }
                                } else {
                                    sleepTime(1000);
                                    app.setQQAllowAKeySendMessage(false);
                                    Log.i("123", isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_ELV_BUDDIES) + ":" + friendlisttext);
                                    QQfindIdAndClick(QQIDConstant.QQ_FRIEND_TEXT, friendlisttext, QQIDConstant.QQ_ELV_BUDDIES);
                                    friendlisttext++;
                                }
                            }
                        } else {
                            //                            while (!isFindQQIdText(QQ_NAME,"联系人")) {
                            //                                Log.i("123","lianxiren");
                            //                                sleepTime(500);
                            //                            }
                            //                            if (isFindQQIdText(QQ_NAME,"联系人")) {
                            //                                sleepTime(500);\
                            //                                QQfindIdTextAndClick(QQ_NAME,"联系人", 0);
                            //                                sleepTime(500);
                            //                            }
                            while (!isFindText("联系人")) {
                                Log.i("123", "lianxiren");
                                sleepTime(500);
                            }
                            if (isFindText("联系人")) {
                                sleepTime(500);
                                findTextAndClick("联系人", 0);
                                sleepTime(500);
                            }
                            //没写呢
                            if (isFindText("发现你可能认识的人")) {
                                Log.d("zzzzz", 11 + "");
                                sleepTime(1500);
                                execSlipCmd("input swipe 300 280 600 280");
                                //                                SWIPE_LEFT_AND_RIGHTClick();
                                sleepTime(4000);
                            }
                            while (!isFindText("好友")) {
                                sleepTime(500);
                            }
                            if (isFindText("好友")) {
                                sleepTime(500);
                                findTextAndClick("好友", 0);
                                sleepTime(500);
                            }
                            while (!isFindText("联系人")) {
                                sleepTime(500);
                            }
                            if (isFindText("联系人")) {
                                sleepTime(500);
                                findTextAndClick("联系人", 1);
                                sleepTime(500);
                            }

                            Log.i("123", "6");
                            if (isFindQQParentItemNum(QQIDConstant.QQ_FRIEND_ID, QQIDConstant.QQ_ELV_BUDDIES) > 0) {
                                frienditem = isFindQQParentItemNum(QQIDConstant.QQ_FRIEND_ID, QQIDConstant.QQ_ELV_BUDDIES) - 1;
                                for (int i = frienditem; i >= 0; i--) {
                                    sleepTime(500);
                                    QQfindIdAndClick(QQIDConstant.QQ_FRIEND_ID, i, QQIDConstant.QQ_ELV_BUDDIES);
                                }
                            }
                            isfirstsendmessage = true;
                            Log.i("123", "7");
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.FriendProfileCardActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        while (!isFindQQIdText("txt", "发消息")) {
                            sleepTime(500);
                        }
                        if (isFindQQIdText("txt", "发消息")) {
                            sleepTime(2500);
                            QQfindIdTextAndClick("txt", "发消息", 0);
                        }

                    } else if (event.getClassName().equals("com.tencent.qidian.QidianProfileCardActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        while (!isFindText("发消息")) {
                            sleepTime(500);
                        }
                        if (isFindText("发消息")) {
                            sleepTime(2500);
                            findTextAndClick("发消息", 0);
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.SplashActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindQQId(QQIDConstant.QQ_INPUT)) {
                            sleepTime(1000);
                            QQfindIdAndClick(QQIDConstant.QQ_INPUT, 0);
                            String str = PrefUtils.getString(getApplicationContext(), Constant.qqFunction[11], null);
                            String str1 = PrefUtils.getString(getApplicationContext(), "OneKeyDialogText", null);
                            if (str.equals("null") || TextUtils.isEmpty(str) || str == null) {
                                if (str1 == "" || TextUtils.isEmpty(str1)) {
                                    str = AKeyContent;
                                } else {
                                    str = str1;
                                }
                            }
                            //粘贴信息
                            sleepTime(1500);
                            inputHello(str);
                            sleepTime(1000);
                            QQfindIdAndClick(QQIDConstant.QQ_FUN_BTN, 0);
                        }
                        sleepTime(1000);
                        //                        execSlipCmd("input keyevent 4");
                        BackClick();
                        sleepTime(1000);
                        //                        execSlipCmd("input keyevent 4");
                        BackClick();
                        sleepTime(1000);
                        app.setQQAllowAKeySendMessage(true);
                        while (!isFindText("联系人")) {
                            sleepTime(500);
                        }
                        if (isFindText("联系人")) {
                            sleepTime(1500);
                            findTextAndClick("联系人", 0);
                            sleepTime(1500);
                        }
                    }
                }
                Log.i("123", event.getClassName().toString() + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


                //一键加群友
                if (app.isQQFriendStream()) {

                    if (grouptext == 0) {
                        grouptext = PrefUtils.getInt(QQAccessibilityService.this, "mQQGroupText", 1) - 1;
                    }
                    Log.i("123", event.getClassName().toString());

                    if (app.isAllowQQFriendStream()) {
                        Log.i("123", event.getClassName().toString() + isaddfriend);
                        if (isaddfriend) {
                            Log.i("123", "isallowqqfriend" + isgroupend);
                            if (isgroupend) {
                                while (!isFindText("联系人")) {
                                    sleepTime(500);
                                }
                                if (isFindText("联系人")) {
                                    sleepTime(500);
                                    findTextAndClick("联系人", 0);
                                    sleepTime(500);
                                }
                                isgroupend = false;
                            }
                            Log.i("123", "1" + isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW));
                            if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) > 0) {
                                if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) == grouptext) {
                                    Log.i("123", isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) + ":" + grouptext);
                                    if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) < 6) {
                                        Log.i("123", "3");
                                        app.setQQFriendStream(false);
                                        app.setAllowQQFriendStream(false);
                                        isaddfriend = false;
                                        isgroupswipe = false;
                                        isgroupsend = false;
                                        isdanger = false;
                                        groupfrienditem = 0;
                                        grouptext = 0;
                                        groupitem = 0;
                                        //杀死QQ后台
                                        execShellCmd("am force-stop com.tencent.mobileqq");
                                        sleepTime(1000);
                                        execShellCmd("input keyevent 3");
                                        sleepTime(1000);
                                    } else {
                                        Log.i("123", "equal");
                                        if (isFindQQBouns(QQIDConstant.QQ_FRIEND_TEXT, grouptext - 1, QQIDConstant.QQ_TROOP_LIST_VIEW).bottom == 1674 && isFindQQBouns(QQIDConstant.QQ_FRIEND_TEXT, grouptext - 1, QQIDConstant.QQ_TROOP_LIST_VIEW).top == 1630) {
                                            Log.i("123", "4");
                                            app.setQQFriendStream(false);
                                            app.setAllowQQFriendStream(false);
                                            isaddfriend = false;
                                            isgroupswipe = false;
                                            isgroupsend = false;
                                            isdanger = false;
                                            groupfrienditem = 0;
                                            grouptext = 0;
                                            groupitem = 0;
                                            //杀死QQ后台
                                            execShellCmd("am force-stop com.tencent.mobileqq");
                                            sleepTime(1000);
                                            execShellCmd("input keyevent 3");
                                            sleepTime(1000);
                                        } else {
                                            Log.i("123", "swipe");
                                            if (!isgroupswipe) {
                                                sleepTime(1500);
                                                Log.i("123", grouptext + ":");
                                                execSlipCmd("input swipe 600 980 600 800");
                                                //                                            SWIPE_DOWN_AND_UPClick();
                                                sleepTime(2000);
                                                isgroupswipe = true;
                                            } else {
                                                sleepTime(1000);
                                                Log.i("123", "swipeclick" + isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW));
                                                QQfindIdAndClick(QQIDConstant.QQ_FRIEND_TEXT, grouptext - 1, QQIDConstant.QQ_TROOP_LIST_VIEW);
                                                Log.i("123", "isclick");
                                                sleepTime(1000);
                                                if (isFindText("退出该群")) {
                                                    Log.i("123", "群被封");
                                                    //                                                    findTextAndClick("退出该群", 0);
                                                    QQfindIdAndClick(QQIDConstant.QQ_DIALOG_RIGHT_BTN, 0);
                                                    sleepTime(500);
                                                    isgroupend = true;

                                                } else if (isFindQQId(QQIDConstant.QQ_IVTITLEBTNRIGHTIMAGE)) {
                                                    sleepTime(500);
                                                    QQfindIdAndClick(QQIDConstant.QQ_IVTITLEBTNRIGHTIMAGE, 0);
                                                    app.setAllowQQFriendStream(false);

                                                    Log.i("123", "dianji");
                                                }
                                                isgroupswipe = false;
                                            }
                                        }
                                    }
                                } else if (isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) < grouptext) {
                                    sleepTime(1500);
                                    Log.i("123", grouptext + ":");
                                    grouptext = grouptext - 1;
                                    execSlipCmd("input swipe 600 950 600 800");
                                    //                                  SWIPE_DOWN_AND_UPClick();
                                    sleepTime(2000);
                                } else {
                                    sleepTime(1000);
                                    Log.i("123", isFindQQNum(QQIDConstant.QQ_FRIEND_TEXT, QQIDConstant.QQ_TROOP_LIST_VIEW) + ":" + grouptext);
                                    QQfindIdAndClick(QQIDConstant.QQ_FRIEND_TEXT, grouptext, QQIDConstant.QQ_TROOP_LIST_VIEW);
                                    sleepTime(1000);
                                    if (isFindText("退出该群")) {
                                        Log.i("123", "群被封");
                                        sleepTime(1000);
                                        //findTextAndClick("退出该群", 0);
                                        QQfindIdAndClick(QQIDConstant.QQ_DIALOG_RIGHT_BTN, 0);
                                        sleepTime(500);
                                        isgroupend = true;
                                    } else if (isFindQQId(QQIDConstant.QQ_IVTITLEBTNRIGHTIMAGE)) {
                                        sleepTime(500);
                                        QQfindIdAndClick(QQIDConstant.QQ_IVTITLEBTNRIGHTIMAGE, 0);
                                        app.setAllowQQFriendStream(false);
                                    }
                                    grouptext++;
                                }
                            }
                        } else {
                            while (!isFindText("联系人")) {
                                sleepTime(500);
                            }
                            if (isFindText("联系人")) {
                                sleepTime(500);
                                findTextAndClick("联系人", 0);
                                sleepTime(500);
                            }
                            if (isFindText("发现你可能认识的人")) {
                                Log.d("zzzzz", 11 + "");
                                sleepTime(1500);
                                execSlipCmd("input swipe 300 320 600 320");
                                //                                SWIPE_LEFT_AND_RIGHTClick();
                                sleepTime(4000);
                            }
                            while (!isFindText("群")) {
                                sleepTime(500);
                            }
                            if (isFindText("群")) {
                                sleepTime(500);
                                findTextAndClick("群", 0);
                                sleepTime(500);
                            }
                            while (!isFindText("联系人")) {
                                sleepTime(500);
                            }
                            if (isFindText("联系人")) {
                                sleepTime(500);
                                findTextAndClick("联系人", 1);
                                sleepTime(500);
                            }
                            if (isFindQQParentItemNum(QQIDConstant.QQ_GROUP_NAME, QQIDConstant.QQ_TROOP_LIST_VIEW) > 0) {
                                Log.i("123", "6");
                                groupitem = isFindQQParentItemNum(QQIDConstant.QQ_GROUP_NAME, QQIDConstant.QQ_TROOP_LIST_VIEW) - 1;
                                Log.i("123", groupitem + "");
                                for (int i = groupitem; i >= 0; i--) {

                                    sleepTime(500);
                                    QQfindIdAndClick(QQIDConstant.QQ_GROUP_NAME, i, QQIDConstant.QQ_TROOP_LIST_VIEW);
                                }
                            }
                            isaddfriend = true;
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.ChatSettingForTroop") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        Log.i("123", "splash");
                        if (isFindText("名成员")) ;
                        {
                            sleepTime(1000);
                            findTextAndClick("名成员", 0);
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.TroopMemberListActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        while (!isFindQQId(QQIDConstant.QQ_TV_NAME)) {
                            sleepTime(500);
                        }
                        if (isFindQQNum(QQIDConstant.QQ_TV_NAME) > 0) {
                            if (isFindQQNum(QQIDConstant.QQ_TV_NAME) == groupfrienditem) {
                                Log.i("123", isFindQQNum(QQIDConstant.QQ_TV_NAME) + ":" + groupfrienditem);
                                if (isFindQQNum(QQIDConstant.QQ_TV_NAME) < 10) {
                                    groupfrienditem = 0;
                                    BackClick();
                                    sleepTime(1000);
                                    BackClick();
                                    sleepTime(1000);
                                    BackClick();
                                    sleepTime(1000);
                                    isgroupend = true;
                                    app.setAllowQQFriendStream(true);
                                } else {
                                    if (isFindQQidBouns(QQIDConstant.QQ_TV_NAME, groupfrienditem - 1).bottom == 1093 && isFindQQidBouns(QQIDConstant.QQ_TV_NAME, groupfrienditem - 1).top == 1138) {
                                        Log.i("123", "4");
                                        groupfrienditem = 0;
                                        BackClick();
                                        sleepTime(1000);
                                        BackClick();
                                        sleepTime(1000);
                                        BackClick();
                                        sleepTime(1000);
                                        isgroupend = true;
                                        app.setAllowQQFriendStream(true);

                                    } else {
                                        Log.i("123", "5");
                                        sleepTime(1500);
                                        groupfrienditem = 0;
                                        execSlipCmd("input swipe 600 1000 600 300");
                                        //                                            SWIPE_DOWN_AND_UPClick();
                                        sleepTime(4000);
                                        QQfindIdAndClick(QQIDConstant.QQ_TV_NAME, groupfrienditem);

                                        groupfrienditem++;
                                    }
                                }
                            } else {
                                sleepTime(1000);

                                Log.i("123", isFindQQNum(QQIDConstant.QQ_TV_NAME) + ":" + groupfrienditem);
                                QQfindIdAndClick(QQIDConstant.QQ_TV_NAME, groupfrienditem);

                                groupfrienditem++;
                            }
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.TroopMemberCardActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (!isdanger) {
                            if (!isgroupsend) {
                                if (isFindText("加好友")) {
                                    findTextAndClick("加好友", 0);
                                    sleepTime(1000);

                                } else {
                                    BackClick();
                                    sleepTime(1000);
                                }
                            } else {
                                BackClick();
                                sleepTime(1000);
                                isgroupsend = false;
                            }
                        } else {
                            BackClick();
                            sleepTime(1000);
                            isgroupsend = false;
                            isdanger = false;
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.AddFriendVerifyActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindText("发送")) {
                            sleepTime(1000);
                            if (isFindText("问题")) {

                                sleepTime(1000);

                                QQfindIdAndClick(QQIDConstant.QQ_GROUP_QIUXIAO, 0);
                                sleepTime(1000);
                            } else {
                                sleepTime(2000);
                                Log.i("123", "发送...");
                                while (!isFindText("发送")) {
                                    sleepTime(500);
                                    Log.i("123", "while");
                                }
                                findTextAndClick("发送", 0);
                                sleepTime(1000);
                                Log.i("123", "发送good");
                            }
                            isgroupsend = true;
                            if (isFindText("请求发送失败")) {
                                sleepTime(1500);
                                findTextAndClick("确定", 0);
                                sleepTime(1000);
                                findTextAndClick("取消", 0);
                            }
                            if (isFindText("添加失败，对方不允许被加为好友")) {
                                sleepTime(1000);
                                findTextAndClick("确定", 0);
                                sleepTime(1000);
                                findTextAndClick("取消", 0);
                            }
                            if (isFindText("添加失败，请勿频繁操作")) {
                                Log.i("zw91", "操作频繁");
                                app.setAllowQQFriendStream(false);
                                app.setQQFriendStream(false);
                                //杀死QQ后台
                                execShellCmd("am force-stop com.tencent.mobileqq");
                                sleepTime(1000);
                                execShellCmd("input keyevent 3");
                                sleepTime(1000);
                                return;
                            }
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.QQBrowserActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        BackClick();
                        sleepTime(1000);
                        isgroupsend = false;
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.AddFriendLogicActivity") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                        sleepTime(1000);
                        Log.i("123", "风险提示1");

                        if (isFindQQId("dialogTitle")) {
                            Log.i("123", "风险提示");
                            isdanger = true;
                            sleepTime(1000);
                            findTextAndClick("取消", 0);
                        }
                    } else {
                        if (isFindQQId("dialogTitle")) {
                            Log.i("123", "风险提示");
                            isdanger = true;
                            sleepTime(1000);
                            findTextAndClick("取消", 0);
                        }
                    }
                }

                if (app.getAllowQQWhite()) {
                    if (app.getStart()) {
                        sleepTime(1000);
                        mWhiteListContentList = new WhiteListDao(QQAccessibilityService.this).queryForAll();
                        Collections.reverse(mWhiteListContentList);
                        sleepTime(1000);
                        if (isFindText("联系人")) {
                            sleepTime(1000);
                            findTextAndClick("联系人", 0);
                        }
                        if (isFindText("添加")) {
                            sleepTime(1000);
                            findTextAndClick("添加", 0);
                            app.setStart(false);
                        }
                    }
                    if (event.getClassName().equals("com.tencent.mobileqq.activity.contact.addcontact.AddContactsActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus1 = 0;
                        pageStatus = 0;
                        sleepTime(1500);
                        execShellCmd("input tap 600 315");
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.contact.addcontact.SearchContactsActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        String WhiteQQText = mWhiteListContentList.get(mWhiteNumber).getWhiteContent();
                        if (pageStatus == WHITEBACK || pageStatus1 == SENDBACK) {
                            sleepTime(1500);
                            execShellCmd("input keyevent 4");
                        } else {
                            sleepTime(1500);
                            inputHello(WhiteQQText);//输入搜索内容
                            sleepTime(1500);
                            if (isFindText("找人")) {
                                findTextAndClick("找人", 0);
                                mWhiteNumber++;
                            } else if (isFindText("没有找到相关结果")) {
                                sleepTime(1500);
                                execShellCmd("input keyevent 4");
                            }
                        }
                        if (mWhiteNumber == mWhiteListContentList.size()) {
                            app.setAllowQQWhite(false);     //将朋友圈模块开启状态变成false
                            performBackClick();     //退出
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.contact.addcontact.ClassificationSearchActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus == WHITEBACK || pageStatus1 == SENDBACK) {
                            sleepTime(1500);
                            execShellCmd("input keyevent 4");
                        } else {
                            sleepTime(1500);
                            execShellCmd("input tap 360 254");
                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.FriendProfileCardActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus1 == SENDBACK) {
                            sleepTime(1500);
                            execShellCmd("input keyevent 4");
                            // findTextAndClick("返回", 0);
                        } else {
                            sleepTime(1000);
                            QQfindIdTextAndClick("txt", "加好友", 0);
                            whitexz++;
                            // findIdAndClick("txt",0);
                            Log.e(TAG, "BBBBBBBBBBB");
                            //execShellCmd("input tap 600 1788");
                        }
                        if (whitexz == 2) {
                            sleepTime(1500);
                            findTextAndClick("返回", 0);
                            //   mWhiteNumber++;
                            pageStatus = WHITEBACK;
                            whitexz = 0;
                        }
                        //                        if (pageStatus1 == SENDBACK) {
                        //                            sleepTime(1000);
                        //                            findTextAndClick("返回", 0);
                        //                        }
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.AddFriendVerifyActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(2000);
                        if (isFindText("问题")) {
                            sleepTime(1000);
                            //execShellCmd("input keyevent 4");
                            findTextAndClick("取消", 0);
                            //  mWhiteNumber++;
                            pageStatus1 = SENDBACK;
                        } else {
                            sleepTime(1000);
                            findTextAndClick("发送", 0);
                            //   mWhiteNumber++;
                            pageStatus1 = SENDBACK;
                        }
                    }
                }
                /**
                 * QQ一键加好友
                 */
                if (app.getQQAddFriends()) {
                    int mQQAddFriendsNumText = PrefUtils.getInt(getApplicationContext(), "mQQAddFriendsText", 0);
                    //QQ主界面
                    if (event.getClassName().equals("com.tencent.mobileqq.activity.SplashActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1500);
                        findTextAndClick("联系人", 0);
                        sleepTime(1500);
                        QQfindIdTextAndClick("ivTitleBtnRightText", "添加", 0);
                    }
                    //添加界面
                    else if (event.getClassName().equals("com.tencent.mobileqq.activity.contact.addcontact.AddContactsActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        sleepTime(1500);
                        findTextAndClick("添加手机联系人", 0);
                    } else if (event.getClassName().equals("com.tencent.mobileqq.activity.phone.PhoneMatchActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (isFindText("匹配手机通讯录")) {
                            findTextAndClick("匹配手机通讯录", 1);
                            sleepTime(8000);
                        }
                    }
                    //手机通讯录界面
                    else if (event.getClassName().equals("com.tencent.mobileqq.activity.ContactBindedActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = 1;
                        mQQClickAddFriendsBtnNum = 0;
                        if (mQQClickPhoneListNum >= 7) {
                            //滑页操作
                            sleepTime(1000);
                            execSlipCmd("input swipe 360 1000 360 300");
                            sleepTime(5000);
                            mQQClickPhoneListNum = 0;
                        }
                        sleepTime(1500);
                        QQfindIdAndClick(QQIDConstant.QQ_PHONE_LIST, mQQClickPhoneListNum);
                        mQQClickPhoneListNum++;
                    }
                    //详细资料界面
                    else if (event.getClassName().equals("com.tencent.mobileqq.activity.FriendProfileCardActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (pageStatus == 1) {
                            sleepTime(1500);
                            //                            findTextAndClick("加好友", 0);
                            QQfindIdAndClick(QQIDConstant.QQ_ADD_FRIENDS_BUTTON, 0);
                            if (isFindText("风险提示")) {
                                findTextAndClick("继续", 0);
                                sleepTime(1000);
                            }

                            mQQClickAddFriendsBtnNum++;
                            mQQAddFriendsTotalNum++;
                            if (mQQClickAddFriendsBtnNum == 3 || mQQAddFriendsTotalNum >= mQQAddFriendsNumText) {
                                //退出服务
                                mQQClickPhoneListNum = 0;
                                mQQClickAddFriendsBtnNum = 0;
                                mQQAddFriendsTotalNum = 0;
                                performBackClick();
                                MyToast.show(QQAccessibilityService.this, "服务已完结");
                            }
                        } else if (pageStatus == 2) {//添加好友过来的
                            findTextAndClick("返回", 0);
                        }
                    }
                    //添加好友界面
                    else if (event.getClassName().equals("com.tencent.mobileqq.activity.AddFriendVerifyActivity")
                            && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        pageStatus = 2;
                        if (isFindQQId("textView1")) {
                            sleepTime(1500);
                            findTextAndClick("取消", 0);
                        } else {
                            sleepTime(1500);
                            QQfindIdAndClick(QQIDConstant.QQ_ADD_FRIENDS_SEND, 0);
                            sleepTime(1000);
                        }
                        if (isFindText("请求发送失败")) {
                            sleepTime(1500);
                            findTextAndClick("确定", 0);
                            sleepTime(1000);
                            findTextAndClick("取消", 0);
                        }
                        if (isFindText("添加失败，对方不允许被加为好友")) {
                            sleepTime(1000);
                            findTextAndClick("确定", 0);
                        }
                        if (isFindText("添加失败，请勿频繁操作")) {
                            Log.i("zw91", "操作频繁");
                            app.setAllowQQFriendStream(false);
                            app.setQQFriendStream(false);

                            //杀死QQ后台
                            execShellCmd("am force-stop com.tencent.mobileqq");
                            sleepTime(1000);
                            execShellCmd("input keyevent 3");
                            sleepTime(1000);
                            return;
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onInterrupt() {
        super.onInterrupt();
    }

    /**
     * 跳转QQ主界面
     */
    public void intentQq() {
        Intent qqIntent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
        qqIntent.setAction(Intent.ACTION_MAIN);
        qqIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        qqIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        qqIntent.setComponent(cmp);
        startActivity(qqIntent);
    }


    /**
     * 切换地点
     */

    private void ChangeLocation() {
        mMapSearchBeanList = new MyApplication().mAllAddressList;
        if (mMapSearchBeanList.size() <= index) {
            app.setQQNearbyPeople(false);
            app.setAllowQQNearbyPeople(false);
            QQfindIdAndClick(QQIDConstant.QQ_BACK, 0);
            sleepTime(1500);
            performBackClick();
            index = 0;
            MyToast.show(QQAccessibilityService.this, "服务已完结");
        } else {
            app.setQQNearbyPeople(false);     //将附近人模块开启状态变成false
            startThread();
        }
    }

    /**
     * 线程跳转QQ
     */
    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //先执行杀掉微信后台操作
                //先执行杀掉QQ后台操作
                execShellCmd("am force-stop com.tencent.mobileqq");
                execShellCmd("input keyevent 3");
                startMock(mMapSearchBeanList.get(index).getLatitudes(), mMapSearchBeanList.get(index).getLongtitudes(), mMapSearchBeanList.get(index).getLac(), mMapSearchBeanList.get(index).getCid(), mMapSearchBeanList.get(index).getMnc());
                Log.i("456", mMapSearchBeanList.get(index).getLatitudes() + "  " + mMapSearchBeanList.get(index).getLongtitudes() + "  " + mMapSearchBeanList.get(index).getLac() + "  " + mMapSearchBeanList.get(index).getCid());
                app.setAllowQQNearbyPeople(true);
                app.setQQNearbyPeople(true);
                //app.setAllowQQDianZan(true);
                //app.setAllowQQDialogCancel(true);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intentQq(); //跳转QQ主界面
            }
        }).start();
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
        contentValues.put("package_name", "com.tencent.mobileqq");
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("lac", lac);
        contentValues.put("cid", cid);
        contentValues.put("mnc", mnc);

        //将集合里的参数插入到数据库
        mSQLiteDatabase.insertWithOnConflict(PxDBHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
