package vicmob.micropowder.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Eren on 2017/6/24.
 * <p/>
 * 辅助服务基类，包含公共的动作
 */
public class BaseAccessibilityService extends AccessibilityService {
    public static final String TAG = "test";
    public AccessibilityNodeInfo node = null;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    /**
     * 全局返回事件
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
     * 返回上一级
     */
    public void BackClick() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    /**
     * 滑动左到右
     */
    public void SWIPE_LEFT_AND_RIGHTClick() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GESTURE_SWIPE_RIGHT);
    }

    /**
     * 滑动下到上
     */
    public void SWIPE_DOWN_AND_UPClick() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GESTURE_SWIPE_UP);
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
     * 通过一部分TEXT获得全部TEXT
     *
     * @param text
     * @return
     */
    public String getTextByText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
            return list.get(0).getText().toString();
            //String text = nodeInfo.getText().toString();
        }
        return null;
    }

    /**
     * 通过ID获得TEXT
     *
     * @param id
     * @return
     */

    public String getTextById(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            return list.get(0).getText().toString();
            //String text = nodeInfo.getText().toString();
        }
        return "没有群";
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
     * QQ模拟点击id事件
     *
     * @param id
     * @param i
     */
    public void QQfindIdAndClick(String id, int i, String parentid) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;

        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + parentid);
            if (list.size() > 0) {
                node = list.get(0);
                List<AccessibilityNodeInfo> list1 = node.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
                if (list1.size() > 0) {
                    node = list1.get(i);
                }
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performClick(n);
        }
    }

    public int numFindQQId(String id) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        int num = 0;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                num = list.size();
            }
        }
        return num;
    }

    public void QQfindIdAndClick(String id, int i) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;

        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                node = list.get(i);
            }
        }
        targetNode = node;

        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performClick(n);
            Log.i("890", n + "node");
        }
    }

    public void QQfindIdTextAndClick(String id, String text, int i) {
        // 查找当前窗口中id为“id”的按钮
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        AccessibilityNodeInfo targetNode1 = null;
        node = null;
        if (targetNode == null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                Log.i("123", "click" + list.size());
                Log.i("123", "click" + list.get(i).getText());
                for (int j = 0; j < list.size(); j++) {
                    targetNode = list.get(j);
                    if (targetNode.getText().toString().equals(text)) {
                        node = targetNode;
                        break;
                    }
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
     * 判断是否找到某个ID
     *
     * @param id
     * @return
     */
    public boolean isFindId(String id, int i) {
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
            return true;
        }
        return false;
    }

    /**
     * 判断是否找到某个QQID
     *
     * @param id
     * @return
     */
    public boolean isFindQQId(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据id and text 查找元素是否存在
     *
     * @param id
     * @return
     */
    public boolean isFindQQIdText(String id, String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {

            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            Log.i("123", "com.tencent.mobileqq:id/" + id);
            Log.i("123", "循环" + list.size() + text);
            if (list.size() > 0) {
                Log.i("123", "循环1");
                for (int i = 0; i < list.size(); i++) {
                    Log.i("123", "循环");
                    Log.i("123", list.get(i).getText().toString());
                    Log.i("123", list.get(i).getWindowId() + "");
                    if (list.get(i).getText().equals(text)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否找到某个QQID
     *
     * @param id
     * @return
     */
    public boolean isFindQQId(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                node = list.get(i);
            }
        }
        targetNode = node;
        if (targetNode != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否找到某个QQID及parentID确定哪个，qq好友专用
     *
     * @param id
     * @return
     */
    public int isFindQQNum(String id, String parentid) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + parentid);
        AccessibilityNodeInfo targetNode = null;
        if (list.size() > 0) {
            targetNode = list.get(0);
            List<AccessibilityNodeInfo> list1 = targetNode.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list1.size() > 0) {
                return list1.size();
            }

        }
        return 0;
    }

    public Rect isFindQQBouns(String id, int i, String parentid) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + parentid);
        AccessibilityNodeInfo targetNode = null;
        if (list.size() > 0) {
            targetNode = list.get(0);
            List<AccessibilityNodeInfo> list1 = targetNode.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list1.size() > 0) {
                Rect nodeRect = new Rect();
                list1.get(i).getBoundsInScreen(nodeRect);
                return nodeRect;
            }
        }
        return null;
    }

    public Rect isFindWXBouns(String id, int i, String parentid) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + parentid);
        AccessibilityNodeInfo targetNode = null;
        if (list.size() > 0) {
            targetNode = list.get(0);
            List<AccessibilityNodeInfo> list1 = targetNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
            if (list1.size() > 0) {
                Rect nodeRect = new Rect();
                list1.get(i).getBoundsInScreen(nodeRect);
                return nodeRect;
            }
        }
        return null;
    }

    public boolean isFindWXBouns(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/" + id);
        AccessibilityNodeInfo targetNode = null;
        Log.i("zzz1", list.size() + "aaaa"+i);
        if (list.size() > 0) {

            Rect nodeRect = new Rect();
            AccessibilityNodeInfo accessibilityNodeInfo=list.get(i);
            Log.i("zzz1", list.size() + "bb"+accessibilityNodeInfo);
            accessibilityNodeInfo.getBoundsInScreen(nodeRect);
            Log.i("zzz1", nodeRect.bottom + "" + nodeRect.top);
            if (nodeRect.bottom ==1263  && nodeRect.top == 1167) {
                return true;
            }

        }
        return false;
    }


    public boolean isFindQQBouns(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
        AccessibilityNodeInfo targetNode = null;
        Log.i("789", list.size() + "");
        if (list.size() > 0) {
            Rect nodeRect = new Rect();
            list.get(i).getBoundsInScreen(nodeRect);
            Log.i("789", nodeRect.bottom + "" + nodeRect.top);
            if (nodeRect.bottom == 1128 && nodeRect.top == 1008) {
                return true;
            }

        }
        return false;
    }

    /**
     * 判断是否找到某个QQ给定父id与子id的数量
     *
     * @param id
     * @return
     */
    public int isFindQQParentItemNum(String id, String parentid) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + parentid);
        AccessibilityNodeInfo targetNode = null;
        if (list.size() > 0) {
            targetNode = list.get(0);
            //com.tencent.mobileqq:id/name
            List<AccessibilityNodeInfo> list1 = targetNode.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list1.size() > 0) {
                return list1.size();
            }

        }
        return 0;
    }

    /**
     * 判断是否找到某个QQID的item的数量
     *
     * @param id
     * @return
     */
    public int isFindQQNum(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
        if (list.size() > 0) {

            return list.size();
        }
        return 0;
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
     * 模拟物理滑动方法,翻页操作
     *
     * @param cmd
     */
    public static void execSlipCmd(String cmd) {
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
     * 点击匹配的nodeInfo
     *
     * @param str text关键字
     */
    public void openNext(String str) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Toast.makeText(this, "rootWindow为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(str);
        if (list != null && list.size() > 0) {
            list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            list.get(list.size() - 1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
     * 跳转QQ主界面
     */
    public void intentQQ() {
        try {
            PackageManager packageManager = getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 此方法用来判断当前应用的辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public boolean isQQServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + QQAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * QQ粘贴
     *
     * @param id
     * @param i
     */
    public void QQpaste(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        try {
            if (node == null) {
                List<AccessibilityNodeInfo> list = nodeInfo
                        .findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
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

    public void findContentAndClick(String id, int i, String text, int child) {
        // 查找当前窗口中包含“xx”文字的按钮
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
            final AccessibilityNodeInfo n = targetNode.getChild(child);
            Log.i("123", n + "");
            if (n.getContentDescription().toString().equals(text)) {
                performClick(n);
            }
        }
    }

    /*****
     * qq聊天界面返回点击
     */
    public void performChatBackClick(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
            if (list.size() > 0) {
                node = list.get(i);
                Log.i("123", node + "");
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode.getChild(0);
            Log.i("123", n + "");
            performClick(n);
        }
    }

    public void performDialogClick(String id, int i, int child, String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/" + id);
            Log.i("dialog", list.size() + "");
            if (list.size() > 0) {
                node = list.get(i);
                Log.i("dialog", node + "");
            }
        }
        targetNode = node;
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode.getChild(child);
            Log.i("dialog", targetNode.getChild(0) + "child");
            Log.i("dialog", targetNode.getChild(1) + "child");
            Log.i("dialog", targetNode.getChild(2) + "child");

            if (n.getClassName().toString().equals("android.widget.ImageView") || n.getContentDescription().toString().equals(text)) {
                performClick(n);
            }
        }
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

    public int isFindQQIdlistnum(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        AccessibilityNodeInfo targetNode = null;
        node = null;
        if (node == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
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

    public Rect isFindQQidBouns(String id, int i) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/" + id);
        AccessibilityNodeInfo targetNode = null;
        if (list.size() > 0) {

            Rect nodeRect = new Rect();
            list.get(i).getBoundsInScreen(nodeRect);
            return nodeRect;

        }
        return null;
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
}
