package vicmob.micropowder.daoman.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjing on 2017/8/1.
 */

public class GpsBean {
    public static List<LacCidBean> getJSONlist(JSONArray array) throws Exception {
        List<LacCidBean> orgList = new ArrayList<LacCidBean>();

        for (int i = 0; i < array.length(); i++) {
            int b = array.length();
            if (i > b - 1) {
                return orgList;
            }
            JSONObject jsonObj = array.getJSONObject(i);
            LacCidBean model = new LacCidBean();
            model.acc = jsonObj.getInt("acc");
            model.mnc = jsonObj.getInt("mnc");
            model.lac = jsonObj.getInt("lac");
            model.ci = jsonObj.getInt("ci");
            List<LacCidBean> locList = new ArrayList<LacCidBean>();
            JSONObject jl = jsonObj.getJSONObject("location");
            for (int j = 0; j < jl.length(); j++) {
                LacCidBean loc = new LacCidBean();
                loc.lon = jl.getDouble("lon");
                loc.lat = jl.getDouble("lat");
                locList.add(loc);
            }
            model.location = locList;
            orgList.add(model);
        }
        return orgList;
    }

    public static class LacCidBean {
        public int mnc;
        public int lac;
        public int ci;
        public int acc;
        public double lon;
        public double lat;
        public List<LacCidBean> location;
    }

}
