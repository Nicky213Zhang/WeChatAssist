package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.PublicNumBean;

/**
 * Created by Eren on 2017/6/28.
 * <p>
 * 朋友圈数据的增删改查
 */
public class PublicNumDao {

    private DBHelper mDbHelper;
    private Dao<PublicNumBean, Integer> mPublicNumDao;

    public PublicNumDao(Context context) {
        try {
            mDbHelper = new DBHelper(context);
            mPublicNumDao = mDbHelper.getDao(PublicNumBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     */
    public void add(PublicNumBean mPublicNumBean) {
        try {
            mPublicNumDao.create(mPublicNumBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除一条数据
     * @param mPublicNumBean
     */
    public void delete(PublicNumBean mPublicNumBean) {
        try {
            mPublicNumDao.delete(mPublicNumBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条数据
     * @param mPublicNumBean
     */
    public void update(PublicNumBean mPublicNumBean) {
        try {
            mPublicNumDao.update(mPublicNumBean);
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
    public PublicNumBean queryForId(int id) {
        PublicNumBean publicNumBean = null;
        try {
            publicNumBean = mPublicNumDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicNumBean;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<PublicNumBean> queryForAll() {
        List<PublicNumBean> publicNumBeanList = new ArrayList<PublicNumBean>();
        try {
            publicNumBeanList = mPublicNumDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicNumBeanList;
    }

    /**
     * 删除所有记录
     */
    public List<PublicNumBean> deleteAll() {
        List<PublicNumBean> publicNumBeanList = new ArrayList<PublicNumBean>();
        try {
            for (int i = 0; i < publicNumBeanList.size(); i++) {
                mPublicNumDao.delete(publicNumBeanList.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicNumBeanList;
    }


}
