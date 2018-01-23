package vicmob.micropowder.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;

import vicmob.micropowder.config.IDConstant;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.daoman.PxDBHelper;

/**
 * Created by admin on 2017/7/20.
 */
public class OnekeyService extends AccessibilityService {
    private int FriendCircleTime = 10000;//朋友圈点赞时间延迟
    private int BottleTime = 3000;//漂流瓶时间延迟
    private int FriendTime = 10000;//发朋友圈时间延迟
    private int GroupChatTime = 10000;//发消息时间延迟
    private int NearbyTime = 6000;//发消息时间延迟

    private int mNearNum = 2;//人数
    private int FriendCircleNum = 6; //朋友圈点赞  数量
    private int DriftBottleNum = 2;  //漂流瓶
    private int StartFriendNum =1; //发朋友圈

    /**
     * 全局
     */
    private MyApplication app;
    public static final String TAG = "test";
    public AccessibilityNodeInfo node = null;
    private String hello = "你好";//附近的人
    private String hellocle = "你好";//附近的人
    //先随机产生一个下标再获取元素
    private String[] doc = {"我们这些年轻人，盲，忙，茫", "生活，终归会有些无奈", "愿时光能缓、愿故人不散", "此图无关", "摔倒了爬起来就好。", "虽然过去不能改变，未来可以。", "有能力的人影响别人，没能力的人，受人影响。", "改变是一种力量。", "混的意思就是:日子过得比流水快。",
            "我以为我很颓废，今天我才知道，原来我早报废了。", "当幸福来敲门的时候，我怕我不在家，所以一直都很宅。", "阳光温热，岁月静好，你还不来，我怎敢老去？"};
    private String[] circle = {"你好", "厉害了", "打扰了"};
    //纬度数组
    private double[] latitude = {32.05000, 31.19, 34.50000, 39.13333, 30.26667, 28.23, 30.67, 26.65};
    //经度数组
    private double[] longitude = {118.78333, 120.37, 116.41667, 117.20000, 120.20000, 112.93, 104.07, 106.63};
    private String frinend = "Hello 6月";//朋友圈
    private String bottle = "Hello!";//漂流瓶
    private String chat = "Hello !";//聊天
    private int i = 0;//记录已打招呼的人数
    private int page = 1;
    private int addpage = 1;//添加通讯录页面标记
    private int prepos = -1;//记录页面跳转来源，0--从附近的人页面跳转到详细资料页，1--从打招呼页面跳转到详细资料页
    public static final int LAUNCHER_UI = 1;
    public static final int CHAT_LAST = 2;

    private int pageStatus1 = 1;
    private int pageStatus = 0;
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
     * 当前第几个地点
     */
    public static int index = 0;

    /**
     * 当前附近的访问总人数
     */
    private int mNearbySumNumber = 0;
    /**
     * 当前附近人访问的位置
     */
    private int mNearbyNumber = 0;
    /**
     * 群成员计数
     */

