package com.wl.dudian.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wl.dudian.R;
import com.wl.dudian.app.fragment.AboutFragment;
import com.wl.dudian.app.fragment.ColumnFragment;
import com.wl.dudian.app.fragment.FavoriteFragment;
import com.wl.dudian.app.fragment.LatestNewsFragment;
import com.wl.dudian.app.fragment.SettingsFragment;
import com.wl.dudian.framework.ACache;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.Constants;
import com.wl.dudian.framework.ScreenShotUtils;
import com.wl.dudian.framework.Variable;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity11111";
    private static final String ARGUR = "ARGU";
    private static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout mConnectFalseRl;
    private ImageView mNoNetImage;
    private CoordinatorLayout mCollapsingToolbarLayout;
    private FrameLayout mContentFl;

    private LatestNewsFragment mLatestNewsFragment;
    private FavoriteFragment mFavoriteFragment;
    private ColumnFragment mColumnFragment;
    private SettingsFragment mSettingsFragment;
    private AboutFragment mAboutFragment;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main_swiperefresh);
        mConnectFalseRl = (RelativeLayout) findViewById(R.id.content_main_notconnected_rl);
        mContentFl = (FrameLayout) findViewById(R.id.content_main);
        mCollapsingToolbarLayout = (CoordinatorLayout) findViewById(R.id.app_bar_main_coordinatorlayout);
        mNoNetImage = (ImageView) findViewById(R.id.content_main_wifilogo_img);

        if (BusinessUtil.isNetConnected(this)) {
            mContentFl.setVisibility(View.VISIBLE);
            mConnectFalseRl.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mNoNetImage.setVisibility(View.GONE);
            showLatestNews(savedInstanceState);
        } else {
            mConnectFalseRl.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mNoNetImage.setVisibility(View.VISIBLE);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("读点日报");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MainActiivty");
    }

    private void showLatestNews(Bundle savedInstanceState) {

        FragmentManager fm = getSupportFragmentManager();
//        if (null != savedInstanceState) {
//            index = savedInstanceState.getInt(FRAGMENT_INDEX);
//            setNavSelection(index);
//        } else {

        mLatestNewsFragment = (LatestNewsFragment) fm.findFragmentById(R.id.content_main);
        if (mLatestNewsFragment == null) {
            mLatestNewsFragment = LatestNewsFragment.newInstance();
            fm.beginTransaction().add(R.id.content_main, mLatestNewsFragment, mLatestNewsFragment.getClass().getName()).
                    commit();
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (null != mLatestNewsFragment) {
                        String isRefreshed = ACache.get(MainActivity.this).getAsString(Constants.REFRESH_NETWORK);
                        if (null == isRefreshed || !isRefreshed.equals(Constants.REFRESH_FLAG)) {
                            mLatestNewsFragment.refreshLatestNewsInfo();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            });

            mLatestNewsFragment.setOnRefreshedListener(new LatestNewsFragment.OnRefreshedListener() {
                @Override
                public void onRefreshed() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(mCollapsingToolbarLayout, "刷新数据成功", Snackbar.LENGTH_SHORT).show();

                }

                @Override
                public void onRefreshError() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(mCollapsingToolbarLayout, "主人, 刷新失败, 咱们有没有连上网啊?", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

//        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setNavSelection(0);
                break;
            case R.id.nav_column:
                setNavSelection(1);
                break;
            case R.id.nav_favorite:
                setNavSelection(2);
                break;
            case R.id.nav_daynight:
                changeDayNightModel();
                break;
            case R.id.nav_setting:
                setNavSelection(3);
                break;
            case R.id.nav_about:
                setNavSelection(4);
                break;
            default:
                break;
        }
        return false;
    }

    private void setNavSelection(int i) {
        index = i;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragments(ft);
        switch (i) {
            case 0:
                if (null == mLatestNewsFragment) {
                    mLatestNewsFragment = LatestNewsFragment.newInstance();
                    ft.add(R.id.content_main, mLatestNewsFragment);
                } else {
                    ft.show(mLatestNewsFragment);
                }
                break;
            case 1:
                if (null == mColumnFragment) {
                    mColumnFragment = ColumnFragment.newInstance();
                    ft.add(R.id.content_main, mColumnFragment);
                } else {
                    ft.show(mColumnFragment);
                }
                break;
            case 2:
                if (null == mFavoriteFragment) {
                    mFavoriteFragment = FavoriteFragment.newInstance();
                    ft.add(R.id.content_main, mFavoriteFragment);
                } else {
                    ft.show(mFavoriteFragment);
                }
                break;
            case 3:
                if (null == mSettingsFragment) {
                    mSettingsFragment = SettingsFragment.newInstance();
                    ft.add(R.id.content_main, mSettingsFragment);
                } else {
                    ft.show(mSettingsFragment);
                }
                break;
            case 4:
                if (null == mAboutFragment) {
                    mAboutFragment = AboutFragment.newInstance();
                    ft.add(R.id.content_main, mAboutFragment);
                } else {
                    ft.show(mAboutFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (null != mLatestNewsFragment) {
            ft.hide(mLatestNewsFragment);
        }
        if (null != mColumnFragment) {
            ft.hide(mColumnFragment);
        }
        if (null != mFavoriteFragment) {
            ft.hide(mFavoriteFragment);
        }
        if (null != mSettingsFragment) {
            ft.hide(mSettingsFragment);
        }
        if (null != mAboutFragment) {
            ft.hide(mAboutFragment);
        }
    }

    private void changeDayNightModel() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                beforeChangeMode();
                Log.d(TAG, "onNavigationItemSelected: end time");
                if (!Variable.isNight) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Variable.isNight = true;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Variable.isNight = false;
                }

            }
        });
    }

    private void beforeChangeMode() {
        Bitmap bitmap = ScreenShotUtils.captureScreen(this);
        ACache.get(this).put("image", bitmap, 1);
        Intent intent = new Intent(this, TestActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        recreate();
//        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 当切换DayNight时,保存当前展示Fragment的index
        outState.putInt(FRAGMENT_INDEX, index);
    }
}
