package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vicmob.earn.R;

/**
 * Created by Eren on 2017/7/3.
 * <p/>
 * 通讯录RecyclerView适配器
 */
public class PhoneRvAdapter extends RecyclerView.Adapter {

    private List<String> mCityPhoneBeanList;

    private Context mContext;
    private String mPhoneNumber;

    public PhoneRvAdapter(Context context, List<String> mCityPhoneBeanList) {
        mContext = context;
        this.mCityPhoneBeanList = mCityPhoneBeanList;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.phone_number, parent, false);
        PhoneViewHolder phoneViewHolder = new PhoneViewHolder(view);
        return phoneViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhoneViewHolder phoneViewHolder = (PhoneViewHolder) holder;
        mPhoneNumber = mCityPhoneBeanList.get(position);
        phoneViewHolder.mTvPhoneNum.setText((position + 1) + "、" + mPhoneNumber);
    }

    @Override
    public int getItemCount() {
        return mCityPhoneBeanList != null ? mCityPhoneBeanList.size() : 0;
    }

    static class PhoneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_phone_num)
        TextView mTvPhoneNum;

        public PhoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
