package vicmob.micropowder.ui.views;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lxj on 2016/8/26.
 */
public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    ViewDragHelper viewDragHelper;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private SwipeState mState = SwipeState.Close;//默认是关闭

    public enum SwipeState {
        Open, Close
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
    }

    /**
     * 摆放子View
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentWidth, 0, contentWidth + deleteWidth, contentHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);

        //在触摸的时候判断是否可以滑动
        if (!SwipeLayoutManager.create().isCanSwipe(this)) {
            //先关闭已经打开的
            SwipeLayoutManager.create().closeLayout();

            //而且不应该拦截,因为如果拦截，则会执行onTouch中的滑动逻辑
            result = false;
        }
        return result;
    }

    /**
     * 当View从界面移除的时候调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //清空已经打开的
        SwipeLayoutManager.create().clearSwipeLayout();
    }

    float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在onTouch中进行再次判断
        if (!SwipeLayoutManager.create().isCanSwipe(this)) {
            //再次请求父View不拦截
            requestDisallowInterceptTouchEvent(true);

            //不满足滑动的条件，则下面的滑动的逻辑不能执行
            return true;//自己消费，也不让listview接受
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //1.获取移动的坐标
                float moveX = event.getX();
                float moveY = event.getY();
                //2.计算移动的距离
                float deltaX = moveX - x;
                float deltaY = moveY - y;
                //3.判断滑动的距离是偏向于哪个方向
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    //说明是水平方向，那么我们认为用户想滑动条目，则要请求listview不应该拦截
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        viewDragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /**
         * 修正移动的值
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制contentView的移动
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteWidth) {
                    left = -deleteWidth;
                }
            } else if (child == deleteView) {
                //限制deleteView的移动
                if (left < (contentWidth - deleteWidth)) {
                    left = (contentWidth - deleteWidth);
                } else if (left > contentWidth) {
                    left = contentWidth;
                }

            }
            return left;
        }

        /**
         * 一般用来做伴随移动
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //contentView移动的时候，让deleteView也移动
            if (changedView == contentView) {
                int newLeft = deleteView.getLeft() + dx;
                deleteView.layout(newLeft, deleteView.getTop(), newLeft + deleteWidth, deleteView.getBottom());
            } else if (changedView == deleteView) {
                int newLeft = contentView.getLeft() + dx;
                contentView.layout(newLeft, contentView.getTop(), newLeft + contentWidth, contentView.getBottom());
            }

            //判断打开与关闭
            if (contentView.getLeft() == 0 && mState != SwipeState.Close) {
                //清除
                mState = SwipeState.Close;

                SwipeLayoutManager.create().clearSwipeLayout();
            } else if (contentView.getLeft() == -deleteWidth && mState != SwipeState.Open) {
                //需要记录
                mState = SwipeState.Open;

                SwipeLayoutManager.create().setSwipeLayout(SwipeLayout.this);
            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentView.getLeft() < -deleteWidth / 2) {
                //向左
                open();
            } else {
                //向右
                close();
            }
        }

    };

    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            //继续刷新
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }
}
