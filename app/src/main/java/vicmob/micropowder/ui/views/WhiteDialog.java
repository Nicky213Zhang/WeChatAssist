package vicmob.micropowder.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vicmob.earn.R;
import vicmob.micropowder.config.Callback;
import vicmob.micropowder.config.MyApplication;
import vicmob.micropowder.config.Whiteback;

/**
 * Created by admin on 2017/7/27.
 */
public class WhiteDialog extends Dialog implements View.OnClickListener{
    Whiteback whiteback;
    private TextView content;
    private TextView wechatBtn;
    private TextView qqBtn;
    private TextView cancleBtn;


    public WhiteDialog(Context context,Whiteback whiteback) {

        super(context, R.style.ContentDialog);
        this.whiteback = whiteback;
        setCustomDialog();

    }


    private void setCustomDialog() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_white,null);
        wechatBtn =(TextView)mView.findViewById(R.id.dialog_confirm_wechat);
        qqBtn = (TextView)mView.findViewById(R.id.dialog_confirm_qq);
        cancleBtn =(TextView)mView.findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) mView.findViewById(R.id.dialog_confirm_title);
        wechatBtn.setOnClickListener(this);
        qqBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        super.setContentView(mView);
    }
    public WhiteDialog setContent(String s) {
        content.setText(s);
        return this;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_confirm_cancle:
                whiteback.Cancel();

                this.cancel();
                break;

            case R.id.dialog_confirm_wechat:

                whiteback.WeChat();
                this.cancel();
                break;
            case R.id.dialog_confirm_qq:

                whiteback.QQ();
                this.cancel();
                break;
        }
    }


}
