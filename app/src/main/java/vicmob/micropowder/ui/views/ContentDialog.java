package vicmob.micropowder.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import vicmob.earn.R;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Eren on 2017/6/27.
 * <p/>
 * 仿苹果自定义Dialog
 */
public class ContentDialog extends Dialog implements View.OnClickListener {

    Callback callback;
    Context context;
    private ImageView ivCancel;
    private EditText etContent;
    private Button butContent;

    /**
     * 仿苹果自定义Dialog
     *
     * @param context  上下文
     * @param callback 按钮回调
     */
    public ContentDialog(Context context, Callback callback) {
        super(context, R.style.ContentDialog);
        this.context = context;
        this.callback = callback;
        setContentDialog();
    }

    private void setContentDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_content, null);
        ivCancel = (ImageView) mView.findViewById(R.id.iv_cancel);
        etContent = (EditText) mView.findViewById(R.id.et_content);
        butContent = (Button) mView.findViewById(R.id.but_content);

        ivCancel.setOnClickListener(this);
        butContent.setOnClickListener(this);
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                callback.Negative();
                //this.cancel();
                break;

            case R.id.but_content:
                //把输入内容储存到SP中
                String ContentDialogText = etContent.getText().toString().trim();
                PrefUtils.putString(context, "ContentDialogText", ContentDialogText);
                callback.Positive();
                //this.cancel();
                break;
        }
    }
}
