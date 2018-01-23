package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vicmob.earn.R;
import vicmob.micropowder.daoman.bean.MainItemBean;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 主页面适配器
 */
public class MainAdapter extends BaseAdapter {

    private List<MainItemBean> mMainItemBeanList;

    private LayoutInflater inflater;

    public MainAdapter(Context context, String[] names, int[] images) {
        super();
        mMainItemBeanList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < images.length; i++) {
            MainItemBean mainItemBean = new MainItemBean(names[i], images[i]);
            mMainItemBeanList.add(mainItemBean);
        }
    }


    @Override
    public int getCount() {
        return mMainItemBeanList != null ? mMainItemBeanList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mMainItemBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_card);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_card);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mMainItemBeanList.get(position).getIcon());
        viewHolder.image.setImageResource(mMainItemBeanList.get(position).getImageId());
        return convertView;
    }

    class ViewHolder {
        public TextView name;
        public ImageView image;
    }
}
