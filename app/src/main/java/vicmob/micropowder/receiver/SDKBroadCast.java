package vicmob.micropowder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;

import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.utils.MyToast;

/**
 * Created by Eren on 2017/6/23.
 * <p/>
 * 百度SDK KEY校验广播
 */
public class SDKBroadCast extends BroadcastReceiver {
    private Context mContext;
    public SDKBroadCast(Context context) {
        mContext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getAction();
        // 网络错误广播
        if (result
                .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
            MyToast.show(mContext.getApplicationContext(), "无网络", MyApplication.isDeBug);
        }
        // KEY 校验失败
        else if (result
                .equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            MyToast.show(mContext.getApplicationContext(), "校验失败",MyApplication.isDeBug);
        }
    }

}