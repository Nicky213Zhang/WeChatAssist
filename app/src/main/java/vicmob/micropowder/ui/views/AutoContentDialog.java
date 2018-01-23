package vicmob.micropowder.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import vicmob.earn.R;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.utils.PrefUtils;

/**
 * Created by Twisted on 2017/7/4.
 */

public class AutoContentDialog extends Dialog implements View.OnClickListener {
    Callback callback;
    Context context;
    ImageView mIvAutoReply;
    EditText mEtAutoReplyKeyword;
    EditText mEtAutoReplyContent;
    Button mBtnAutoReplyConfirem;


    @Override
    protected void onStart() {
        super.onStart();
        //在布局可见时清空编辑框
        String strKeyword = PrefUtils.getString(context, "strKeyword", null);
        String strContent = PrefUtils.getString(context, "strContent", null);
        if (strContent != null || strKeyword != null) {
            mEtAutoReplyKeyword.setText(strKeyword);
            mEtAutoReplyContent.setText(strContent);
        }

    }

    public AutoContentDialog(Context context, Callback callback) {
        super(context, R.style.ContentDialog);
        this.context = context;
        this.callback = callback;
        setContentDialog();
    }

    private void setContentDialog() {
        //初始化控件
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_auto_reply, null);
        mIvAutoReply = (ImageView) mView.findViewById(R.id.iv_auto_reply);
        mEtAutoReplyKeyword = (EditText) mView.findViewById(R.id.et_auto_reply_keyword);
        mEtAutoReplyContent = (EditText) mView.findViewById(R.id.et_auto_reply_content);
        mBtnAutoReplyConfirem = (Button) mView.findViewById(R.id.btn_auto_reply_confirem);
        mIvAutoReply.setOnClickListener(this);
        mBtnAutoReplyConfirem.setOnClickListener(this);
        super.setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_auto_reply:
                //清空Edit
                PrefUtils.putString(context, "strKeyword", null);
                PrefUtils.putString(context, "strContent", null);
                callback.Negative();
                this.cancel();
                break;
            case R.id.btn_auto_reply_confirem:
                //点击确认按钮时把editview 里的内容以sp的方式存储起来
                String Keyword = mEtAutoReplyKeyword.getText().toString().trim();
                String Content = mEtAutoReplyContent.getText().toString().trim();
                PrefUtils.putString(context, "autoKeyword", Keyword);
                PrefUtils.putString(context, "autoContent", Content);
                PrefUtils.putString(context, "strKeyword", null);
                PrefUtils.putString(context, "strContent", null);
                callback.Positive();
                //this.cancel();
                break;

        }
    }

    //点击返回键按钮时清空关键词和回复内容的额编辑框
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //清空Edit
            PrefUtils.putString(context, "strKeyword", null);
            PrefUtils.putString(context, "strContent", null);

        }
        return super.onKeyDown(keyCode, event);
    }
}
