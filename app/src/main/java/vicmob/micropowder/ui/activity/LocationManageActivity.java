package vicmob.micropowder.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Delete;
import vicmob.micropowder.config.LoadData;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.JsonBean;
import vicmob.micropowder.daoman.bean.LoadLocationBean;
import vicmob.micropowder.daoman.bean.MapSearchBean;
import vicmob.micropowder.daoman.dao.MapSearchDao;
import vicmob.micropowder.ui.adapter.LocationRecyclerAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.ui.views.SwipeLayoutManager;
import vicmob.micropowder.utils.MyJsonUtil;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 地点管理
 */
public class LocationManageActivity extends BaseActivity {
    MyApplication app;
    @BindView(R.id.iv_back_location_manage)
    ImageView mIvBackLocationManage;
    @BindView(R.id.rlv_location_manage)
    RecyclerView mRlvLocationManage;
    @BindView(R.id.iv_location_manage)
    ImageView mIvLocationManage;
    @BindView(R.id.tv_location_manage)
    TextView mTvLocationManage;
    @BindView(R.id.btn_all_delete)
    Button mBtnAllDelete;
    @BindView(R.id.accmanager_load)
    RelativeLayout accmanager_load;
    private List<MapSearchBean> mMapSearchBeanList = new ArrayList<>();
    private LocationRecyclerAdapter mRecyclerAdapter;
    private MapSearchDao mMapSearchDao;
    private ConfirmDialog mConfirmDialog;
    //加载动画图片
    private ImageView imageView;
    //动画
    AnimationDrawable loadingDrawable;

    /**
     * 网络获取实例
     */
    private LoadLocationBean mLoadLocationBean;

    private static final int DELETE = 1;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELETE:
                    if (mMapSearchBeanList.size() == 0) {
                        mIvLocationManage.setVisibility(View.VISIBLE);
                        mTvLocationManage.setVisibility(View.VISIBLE);
                    }
                    mRecyclerAdapter.notifyDataSetChanged();
                    MyToast.show(LocationManageActivity.this, "删除成功");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_manage);
        ButterKnife.bind(this);
        app = (MyApplication) getApplication();
        mIvLocationManage.setVisibility(View.INVISIBLE);
        mTvLocationManage.setVisibility(View.INVISIBLE);
        accmanager_load.setVisibility(View.VISIBLE);
        //显示加载的那个动画
        imageView = (ImageView) findViewById(R.id.accmanagerloading_img);
        loadingDrawable = (AnimationDrawable) imageView.getBackground();
        loadingDrawable.start();
        initDB();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRV() {
        mRlvLocationManage.setLayoutManager(new LinearLayoutManager(this));
        mRlvLocationManage.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerAdapter = new LocationRecyclerAdapter(LocationManageActivity.this, mMapSearchBeanList, new Delete() {
            @Override
            public void Delete(int position) {
                MapSearchDao mapSearchDao = new MapSearchDao(LocationManageActivity.this);
                //删除数据库
                if (mMapSearchBeanList.get(position).getIsLocal() == 0) {
                    mapSearchDao.delete(mMapSearchBeanList.get(position));
                    myHandler.sendEmptyMessage(DELETE);
                }
                if(app.mNetworkAddressList.size() > 0){
                    app.mNetworkAddressList.remove(app.mNetworkAddressList.get(position));
                }
                //删除网络
                if (mMapSearchBeanList.get(position).getIsLocal() == 1) {
                    deleteData(mMapSearchBeanList.get(position).getDataId());
                }

                //关闭滑动
                SwipeLayoutManager.create().closeLayout();

            }
        });
        mRlvLocationManage.setAdapter(mRecyclerAdapter);
        mRlvLocationManage.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        if (mMapSearchBeanList.size() > 0) {
            mMapSearchBeanList.clear();
        }
        if (app.mNetworkAddressList.size() > 0 && app.mNetworkAddressList != null) {
            mMapSearchBeanList.addAll(app.mNetworkAddressList);
        }
        mMapSearchDao = new MapSearchDao(LocationManageActivity.this);
        List<MapSearchBean> mMapSearchBeanCurrentList = mMapSearchDao.queryForAll();
        if (mMapSearchBeanCurrentList.size() > 0) {
            mMapSearchBeanList.addAll(mMapSearchBeanCurrentList);
        }
        if (app.mAllAddressList.size() > 0) {
            app.mAllAddressList.clear();
        }
        app.mAllAddressList = mMapSearchBeanList;
        Log.i("123", new MyApplication().mAllAddressList.size() + ":" + mMapSearchBeanList.size());
        //初始化数据库倒叙显示！！！
//        Collections.reverse(mMapSearchBeanList);
        accmanager_load.setVisibility(View.INVISIBLE);
        loadingDrawable.stop();
        if (mMapSearchBeanList.size() != 0 && !mMapSearchBeanList.get(0).getSearchAddresses().isEmpty()) {
            mIvLocationManage.setVisibility(View.INVISIBLE);
            mTvLocationManage.setVisibility(View.INVISIBLE);
        } else {
            mIvLocationManage.setVisibility(View.VISIBLE);
            mTvLocationManage.setVisibility(View.VISIBLE);
        }
        initRV();
    }

    @OnClick({R.id.iv_back_location_manage, R.id.btn_all_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_location_manage:
                outAnimation();
                break;
            case R.id.btn_all_delete:
                if (mMapSearchBeanList.size() != 0) {

                    mConfirmDialog = new ConfirmDialog(LocationManageActivity.this, new Callback() {
                        @Override
                        public void Positive() {
                            mMapSearchDao.deleteAll();
                            //删除网络
                            for (int i = 0; i < app.mNetworkAddressList.size(); i++) {
                                deleteData(app.mNetworkAddressList.get(i).getDataId());
                            }
                            mMapSearchBeanList.clear();
                            app.mNetworkAddressList.clear();
                            app.mAllAddressList.clear();
                            myHandler.sendEmptyMessage(DELETE);

                        }

                        @Override
                        public void Negative() {
                            mConfirmDialog.dismiss();
                        }
                    });
                    mConfirmDialog.setContent("提示：" + "\n是否删除所有地点");
                    mConfirmDialog.setCancelable(true);
                    mConfirmDialog.show();
                }
                break;
        }
    }

    /**
     * 删除网络数据
     */
    public void deleteData(String dataID) {
        String url = Url.deleteAdd;
        Log.i("123", dataID);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("dataId", dataID.trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (MyJsonUtil.getBeanByJson(response)) {
                            myHandler.sendEmptyMessage(DELETE);
                        }
                    }
                });
    }
}
