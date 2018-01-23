package vicmob.micropowder.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Apple on 2016/9/26.
 * 吐司的工具类
 */
public final class MyToast {

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void show(Context context, String msg,Boolean b) {
        if(b){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
