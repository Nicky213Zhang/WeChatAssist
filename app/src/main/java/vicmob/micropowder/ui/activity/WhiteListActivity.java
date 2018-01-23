package vicmob.micropowder.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.config.Whiteback;
import vicmob.micropowder.daoman.bean.LoadWhiteBean;
import vicmob.micropowder.daoman.bean.WhiteListContentBean;
import vicmob.micropowder.daoman.dao.WhiteListDao;
import vicmob.micropowder.service.BaseAccessibilityService;
import vicmob.micropowder.ui.adapter.WhiteListAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.DividerItemDecoration;
import vicmob.micropowder.ui.views.LoadingDialog;
import vicmob.micropowder.ui.views.WhiteDialog;
import vicmob.micropowder.utils.MyJsonUtil;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;


/**
 * Created by admin on 2017/7/4.
 * <p/>
 * 白名单界面
 */
public class WhiteListActivity extends BaseActivity {


    /**
     * 输入框输入的内容
     */
    private String WhiteListContentText;
    /**
     * 全局变量
     */
    private MyApplication app;
    /**
     * 开启服务对话框
     */
    private ConfirmDialog mConfirmDialog;
    private WhiteDialog mWhiteDialog;
    private WhiteListContentBean mWhiteListContentBean;
    private WhiteListAdapter mwhiteListAdapter;
    private List<WhiteListContentBean> mWhiteListContentList=new ArrayList<>();

    @BindView(R.id.wl_back)
    ImageView wlBack;
    @BindView(R.id.wl_local)
    RelativeLayout wlLocal;
    @BindView(R.id.wl_address_list)
    RelativeLayout wlAddressList;
    @BindView(R.id.wl_white_list)
    RelativeLayout wlWhiteList;
    @BindView(R.id.wl_delete)
    RelativeLayout wlDelete;
    @BindView(R.id.wl_content)
    RecyclerView wlContent;
    @BindView(R.id.wl_num_ed)
    EditText wlNumEd;
    @BindView(R.id.wl_begin)
    Button wlBegin;
    private static final int ADDSUCCESS = 1;
    private WhiteListDao whiteListDao;

    private LoadingDialog mLoadingDialog;

    private static final int DELECT = 2;
    private static final int XLS = 3;
    private static final int IMPORT_MSG = 9;
    public static final int LOADING_MSG = 5;
    public static final int LOADING_LOMSG = 6;
    public static final int IMPORT_LOMSG = 7;
    public static final int LOADING_DELETE = 8;
    public static final int FILE_Format = 10;

