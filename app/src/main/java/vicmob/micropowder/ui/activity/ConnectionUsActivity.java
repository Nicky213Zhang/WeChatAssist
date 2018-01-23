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
 * 联系我们
 */
public class ConnectionUsActivity extends BaseActivity {

    @BindView(R.id.aboutVicmob_back)
    ImageView mAboutVicmobBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        ButterKnife.bind(this);
    }




    @OnClick(R.id.aboutVicmob_back)
    public void onViewClicked() {
        outAnimation();
    }
}
