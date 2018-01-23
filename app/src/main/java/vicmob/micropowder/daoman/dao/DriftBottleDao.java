package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.DriftBottleContentBean;


/**
 * Created by Eren on 2017/6/28.
 * <p>
 * 漂流瓶数据的增删改查
 */
public class DriftBottleDao {

    private DBHelper mDbHelper;
    private Dao<DriftBottleContentBean, Integer> mDriftBottleDao;

    public DriftBottleDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mDriftBottleDao = mDbHelper.getDao(DriftBottleContentBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(DriftBottleContentBean driftBottleContentBean) {
        try {
            mDriftBottleDao.create(driftBottleContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param driftBottleContentBean
     */
    public void delete(DriftBottleContentBean driftBottleContentBean) {
        try {
            mDriftBottleDao.delete(driftBottleContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param driftBottleContentBean
     */
    public void update(DriftBottleContentBean driftBottleContentBean) {
        try {
            mDriftBottleDao.update(driftBottleContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    public DriftBottleContentBean queryForId(int id) {
        DriftBottleContentBean driftBottle = null;
        try {
            driftBottle = mDriftBottleDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driftBottle;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<DriftBottleContentBean> queryForAll() {
        List<DriftBottleContentBean> driftBottle = new ArrayList<DriftBottleContentBean>();
        try {
            driftBottle = mDriftBottleDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driftBottle;
    }

    /**
     * 删除所有记录
     */
    public List<DriftBottleContentBean> deleteAll() {
        List<DriftBottleContentBean> driftBottle = new ArrayList<DriftBottleContentBean>();
        try {
            for (int i = 0; i < driftBottle.size(); i++) {
                mDriftBottleDao.delete(driftBottle.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driftBottle;
    }

    /**
     * 查询特殊字段是否存在
     *
     * @param parameter
     * @return
     */
    public boolean queryOne(String parameter) {
        List<DriftBottleContentBean> mDriftBottleContent = null;
        try {
            mDriftBottleContent = mDriftBottleDao.queryBuilder().where().eq("BottleContent", parameter).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mDriftBottleContent.size() != 0 ? true : false;
    }
}
