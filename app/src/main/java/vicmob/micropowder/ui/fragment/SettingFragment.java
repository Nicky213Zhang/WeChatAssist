package vicmob.micropowder.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import vicmob.earn.R;
import vicmob.micropowder.base.BaseFragment;
import vicmob.micropowder.ui.activity.AboutVersionActivity;
import vicmob.micropowder.ui.activity.AboutVicmobActivity;
import vicmob.micropowder.ui.activity.ConnectionUsActivity;
import vicmob.micropowder.ui.activity.UpdateVersionActivity;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 设置
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    // 版本更新
    private View layout1;
    // 关于版本
    private View layout2;
    // 联系我们
    private View layout3;
    // 公司相关
    private View layout4;


    @Override
    public void initView() {

        setTitle("设 置");

        View view = createContent();

        addView(view);

    }

    @Override
    public View createContent() {
        //创建内容
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_setting, (ViewGroup) getView(), false);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);
        layout4 = view.findViewById(R.id.layout4);


        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout1:
                inAnimation(UpdateVersionActivity.class);
                break;

            case R.id.layout2:
                inAnimation(AboutVersionActivity.class);
                break;

            case R.id.layout3:
                inAnimation(AboutVicmobActivity.class);
                break;

            case R.id.layout4:
                inAnimation(ConnectionUsActivity.class);
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingFragment");
    }
}
