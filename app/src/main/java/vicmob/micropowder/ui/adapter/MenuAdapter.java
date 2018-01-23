package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vicmob.earn.R;
import vicmob.micropowder.config.Constant;

/**
 * Created by Eren on 2017/6/16.
 * <p/>
 * 侧滑菜单适配器
 */
public class MenuAdapter extends BaseAdapter {

    private Context mContext;

    public MenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (Constant.MENUIMAGES != null && Constant.MENUNAMES != null) ? Constant.MENUIMAGES.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.menu_item, null);
            holder = new ViewHolder();
            holder.ivMenu = (ImageView) convertView.findViewById(R.id.iv_item_menu);
            holder.tvMenu = (TextView) convertView.findViewById(R.id.tv_item_menu);
            convertView.setTag(holder);
        }
        holder.ivMenu.setImageResource(Constant.MENUIMAGES[position]);
        holder.tvMenu.setText(Constant.MENUNAMES[position]);
        return convertView;
    }

    static class ViewHolder {
        ImageView ivMenu;
        TextView tvMenu;
    }
}
