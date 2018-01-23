package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vicmob.earn.R;

/**
 * Created by Administrator on 2017/1/6.
 * <p/>
 * 地图搜索适配器
 */
public class MyAdapter extends BaseAdapter {
    // 搜索地址名集合
    private List<String> searchAddressesName = new ArrayList<String>();
    // 搜索地址集合
    private List<String> searchAddresses = new ArrayList<String>();

    private LayoutInflater layoutInflater;
    private Context context;

    public MyAdapter(List<String> searchAddressesName, List<String> searchAddresses, Context context) {
        super();
        this.searchAddressesName = searchAddressesName;
        this.searchAddresses = searchAddresses;
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return searchAddressesName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return searchAddressesName.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.search_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.name);
            holder.tv_add = (TextView) convertView.findViewById(R.id.add);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(searchAddressesName.get(position));
        holder.tv_add.setText(searchAddresses.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView tv_name;
        private TextView tv_add;
    }

}
