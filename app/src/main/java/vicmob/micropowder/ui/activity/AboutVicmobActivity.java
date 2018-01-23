package vicmob.micropowder.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;

/**
 * Created by Eren on 2017/6/19.
 * <p/>
 * 公司相关
 */
public class AboutVicmobActivity extends BaseActivity {

    @BindView(R.id.aboutVicmob_back)
    ImageView mAboutVicmobBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_vicmob);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.aboutVicmob_back)
    public void onViewClicked() {
        outAnimation();
    }
}
