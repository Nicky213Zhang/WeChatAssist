package vicmob.micropowder.daoman.bean;

/**
 * Created by Eren on 2017/6/16.
 */
public class MenuItemBean {

    private String menuNames;

    private int menuImages;

    public MenuItemBean(String menuNames, int menuImages) {

        this.menuNames = menuNames;
        this.menuImages = menuImages;
    }

    public int getMenuImages() {
        return menuImages;
    }

    public void setMenuImages(int menuImages) {
        this.menuImages = menuImages;
    }

    public String getMenuNames() {

        return menuNames;
    }

    public void setMenuNames(String menuNames) {
        this.menuNames = menuNames;
    }
}
