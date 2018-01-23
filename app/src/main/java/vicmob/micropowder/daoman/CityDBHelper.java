package vicmob.micropowder.daoman;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Eren on 2017/7/4.
 * <p/>
 * 城市号码段
 */
public class CityDBHelper extends SQLiteOpenHelper {

    private Context content;

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "meituan_cities.db";

    // 数据库文件目标存放路径为系统默认位置，com.vicmob.weichatassist1 是你的包名
    private static String DB_PATH = "/data/data/vicmob.earn/databases/";
    private static String DB_NAME = "meituan_cities.db";
    private static String ASSETS_NAME = "meituan_cities.db";
    private SQLiteDatabase myDataBase = null;

    public CityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.content = context;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // 数据库已存在，do nothing.
        } else {
            // 创建数据库
            try {
                //文件夹是否存在
                File dir = new File(DB_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //数据库是否存在
                File dbf = new File(DB_PATH + DB_NAME);
                if (dbf.exists()) {//如果存在，先删除
                    dbf.delete();
                }
                SQLiteDatabase.openOrCreateDatabase(dbf, null);
                // 复制assets中的db文件到DB_PATH下
                copyDataBase();
            } catch (IOException e) {
                throw new Error("数据库创建失败");
            }
        }
    }

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = content.getAssets().open(ASSETS_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    // 检查数据库是否有效
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
