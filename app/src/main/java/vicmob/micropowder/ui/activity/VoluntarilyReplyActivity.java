package vicmob.micropowder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sevenheaven.iosswitch.ShSwitchView;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.daoman.bean.AutoReplyBean;
import vicmob.micropowder.daoman.dao.AutoReplyDao;
import vicmob.micropowder.ui.adapter.AutoReplyAdapter;
import vicmob.micropowder.ui.views.AutoContentDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 自动回复
 */
public class VoluntarilyReplyActivity extends BaseActivity {
    @BindView(R.id.iv_back_autoresponse)
    ImageView mIvBackAutoresponse;
    @BindView(R.id.rl_title_autoresponse)
    RelativeLayout mRlTitleAutoresponse;
    @BindView(R.id.iv_begin_autoresponse)
    ShSwitchView mIvBeginAutoresponse;
    @BindView(R.id.rl_open_autoresponse)
    RelativeLayout mRlOpenAutoresponse;
    @BindView(R.id.tv_remarks_autoresponse)
    TextView mTvRemarksAutoresponse;
    @BindView(R.id.rlv_auto_reply)
    RecyclerView mRlvAutoReply;
    @BindView(R.id.btn_auto_reply)
    Button mBtnAutoReply;
    /**
     * 开启服务对话框
     */
    private AutoContentDialog mAutoContentDialog;
    /**
     * 关键词编辑框的值
     */
    private String autoKeyword;
    /**
     * 回复内容编辑框的值
     */
    private String autoContent;
    private List<AutoReplyBean> mAutoReplyList;
    private AutoReplyAdapter autoReplyAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_response);
        ButterKnife.bind(this);
        initDB();
        initRecyclerView();
    }


    /**
     * 布局可见时调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        initOpenState();
    }


    /**
     * 布局交互时调用
     */
    @Override
    public void onResume() {
        super.onResume();
        SwitchChanged();
    }

    @OnClick({R.id.iv_back_autoresponse, R.id.rl_open_autoresponse, R.id.btn_auto_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back_autoresponse:
                outAnimation();
                break;
//            微信辅助
            case R.id.rl_open_autoresponse:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.btn_auto_reply:
                //点击添加回复按钮出现dialog
                mAutoContentDialog = new AutoContentDialog(VoluntarilyReplyActivity.this, new Callback() {
                    @Override
                    /**
                     * 点击添加内容
                     */
                    public void Positive() {
                        //通过sp得到关键词和回复内容的值
                        autoKeyword = PrefUtils.getString(VoluntarilyReplyActivity.this, "autoKeyword", null);
                        autoContent = PrefUtils.getString(VoluntarilyReplyActivity.this, "autoContent", null);
                        //判断关键词和内容不能为空
                        if (TextUtils.isEmpty(autoContent) || TextUtils.isEmpty(autoKeyword)) {
                            MyToast.show(VoluntarilyReplyActivity.this, "关键词和内容不能为空");
                        } else if (isRepeat()) {
                            MyToast.show(VoluntarilyReplyActivity.this, "关键词不能包含重复");
                        } else {
                            //添加数据库
                            AutoReplyDao autoReplyDao = new AutoReplyDao(VoluntarilyReplyActivity.this);
                            AutoReplyBean autoReplyBean = new AutoReplyBean();
                            autoReplyBean.setKeyWord(autoKeyword);
                            autoReplyBean.setContent(autoContent);
                            autoReplyDao.add(autoReplyBean);
                            //数据添加到集合并置顶选中，并刷新界面
                            mAutoReplyList.add(0, autoReplyBean);
                            autoReplyAdapter.notifyDataSetChanged();
                            mAutoContentDialog.dismiss();
                        }
                    }

                    @Override
                    public void Negative() {
//                        点击确定dialog不消失
                        try {
                            Field field = mAutoContentDialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(mAutoContentDialog, true);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        mAutoContentDialog.dismiss();
                    }
                });

                mAutoContentDialog.setCancelable(true);
                mAutoContentDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                mAutoContentDialog.show();
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRlvAutoReply.setLayoutManager(new LinearLayoutManager(this));         //设置布局管理器
        mRlvAutoReply.addItemDecoration(new DividerItemDecoration(this));      //设置分割线样式
        autoReplyAdapter = new AutoReplyAdapter(mAutoReplyList, VoluntarilyReplyActivity.this);//添加RecyclerView适配器
        mRlvAutoReply.setAdapter(autoReplyAdapter);
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        //查询数据库
        AutoReplyDao mAutoReplyDao = new AutoReplyDao(VoluntarilyReplyActivity.this);
        mAutoReplyList = mAutoReplyDao.queryForAll();
        //初始化数据库倒叙显示！！！
        Collections.reverse(mAutoReplyList);
    }

    /**
     * 开关状态发生改变
     */
    private void SwitchChanged() {
        mIvBeginAutoresponse.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
            }
        });
    }

    /**
     * 初始化服务按钮开启的状态
     */
    private void initOpenState() {
        if (isAutoServiceOpening(VoluntarilyReplyActivity.this)) {
            mIvBeginAutoresponse.setOn(true);   //打开

        } else {
            mIvBeginAutoresponse.setOn(false);  //关闭
        }
    }

    private boolean isRepeat() {
        //查询数据 判断是否又出现重复或包含关键词的  关键词
        AutoReplyDao mAutoReplyDao = new AutoReplyDao(VoluntarilyReplyActivity.this);
        List<AutoReplyBean> mAutoReplyBeenList = mAutoReplyDao.queryForAll();
        for (int i = 0; i < mAutoReplyBeenList.size(); i++) {
            if (mAutoReplyBeenList.get(i).getKeyWord().contains(autoKeyword)
                    || autoKeyword.contains(mAutoReplyBeenList.get(i).getKeyWord())) {
                return true;
            }
        }
        return false;
    }


}
