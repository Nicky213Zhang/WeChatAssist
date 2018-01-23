package vicmob.micropowder.ui.activity;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import vicmob.earn.R;
import vicmob.micropowder.base.BaseActivity;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.LoadData;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Url;
import vicmob.micropowder.daoman.bean.CityNumberBean;
import vicmob.micropowder.daoman.bean.CityPhoneBean;
import vicmob.micropowder.daoman.dao.CityPhoneDao;
import vicmob.micropowder.ui.adapter.PhoneRvAdapter;
import vicmob.micropowder.ui.views.ConfirmDialog;
import vicmob.micropowder.ui.views.LoadingDialog;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.MyToast;
import vicmob.micropowder.utils.PrefUtils;
import vicmob.micropowder.utils.SpaceItemDecoration;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 通讯录
 */
public class PhoneActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_content_city)
    TextView mTvContentCity;
    @BindView(R.id.rl_city)
    RelativeLayout mRlCity;
    @BindView(R.id.rv_phone)
    RecyclerView mRvPhone;
    @BindView(R.id.but_begin)
    Button mButBegin;
    @BindView(R.id.clean_phone)
    RelativeLayout mCleanPhone;
    @BindView(R.id.open_phone)
    RelativeLayout mOpenPhone;
    @BindView(R.id.import_phone)
    RelativeLayout mImportPhone;
    @BindView(R.id.smash_phone)
    RelativeLayout mSmashPhone;
    @BindView(R.id.mobile)
    RadioButton mMobile;
    @BindView(R.id.telecom)
    RadioButton mTelecom;
    @BindView(R.id.unicom)
    RadioButton mUnicom;
    @BindView(R.id.rg_opera)
    RadioGroup mRgOpera;
    private MyApplication mApp;
    /**
     * 自定义对话框
     */
    private ConfirmDialog mConfirmDialog;
    /**
     * 跳转返回时的flag
     */
    private static final int REQUEST_CODE_PICK_CITY = 0;
    /**
     * 选中的城市
     */
    private String city;
    /**
     * 获取所有符合号段集合
     */
    private List<CityPhoneBean> mCityPhoneList;

    private List<String> phoneNumber = new ArrayList<>();
    private PhoneRvAdapter mRvAdapter;

    private LoadingDialog mLoadingDialog;

    /**
     * 导入通讯录
     */
    public static final int IMPORT_PHONE = 0;
    /**
     * 导入完成
     */
    public static final int IMPORT_PHONE_OVER = 1;
    /**
     * 清空号码段
     */
    public static final int CLEAN_PHONE = 2;
    /**
     * 清空通讯录
     */
    public static final int CLEAN_PHONE_BOOK = 3;
    /**
     * 通讯录清空完成
     */
    public static final int CLEAN_PHONE_OVER = 4;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IMPORT_PHONE:
                    //加载进度
                    mLoadingDialog = new LoadingDialog(PhoneActivity.this);
                    mLoadingDialog.setContent("正在导入通讯录...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;

                case IMPORT_PHONE_OVER:
                    mLoadingDialog.dismiss();

                    MyToast.show(PhoneActivity.this, "导入成功");
                    break;

                case CLEAN_PHONE:
                    MyToast.show(PhoneActivity.this, "清空成功");

                    break;

                case CLEAN_PHONE_BOOK:
                    //清空通讯录
                    mLoadingDialog = new LoadingDialog(PhoneActivity.this);
                    mLoadingDialog.setContent("正在清空通讯录...");
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.show();
                    break;

                case CLEAN_PHONE_OVER:
                    mLoadingDialog.dismiss();
                    MyToast.show(PhoneActivity.this, "清空成功");
                    break;
            }
        }
    };
    private CityNumberBean.AutoCityBean mCityNumberBean = new CityNumberBean.AutoCityBean();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        mApp = (MyApplication) getApplication();
        initRg();
        initRv();
    }

    /**
     * 初始化运营商
     */
    private void initRg() {
        if (mApp.mCityNumberList.size() > 0 && mApp.mCityNumberList != null) { //如果网络获取成功
            mCityNumberBean = mApp.mCityNumberList.get(0);
            //从网络里获取城市
            city = mCityNumberBean.getCity();
            mTvContentCity.setText(city);

            PrefUtils.putString(PhoneActivity.this, "phone_area", city);

            //从网络里获取运营商
            String operator = mCityNumberBean.getOperator();
            Log.i("123", operator);
            PrefUtils.putString(PhoneActivity.this, "phone_opera", operator);   //如果网络获取成功，设置成网络运营商
        } else {
            PrefUtils.putString(PhoneActivity.this, "phone_opera", "移动");   //如果网络获取失败，第一次默认是移动，不设置会出现空白状态
            PrefUtils.putString(PhoneActivity.this, "phone_area", "无锡");
            city = "无锡";
            mTvContentCity.setText("无锡");
            Log.i("123", "AAAA");
        }
        //从网络获取的运营商选中状态
        switch (PrefUtils.getString(PhoneActivity.this, "phone_opera", "移动")) {
            case "移动":
                mMobile.setChecked(true);
                break;

            case "电信":
                mTelecom.setChecked(true);
                break;

            case "联通":
                mUnicom.setChecked(true);
                break;
        }
        mRgOpera.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRv() {
        mRvPhone.setLayoutManager(new GridLayoutManager(this, 2));
        mRvAdapter = new PhoneRvAdapter(PhoneActivity.this, phoneNumber);
        mRvPhone.setAdapter(mRvAdapter);
        mRvPhone.addItemDecoration(new SpaceItemDecoration(10));

    }

    @OnClick({R.id.iv_back, R.id.rl_city, R.id.but_begin, R.id.clean_phone, R.id.open_phone, R.id.import_phone, R.id.smash_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:      //返回按钮
                outAnimation();
                break;
            case R.id.rl_city:      //选择城市
                startActivityForResult(new Intent(PhoneActivity.this, CityPickerActivity.class), REQUEST_CODE_PICK_CITY);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                break;
            case R.id.but_begin:    //生成号码段
                phoneNumber.clear();
                if (city == null) {     // 判断有没有选择城市
                    MyToast.show(PhoneActivity.this, "未选择城市");
                } else {
                    mRvPhone.setVisibility(View.VISIBLE);
                    CityPhoneDao cityPhoneDao = new CityPhoneDao(PhoneActivity.this);
                    mCityPhoneList = cityPhoneDao.findAll();
                    // 循环加入集合，后四位随机生成
                    for (int i = 0; i < mCityPhoneList.size(); i++) {
                        for (int j = 0; j < 5; j++) {
                            String phoneNum = mCityPhoneList.get(i).getName() + (int) (Math.random() * 9000 + 1000);
                            phoneNumber.add(phoneNum);
                        }
                    }
                    //刷新适配器
                    mRvAdapter = new PhoneRvAdapter(PhoneActivity.this, phoneNumber);
                    mRvPhone.setAdapter(mRvAdapter);
                }
                break;

            case R.id.clean_phone:      //导入通讯录

                if (phoneNumber.size() == 0) {
                    MyToast.show(PhoneActivity.this, "号码不能为空");
                } else {
                    mConfirmDialog = new ConfirmDialog(PhoneActivity.this, new Callback() {
                        @Override
                        public void Positive() {
                            new Thread( new Runnable() {
                                @Override
                                public void run() {
                                    myHandler.sendEmptyMessage(IMPORT_PHONE);
                                    try {
                                        testAddContactsInTransaction();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    myHandler.sendEmptyMessage(IMPORT_PHONE_OVER);
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

            case R.id.open_phone:       // 打开联系人界面

                Intent intent = new Intent();
                intent.setClassName("com.android.contacts", "com.android.contacts.activities.PeopleActivity");
                startActivity(intent);
                break;
            case R.id.import_phone:     //清空号码段
                if (phoneNumber.size() == 0) {
                    MyToast.show(PhoneActivity.this, "号码为空");
                } else {
                    mConfirmDialog = new ConfirmDialog(PhoneActivity.this, new Callback() {
                        @Override
                        public void Positive() {
                            phoneNumber.clear();
                            mRvPhone.setVisibility(View.GONE);
                            myHandler.sendEmptyMessage(CLEAN_PHONE);
                        }
                        @Override
                        public void Negative() {
                            mConfirmDialog.dismiss();
                        }
                    });
                    mConfirmDialog.setContent("提示：" + "\n是否清空电话号码");
                    mConfirmDialog.setCancelable(true);
                    mConfirmDialog.show();
                }
                break;
            case R.id.smash_phone:      //清空通讯录
                mConfirmDialog = new ConfirmDialog(PhoneActivity.this, new Callback() {
                    @Override
                    public void Positive() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                myHandler.sendEmptyMessage(CLEAN_PHONE_BOOK);
                                clearContent();
//                                cleanphone();
                                myHandler.sendEmptyMessage(CLEAN_PHONE_OVER);
                            }
                        }).start();
                    }

                    @Override
                    public void Negative() {
                        mConfirmDialog.dismiss();
                    }
                });
                mConfirmDialog.setContent("提示：" + "\n是否清空通讯录");
                mConfirmDialog.setCancelable(true);
                mConfirmDialog.show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                mTvContentCity.setText(city);
                PrefUtils.putString(PhoneActivity.this, "phone_area", city);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mobile:
                PrefUtils.putString(PhoneActivity.this, "phone_opera", "移动");
                break;

            case R.id.telecom:
                PrefUtils.putString(PhoneActivity.this, "phone_opera", "电信");
                break;

            case R.id.unicom:
                PrefUtils.putString(PhoneActivity.this, "phone_opera", "联通");
                break;
        }
    }


    /**
     * 清空系统通信录数据
     */
    public void clearContent() {
        ContentResolver cr = PhoneActivity.this.getContentResolver();// 获取
        // ContentResolver对象查询在ContentProvider里定义的共享对象
        // 根据URI对象ContactsContract.Contacts.CONTENT_URI查询所有联系人
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        try {
            // 如果记录不为空
            if (cursor != null) {
                // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
                // 下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
                // 循环
                while (cursor.moveToNext()) {
                    String name = cursor
                            .getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
                    // 根据姓名求id
                    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

                    Cursor cursor1 = cr.query(uri, new String[]{ContactsContract.Data._ID}, "display_name=?", new String[]{name},
                            null);
                    // 除去姓名对应的号码
                    if (cursor1.moveToFirst()) {
                        int id = cursor1.getInt(0);
                        cr.delete(uri, "display_name=?", new String[]{name});
                        // 根据id删除data中的相应数据
                        uri = Uri.parse("content://com.android.contacts/data");
                        cr.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                    }
                    cursor1.close();// Cursor循环内再申请Cursor，记得将内部申请的每个Cursor都加上close
                }
                cursor.close();

            } else {
                Toast.makeText(PhoneActivity.this, "通讯录为空", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

//    private void cleanphone(){
//        ContentResolver cr = PhoneActivity.this.getContentResolver();// 获取
//        Cursor contactsCur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        while(contactsCur.moveToNext()){
//            //获取ID
//            String rawId = contactsCur.getString(contactsCur.getColumnIndex(ContactsContract.Contacts._ID));
//            //删除
//            String where = ContactsContract.Data._ID  + " =?";
//            String[] whereparams = new String[]{rawId};
//            getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams);
//        }
//    }


    /**
     * 批量添加联系人到通讯录中
     *
     * @throws Exception
     */
    public void testAddContactsInTransaction() throws Exception {
        ArrayList<ContentProviderOperation> mOperations = new ArrayList<>();
        ContentResolver resolver = PhoneActivity.this.getContentResolver();
        int rawContactInsertIndex;
        // 循环添加
        for (int i = 0; i < phoneNumber.size(); i++) {
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            rawContactInsertIndex = mOperations.size();// 这句好很重要，有了它才能给真正的实现批量添加。
            // 向raw_contact表添加一条记录
            // 此处.withValue("account_name", null)一定要加，不然会抛NullPointerException
            ContentProviderOperation operation1 = ContentProviderOperation.newInsert(uri)
                    .withValue("account_name", null).build();
            mOperations.add(operation1);
            // 向data添加数据
            uri = Uri.parse("content://com.android.contacts/data");
            // 添加姓名
            ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    // withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
                    .withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", phoneNumber.get(i))
                    .withYieldAllowed(true).build();
            mOperations.add(operation2);
            // 添加手机数据
            ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", rawContactInsertIndex)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data2", "2")
                    .withValue("data1", phoneNumber.get(i)).withYieldAllowed(true).build();
            mOperations.add(operation3);

        }
        try {
            // 这里才调用的批量添加
            resolver.applyBatch("com.android.contacts", mOperations);

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }
}
