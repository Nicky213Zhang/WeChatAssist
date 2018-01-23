package vicmob.micropowder.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import vicmob.earn.R;
import vicmob.micropowder.base.BaseFragment;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 首页
 */
public class HomeUpdateFragment extends BaseFragment {

    private String[] titles = new String[]{"微信助手", "QQ助手", "陌陌助手"};
    private TabLayout tabLayout;

    @Override
    public void initView() {

        setTitle("主 页");

        //创建内容
        View view = createContent();

        //添加内容
        addView(view);

    }

    @Override
    public View createContent() {
        //创建内容
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_main, (ViewGroup) getView(), false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_navi);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(HomeFragment.newInstance("微信助手"), titles[0]);
        pagerAdapter.addFragment(QQHomeFragment.newInstance("qq助手"), titles[1]);
        pagerAdapter.addFragment(MoMoHomeFragment.newInstance("陌陌助手"), titles[2]);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 30, 30);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeUpdateFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeUpdateFragment");
    }
}
