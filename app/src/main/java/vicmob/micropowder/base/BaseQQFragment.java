package vicmob.micropowder.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

import vicmob.earn.R;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.service.QQAccessibilityService;
import vicmob.micropowder.ui.activity.MainActivity;


/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * Fragment基类
 */
public abstract class BaseQQFragment extends Fragment {

    public Activity mActivity;//当做上下文，获取Fragment所依赖的Activity对象
    protected View mRootView;
    protected Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

    }
    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mRootView == null)
        {
            mRootView = inflater.inflate(getLayoutId(), container, false);


            init(inflater, container, savedInstanceState);
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
        {
            parent.removeAllViews();
        }

        return mRootView;
    }

    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    }

    protected View findViewById(int id)
    {
        return mRootView.findViewById(id);
    }

    /**
     * 设置一个layout id. 子类不调用此父类的
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)},则此方法没用
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();

    @Override
    public void onDetach()
    {

        try
        {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e)
        {

        } catch (IllegalAccessException e)
        {

        }
        super.onDetach();

    }


    /**
     * 跳转并展示动画
     *
     * @param cls
     */
    public void inAnimation(Class<?> cls) {
        startActivity(new Intent(mActivity, cls));
        mActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    /**
     * 返回并展示动画
     */
    public void outAnimation() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
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
}
