package vicmob.micropowder.daoman.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vicmob.micropowder.daoman.DBHelper;
import vicmob.micropowder.daoman.bean.AccountBean;

/**
 * Created by Twisted on 2017/7/3.
 */

public class AccountDao  {
    private DBHelper mDbHelper;
    private Dao<AccountBean, Integer> mAccountDao;

    public AccountDao(Context context) {

        try {
            mDbHelper =  new DBHelper(context);
            mAccountDao = mDbHelper.getDao(AccountBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加一条数据
     */
    public void add(AccountBean accountBean) {
        try {
            mAccountDao.create(accountBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param
     */
    public void delete(AccountBean accountBean) {
        try {
            mAccountDao.delete(accountBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新一条记录
     *
     * @param
     */
    public void update(AccountBean accountBean) {
        try {
            mAccountDao.update(accountBean);
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
    public AccountBean queryForId(int id) {
        AccountBean account = null;
        try {
            account = mAccountDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<AccountBean> queryForAll() {
        List<AccountBean> account = new ArrayList<AccountBean>();
        try {
            account = mAccountDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * 删除所有记录
     */
    public List<AccountBean> deleteAll() {
        List<AccountBean> account = new ArrayList<AccountBean>();
        try {
            for (int i = 0; i < account.size(); i++) {
                mAccountDao.delete(account.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * 查询特殊字段是否存在
     *
     * @param parameter
     * @return
     */
    public boolean queryOne(String parameter) {
        List<AccountBean> mAccountBean = null;
        try {
            mAccountBean = mAccountDao.queryBuilder().where().eq("UserName", parameter).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mAccountBean.size() != 0 ? true : false;
    }

}
