package vicmob.micropowder.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Apple on 2016/9/30.
 * 缓存的工具类：缓存服务器返回的json字符串
 */
public final class CacheUtils {

    //缓存json
    public static void saveCache(Context context,String url,String json) throws  Exception{
        //文件名
        String name = Md5Utils.encode(url);
        //输出流
        FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
        //写数据
        fileOutputStream.write(json.getBytes());
        //关流
        fileOutputStream.close();
    }

    //读取json
    public static String readCache(Context context,String url)throws  Exception{
        //文件名
        String name = Md5Utils.encode(url);
        FileInputStream fileInputStream = context.openFileInput(name);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fileInputStream.read(buffer)) != -1){
            bos.write(buffer,0,len);
        }
        String json = bos.toString();
        bos.close();
        fileInputStream.close();
        return json;
    }
}
