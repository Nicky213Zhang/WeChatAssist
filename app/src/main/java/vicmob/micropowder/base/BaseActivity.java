package vicmob.micropowder.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;

import vicmob.earn.R;
import vicmob.micropowder.service.AutoReplyService;
import vicmob.micropowder.service.EarnAccessibilityService;
import vicmob.micropowder.service.OnekeyService;
import vicmob.micropowder.service.QQAccessibilityService;

/**
 * Created by Eren on 2017/6/19.
 * <p/>
 * Activity基类
 */
public abstract class BaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //软键盘开启布局向上移动
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 跳转并展示动画
     *
     * @param cls
     */
    public void inAnimation(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    /**
     * 返回并展示动画
     */
    public void outAnimation() {
        finish();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

            outAnimation();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * 跳转微信主界面
     */
    public void intentWechat() {
        Intent wxIntent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        wxIntent.setAction(Intent.ACTION_MAIN);
        wxIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        wxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        wxIntent.setComponent(cmp);
        startActivity(wxIntent);
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
     * 此方法用来判断当前应用的辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public  boolean isServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + EarnAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
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
     * 此方法用来判断当前应用的辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public  boolean isOneServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + OnekeyService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
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
     * 跳转QQ主界面
     */
    public void intentQQ(){
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
     * 此方法用来判断自动回复辅助功能服务是否开启
     *
     * @param context
     * @return
     */
    public boolean isAutoServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + AutoReplyService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
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

    public boolean isQQServiceOpening(Context context) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        String service = context.getPackageName() + "/" + QQAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
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


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
