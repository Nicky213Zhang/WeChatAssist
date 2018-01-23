package vicmob.micropowder.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.ui.adapter.AttentionAdapter;

/**
 * author: Twisted
 * created on: 2017/9/11 14:36
 * description:
 */

public class QQUserAttentionActivity extends BaseActivity {
    @BindView(R.id.attention_rclv)
    RecyclerView attentionRclv;
    @BindView(R.id.attention_back)
    ImageView attentionBack;
    private List<String> mDatasContent;
    private AttentionAdapter mAttentionAdapter;
    private List<String> mDatasTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        initDate();
        mAttentionAdapter = new AttentionAdapter(QQUserAttentionActivity.this, mDatasTitle, mDatasContent);
        attentionRclv.setLayoutManager(new LinearLayoutManager(this));
        attentionRclv.setAdapter(mAttentionAdapter);
    }

    private void initDate() {
        mDatasTitle = new ArrayList<>();
        mDatasContent = new ArrayList<>();
        String attentionTitle[] = {
                getResources().getString(R.string.qq_add_friend), getResources().getString(R.string.qq_add_group_friends),
                getResources().getString(R.string.qq_nearby)};
        String attentionContent[] = {
                getResources().getString(R.string.qq_add_friend_content), getResources().getString(R.string.qq_add_group_friends_content),
                 getResources().getString(R.string.qq_nearby_content)};
        for (int i = 0; i < attentionContent.length; i++) {
            mDatasContent.add(attentionContent[i]);
            mDatasTitle.add(attentionTitle[i]);
        }
    }

    @OnClick(R.id.attention_back)
    public void onViewClicked() {
        outAnimation();
    }
}
