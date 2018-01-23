package vicmob.micropowder.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public abstract class BaseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public Activity mActivity;//当做上下文，获取Fragment所依赖的Activity对象
    private ImageView menuBack;
    private TextView menuTitle;
    private FrameLayout mContainer;
    private MyApplication mMyApplication;
    private RelativeLayout mRl_top_header;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mMyApplication = (MyApplication) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        mRl_top_header= (RelativeLayout) view.findViewById(R.id.rl_top_header);
        menuBack = (ImageView) view.findViewById(R.id.menu_back);
        menuTitle = (TextView) view.findViewById(R.id.tv_title);
        mContainer = (FrameLayout) view.findViewById(R.id.container);
        menuTitle.setText(mParam1);
        menuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mActivity).mSlidingMenu.toggle();
            }
        });

        initView();

        initListener();

        return view;
    }

    /**
     * 初始化监听事件
     */
    public void initListener() {

    }

    /**
     * 初始布局
     *
     * @return
     */
    public abstract void initView();

    //设置标题内容
    public void setTitle(String title) {
        menuTitle.setVisibility(View.VISIBLE);
        menuTitle.setText(title);
    }

    /**
     * 创建内容,子类必须实现
     *
     * @return
     */
    public abstract View createContent();

    //向容器里面添加内容
    public void addView(View view) {
        //清空原来的内容
        mContainer.removeAllViews();
        //添加内容
        mContainer.addView(view);
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
    public void setheader(boolean title) {
        if (title){
            mRl_top_header.setVisibility(View.VISIBLE);
        }else {
            mRl_top_header.setVisibility(View.GONE);
        }
    }
    @Override
    public void onAttach(Context context)
    {

        super.onAttach(context);
    }
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
     * 设置tablayout下划线长度的方法
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
