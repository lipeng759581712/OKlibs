package com.max.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * View工具，提供了view的绑定与解绑、设置view的背景等方法
 *
 * @author Created by elwinxiao on 2015/9/18.
 */
public class ViewUtils {

    /**
     * 绑定view，如果placeHolderView有父view，用view取代placeHolderView的位置（通过{@link #replaceView(View, View, ViewGroup.LayoutParams)}方法实现）；
     * 否则将placeHolderView的所有子view清空，并再嵌入view（通过{@link #embedView(View, View, ViewGroup.LayoutParams)}方法实现）
     *
     * @param view            待绑定的view
     * @param placeHolderView 用于装载view
     */
    public static void attachView(View view, View placeHolderView) {
        attachView(view, placeHolderView, null);
    }

    /**
     * 与{@link ViewUtils#attachView(View, View)}方法的功能相同，但可指定View的布局规格
     *
     * @param params 指定view布局规格
     */
    public static void attachView(View view, View placeHolderView, ViewGroup.LayoutParams params) {
        if (placeHolderView.getParent() != null) {
            replaceView(view, placeHolderView, params);
        } else {
            embedView(view, placeHolderView, params);
        }
    }

    /**
     * 清空parentView中所有的子View，并将view嵌入parentView中
     */
    public static void embedView(View view, View parentView) {
        embedView(view, parentView, null);
    }

    /**
     * 与{@link ViewUtils#embedView(View, View)}方法的功能相同，但可指定view的布局规格
     */
    public static void embedView(View view, View parentView, ViewGroup.LayoutParams params) {
        if (view == parentView) {
            return;
        }

        ViewGroup containerView = (ViewGroup) parentView;
//        containerView.removeAllViews();
        detachView(view);
        if (params != null) view.setLayoutParams(params);
        containerView.addView(view);
    }

    /**
     * 用view取代placeHolderView的位置
     */
    public static void replaceView(View view, View placeHolderView) {
        replaceView(view, placeHolderView, null);
    }

    /**
     * 与{@link ViewUtils#replaceView(View, View)}方法的功能相同，但可指定view的布局规格
     */
    public static void replaceView(View view, View placeHolderView, ViewGroup.LayoutParams params) {
        if (placeHolderView == view) {
            return;
        }

        if (params == null && view.getLayoutParams() == null) {
            params = placeHolderView.getLayoutParams();
        }

        ViewGroup directParent = (ViewGroup) placeHolderView.getParent();
        int oriId = placeHolderView.getId();

        int oriIndex = directParent.indexOfChild(placeHolderView);

        if (params != null) view.setLayoutParams(params);
        view.setId(oriId);

        directParent.removeView(placeHolderView);
        detachView(view);
        if (params == null) {
            directParent.addView(view, oriIndex);
        } else {
            directParent.addView(view, oriIndex, params);
        }
    }

    /**
     * 解除view与其父view的关联
     */
    public static void detachView(View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

//    /**
//     * 获取view在其父view中的索引
//     */
//    public static int findViewIndex(ViewGroup parent, View view) {
//        for (int i = 0; i < parent.getChildCount(); ++i) {
//            if (parent.getChildAt(i) == view) {
//                return i;
//            }
//        }
//
//        return -1;
//    }

    /**
     * 获取view的Rect
     * <p/>
     * Rect中包含了该view四条边在纵横坐标上的投影的坐标值，坐标原点为屏幕左上角的点，单位为px
     */
    public static Rect getViewScreenRect(View view) {
        int[] locOnScreen = new int[2];
        view.getLocationOnScreen(locOnScreen);

        int widthInPX = view.getMeasuredWidth();
        int heightInPX = view.getMeasuredHeight();

        return new Rect(locOnScreen[0], locOnScreen[1], locOnScreen[0] + widthInPX, locOnScreen[1] + heightInPX);
    }

    /**
     * 将Drawable对象设置为View的背景
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 将Bitmap对象设置为View的背景
     */
    public static void setBackgroundBitmap(View view, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(new BitmapDrawable(view.getResources(), bitmap));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    /**
     * 关闭View硬件加速
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableHardwareAcceleration(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * 移除全局布局侦听器
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeOnGlobalLayoutListener(victim);
        } else {
            observer.removeGlobalOnLayoutListener(victim);
        }
    }
}
