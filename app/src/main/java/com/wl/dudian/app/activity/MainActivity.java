package com.wl.dudian.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import com.wl.dudian.app.model.ThemesModel;
import com.wl.dudian.framework.ACache;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.HttpUtil;
import com.wl.dudian.framework.ScreenShotUtils;
import com.wl.dudian.framework.Variable;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity11111";
    private static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content_main_wifilogo_img)
    ImageView mContentMainWifilogoImg;
    @BindView(R.id.content_main_notconnected_rl)
    RelativeLayout mContentMainNotconnectedRl;
    @BindView(R.id.content_main)
    FrameLayout mContentMain;
    @BindView(R.id.app_bar_main_coordinatorlayout)
    CoordinatorLayout mAppBarMainCoordinatorlayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private LatestNewsFragment mLatestNewsFragment;
    private FavoriteFragment mFavoriteFragment;
    private ColumnFragment mColumnFragment;
    private SettingsFragment mSettingsFragment;
    private AboutFragment mAboutFragment;
    private ThemesModel mThemesModel;
    private int index = 0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (BusinessUtil.isNetConnected(this)) {
            mContentMain.setVisibility(View.VISIBLE);
            mContentMainNotconnectedRl.setVisibility(View.GONE);
            mContentMainWifilogoImg.setVisibility(View.GONE);
            showLatestNews(savedInstanceState);
        } else {
            mContentMainNotconnectedRl.setVisibility(View.VISIBLE);
            mContentMainWifilogoImg.setVisibility(View.VISIBLE);
        }

        mToolbar.setTitle("读点日报");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
        getThemes();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 当切换DayNight时,保存当前展示Fragment的index
        outState.putInt(FRAGMENT_INDEX, index);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    private void getThemes() {
        HttpUtil.getInstance().getThemesModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThemesModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ThemesModel themesModel) {
                        mThemesModel = themesModel;
                    }
                });
    }

    private void showLatestNews(Bundle savedInstanceState) {

        FragmentManager fm = getSupportFragmentManager();
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt(FRAGMENT_INDEX);
            setNavSelection(index);
        } else {
            mLatestNewsFragment = (LatestNewsFragment) fm.findFragmentById(R.id.content_main);
            if (mLatestNewsFragment == null) {
                mLatestNewsFragment = LatestNewsFragment.newInstance();
                fm.beginTransaction()
                  .add(R.id.content_main, mLatestNewsFragment, mLatestNewsFragment.getClass().getName())
                  .commit();
            }

        }

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
                    mColumnFragment = ColumnFragment.newInstance(mThemesModel);
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
                    // 主动刷新数据
                    mFavoriteFragment.updateFavoriteItem();
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
        // close drawer
        mDrawerLayout.closeDrawers();
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
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}
