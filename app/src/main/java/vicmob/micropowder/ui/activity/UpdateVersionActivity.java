package vicmob.micropowder.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.base.BaseLoadNetDataOperator;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.ApkBean;
import vicmob.micropowder.ui.views.HintDialog;
import vicmob.micropowder.ui.views.LoadingDialog;
import vicmob.micropowder.utils.DownLoadAppUtils;


/**
 * /reated by Eren on 2017/6/19.
 * <p/>
 * 版本更新
 */
public class UpdateVersionActivity extends BaseActivity implements BaseLoadNetDataOperator {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.but_local_update)
    Button mButLocalUpdate;
    @BindView(R.id.but_line_update)
    Button mButLineUpdate;

    // apk更新地址
    private String path = Url.APK_URL;

    //网络更新接口实例
    private ApkBean mApkBean;

    //当前版本号
    private int mCurrentCode;
    private LoadingDialog mLoadingDialog;
    private HintDialog mHintDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_version);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.iv_back, R.id.but_local_update, R.id.but_line_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                outAnimation();
                break;

            case R.id.but_local_update:     //本地更新
                mLoadingDialog = new LoadingDialog(UpdateVersionActivity.this);
                mLoadingDialog.setContent("正在获取最新版本信息");
                mLoadingDialog.setCancelable(false);
                mLoadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loadNetData();
                    }
                }).start();
                break;

            case R.id.but_line_update:      //浏览器更新
                if (path == null || TextUtils.isEmpty(path)) {
                    Snackbar.make(view, "请稍后再试", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
                    return;
                } else {
                    openBrowser();
                }
                break;
            default:
                break;
        }
    }


    // 跳转浏览器更新
    private void openBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(path);
        intent.setData(content_url);
        startActivity(intent);
    }


    //联网操作
    @Override
    public void loadNetData() {
        String url = Url.VERSION_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoadingDialog.dismiss();
                        mHintDialog = new HintDialog(UpdateVersionActivity.this);
                        mHintDialog.setContent("网络获取失败,请检查");
                        mHintDialog.setCancelable(true);
                        mHintDialog.show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("123",response);
                        mLoadingDialog.dismiss();
                        try {
                        DownLoadAppUtils downLoadAppUtils = new DownLoadAppUtils(path, UpdateVersionActivity.this);
                        mCurrentCode = downLoadAppUtils.setVersionCode();
                        Gson gson = new Gson();
                        mApkBean = new ApkBean();
                            if (response!=null&&!TextUtils.isEmpty(response)) {
                                mApkBean = gson.fromJson(response, ApkBean.class);
                                if (mApkBean.getWxversion().getVersionCode() <= mCurrentCode) {
                                    mHintDialog = new HintDialog(UpdateVersionActivity.this);
                                    mHintDialog.setContent("当前已是最新版本");
                                    mHintDialog.setCancelable(true);
                                    mHintDialog.show();
                                } else if (mApkBean.getWxversion().getVersionCode() > mCurrentCode) {
                                    downLoadAppUtils.updateApp(UpdateVersionActivity.this, mApkBean.getWxversion().getVersionName());
                                }
                            }
                        }catch (Exception e){

                        }
                    }

                });
    }
}