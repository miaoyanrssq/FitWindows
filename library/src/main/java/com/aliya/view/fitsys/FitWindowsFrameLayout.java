package com.aliya.view.fitsys;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * 自定义拓展FitSystemWindow属性 - FrameLayout.
 * 可以分别适配状态栏和导航栏
 * <p>
 * 通过自定属性fitType来设置:
 * <code>
 * app:fitType="both"      // 默认属性
 * app:fitType="top"       // fit仅适配状态栏
 * app:fitType="bottom"    // fit仅适配导航栏
 * </code>
 *
 * @author a_liYa
 * @date 2017/7/19 上午11:24.
 */
public class FitWindowsFrameLayout extends FrameLayout {

    Rect rectInsets;
    WindowInsets windowInsets;
    private FitHelper helper;

    public FitWindowsFrameLayout(Context context) {
        this(context, null);
    }

    public FitWindowsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitWindowsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new FitHelper(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (windowInsets != null || rectInsets != null) { // 系统已分发过，此时需要手动分发给子View
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                if (rectInsets != null) {
                    super.fitSystemWindows(rectInsets);
                }
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                if (windowInsets != null) {
                    child.dispatchApplyWindowInsets(windowInsets);
                }
            }
        }
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        windowInsets = insets;
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        rectInsets = new Rect(insets);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            if (getFitsSystemWindows()) {
                setFitsSystemWindows(false);
                int left = insets.left;
                int top = insets.top;
                int right = insets.right;
                int bottom = insets.bottom;
                super.fitSystemWindows(insets);
                insets.set(left, top, right, bottom);
                setFitsSystemWindows(true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (getFitsSystemWindows()) {
                insets = helper.fitSystemWindows(insets);
            }
        }
        return false & super.fitSystemWindows(insets);
    }

}
