package vicmob.micropowder.ui.fragment;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import vicmob.earn.R;
import vicmob.micropowder.base.BaseQQFragment;

/**
 * author: Twisted
 * created on: 2017/8/5 13:52
 * description:
 */

public class MoMoHomeFragment extends BaseQQFragment  {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_momo_home;
    }
    public static MoMoHomeFragment newInstance(String title) {
        MoMoHomeFragment fragment = new MoMoHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", title);
        fragment.setArguments(bundle);

        return fragment;
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MoMoHomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MoMoHomeFragment");
    }
}
