package vicmob.micropowder.daoman.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.CityDBHelper;
import vicmob.micropowder.daoman.bean.CityPhoneBean;
import vicmob.micropowder.utils.MyLogger;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/7/4.
 * <p/>
 * 通讯录省删改查操作
 */
public class CityPhoneDao {

    private Context mContext;
    /**
     * 城市号码段的数据库实例
     */
    private CityPhoneBean mCityPhoneBean;
    /**
     * 选中的运营商
     */
    private String mPhoneOpera;

    /**
     * 选中的城市
     */
    private String mPhoneArea;

    public CityPhoneDao(Context context) {
        mContext = context;
    }

    /**
     * 查询demo表中所有记录
     *
     * @return
     */
    public List<CityPhoneBean> findAll() {

        mPhoneOpera = PrefUtils.getString(mContext, "phone_opera", null);
        mPhoneArea = PrefUtils.getString(mContext, "phone_area", null);


        // 打开数据库输出流
        CityDBHelper cityDBHelper = new CityDBHelper(mContext);
        List<CityPhoneBean> cityPhoneBeanList = new ArrayList<>();

        try {
            cityDBHelper.createDataBase();
            SQLiteDatabase db = cityDBHelper.getWritableDatabase();

            // //这里支持类型MYSQL的limit分页操作
            String sqlContent = "select * from demo where area= '" + mPhoneArea + "' and opera = '"
                    + mPhoneOpera + "' order by RANDOM() limit 100";   //查询
            MyLogger.d("qqq", mPhoneArea);
            //MyLogger.d("qqq", mPhoneOpera);
            Cursor cursor = db.rawQuery(sqlContent, null);
            while (cursor.moveToNext()) {
                mCityPhoneBean = new CityPhoneBean();// 每次循环的时候都给对象初始化（重要），不然会添加的都是一个值
                mCityPhoneBean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                mCityPhoneBean.setName(cursor.getString(cursor.getColumnIndex("name")));  //号码段
                mCityPhoneBean.setArea(cursor.getString(cursor.getColumnIndex("area")));
                mCityPhoneBean.setOpera(cursor.getString(cursor.getColumnIndex("opera")));
                cityPhoneBeanList.add(mCityPhoneBean);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return cityPhoneBeanList;
    }
}
