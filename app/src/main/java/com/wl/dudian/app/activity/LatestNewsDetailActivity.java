package com.wl.dudian.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wl.dudian.R;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.framework.HttpUtil;
import com.wl.dudian.framework.Variable;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yisheng on 16/6/22.
 */

public class LatestNewsDetailActivity extends BaseActivity {

    private static final String ARGU_NEWSID = "ARGU_NEWSID";
    private static final String ARGU_TITLE = "ARGU_TITLE";

    private String mNewsId;
    private String mNewsTitle;
    private Toolbar mToolbar;
    private ImageView mBackgourndImg;
    private TextView mTitleTv;
    private WebView mWebView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Handler mHandler = new Handler();

    public static void launch(Context activity, String newsId, String title) {
        Intent intent = new Intent(activity, LatestNewsDetailActivity.class);
        intent.putExtra(ARGU_NEWSID, newsId);
        intent.putExtra(ARGU_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news_detail_activity);

        mNewsId = getIntent().getStringExtra(ARGU_NEWSID);
        mNewsTitle = getIntent().getStringExtra(ARGU_TITLE);
        getNewsDetail(mNewsId);
        mToolbar = (Toolbar) findViewById(R.id.latest_news_detail_toolbar);
        mWebView = (WebView) findViewById(R.id.latest_news_detail_webview);
        mTitleTv = (TextView) findViewById(R.id.latest_news_detail_title_tv);
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        mCollapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.latest_news_detail_collapsingtoolbarlayout);
        mCollapsingToolbarLayout.setTitle(" ");
        mTitleTv.setText(mNewsTitle);
        mToolbar.setTitleTextColor(0x333333);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBackgourndImg = (ImageView) findViewById(R.id.latest_news_detail_bg_img);
    }

    private void getNewsDetail(String newsId) {
        HttpUtil.getInstance().getNewsDetails(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsDetails>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @SuppressLint("JavascriptInterface")
                    @Override
                    public void onNext(NewsDetails newsDetails) {
                        String imageUrl = newsDetails.getImage();
                        downloadImage(imageUrl);
//

                        if (Variable.isNight) {
                            showNightModeNews(newsDetails);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mWebView.setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        } else {
                            showDayModeNews(newsDetails);
                            mWebView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 显示夜间模式
     *
     * @param newsDetails 新闻内容
     */
    private void showNightModeNews(NewsDetails newsDetails) {
        String js = "<script src=\"file:///android_asset/js/night.js\"></script>";
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsDetails.getBody() + "</body>" + js + "</html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    /**
     * 显示白天模式
     *
     * @param newsDetails 新闻内容
     */
    private void showDayModeNews(NewsDetails newsDetails) {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsDetails.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    /**
     * 下载背景图片并填充到imageview中
     *
     * @param imageUrl
     */
    private void downloadImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(mBackgourndImg);
    }
}
