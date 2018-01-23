package vicmob.micropowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vicmob.earn.R;

/**
 * Created by Eren on 2017/7/6.
 * <p/>
 * 地图适配器
 */
public class MapRvAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private LatLng currentLatlng;

    private List<String> district = new ArrayList<>();

    private List<String> key = new ArrayList<>();

    private List<Double> longtitudes = new ArrayList<>();

    private List<Double> latitudes = new ArrayList<>();


    public MapRvAdapter(Context context, List<String> district, List<String> key, List<Double> longtitudes, List<Double> latitudes) {
        mContext = context;
        this.district = district;
        this.key = key;
        this.longtitudes = longtitudes;
        this.latitudes = latitudes;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.search_item, parent, false);
        MapViewHolder mapViewHolder = new MapViewHolder(view);
        return mapViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MapViewHolder mapViewHolder = (MapViewHolder) holder;
        mapViewHolder.mName.setText(district.get(position));
        mapViewHolder.mKey.setText(key.get(position));

        mapViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return district != null ? district.size() : 0;
    }


    static class MapViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.add)
        TextView mKey;

        public MapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
