package com.wl.dudian.app.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.wl.dudian.R;
import com.wl.dudian.app.latestnews.LatestNewsFragment;
import com.wl.dudian.framework.db.model.ThemesModel;
import com.wl.dudian.framework.repository.DomainService;
import com.wl.dudian.app.ui.fragment.ColumnFragment;
import com.wl.dudian.app.favorite.FavoriteFragment;
import com.wl.dudian.app.ui.fragment.SettingsFragment;
import com.wl.dudian.databinding.ActivityMainBinding;
import com.wl.dudian.framework.Constants;
import com.wl.dudian.framework.ScreenShotUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";

    private LatestNewsFragment mLatestNewsFragment;
    private FavoriteFragment mFavoriteFragment;
    private ColumnFragment mColumnFragment;
    private SettingsFragment mSettingsFragment;
    private ThemesModel mThemesModel;

    private ActivityMainBinding binding;

    private DomainService domainService;
    private int index = 0;
    private boolean isExit = false;
    private SharedPreferences mSharedPreferences;

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
            case R.id.nav_pic:
                switchImage();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    private void switchImage() {
        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        boolean hideImage = sp.getBoolean(Constants.HIDE_IMAGE, false);
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE).edit();
        if (hideImage) {
            editor.putBoolean(Constants.HIDE_IMAGE, false).apply();
            binding.navView.getMenu().findItem(R.id.nav_pic).setTitle("无图").setIcon(R.drawable.ic_filter_none_grey);
            Snackbar.make(binding.content.contentLayout.contentMain, "有图模式设置成功！", Snackbar.LENGTH_SHORT).show();
        } else {
            editor.putBoolean(Constants.HIDE_IMAGE, true).apply();
            binding.navView.getMenu().findItem(R.id.nav_pic).setTitle("有图").setIcon(R.drawable.ic_filter_grey);
            Snackbar.make(binding.content.contentLayout.contentMain, "无图模式设置成功！", Snackbar.LENGTH_SHORT).show();
        }
        binding.drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.closeDrawers();
            } else {
                exitBy2Click();
            }
        }
        // 不执行退出事件
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.content.contentLayout.contentMain.setVisibility(View.VISIBLE);
        binding.content.contentLayout.contentMainNotconnectedRl.setVisibility(View.GONE);
        binding.content.contentLayout.contentMainWifilogoImg.setVisibility(View.GONE);
        showLatestNews(savedInstanceState);

        binding.content.toolbar.setTitle("读点日报");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.content.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        domainService = DomainService.getInstance(this);
        mSharedPreferences = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        getThemes();
        setNavMenu();

    }

    /**
     * 显示是否有图片
     */
    private void setNavMenu() {
        if (mSharedPreferences.getBoolean(Constants.HIDE_IMAGE, false)) {
            binding.navView.getMenu().findItem(R.id.nav_pic).setTitle("有图")
                    .setIcon(R.drawable.ic_filter_grey);
        } else {
            binding.navView.getMenu().findItem(R.id.nav_pic).setTitle("无图")
                    .setIcon(R.drawable.ic_filter_none_grey);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 当切换DayNight时,保存当前展示Fragment的index
        outState.putInt(FRAGMENT_INDEX, index);
    }

    private void getThemes() {
        domainService.getTheme().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, ThemesModel>() {
                    @Override
                    public ThemesModel call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Action1<ThemesModel>() {
                    @Override
                    public void call(ThemesModel themesModel) {
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
                    binding.content.toolbar.setTitle("读点日报");
                    mLatestNewsFragment = LatestNewsFragment.newInstance();
                    ft.add(R.id.content_main, mLatestNewsFragment);
                } else {
                    binding.content.toolbar.setTitle("读点日报");
                    ft.show(mLatestNewsFragment);
                }
                break;
            case 1:
                if (null == mColumnFragment) {
                    binding.content.toolbar.setTitle("专栏");
                    mColumnFragment = ColumnFragment.newInstance(mThemesModel);
                    ft.add(R.id.content_main, mColumnFragment);
                } else {
                    binding.content.toolbar.setTitle("专栏");
                    ft.show(mColumnFragment);
                }
                break;
            case 2:
                if (null == mFavoriteFragment) {
                    binding.content.toolbar.setTitle("收藏");
                    mFavoriteFragment = FavoriteFragment.newInstance();
                    ft.add(R.id.content_main, mFavoriteFragment);
                } else {
                    binding.content.toolbar.setTitle("收藏");
                    // 主动刷新数据
                    mFavoriteFragment.updateFavoriteItem();
                    ft.show(mFavoriteFragment);
                }
                break;
            case 3:
                if (null == mSettingsFragment) {
                    binding.content.toolbar.setTitle("设置");
                    mSettingsFragment = SettingsFragment.newInstance();
                    ft.add(R.id.content_main, mSettingsFragment);
                } else {
                    binding.content.toolbar.setTitle("设置");
                    ft.show(mSettingsFragment);
                }
                break;
        }
        ft.commit();
        // close drawer
        binding.drawerLayout.closeDrawers();
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
    }

    /**
     * 夜间模式切换
     */
    private void changeDayNightModel() {
        if (mSharedPreferences.getBoolean(Constants.IS_NIGHT, false)) {
            mSharedPreferences.edit().putBoolean(Constants.IS_NIGHT, false).apply();
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
            beforeChangeMode();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            mSharedPreferences.edit().putBoolean(Constants.IS_NIGHT, true).apply();
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
            beforeChangeMode();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void beforeChangeMode() {
        Bitmap bitmap = ScreenShotUtils.captureScreen(this);
        bitmap = zoomBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10);
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra(TransitionActivity.IMAGE_NAME, bitmap);
        startActivity(intent);
        finish();
    }

    /**
     * 双击退出程序
     */
    private void exitBy2Click() {
        if (!isExit) {
            isExit = true;
            Snackbar.make(binding.content.contentLayout.contentMain, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
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
}
