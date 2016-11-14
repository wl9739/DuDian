package com.wl.dudian.app.newsdetail;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * 自定义 Behavior
 * Created by Qiushui on 2016/11/11.
 */

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean isAnimationOut = false;

    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        // 处理垂直方向上的滚动事件
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
        child.setTranslationY(0);
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        // 向上滚动进入, 向下滚动隐藏
        if (dyConsumed > 20 && !this.isAnimationOut && child.getVisibility() == View.VISIBLE) {
            // 如果是展开的就先收回去
            if (child.isExpanded()) {
                child.collapse();
            }
            // animateOut()和animateIn()都是私有方法, 需要重新实现
            animateOut(child);
        } else if (dyConsumed < -20 && child.getVisibility() != View.VISIBLE) {
            animatæeIn(child);
        }
    }

    private void animatæeIn(FloatingActionsMenu child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child).translationY(0)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(null)
                .start();
    }

    private void animateOut(FloatingActionsMenu child) {
        ViewCompat.animate(child)
                .translationY(500)
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        FloatingActionButtonBehavior.this.isAnimationOut = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        FloatingActionButtonBehavior.this.isAnimationOut = false;
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        FloatingActionButtonBehavior.this.isAnimationOut = false;
                    }
                })
                .start();
    }
}
