package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.NearbyPeopleBean;

/**
 * Created by Eren on 2017/6/29.
 * <p>
 * 附近人数据库操作
 */
public class NearbyPeopleDao {

    private DBHelper mDbHelper;
    private Dao<NearbyPeopleBean, Integer> mNearbyPeopleDao;


    public NearbyPeopleDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mNearbyPeopleDao = mDbHelper.getDao(NearbyPeopleBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(NearbyPeopleBean nearbyPeopleBean) {
        try {
            mNearbyPeopleDao.create(nearbyPeopleBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param nearbyPeopleBean
     */
    public void delete(NearbyPeopleBean nearbyPeopleBean) {
        try {
            mNearbyPeopleDao.delete(nearbyPeopleBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<NearbyPeopleBean> queryForAll() {
        List<NearbyPeopleBean> nearbyPeople = new ArrayList<>();
        try {
            nearbyPeople = mNearbyPeopleDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nearbyPeople;
    }

    /**
     * 查询特殊字段是否存在
     *
     * @param parameter
     * @return
     */
    public boolean queryOne(String parameter) {
        List<NearbyPeopleBean> mNearbyContent = null;
        try {
            mNearbyContent = mNearbyPeopleDao.queryBuilder().where().eq("NearbyContent", parameter).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mNearbyContent.size() != 0 ? true : false;
    }
}
