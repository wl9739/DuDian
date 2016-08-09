package com.wl.dudian.app.viewmodel;

import android.databinding.BaseObservable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 *
 * Created by Qiushui on 16/8/8.
 */

public class NewsDetailVM extends BaseObservable {

    private ImageView backgroundImage;

    private TextView titleTv;

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private AppBarLayout appBarLayout;

    private WebView webView;

    private CoordinatorLayout coordinatorLayout;

    private FloatingActionsMenu floatingActionsMenu;

    private FloatingActionButton favoriteBtn;

    private FloatingActionButton discussBtn;

    private FloatingActionButton shareBtn;

}
