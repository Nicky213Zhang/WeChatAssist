package vicmob.micropowder.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.ui.views.ConfirmDialog;

/**
 * Created by Eren on 2017/6/21.
 * <p/>
 * 更新APP工具
 */
public class DownLoadAppUtils {

    private String path;

    private Context mContext;

    private ConfirmDialog mDialog;

    private PackageManager mManager;
    private int currentVersionCode = 0;


    public DownLoadAppUtils(String path, Context context) {
        this.path = path;
        mContext = context;
    }

    // 开启的下载线程数
    private int threadCount = 8;
    // 所有线程下载总进度
    private int downloadProgress = 0;
    // 结束线程
    private int finishedThread = 0;

    private ProgressDialog pd; // 进度条对话框


    //获取当前apk的版本号 currentVersionCode
    public int setVersionCode() {
        mManager = mContext.getPackageManager();
        try {
            PackageInfo info = mManager.getPackageInfo(mContext.getPackageName(), 0);
            //String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }

    /**
     * 是否更新对话框
     *
     * @param context
     */
    public void updateApp(final Context context, String versionNmae) {
        mDialog = new ConfirmDialog(context, new Callback() {
            @Override
            public void Positive() {        //确定更新
                String apkPath = Url.APK_URL;
                //DownloadAppUtils.downLoadApk(UpdateVersionActivity.this, apkPath, "更新中...");
                DownLoadAppUtils loadAppUtil = new DownLoadAppUtils(apkPath, context);
                loadAppUtil.startThread();
            }

            @Override
            public void Negative() {
                MyToast.show(mContext, "可以手动设置更新");
            }
        });

        mDialog.setContent("发现新版本:" + versionNmae + "\n是否下载更新?");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    /**
     * 获取名字
     *
     * @param path
     * @return
     */
    public String getNameFromPath(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    public void DialogOpen() {
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);// 必须一直下载完，不可取消
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载安装包，请稍后");
        pd.setTitle("版本升级");
        pd.show();
    }

    /**
     * 开始
     */
    public void startThread() {
        DialogOpen();
        Thread t = new Thread() {
            @Override
            public void run() {
                // 发送http请求，拿到目标文件长度
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);

                    if (conn.getResponseCode() == 200) {
                        // 获取长度
                        int length = conn.getContentLength();
                        pd.setMax(length);
                        // 创建临时文件
                        File file = new File(Environment.getExternalStorageDirectory(), getNameFromPath(path));
                        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                        // 设置临时文件大小与目标文件一致
                        raf.setLength(length);
                        raf.close();

                        // 计算每个线程下载区间
                        int size = length / threadCount;

                        for (int id = 0; id < threadCount; id++) {
                            // 计算每个线程下载的开始位置和结束位置
                            int startIndex = id * size;
                            int endIndex = (id + 1) * size - 1;
                            if (id == threadCount - 1) {
                                endIndex = length - 1;
                            }
                            System.out.println("线程" + id + "下载的区间：" + startIndex + " ~ " + endIndex);
                            new DownLoadThread(id, startIndex, endIndex).start();
                        }

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        t.start();
    }


    class DownLoadThread extends Thread {

        int threadId;
        int startIndex;
        int endIndex;

        public DownLoadThread(int threadId, int startIndex, int endIndex) {
            super();
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            try {
                File fileProgress = new File(Environment.getExternalStorageDirectory(), threadId + ".txt");
                int lastProgress = 0;
                if (fileProgress.exists()) {//如果临时文件存在
                    fileProgress.delete();
                }

                // 发送http请求，请求要下载的数据
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                // 设置请求数据的区间
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

                // 请求部分数据，成功的响应码是206
                if (conn.getResponseCode() == 206) {
                    InputStream is = conn.getInputStream();

                    byte[] b = new byte[1024];
                    int len = 0;
                    // 当前线程下载的总进度
                    int total = lastProgress;
                    File file = new File(Environment.getExternalStorageDirectory(), getNameFromPath(path));
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    // 设置写入的开始位置
                    raf.seek(startIndex);
                    while ((len = is.read(b)) != -1) {
                        raf.write(b, 0, len);
                        total += len;

                        // 每次下载len个长度的字节，马上把len加到下载进度中，让进度条能反应这len个长度的下载进度
                        downloadProgress += len;
                        pd.setProgress(downloadProgress);
                        MyLogger.d("asdf", String.valueOf(downloadProgress));

                        // 创建一个进度临时文件，保存下载进度
                        RandomAccessFile rafProgress = new RandomAccessFile(fileProgress, "rwd");
                        // 每次下载1024个字节，就，就马上把1024写入进度临时文件
                        rafProgress.write((total + "").getBytes());
                        rafProgress.close();

                    }
                    raf.close();

                    // 6条线程全部下载完毕，才去删除进度临时文件
                    finishedThread++;

                    synchronized (path) {
                        if (finishedThread == threadCount) {
                            for (int i = 0; i < threadCount; i++) {
                                File f = new File(Environment.getExternalStorageDirectory(), i + ".txt");
                                f.delete();
                            }
                            finishedThread = 0;
                            // sendNotification("下载完成", "微粉辅助更新", "微粉辅助更新完成");
                            // clearNotification();
                            pd.dismiss();
                            installApk(mContext, file); //安装APK
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安装apk
     */
    public void installApk(Context mContext, File file) {
        Uri fileUri = Uri.fromFile(file);
        Intent it = new Intent();
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(fileUri, "application/vnd.android.package-archive");
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 防止打不开应用
        mContext.startActivity(it);
    }
}
