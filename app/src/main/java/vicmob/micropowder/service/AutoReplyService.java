package vicmob.micropowder.service;


import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.config.IDConstant;
import vicmob.micropowder.daoman.bean.AutoReplyBean;
import vicmob.micropowder.daoman.dao.AutoReplyDao;

/**
 * 自动回复服务
 */

public class AutoReplyService extends AccessibilityService {
    private final static String MM_PNAME = "com.tencent.mm";
    boolean hasAction = false;
    boolean locked = false;
    boolean background = false;
    // 由通知消息得到的姓名和内容
    private String name;
    private String scontent;
    // 我们要回复的消息
    private String mcontent;
    /**
     * 存放关键词的集合
     */
    private List mKeyWordList = new ArrayList();
    /**
     * 存放回复内容的集合
     */
    private List mContentList = new ArrayList();

    AccessibilityNodeInfo itemNodeinfo;
    @SuppressWarnings("deprecation")
    private KeyguardManager.KeyguardLock kl;
    private Handler handler = new Handler();
    public AccessibilityNodeInfo node = null;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

    }

    /**
     * 必须重写的方法，响应各种事件。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {


        //查询数据
        AutoReplyDao mAutoReplyDao = new AutoReplyDao(AutoReplyService.this);
        List<AutoReplyBean> mAutoReplyBeenList = mAutoReplyDao.queryForAll();
        for (int i = 0; i < mAutoReplyBeenList.size(); i++) {
            mKeyWordList.add(mAutoReplyBeenList.get(i).getKeyWord());
            mContentList.add(mAutoReplyBeenList.get(i).getContent());
        }

        int eventType = event.getEventType();
        android.util.Log.d("maptrix", "get event = " + eventType);
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                android.util.Log.d("maptrix", "get notification event");
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (!TextUtils.isEmpty(content)) {
                            if (isScreenLocked()) {// 系统锁屏状态
                                locked = true;
                                wakeAndUnlock();
                                sleepTime(500);
                                android.util.Log.d("maptrix", "the screen is locked");
                                if (isAppForeground(MM_PNAME)) {
                                    background = false;
                                    android.util.Log.d("maptrix", "is mm in foreground");
                                    sendNotifacationReply(event);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendNotifacationReply(event);
                                            if (fill()) {
                                                send();
                                            }
                                        }
                                    }, 2500);
                                } else {
                                    background = true;
                                    android.util.Log.d("maptrix", "is mm in background");
                                    sendNotifacationReply(event);
                                }
                            } else {// 系统不处于锁屏状态
                                locked = false;
                                android.util.Log.d("maptrix", "the screen is unlocked");
                                // 监听到微信notification，打开通知
                                if (isAppForeground(MM_PNAME)) {
                                    background = false;
                                    android.util.Log.d("maptrix", "is mm in foreground");

                                    sendNotifacationReply(event);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (fill()) {
                                                send();
                                            }
                                        }
                                    }, 2000);
                                } else {
                                    background = true;
                                    android.util.Log.d("maptrix", "is mm in background");
                                    sendNotifacationReply(event);
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://微信界面事件
                android.util.Log.d("maptrix", "get type window down event");
                if (!hasAction)
                    break;
                android.util.Log.d("maptrix", "get type window down event");
                itemNodeinfo = null;
                String className = event.getClassName().toString();
                android.util.Log.d("maptrix", className);
                if (className.equals("com.tencent.mm.ui.LauncherUI")) {
                    android.util.Log.d("maptrix", "get type window down event");
                    if (fill()) {
                        send();
                    } else {
                        if (itemNodeinfo != null) {
                            itemNodeinfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fill()) {
                                        send();
                                    }
                                    back2Home();
                                    release();
                                    hasAction = false;
                                }
                            }, 1000);
                            break;
                        }
                    }
                }
                bring2Front();
                back2Home();
                release();
                hasAction = false;
                break;

        }
    }
    /**
     * 模拟点击id事件
     *
     * @param id
     * @param i
     */
    public void findIdAndClick(String id, int i) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;

        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
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
     * 寻找窗体中的“发送”按钮，并且点击。
     */
    @SuppressLint("NewApi")
    private void send() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("发送");
            if (list != null && list.size() > 0) {
                for (AccessibilityNodeInfo n : list) {
                    n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            } else {
                List<AccessibilityNodeInfo> liste = nodeInfo.findAccessibilityNodeInfosByText("Send");
                if (liste != null && liste.size() > 0) {
                    for (AccessibilityNodeInfo n : liste) {
                        n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
            //延迟返回
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            pressBackButton();
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
     * 模拟back按键
     */

    private void pressBackButton() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param event
     */
    private void sendNotifacationReply(AccessibilityEvent event) {
        hasAction = true;
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            String content = notification.tickerText.toString();
            String[] cc = content.split(":");
            // 获取通知栏中消息
            name = cc[0].trim();
            scontent = cc[1].trim();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查找关键词，并设置回复内容
     *
     * @return
     */
    @SuppressLint("NewApi")
    private boolean fill() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            contentResponse(scontent);
            return findEditText(rootNode, mcontent);
        }
        return false;
    }

    /**
     * 根据关键词回复内容
     *
     * @param scontent
     */
    public String contentResponse(String scontent) {
        mcontent = null;
        for (int i = 0; i < mKeyWordList.size(); i++) {
            if (mKeyWordList.get(i) != "") {
                if (!(scontent.indexOf(mKeyWordList.get(i).toString().trim(), 0) == -1)) {
                    return mcontent = mContentList.get(i).toString();
                }
            }
        }
        return mcontent = "正在忙，稍后回复您，么么哒";
    }


    /**
     * 传进要回复的文字，复制到发送框里面，等待发送按钮的确认
     *
     * @param rootNode
     * @param content
     * @return
     */
    private boolean findEditText(AccessibilityNodeInfo rootNode, String content) {
        int count = rootNode.getChildCount();
        android.util.Log.d("218maptrix",
                "root class=" + rootNode.getClassName() + "," + rootNode.getText() + "," + count);
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if (nodeInfo == null) {
                android.util.Log.d("222maptrix", "nodeinfo = null");
                continue;
            }
            android.util.Log.d("226maptrix", "class=" + nodeInfo.getClassName());
            android.util.Log.e("227maptrix", "ds=" + nodeInfo.getContentDescription());
            if (nodeInfo.getContentDescription() != null) {
                int nindex = nodeInfo.getContentDescription().toString().indexOf(name);
                int cindex = nodeInfo.getContentDescription().toString().indexOf(scontent);
                android.util.Log.e("232maptrix", "nindex=" + nindex + " cindex=" + cindex);
                if (nindex != -1) {
                    itemNodeinfo = nodeInfo;
                    android.util.Log.i("235maptrix", "find node info");
                }
            }
            if ("android.widget.EditText".equals(nodeInfo.getClassName())) {
                android.util.Log.i("maptrix", "==================");
                Bundle arguments = new Bundle();
                arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD);
                arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
                // 复制粘贴
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipData clip = ClipData.newPlainText("label", content);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clip);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                return true;
            }
            if (findEditText(nodeInfo, content)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 判断指定的应用是否在前台运行
     *
     * @param packageName
     * @return
     */
    private boolean isAppForeground(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        android.util.Log.i("280maptrix", "==================");
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName)) {
            return true;
        }
        android.util.Log.i("284maptrix", "==================");
        return false;
    }

    /**
     * 将当前应用运行到前台
     */
    private void bring2Front() {
        ActivityManager activtyManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activtyManager.getRunningTasks(3);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            if (this.getPackageName().equals(runningTaskInfo.topActivity.getPackageName())) {
                activtyManager.moveTaskToFront(runningTaskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
    }

    /**
     * 回到系统桌面
     */
    private void back2Home() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    /**
     * 系统是否在锁屏状态
     *
     * @return
     */
    private boolean isScreenLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    private void wakeAndUnlock() {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        PowerManager.WakeLock wl = pm
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire(1000);
        // 得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");
        // 解锁
        kl.disableKeyguard();
    }

    private void release() {
        if (locked && kl != null) {
            android.util.Log.d("maptrix", "release the lock");
            // 得到键盘锁管理器对象
            kl.reenableKeyguard();
            locked = false;
        }
    }

    /**
     * 睡眠
     *
     * @param millis
     */
    public void sleepTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
