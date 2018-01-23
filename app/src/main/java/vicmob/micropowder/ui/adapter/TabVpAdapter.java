package vicmob.micropowder.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Eren on 2017/5/31.
 * <p/>
 * 主界面ViewPager适配器
 */
public class TabVpAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public TabVpAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