    private int inside1 = 0;
    private int inside2 = 72;
    /**
     * 详细信息
     */
    public static final int GF_INFO = 3;

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
    /**
     * 数据库
     */
    public static SQLiteDatabase mSQLiteDatabase;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        app = (MyApplication) getApplication();
        int eventType = event.getEventType();
        if (event.getSource() != null) {

            /**
             * 朋友圈点赞
             */
            if (app.getFriendCircle()) {
                //获取需要评论的人数
                if (app.getStart()) {
                    normalMode(1000);
                    /**
                     * 主界面
                     */
                    //如果是主界面，并找到发现按钮
                    if (isFindText("发现")) {
                        normalMode(1000);
                        findTextAndClick("发现", 0);  //点击发现
                    }
                    if (isFindText("朋友圈")) {
                        normalMode(1000);
                        findTextAndClick("朋友圈", 0);  //点击朋友圈
                        app.setStart(false);
                    }
                }
                /**
                 *朋友圈界面
                 */
                if (event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    for (int i = 0; i < FriendCircleNum; i++) {
                        if (isFindIdlistnum(IDConstant.THUMBS_UP) > 0) {
                            if (isFindIdlistnum(IDConstant.THUMBS_UP) >= 2) {
                                findAndClick(IDConstant.THUMBS_UP, 1);
                                normalMode(1500);
                                //判断是否已经被点赞
                                if (!isFindText("取消") && isFindId(IDConstant.WX_DIANZAN)) {
                                    //findIdAndClick(IDConstant.WX_DIANZAN, 0);
                                    //findTextAndClick("赞", 0);
                                    WXfindIdTextAndClick(IDConstant.WX_DIANZAN, "赞", 0);
                                    normalMode(1500);
                                    //点赞操作
                                    findAndClick(IDConstant.THUMBS_UP, 1);
                                }
                                normalMode(2000);
                            } else {
                                findAndClick(IDConstant.THUMBS_UP, 0);
                                normalMode(1500);
                                //判断是否已经被点赞
                                if (!isFindText("取消") && isFindId(IDConstant.WX_DIANZAN)) {
                                    //findIdAndClick(IDConstant.WX_DIANZAN, 0);
                                    //findTextAndClick("赞", 0);
                                    WXfindIdTextAndClick(IDConstant.WX_DIANZAN, "赞", 0);
                                    normalMode(1500);
                                    //点赞操作
                                    findAndClick(IDConstant.THUMBS_UP, 0);
                                }
                                normalMode(2000);
                            }
                            Log.i("123", "滑动");
                            //翻页操作
                            execShellCmd("input swipe 400 1100 400 700");
                            normalMode(2000);
                        } else {
                            //翻页操作
                            execShellCmd("input swipe 400 1100 400 700");
                            normalMode(2000);
                        }
//                        int index = (int) (Math.random() * circle.length);
//                        hellocle = circle[index];
                        //                      normalMode(2000);
                        //点赞操作
                        //                     findAndClick(IDConstant.THUMBS_UP, 0);
                        //                     normalMode(1500);
                        //判断是否已经被点赞
                        //                     if (!isFindText("取消")) {
                        //                          findTextAndClick("赞", 0);
                        //                          normalMode(1500);
                        //点赞操作
                        //                         findAndClick(IDConstant.THUMBS_UP, 0);
                        //                     }
                        //                     normalMode(2000);
                        //                      //findTextAndClick("评论", 0);
//                        findAndClick(IDConstant.COMMENT, 0);
//                        normalMode(1500);
//                        findAndClick(IDConstant.COMMENT_CONTENT, 0);
//                        //评论操作
//                        inputHello(hellocle);
//                        // paste(IDConstant.COMMENT_CONTENT, 0);
//                        normalMode(1500);
//                        findTextAndClick("发送", 0);
//                        normalMode(1500);
                        //翻页操作
//                        execShellCmd("input swipe 350 1220 350 760");
                        //                      normalMode(2000);

                    }

                    performBackClick(); //退出
                    Toast.makeText(OnekeyService.this, "朋友圈点赞服务已完结", Toast.LENGTH_SHORT).show();
                    execShellCmd("am force-stop com.tencent.mm");
                    app.setFriendCircle(false);     //将朋友圈模块开启状态变成false
                    normalMode(BottleTime);
                    app.setDriftBottle(true);
                    app.setStart(true);
                    openApp();
                    normalMode(8000);
                }
            }
            //漂流瓶
            if (app.getDriftBottle()) {
                if (app.getStart()) {
                    //如果是主界面，并找到发现按钮
                    if (isFindText("发现")) {
                        normalMode(800);
                        findTextAndClick("发现", 0);  //点击发现
                        normalMode(800);
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
                                findAndClick(IDConstant.BOTTLE_GLOBAL, 4);  //点击发现
                            }
                            if (isFindId(IDConstant.BOTTLE_GLOBAL)) {
                                normalMode(800);
                                findAndClick(IDConstant.BOTTLE_GLOBAL, 5);  //点击发现
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
                            }

                        }
                    }
                    app.setStart(false);
                }
                if (event.getClassName().equals("com.tencent.mm.plugin.bottle.ui.BottleBeachUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    copyToBoard("hellow 你好");
                    int m = 0;
                    while (m < DriftBottleNum) {
                        normalMode(1500);
                        if (isFindText("扔一个")) {
                            normalMode(1000);
                            openNext("扔一个");
                        }
                        normalMode(1500);
                        findAndClick(IDConstant.WX_ChangeText, 0);//键盘
//                            execShellCmd("input tap 70 1780");
                        normalMode(1500);
                        paste(IDConstant.BOTTLE_CONTENT, 0);
                        normalMode(1500);
                        execShellCmd("input keyevent 4");
                        normalMode(1500);
                        // findAndClick("tf",0);//扔出去
                        execShellCmd("input tap 430 1230");
                        normalMode(6500);
                        m++;
                    }

                    performBackClick();
                    Toast.makeText(OnekeyService.this, "漂流瓶服务已完结", Toast.LENGTH_SHORT).show();
                    app.setDriftBottle(false);
                    execShellCmd("am force-stop com.tencent.mm");
                    normalMode(FriendTime);
                    app.setStartFriend(true);
                    app.setStart(true);
                    openApp();
                }
            }
            //发朋友圈
            if (app.getStartFriend()) {
                if (app.getStart()) {
                    //如果是主界面，并找到发现按钮
                    if (isFindText("发现")) {
                        normalMode(800);
                        findTextAndClick("发现", 0);  //点击发现
                    }
                    if (isFindText("朋友圈")) {
                        normalMode(800);
                        findTextAndClick("朋友圈", 0);  //点击朋友圈
                        app.setStart(false);
                    }
                }
                if (event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    if (i < StartFriendNum) {
                        normalMode(2500);
                        if (isFindId(IDConstant.GC_PIC)) {
                            normalMode(800);
                            findAndLongClick(IDConstant.GC_PIC, 0);
                        }
                    }

//                        if (i < 1) {
//                            normalMode(2500);
//                            findAndClick(IDConstant.GC_PIC, 0);//点击选择照片
//                            normalMode(2000);
//                            if (isFindText("从相册选择")) {
//                                findTextAndClick("从相册选择", 0);
//                            }
//                            normalMode(2000);
//                            if (isFindText("我知道了")) {
//                                findTextAndClick("我知道了", 0);
//                            }
//                        }
                    else {
                        Toast.makeText(OnekeyService.this, "服务已完结", Toast.LENGTH_SHORT).show();
                        performBackClick();//退出
                        app.setStartFriend(false);
                        execShellCmd("am force-stop com.tencent.mm");
                        normalMode(GroupChatTime);
                        app.setGroupFriendChat(true);
                        app.setStart(true);
                        openApp();
                    }
                } else if (event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsLongMsgUI") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    normalMode(2000);
                    if (isFindText("我知道了")) {
                        findTextAndClick("我知道了", 0);
                    }

                } else if (event.getClassName().equals("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    int number = (int) (1 + Math.random() * 6);
//                    normalMode(2000);
//                    execShellCmd("input swipe 600 1735 600 1000");
                    normalMode(2500);
                    findAndClick(IDConstant.GC_PICSELECT, number);
                    normalMode(1000);
                    findAndClick(IDConstant.GC_CONFIRM, 0);
                } else if (event.getClassName().equals("com.tencent.mm.plugin.sns.ui.SnsUploadUI") && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    //先随机产生一个下标再获取元素

                    int index = (int) (Math.random() * doc.length);
                    frinend = doc[index];
                    normalMode(1500);
                    if (isFindId(IDConstant.GC_INPUTBOX)) {
                        findAndClick(IDConstant.GC_INPUTBOX, 0);
                    }
                    normalMode(1500);
                    inputHello(frinend);
                    normalMode(1500);
                    if (isFindId(IDConstant.GC_CONFIRM)) {
                        findAndClick(IDConstant.GC_CONFIRM, 0);
                        i++;
                    }
                }
            }
            /**
             * 群友聊天
             */
            if (app.getGroupFriendChat()) {
                /**
                 * 主界面
                 */
                if (app.getStart()) {

                    normalMode(1000);
                    pageStatus = LAUNCHER_UI;
                    pageStatus1 = LAUNCHER_UI;   
                    //如果是主界面，并找到发现按钮
                    if (isFindText("通讯录")) {
                        normalMode(1000);
                        findTextAndClick("通讯录", 0);  //点击发现
                    }
                    if (isFindText("群聊")) {
                        normalMode(1000);
                        findTextAndClick("群聊", 0);  //点击附近的人
                        app.setStart(false);
                        normalMode(1000);
                    }
                }
                /**
                 * 群list界面
                 */
                if (event.getClassName().equals("com.tencent.mm.ui.contact.ChatroomContactUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
//                    int mGroupText = 1;
//
//                    if (mGroupText > out) {
//                        out = mGroupText - 1;
//                    }
                    //获取群list个数并点击
                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId
                            ("com.tencent.mm:id/" + IDConstant.GR_LiST_IMG);
                    // stopi = 1;
                    //list.size() - 1;
                    if (out < list.size()) {
                        list.get(out).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        list.get(out).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        normalMode(1000);
                    }
                    /**
                     * 群聊界面
                     */
                } else if (event.getClassName().equals("com.tencent.mm.ui.chatting.ChattingUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                    if (pageStatus == LAUNCHER_UI) {  //向内执行

                        execShellCmd("input tap 662 100");
                    }//右上角图标
                    if (pageStatus1 == GF_TEST) {   //向外执行
                        normalMode(2500);
                        findAndClick(IDConstant.GR_INPUT, 0);
                        // execShellCmd("input tap 600 1785");
                        normalMode(1500);
                        inputHello("测试，打扰了- -");
                        normalMode(1000);
                        findAndClick(IDConstant.GR_INSEND, 0);
                        normalMode(1500);
//                        execShellCmd("input keyevent 4");
//                        normalMode(1500);
//                        execShellCmd("input keyevent 4");
                        findAndClick(IDConstant.GR_INBACK, 0);
                        app.setStart(true);
                        normalMode(1500);
                    }
                    if (pageStatus1 == GF_LAST) {
                        execShellCmd("input keyevent 4");
                        app.setStart(true);
                    }
                    /**
                     * 聊天信息界面
                     */
                } else if (event.getClassName().equals("com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                    pageStatus = GF_INFO;//页面赋值

                    if (pageStatus1 == LAUNCHER_UI) {  //好友遍历并点击
                        if (isFindText("查看全部群成员")) {
                            findTextAndClick("查看全部群成员", 0);
                        } else {
                            final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.GR_MEMBER_IMG);
                            /**
                             * inside < list.size()
                             * **/
                            if (inside < 3) {
                                list.get(inside).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                list.get(inside).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            } else {
                                app.setGroupFriendChat(false);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        app.setNearbyPeople(true);
                                        //先执行杀掉微信后台操作
                                        execShellCmd("am force-stop com.tencent.mm");
                                        execShellCmd("input keyevent 3");
                                        app.setNearbyPeople(true);
                                        app.setStart(true);
                                        try {
                                            Thread.sleep(6000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        normalMode(NearbyTime);
                                        openApp(); //跳转微信主界面
                                    }
                                }).start();
                                normalMode(5000);

                            }

                        }
                    } else if (pageStatus1 == GF_LAST) {  //第一个群执行结束初始化下一个
                        out++;
                        inside = 0;
                        inside1 = 0;
                        execShellCmd("input keyevent 4");
                        normalMode(1500);
                    }


                    /**
                     * 聊天成员
                     */
                } else if (event.getClassName().equals("com.tencent.mm.plugin.chatroom.ui.SeeRoomMemberUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    pageStatus = GF_MCHAT;
                    if (pageStatus1 == GF_LAST) {
                        execShellCmd("input keyevent 4");
                    } else {
                        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + IDConstant.GR_MEMBER_IMG1);
                        if (inside1 < 72) {
                            list.get(inside1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            list.get(inside1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            normalMode(1500);
                            inside1++;
                        } else {
                            if (inside2 > 71) {
                                normalMode(1500);
                                execShellCmd("input swipe 600 1700 600 1560");
                                inside2 = 64;
                            }
                            normalMode(1500);
                            findAndClick(IDConstant.GR_MEMBER_IMG1, inside2);
                            inside2++;
                            normalMode(1500);
                            //      findIdAndClick(IDConstant.GR_MEMBER_IMG1,inside1);

                        }
                    }
                }
                /**
                 * 详细资料界面
                 */
                else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                    if (pageStatus == GF_TEST) {    //向外执行
                        normalMode(1500);
                        findAndClick(IDConstant.GR_Back, 0);
                    } else if (pageStatus == GF_INFO || pageStatus == GF_MCHAT) {
                        if (isFindText("发消息")) {
                            pageStatus1 = GF_TEST;
                            normalMode(1500);
                            findTextAndClick("发消息", 0);
                            inside++;

                        } else {
                            normalMode(2000);
                            execShellCmd("input keyevent 4");
                            inside++;
                        }
                        if (isFindId(IDConstant.GR_AFFIRM)) {
                            normalMode(1500);
                            findAndClick(IDConstant.GR_AFFIRM, 0);
                            normalMode(1500);
                            findAndClick(IDConstant.GR_Back, 0);
                            pageStatus1 = LAUNCHER_UI;
                            inside++;
                        }
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
                        app.setGroupFriendChat(false);
                        //将附近人模块开启状态变成false
//                        int local = (int) (Math.random() * latitude.length);
//                        final double weidu = latitude[local];
//                        final double jindu = longitude[local];
//                        performBackClick();     //退出
//                        execShellCmd("am force-stop com.tencent.mm");
//                        //TODO 切换账号
//                        Toast.makeText(OnekeyService.this, "服务已完结", Toast.LENGTH_SHORT);
//                        normalMode(NearbyTime);
//                        startMock(120.20000,30.26667);
//                        app.setStartNearby(true);
//                        app.setStart(true);
//                        openApp();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                app.setNearbyPeople(true);
                                //先执行杀掉微信后台操作
                                execShellCmd("am force-stop com.tencent.mm");
                                execShellCmd("input keyevent 3");
//                                startMock(weidu, jindu);
//                                //startMock(31.23, 121.48);
                                app.setNearbyPeople(true);
                                app.setStart(true);
                                try {
                                    Thread.sleep(6000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                openApp(); //跳转微信主界面
                            }
                        }).start();
                        normalMode(3000);

                    }
                }
                /**
                 * 验证申请界面
                 */
                else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    normalMode(1500);
                    findAndClick(IDConstant.GR_SEND, 0);
                    inside++;
                    pageStatus = GF_TEST;
                }
            }
            /**
             * 附近人功能 01
             */
            if (app.getNearbyPeople()) {
                /**
                 * 主界面
                 */

                if (app.getStart()) {
                    normalMode(1000);

                    copyToBoard("hellow ！");

                    //如果是主界面，并找到发现按钮
                    if (isFindText("发现")) {
                        normalMode(1000);
                        findTextAndClick("发现", 0);  //点击发现
                    }
                    if (isFindText("附近的人")) {
                        normalMode(1000);
                        findTextAndClick("附近的人", 0);  //点击附近的人
                        mNearbyNumber = 0;
                        app.setStart(false);
                    }

                }
                /**
                 * 附近人界面
                 */
                if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    pageStatus = NEARBY_FRIENDS_UI;     //当前在附近人界面
                    int local = (int) (Math.random() * latitude.length);
                    double weidu1 = latitude[local];
                    double jindu1 = longitude[local];
                    startMock(weidu1, jindu1);

                    findAndClick(IDConstant.NEARBY_LIST_VIEW, mNearbyNumber);
                    mNearbyNumber++;
                    if (mNearbyNumber == mNearNum) {
                        performBackClick(); //退出
                        app.setNearbyPeople(false);//将附近人模块开启状态变成false
                        app.setStart(false);
                    }
                    //当浏览完毕退出，并将附近人模块开启状态变成false

                } else if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendsIntroUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {//第一次进入附近的人
                    //第一次打开微信附近的人会出现开始查看
                    findTextAndClick("开始查看", 0);
                    normalMode(1000);
                    // 下次不提示
                    findAndClick(IDConstant.WX_NO_NEXT, 0);
                    normalMode(1000);
                    // 确定
                    findAndClick(IDConstant.WX_NO_NEXT_YES, 0);

                } else if (event.getClassName().equals("com.tencent.mm.plugin.nearby.ui.NearbyFriendShowSayHiUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    if (isFindText("查看附近的人")) {
                        findTextAndClick("查看附近的人", 0);
                    }

                    /**
                     * 详细资料界面
                     */
                } else if (event.getClassName().equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                    if (pageStatus == SAY_HI_EDIT_UI) { //从打招呼界面跳转来的或者是遇到发消息，则点击返回到附近的人页面
                        normalMode(1000);
                        // 返回
                        findAndClick(IDConstant.WX_BACK, 0);
                    } else if (pageStatus == NEARBY_FRIENDS_UI) {   //从附近的人跳转来的，则点击打招呼按钮
                        normalMode(1000);
                        if (isFindId(IDConstant.WX_SEND_MESSAGE)) {
                            // 返回
                            findAndClick(IDConstant.WX_BACK, 0);
                        }
                        findAndClick(IDConstant.WX_HELLO_BUTTON, 0);
                    }

                    /**
                     * 打招呼界面
                     */
                } else if (event.getClassName().equals("com.tencent.mm.ui.contact.SayHiEditUI")
                        && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                    pageStatus = SAY_HI_EDIT_UI;    //当前在打招呼界面
                    normalMode(1000);
                    paste(IDConstant.SAY_HELLO_EDIT_TEXT, 0);   // 粘贴进剪切板
                    normalMode(1000);
                    findAndClick(IDConstant.WX_HELLO_SEND_BUTTON, 0); // 点击发送
                }

            }
        }
    }


    /**
     * 切换地点
     */

    private void ChangeLocation() {
//        mMapSearchDao = new MapSearchDao(getApplicationContext());
//        mMapSearchBeanList = mMapSearchDao.queryForAll();

        int stop = 1;
        if (stop == index) {

            performBackClick(); //退出
            app.setNearbyPeople(false);//将附近人模块开启状态变成false
            app.setStart(false);
//        } else {
//
//            execShellCmd("input keyevent 4");
//            app.setNearbyPeople(false);     //将附近人模块开启状态变成false
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    app.setStartNearby(true);
//                    //先执行杀掉微信后台操作
//                    execShellCmd("am force-stop com.tencent.mm");
//                    execShellCmd("input keyevent 3");
//                    //startMock(31.23, 121.48);
//                    app.setStartNearby(true);
//                    app.setStart(true);
//                    try {
//                        Thread.sleep(6000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    openApp(); //跳转微信主界面
//                }
//            }).start();

//            startThread();
        }
    }

    /**
     * 线程跳转微信
     */
    private void startThread() {
        //先执行杀掉微信后台操作
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execShellCmd("am force-stop com.tencent.mm");
        execShellCmd("input keyevent 3");

        //  startMock(23.10, 113.45);
        app.setNearbyPeople(true);  //开启附近人模块
        app.setStart(true);
        try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openApp(); //跳转微信主界面

    }

    /**
     * 跳转微信主界面
     */
    public void openApp() {
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
     * 复制方法
     *
     * @param string
     */
    public void copyToBoard(String string) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", string);
        cm.setPrimaryClip(clipData);
    }

    /**
     * 粘贴
     *
     * @param id
     * @param i
     */
    public void paste(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        try {
            if (node == null) {
                List<AccessibilityNodeInfo> list = nodeInfo
                        .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
                if (list.size() > 0)
                    node = list.get(i);
            }
            targetNode = node;
            if (targetNode != null) {
                targetNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                targetNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 模拟点击文字按钮事件
     *
     * @param text
     * @param i
     */
    @SuppressLint("NewApi")
    public void findTextAndClick(String text, int i) {
        // 查找当前窗口中包含“xx”文字的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByText(text);
            if (list.size() > 0) {
                node = list.get(i);
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performClick(n);
        }
    }

    /**
     * 判断是否找到某个ID
     *
     * @param id
     * @return
     */
    public boolean isFindId(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击匹配的nodeInfo
     *
     * @param str text关键字
     */
    public void openNext(String str) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.d(TAG, "rootWindow为空");
            Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(str);
        if (list != null && list.size() > 0) {
            list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            list.get(list.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
//        else {
//           Toast.makeText(this, "找不到有效的节点", Toast.LENGTH_SHORT).show();
//
//        }
    }


    //延迟打开界面
    private void openDelay(final int delaytime, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                }
                openNext(text);
            }
        }).start();
    }

    //自动输入打招呼内容
    public void inputHello(String hello) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        //找到当前获取焦点的view
        AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            Log.d(TAG, "inputHello: null");
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", hello);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    /**
     * 执行全局返回事件
     */
    public void performBackClick() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GLOBAL_ACTION_HOME);
    }

    /**
     * 模拟物理点击方法
     *
     * @param cmd
     */
    public static void execShellCmd(String cmd) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 判断是否找到某个ID
     *
     * @param id
     * @return
     */
    public boolean find(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * ID点击
     *
     * @param id
     * @return
     */
    public void findAndClick(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                node = list.get(i);
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performClick(n);
        }
    }

    /**
     * 执行具体的点击
     *
     * @param nodeInfo
     */
    public void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * ID点击
     *
     * @param id
     * @return
     */
    public void findAndLongClick(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                node = list.get(i);
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performLongClick(n);
        }
    }

    /**
     * 执行具体的点击
     *
     * @param nodeInfo
     */
    public void performLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } else {
            performLongClick(nodeInfo.getParent());
        }
    }

    /**
     * 判断是否找到某个文本
     *
     * @param text
     * @return
     */
    public boolean isFindText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
            if (list.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 普通模式
     *
     * @param millis
     */
    public void normalMode(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用开始模拟的方法
     *
     * @param latitude
     * @param longitude
     */
    public void startMock(Double latitude, Double longitude) {

        mSQLiteDatabase = new PxDBHelper(this).getWritableDatabase();
        //创建一个集合
        ContentValues contentValues = new ContentValues();
        contentValues.put("package_name", "com.tencent.mm");
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("lac", 0);
        contentValues.put("cid", 0);
        //将集合里的参数插入到数据库
        mSQLiteDatabase.insertWithOnConflict(PxDBHelper.APP_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 判断是否找到某个ID得到list数量
     *
     * @param id
     * @return
     */
    public int isFindIdlistnum(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                return list.size();
            }
        }
        return 0;
    }

    /*****
     * 根据id和text一起判断点击
     */
    public void WXfindIdTextAndClick(String id, String text, int i) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        AccessibilityNodeInfo targetNode1 = null;
        node = null;
        if (targetNode == null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list.size() > 0) {
                targetNode = list.get(i);
                Log.i("890", targetNode + "node");
                if (targetNode.getText().equals(text)) {
                    node = targetNode;
                }
            }
        }
        targetNode1 = node;

        if (targetNode1 != null) {
            final AccessibilityNodeInfo n = targetNode1;
            performClick(n);
            Log.i("890", n + "node");
        }
    }
}
