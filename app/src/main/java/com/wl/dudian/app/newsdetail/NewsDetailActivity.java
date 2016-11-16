package com.wl.dudian.app.newsdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.wl.dudian.R;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.app.ui.activity.BaseActivity;
import com.wl.dudian.app.ui.activity.DiscussView;
import com.wl.dudian.databinding.NewsDetailActivityBinding;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.Constants;

/**
 * 新闻详情界面
 * <p>
 * Created by Qiushui on 16/6/22.
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {

    private static final String ARGU_STORIES_BEAN = "ARGU_STORIES_BEAN";
    private static final String ARGU_IS_NOHEADER = "ARGU_IS_NOHEADER";

    private StoriesBean mStoriesBean;
    private NewsDetailContract.Presenter mPresenter;
    private NewsDetailActivityBinding mBinding;
    private boolean isFavorite;

    public static void launch(Context activity, StoriesBean storiesBean) {
        launch(activity, storiesBean, false);
    }

    /**
     * launch
     */
    public static void launch(Context activity, StoriesBean storiesBean, boolean hideHeader) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(ARGU_STORIES_BEAN, storiesBean);
        if (hideHeader || storiesBean.getImages().size() < 1) {
            intent.putExtra(ARGU_IS_NOHEADER, true);
        } else {
            intent.putExtra(ARGU_IS_NOHEADER, TextUtils.isEmpty(storiesBean.getImages().get(0)));
        }
        activity.startActivity(intent);
    }

    @Override
    public void share(StoriesBean storiesBean) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT,
                storiesBean.getTitle() + " http://daily.zhihu.com/story/" + storiesBean.getId() + "  （来自土豪炫酷无敌吊炸天的分享）");
        startActivity(Intent.createChooser(intent, storiesBean.getTitle()));
    }

    @Override
    public void showHeaderImage(String imageUrl) {
        BusinessUtil.loadImage(getApplicationContext(), imageUrl, mBinding.headerImage);
    }

    @Override
    public void showWebView(boolean isShowNight) {
        mBinding.webview.setVisibility(isShowNight ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNewsStatus(String image_source, boolean favorite) {
        mBinding.picName.setText(image_source);
        isFavorite = favorite;
        if (favorite) {
            mBinding.favoriteBtn.setIcon(R.drawable.ic_bookmark_white);
            mBinding.favoriteBtn.setTitle("已收藏");
        } else {
            mBinding.favoriteBtn.setIcon(R.drawable.ic_bookmark_border_white);
            mBinding.favoriteBtn.setTitle("收藏");
        }
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        this.mPresenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public void showNormalData(String newsDetails) {
        mBinding.webview.loadDataWithBaseURL("x-data://base", newsDetails, "text/html", "UTF-8", null);
    }

    @Override
    public void showNobodyData(String shareUrl) {
        mBinding.webview.loadUrl(shareUrl);
        mBinding.webview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.menuBtn.isExpanded()) {
            mBinding.menuBtn.toggle();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.news_detail_activity);
        mBinding.setHandler(new Handler());
        initData();

        mBinding.collapsingtoolbarlayout.setTitle(" ");
        mBinding.titleTv.setText(mStoriesBean.getTitle());
        mBinding.toolbar.setTitleTextColor(0x333333);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initWebView();

        // 初始化 mPresenter
        new NewsDetailPresenter(this, this);
        mPresenter.loadData(String.valueOf(mStoriesBean.getId()));
    }

    private void initData() {
        // 获取新闻对象
        mStoriesBean = (StoriesBean) getIntent().getSerializableExtra(ARGU_STORIES_BEAN);
        // 判断是否需要显示 headerview
        boolean isNoHeader = getIntent().getBooleanExtra(ARGU_IS_NOHEADER, false);
        if (isNoHeader) {
            ViewGroup.LayoutParams params = mBinding.appBarLayout.getLayoutParams();
            params.height = 60;
            mBinding.appBarLayout.setLayoutParams(params);
            mBinding.favoriteBtn.setVisibility(View.GONE);
        } else {
            mBinding.appBarLayout.setVisibility(View.VISIBLE);
        }
        setWindows(isNoHeader);
    }

    private void setWindows(boolean isNoHeader) {
        // android  5.0 以上设置全屏模式
        if (Build.VERSION.SDK_INT >= 21 && !isNoHeader) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 初始化WebView页面
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mBinding.webview.setVisibility(View.INVISIBLE);
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mBinding.webview.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mBinding.webview.getSettings().setAppCacheEnabled(true);

        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        boolean hideImage = sp.getBoolean(Constants.HIDE_IMAGE, false);
        mBinding.webview.getSettings().setBlockNetworkImage(hideImage);
    }

    /**
     * 事件点击处理
     */
    public class Handler {
        public void onShareBtnClick(View view) {
            mBinding.menuBtn.toggle();
            share(mStoriesBean);
        }

        public void onFavoritBtnClick(View view) {
            mBinding.menuBtn.toggle();
            if (isFavorite) {
                Snackbar.make(view, "已收藏", Snackbar.LENGTH_SHORT).show();
            } else {
                mBinding.favoriteBtn.setIcon(R.drawable.ic_bookmark_white);
                mBinding.favoriteBtn.setTitle("已收藏");
                // 保存到数据库
                if (mPresenter.favorite()) {
                    isFavorite = true;
                    Snackbar.make(view, "保存成功", Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        public void onDiscussBtnClick(View view) {
            mBinding.menuBtn.toggle();
            new DiscussView(NewsDetailActivity.this, mStoriesBean.getId());
        }
    }
}
