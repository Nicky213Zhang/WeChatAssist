package vicmob.micropowder.daoman;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 模拟定位数据库
 */
public class PxDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "applist.db";
    public final static String APP_TABLE_NAME = "app";
    private final static int DB_VERSION = 2;

    public PxDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " + APP_TABLE_NAME + "(package_name TEXT PRIMARY KEY," + "latitude DOUBLE,longitude DOUBLE,mnc Integer,lac Integer,cid Integer)";
        db.execSQL(CREATE_APP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TEMP_SUBSCRIBE = "drop table if exists app";
        db.execSQL(DROP_TEMP_SUBSCRIBE);
        String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " + APP_TABLE_NAME + "(package_name TEXT PRIMARY KEY," + "latitude DOUBLE,longitude DOUBLE,mnc Integer,lac Integer,cid Integer)";
        db.execSQL(CREATE_APP_TABLE);
    }
}
