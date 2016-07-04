package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shmakova on 29.06.16.
 */
public class FancyBehavior<V extends View>
        extends CoordinatorLayout.Behavior<V> {

    public FancyBehavior() {
    }

    public FancyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)dependency.getLayoutParams()).getBehavior();

        if (behavior instanceof AppBarLayout.Behavior) {
            //child.setPadding(0, 0, 0, 0);
        }

        return true;
    }
}