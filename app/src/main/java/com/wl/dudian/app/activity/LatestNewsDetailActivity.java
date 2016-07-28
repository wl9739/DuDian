package com.wl.dudian.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wl.dudian.R;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.HttpUtil;
import com.wl.dudian.framework.Variable;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    private static final String ARGU_STORIES_BEAN = "ARGU_STORIES_BEAN";
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
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.latest_news_detail_activity_favorite_btn)
    FloatingActionButton mFavoriteBtn;
    @BindView(R.id.latest_news_detail_activity_menu_btn)
    FloatingActionsMenu mMenuBtn;
    @BindView(R.id.latest_news_detail_activity_discuss_btn)
    FloatingActionButton mDiscussBtn;
    private NewsDetails mNewsDetails;
    private StoriesBean mStoriesBean;
    private Handler mHandler = new Handler();

    /**
     * launch
     */
    public static void launch(Context activity, StoriesBean storiesBean) {
        Intent intent = new Intent(activity, LatestNewsDetailActivity.class);
        intent.putExtra(ARGU_STORIES_BEAN, storiesBean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news_detail_activity);
        ButterKnife.bind(this);

        mStoriesBean = (StoriesBean) getIntent().getSerializableExtra(ARGU_STORIES_BEAN);
        if (null == mStoriesBean) {
            return;
        }

        getNewsDetail(String.valueOf(mStoriesBean.getId()));

        initWebView();

        mCollapsingToolbarLayout.setTitle(" ");
        mTitleTv.setText(mStoriesBean.getTitle());
        mToolbar.setTitleTextColor(0x333333);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuBtn.toggle();
                // 保存到数据库
                saveToDatabase();
            }
        });

        mDiscussBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuBtn.toggle();
                new DiscussView(LatestNewsDetailActivity.this, mStoriesBean.getId());
            }
        });

    }

    /**
     * 保存到数据库
     */
    private void saveToDatabase() {
        // 保存到数据库
        if (mNewsDetails != null && mStoriesBean != null) {
            List<NewsDetails> newsDetailsList = DataSupport.where("id=" + mNewsDetails.getId()).find(NewsDetails.class);
            if (newsDetailsList != null && newsDetailsList.size() > 0) {
                Snackbar.make(mCollapsingToolbarLayout, "已收藏", Snackbar.LENGTH_SHORT).show();
            } else if (!mNewsDetails.save() || !mStoriesBean.save()) {
                Snackbar.make(mCollapsingToolbarLayout, "收藏失败", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mCollapsingToolbarLayout, "收藏成功", Snackbar.LENGTH_SHORT).show();
            }
        }
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
        oks.setTitle(mStoriesBean.getTitle());
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
                        BusinessUtil.loadImage(getApplicationContext(), imageUrl, mBackgourndImg);

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
}
