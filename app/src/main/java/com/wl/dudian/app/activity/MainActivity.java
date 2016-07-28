package com.wl.dudian.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
    private boolean isExit = false;


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        // 不执行退出事件
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
                        e.printStackTrace();
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

    /**
     * 隐藏Fragment
     *
     * @param ft FragmentTransaction
     */
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

    /**
     * 夜间模式切换
     */
    private void changeDayNightModel() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                beforeChangeMode();
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
        bitmap = zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("IMAGE", bitmap);
        startActivity(intent);
        finish();
    }

    /**
     * 双击退出程序
     */
    private void exitBy2Click() {
        if (!isExit) {
            isExit = true;
            Snackbar.make(mContentMain, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    /**
     * 缩小图片规格
     *
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight); // 不改变原来图像大小
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }
}
