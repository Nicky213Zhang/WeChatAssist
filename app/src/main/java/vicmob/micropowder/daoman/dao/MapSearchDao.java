package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.MapSearchBean;

/**
 * Created by Eren on 2017/7/6.
 * <p/>
 * 地图数据库操作
 */
public class MapSearchDao {

    private DBHelper mDbHelper;
    private Dao<MapSearchBean, Integer> mMapSearchDao;

    public MapSearchDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mMapSearchDao = mDbHelper.getDao(MapSearchBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(MapSearchBean mapSearchBean) {
        try {
            mMapSearchDao.create(mapSearchBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param mapSearchBean
     */
    public void delete(MapSearchBean mapSearchBean) {
        try {
            mMapSearchDao.delete(mapSearchBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<MapSearchBean> queryForAll() {
        List<MapSearchBean> mapSearch = new ArrayList<>();
        try {
            mapSearch = mMapSearchDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapSearch;
    }


    /**
     * 删除所有记录
     */
    public void deleteAll() {
        try {
            mMapSearchDao.delete(queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
