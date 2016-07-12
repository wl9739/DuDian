package com.wl.dudian.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yisheng on 16/6/22.
 */

public class LatestNewsDetailActivity extends BaseActivity {

    private static final String ARGU_NEWSID = "ARGU_NEWSID";
    private static final String ARGU_TITLE = "ARGU_TITLE";
    @BindView(R.id.latest_news_detail_bg_img)
    ImageView mBackgourndImg;
    @BindView(R.id.latest_news_detail_title_tv)
    TextView mTitleTv;
    @BindView(R.id.latest_news_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.latest_news_detail_collapsingtoolbarlayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.latest_news_detail_webview)
    WebView mWebView;
//    @BindView(R.id.button_remove)
//    FloatingActionButton mButtonRemove;
//    @BindView(R.id.latest_news_detail_actiivty_comment_btn)
//    FloatingActionButton mCommentBtn;
//    @BindView(R.id.latest_news_detail_actiivty_share_button)
//    FloatingActionButton mShareBtn;
//    @BindView(R.id.latest_news_detail_activity_menu_btn)
//    FloatingActionsMenu mMenuBtn;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    private String mNewsId;
    private String mNewsTitle;
    private NewsDetails mNewsDetails;
    private Handler mHandler = new Handler();

    /**
     * launch
     *
     * @param activity activity
     * @param newsId   新闻ID
     * @param title    新闻标题.
     */
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
        ButterKnife.bind(this);

        mNewsId = getIntent().getStringExtra(ARGU_NEWSID);
        mNewsTitle = getIntent().getStringExtra(ARGU_TITLE);
        getNewsDetail(mNewsId);

        initWebView();

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


//        mCommentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handlComment();
//            }
//        });
//        // 分享按钮事件监听
//        mShareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                handlerShare();
//                share();
//                mMenuBtn.toggle();
//            }
//        });
    }

    /**
     * 初始化WebView页面
     */
    private void initWebView() {
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
    }

    private void handlComment() {

    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT,
                "来自读点日报的分享" + mNewsDetails.getTitle() + "，http://daily.zhihu.com/story/" + mNewsDetails.getId());
        startActivity(Intent.createChooser(intent, mNewsDetails.getTitle()));
    }

    /**
     * 使用ShareSDK进行分享
     */
    private void handlerShare() {
        if (null == mNewsDetails) {
            return;
        }
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mNewsTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mNewsDetails.getShare_url());
        // text是分享文本，所有平台都需要这个字段
        oks.setText("读点日报 -- 闲暇时间, 读点日报");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//            oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mNewsDetails.getShare_url());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
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
                        mNewsDetails = newsDetails;
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
        Glide.with(this)
             .load(imageUrl)
             .diskCacheStrategy(DiskCacheStrategy.RESULT)
             .into(mBackgourndImg);
    }
}
