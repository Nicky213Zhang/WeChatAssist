package vicmob.micropowder.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vicmob.earn.R;
import vicmob.micropowder.config.Callback;


/**
 * Created by Teprinciple on 2016/10/13.
 * <p/>
 * 自定义对话框
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {

    Callback callback;
    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    /**
     * 高仿苹果自定义对话框
     *
     * @param context
     * @param callback
     */
    public ConfirmDialog(Context context, Callback callback) {
        super(context, R.style.CustomDialog);
        this.callback = callback;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);
        sureBtn = (TextView) mView.findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView) mView.findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);
        sureBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        super.setContentView(mView);
    }


    public ConfirmDialog setContent(String s) {
        content.setText(s);
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm_cancle:
                callback.Negative();
                this.cancel();
                break;

            case R.id.dialog_confirm_sure:
                callback.Positive();
                this.cancel();
                break;
        }
    }

}
