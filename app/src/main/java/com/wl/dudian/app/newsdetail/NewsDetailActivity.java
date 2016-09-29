package com.wl.dudian.app.newsdetail;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wl.dudian.R;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.ui.activity.BaseActivity;
import com.wl.dudian.app.ui.activity.DiscussView;
import com.wl.dudian.databinding.NewsDetailActivityBinding;
import com.wl.dudian.framework.BusinessUtil;

/**
 * 新闻详情界面
 * <p>
 * Created by Qiushui on 16/6/22.
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {

    public static final String ARGU_STORIES_BEAN = "ARGU_STORIES_BEAN";
    public static final String ARGU_IS_NOHEADER = "ARGU_IS_NOHEADER";

    private StoriesBean mStoriesBean;

    private NewsDetailContract.Presenter presenter;

    private NewsDetailActivityBinding binding;

    /**
     * launch
     */
    public static void launch(Context activity, StoriesBean storiesBean) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        intent.putExtra(ARGU_STORIES_BEAN, storiesBean);
        if (storiesBean.getImages() == null || storiesBean.getImages().size() < 1) {
            intent.putExtra(ARGU_IS_NOHEADER, true);
        } else {
            intent.putExtra(ARGU_IS_NOHEADER, TextUtils.isEmpty(storiesBean.getImages().get(0)));
        }
        activity.startActivity(intent);
    }

    @Override
    public void share(NewsDetails newsDetails) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT,
                "来自读点日报的分享" + newsDetails.getTitle() + "，http://daily.zhihu.com/story/" + newsDetails.getId());
        startActivity(Intent.createChooser(intent, newsDetails.getTitle()));
    }

    @Override
    public void showHeaderImage(String imageUrl) {
        BusinessUtil.loadImage(this, imageUrl, binding.headerImage);
    }

    @Override
    public void showWebView(boolean isShowNight) {
        binding.webview.setVisibility(isShowNight ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        this.presenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public void showNormalData(String newsDetails) {
        binding.webview.loadDataWithBaseURL("x-data://base", newsDetails, "text/html", "UTF-8", null);
    }

    @Override
    public void showNobodyData(String shareUrl) {
        binding.webview.loadUrl(shareUrl);
        binding.webview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (binding.menuBtn.isExpanded()) {
            binding.menuBtn.toggle();
            return;
        }

        binding.menuBtn.animate().scaleX(0).scaleY(0).setListener(new AnimatorLinstenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.news_detail_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tansparentStatusBar();
            transition(savedInstanceState);
        }

        new NewsDetailPresenter(this, this);

        mStoriesBean = (StoriesBean) getIntent().getSerializableExtra(ARGU_STORIES_BEAN);
        boolean isNoHeader = getIntent().getBooleanExtra(ARGU_IS_NOHEADER, false);
        if (isNoHeader) {
            ViewGroup.LayoutParams params = binding.appBarLayout.getLayoutParams();
            params.height = 0;
            binding.appBarLayout.setLayoutParams(params);
        } else {
            binding.appBarLayout.setVisibility(View.VISIBLE);
        }
        if (null == mStoriesBean) {
            return;
        }

        binding.collapsingtoolbarlayout.setTitle(" ");
        binding.titleTv.setText(mStoriesBean.getTitle());
        binding.toolbar.setTitleTextColor(0x333333);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.menuBtn.toggle();
                // 保存到数据库
                presenter.favorite();
                Snackbar.make(binding.webview, "保存成功", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.discussBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.menuBtn.toggle();
                new DiscussView(NewsDetailActivity.this, mStoriesBean.getId());
            }
        });

        initWebView();
        presenter.loadData(String.valueOf(mStoriesBean.getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void tansparentStatusBar() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void transition(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            StoriesBean storiesBean = (StoriesBean) getIntent().getExtras().get(ARGU_STORIES_BEAN);
            BusinessUtil.loadImage(this, storiesBean.getImages().get(0), binding.headerImage);
        }
        if (savedInstanceState == null) {
            binding.menuBtn.setScaleX(0);
            binding.menuBtn.setScaleY(0);
            getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getEnterTransition().removeListener(this);
                    binding.menuBtn.animate().scaleX(1).scaleY(1);
                }
            });
        }
    }

    /**
     * 初始化WebView页面
     */
    private void initWebView() {
        binding.webview.setVisibility(View.INVISIBLE);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        binding.webview.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        binding.webview.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        binding.webview.getSettings().setAppCacheEnabled(true);

        binding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
