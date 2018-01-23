package vicmob.micropowder.ui.views;

/**
 * 负责打开的item的记录，清除，以及判断是否有打开的条目
 * Created by lxj on 2016/8/26.
 */
public class SwipeLayoutManager {
    private static SwipeLayoutManager mInstance = new SwipeLayoutManager();

    public SwipeLayoutManager() {

    }

    public static SwipeLayoutManager create() {
        return mInstance;
    }

    private SwipeLayout openLayout;

    public void setSwipeLayout(SwipeLayout swipeLayout) {
        openLayout = swipeLayout;
    }

    /**
     * 清除所记录的SwipeLayout
     */
    public void clearSwipeLayout() {
        openLayout = null;
    }

    /**
     * 判断当前是否可以滑动
     *
     * @return
     */
    public boolean isCanSwipe(SwipeLayout currentLayout) {
        if (openLayout == null) {
            //说明当前木有打开的，那么可以滑动
            return true;
        } else {
            //说明当前有打开的，那么还需要判断当前滑动的和打开的是否是同一个，
            // 如果是同一个则可以滑动，否则不可以
            return openLayout == currentLayout;
        }
    }

    /**
     * 关闭已经打开的条目
     */
    public void closeLayout() {
        if (openLayout != null) {
            openLayout.close();
        }
    }
}
