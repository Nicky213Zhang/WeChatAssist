package vicmob.micropowder.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vicmob.earn.R;


/**
 * Created by vicmob_yf002 on 2017/6/21.
 * <p/>
 * 读取进度
 */
public class LoadingDialog extends Dialog {
    private TextView content;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_load, null);
        content = (TextView) mView.findViewById(R.id.dialog_customer_title);
        super.setContentView(mView);
    }

    public LoadingDialog setContent(String s) {
        content.setText(s);
        return this;
    }
}
