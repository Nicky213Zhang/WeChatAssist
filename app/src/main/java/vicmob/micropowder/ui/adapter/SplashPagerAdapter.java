package vicmob.micropowder.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by sunjing on 2017/7/12.
 * <p/>
 * 引导界面适配器
 */

public class SplashPagerAdapter extends PagerAdapter {
    private List<View> pages;

    public SplashPagerAdapter(List<View> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size() == 0 ? 0 : pages.size();
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(pages.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(pages.get(position), 0);

        return pages.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
