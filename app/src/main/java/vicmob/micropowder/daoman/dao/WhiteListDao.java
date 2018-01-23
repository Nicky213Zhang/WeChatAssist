package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.WhiteListContentBean;

/**
 * Created by admin on 2017/7/5.
 */
public class WhiteListDao {

    private DBHelper mDbHelper;
    private Dao<WhiteListContentBean, Integer> mWhiteListDao;

    public WhiteListDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mWhiteListDao = mDbHelper.getDao(WhiteListContentBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(WhiteListContentBean whiteListContentBean) {
        try {
            mWhiteListDao.create(whiteListContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param whiteListContentBean
     */
    public void delete(WhiteListContentBean whiteListContentBean) {
        try {
            mWhiteListDao.delete(whiteListContentBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param whiteListContentBean
     */
    public void update(WhiteListContentBean whiteListContentBean) {
        try {
            mWhiteListDao.update(whiteListContentBean);
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
    public WhiteListContentBean queryForId(int id) {
        WhiteListContentBean whiteList = null;
        try {
            whiteList = mWhiteListDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whiteList;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<WhiteListContentBean> queryForAll() {
        List<WhiteListContentBean> whiteList = new ArrayList<WhiteListContentBean>();
        try {
            whiteList = mWhiteListDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return whiteList;
    }

    /**
     * 删除所有记录
     */
    public void deleteAll(List<WhiteListContentBean> whiteListContentBeen) {
        for (WhiteListContentBean whiteListContentBean : whiteListContentBeen) {
            delete(whiteListContentBean);
        }
    }
}
