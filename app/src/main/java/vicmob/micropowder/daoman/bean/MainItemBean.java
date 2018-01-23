package vicmob.micropowder.daoman.bean;

/**
 * Created by Eren on 2017/6/16.
 */
public class MainItemBean {

    private String icon;

    private int imageId;

    public MainItemBean(String icon, int imageId) {
        this.icon = icon;
        this.imageId = imageId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