    private boolean isIntent = false;
    //加载对话框
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case ADDSUCCESS:
                    mWhiteListContentList = new WhiteListDao(WhiteListActivity.this).queryForAll();
                    Collections.reverse(mWhiteListContentList);
                    mwhiteListAdapter = new WhiteListAdapter(WhiteListActivity.this, mWhiteListContentList);   //添加RecyclerView适配器
                    wlContent.setAdapter(mwhiteListAdapter);
                    wlContent.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "添加成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DELECT:
                    wlContent.setVisibility(View.INVISIBLE);
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Log.i("123", new WhiteListDao(WhiteListActivity.this).queryForAll().size() + "12");
                    MyToast.show(WhiteListActivity.this, "删除成功");
                    break;
                case XLS:
                    mWhiteListContentList = new WhiteListDao(WhiteListActivity.this).queryForAll();
                    Collections.reverse(mWhiteListContentList);
                    mwhiteListAdapter = new WhiteListAdapter(WhiteListActivity.this, mWhiteListContentList);   //添加RecyclerView适配器
                    wlContent.setAdapter(mwhiteListAdapter);
                    mwhiteListAdapter.notifyDataSetChanged();
                    wlContent.setVisibility(View.VISIBLE);
                    Log.i("123", mWhiteListContentList.size() + "llllllllll");
                    break;
                case LOADING_MSG:
                    //加载进度显示
                    //加载进度
                    mLoadingDialog = new LoadingDialog(WhiteListActivity.this);
                    mLoadingDialog.setContent("正在导入通讯录...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case IMPORT_MSG:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    MyToast.show(WhiteListActivity.this, "添加成功");
                    break;
                case LOADING_LOMSG:
                    //加载进度显示
                    //加载进度
                    mLoadingDialog = new LoadingDialog(WhiteListActivity.this);
                    mLoadingDialog.setContent("正在导入本地文件...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case IMPORT_LOMSG:
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    MyToast.show(WhiteListActivity.this, "导入成功");
                    break;
                case LOADING_DELETE:
                    mLoadingDialog = new LoadingDialog(WhiteListActivity.this);
                    mLoadingDialog.setContent("正在删除...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;
                case FILE_Format:
                    Toast.makeText(WhiteListActivity.this, "文件格式不对，请选择.xls文件！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_white_list);
        ButterKnife.bind(this);
        initDB();
        initRecyclerView();
        //6.0必加权限
        isGrantExternalRW(this);
        init();
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        app = (MyApplication) getApplication();
        if (app.wList.size() > 0) {
//            for (int i = 0; i < app.wList.size(); i++) {
                Log.i("123", new WhiteListDao(WhiteListActivity.this).queryForAll().size() + "");
                if (app.wList.get(0).getStatus()==0) {//0是导入表,1是导入通讯录,2是添加白名单
                    isIntent = true;
                    String aa = "/storage/emulated/0/" + app.wList.get(0).getFile();
                    File data = new File(aa);
                    getFileContent(data); //点击文本储存到数据库
                } else {
                    mWhiteListContentList.clear();
                    WhiteListDao whiteListDao = new WhiteListDao(WhiteListActivity.this);
                    mWhiteListContentList = whiteListDao.queryForAll();
                    //初始化数据库倒叙显示！！！
                    Collections.reverse(mWhiteListContentList);
                }

//            }
            Log.i("123", new WhiteListDao(WhiteListActivity.this).queryForAll().size() + "");
        } else {
            //查询数据库
            WhiteListDao whiteListDao = new WhiteListDao(WhiteListActivity.this);
            mWhiteListContentList = whiteListDao.queryForAll();
            //初始化数据库倒叙显示！！！
            Collections.reverse(mWhiteListContentList);
        }
    }

    public void init() {
        //设置软键盘回车键
        wlNumEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //获取输入框的内容
                    WhiteListContentText = wlNumEd.getText().toString().trim();

                    if (WhiteListContentText == "" || TextUtils.isEmpty(WhiteListContentText)) {
                        MyToast.show(WhiteListActivity.this, "内容设置不能为空");
                    } else {
                        //添加数据库
                        whiteListDao = new WhiteListDao(WhiteListActivity.this);
                        //添加内容到实例中
                        mWhiteListContentBean = new WhiteListContentBean();
                        mWhiteListContentBean.setWhiteContent(WhiteListContentText);
                        //添加数据库
                        whiteListDao.add(mWhiteListContentBean);
//                    mWhiteListContentList.add(mWhiteListContentBean);
                        wlNumEd.setText("");
                        myHandler.sendEmptyMessage(ADDSUCCESS);

                    }

                    return false;
                }
                return false;
            }
        });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        wlContent.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        wlContent.addItemDecoration(new DividerItemDecoration(this)); //设置分割线样式
        mwhiteListAdapter = new WhiteListAdapter(WhiteListActivity.this, mWhiteListContentList);   //添加RecyclerView适配器
        wlContent.setAdapter(mwhiteListAdapter);

    }

    @OnClick({R.id.wl_back, R.id.wl_local, R.id.wl_address_list, R.id.wl_white_list, R.id.wl_delete, R.id.wl_num_ed, R.id.wl_begin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wl_back:
                outAnimation();  //返回按钮
                break;

            case R.id.wl_local:
                isIntent = false;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                }
                break;

            case R.id.wl_address_list:
                if (mWhiteListContentList.size() < 1) {

                    Snackbar.make(view, "还未添加号码", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
                } else {
                    mConfirmDialog = new ConfirmDialog(WhiteListActivity.this, new Callback() {
                        @Override
                        public void Positive() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    myHandler.sendEmptyMessage(LOADING_MSG);
                                    try {
                                        testAddContactsInTransaction();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    myHandler.sendEmptyMessage(IMPORT_MSG);
                                }
                            }).start();
                        }

                        @Override
                        public void Negative() {
                            mConfirmDialog.dismiss();
                        }
                    });
                    mConfirmDialog.setContent("提示：" + "\n是否导入通讯录");
                    mConfirmDialog.setCancelable(true);
                    mConfirmDialog.show();
                }
                break;

            case R.id.wl_white_list:
                mWhiteListContentList = new WhiteListDao(WhiteListActivity.this).queryForAll();
                if (mWhiteListContentList.size() < 1) {
                    Snackbar.make(view, "请添加号码后再开始", Snackbar.LENGTH_SHORT).setAction("SS", null).show();
                } else {
                    // 是否开启服务的判断
                    anyMethod();
                }
                break;

            case R.id.wl_delete:
                mConfirmDialog = new ConfirmDialog(WhiteListActivity.this, new Callback() {
                    @Override
                    public void Positive() {
                        myHandler.sendEmptyMessage(LOADING_DELETE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                whiteListDao = new WhiteListDao(WhiteListActivity.this);
//                                whiteListDao.deleteAll();
                                whiteListDao.deleteAll(mWhiteListContentList);
                                //删除网络
                                for (int i = 0; i < app.wList.size(); i++) {
                                    loadDeleteWhiteData(app.wList.get(i).getDevicesId());
                                }
                                app.wList.clear();
                                myHandler.sendEmptyMessage(DELECT);
                            }
                        }).start();
                    }

                    @Override
                    public void Negative() {

                    }
                });
                mConfirmDialog.setContent("提示：" + "\n是否清空缓存");
                mConfirmDialog.setCancelable(true);
                mConfirmDialog.show();
                break;

            case R.id.wl_num_ed:
                break;

            case R.id.wl_begin:
                //获取输入框的内容
                WhiteListContentText = wlNumEd.getText().toString().trim();

                if (WhiteListContentText == "" || TextUtils.isEmpty(WhiteListContentText)) {
                    MyToast.show(WhiteListActivity.this, "内容设置不能为空");
                    return;
                } else {
                    //添加数据库
                    whiteListDao = new WhiteListDao(WhiteListActivity.this);
                    //添加内容到实例中
                    mWhiteListContentBean = new WhiteListContentBean();
                    mWhiteListContentBean.setWhiteContent(WhiteListContentText);
                    //添加数据库
                    whiteListDao.add(mWhiteListContentBean);
//                    mWhiteListContentList.add(mWhiteListContentBean);
                    wlNumEd.setText("");
                    myHandler.sendEmptyMessage(ADDSUCCESS);

                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String filePath = URLDecoder.decode(uri.toString());
            int fileIndex = filePath.indexOf("/");//获取第一个/在整个字符串中的位置
            String files = filePath.substring(fileIndex + 2).trim();
            File file = new File(files);//string转化成File对象
            getFileContent(file);   //点击文本储存到数据库
        } else {
            Toast.makeText(WhiteListActivity.this, "您没有选择任何文件", Toast.LENGTH_LONG).show();
        }

    }

    //读取指定目录下的所有TXT文件的文件内容
    protected void getFileContent(final File file) {
        final Thread thr = new Thread() {
            @Override
            public void run() {
                super.run();
                String content = "";
                if (file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
                    Log.i("zeng", "The File doesn't not exist "
                            + file.getName().toString() + file.getPath().toString());
                } else {
                    if (file.getName().endsWith(".xls")) {
                        myHandler.sendEmptyMessage(LOADING_LOMSG);
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            if (fileInputStream != null) {
                                Workbook workbook = Workbook.getWorkbook(fileInputStream);
                                workbook.getNumberOfSheets();
                                //获得第一个工作对象表
                                Sheet sheet = workbook.getSheet(0);
                                int rows = sheet.getRows();
                                //添加数据库
                                whiteListDao = new WhiteListDao(WhiteListActivity.this);
                                if (whiteListDao.queryForAll().size() > 0 && isIntent == true) {
                                    whiteListDao.deleteAll(whiteListDao.queryForAll());
//                                    mWhiteListContentList.clear();
                                }
                                mWhiteListContentBean = new WhiteListContentBean();
                                for (int i = 1; i < rows; ++i) {
                                    String name = (sheet.getCell(0, i)).getContents();
                                    String number = (sheet.getCell(1, i)).getContents();
                                    mWhiteListContentBean.setName(name);
                                    mWhiteListContentBean.setWhiteContent(number);
                                    //将获取到的姓名、号码存入数据库
                                    whiteListDao.add(mWhiteListContentBean);
                                }
                                myHandler.sendEmptyMessage(XLS);
                                workbook.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }
                        myHandler.sendEmptyMessage(IMPORT_LOMSG);
                    } else {
                        myHandler.sendEmptyMessage(FILE_Format);
                    }
                }
            }
        };
        thr.start();

    }

    /**
     * 批量添加联系人
     *
     * @throws Exception
     */
    public void testAddContactsInTransaction() throws Exception {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentResolver resolver = this.getContentResolver();
        int rawContactInsertIndex;
        // 循环添加
        mWhiteListContentList = new WhiteListDao(WhiteListActivity.this).queryForAll();
        for (int i = 0; i < mWhiteListContentList.size(); i++) {
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            rawContactInsertIndex = operations.size();// 这句好很重要，有了它才能给真正的实现批量添加。
            // 向raw_contact表添加一条记录
            // 此处.withValue("account_name", null)一定要加，不然会抛NullPointerException
            ContentProviderOperation operation1 = ContentProviderOperation.newInsert(uri)
                    .withValue("account_name", null).build();
            operations.add(operation1);
            // 向data添加数据
            uri = Uri.parse("content://com.android.contacts/data");
            // 添加姓名
            ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    // withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
                    .withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", mWhiteListContentList.get(i).getName())
                    .withYieldAllowed(true).build();
            operations.add(operation2);
            // 添加手机数据
            ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data2", "2")
                    .withValue("data1", mWhiteListContentList.get(i).getWhiteContent()).withYieldAllowed(true).build();
            operations.add(operation3);

        }
        try {
            // 这里才调用的批量添加
            resolver.applyBatch("com.android.contacts", operations);

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 对服务是否开启进行判断
     */
    private void anyMethod() {
        app = (MyApplication) getApplication();
        // 判断辅助功能是否开启
        if (isServiceOpening(this.getApplicationContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //先执行杀掉微信后台操作
                    BaseAccessibilityService mAccessibilityService = new BaseAccessibilityService();
                    mAccessibilityService.execShellCmd("am force-stop com.tencent.mm");
                    mAccessibilityService.execShellCmd("input keyevent 3");
                    app.setWhiteList(true);
                    app.setAllowWhite(true);
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    intentWechat(); //跳转微信主界面
                }
            }).start();

        } else if (isQQServiceOpening(this.getApplicationContext())) {
            app.setAllowQQWhite(true);
            app.setStart(true);
            intentQQ();
        } else {
            //跳转服务界面对话框
            mWhiteDialog = new WhiteDialog(WhiteListActivity.this, new Whiteback() {
                @Override
                public void WeChat() {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                }

                @Override
                public void QQ() {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));  //跳转服务界面
                }

                @Override
                public void Cancel() {
                    mWhiteDialog.dismiss();   //关闭
                }

            });
            mWhiteDialog.setContent("提示：" + "\n服务没有开启不能进行下一步");
            mWhiteDialog.setCancelable(true);
            mWhiteDialog.show();
        }
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }
        return true;
    }

    /****
     * 删除白名单
     */
    public void loadDeleteWhiteData(int devicesId) {
        String url = Url.deleteBaiMingDan;
        MyLogger.d("123", "设备号为：" + new MyApplication().getDeviceID().trim());
        OkHttpUtils
                .get()
                .url(url)
                .addParams("devicesId", devicesId + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(WhiteListActivity.this, "网络删除白名单失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("123", response);
                        if (MyJsonUtil.getBeanByJson(response)) {
                            MyToast.show(WhiteListActivity.this, "网络删除成功");
                        }
                    }
                });
    }
}
