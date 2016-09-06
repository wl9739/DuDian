package com.wl.dudian.app.newsdetail;

import android.content.Context;
import android.text.TextUtils;

import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.Variable;

import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Listens to user actions from the UI , retrieves the data and updates
 * the UI as required.
 * <p>
 * Created by Qiushui on 16/8/8.
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    private final DomainService domainService;

    private final NewsDetailContract.View newsDetailView;

    private Subscription dataSubscription;

    private Context context;
    private Subscription showDataSubscription;

    private NewsDetails newsDetails;

    public NewsDetailPresenter(Context context, NewsDetailContract.View newsDetailView) {
        this.context = context;
        this.newsDetailView = newsDetailView;
        domainService = DomainService.getInstance(context);

        newsDetailView.setPresenter(this);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        BusinessUtil.unsubscribe(dataSubscription);
        BusinessUtil.unsubscribe(showDataSubscription);
    }

    @Override
    public void loadData(String newsId) {
        Observable<NewsDetails> getNewsDetail = Observable.concat(
                domainService.getNewsDetailFromDb(newsId),
                domainService.getNewsDetailsFromNet(newsId))
                .first()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        dataSubscription = getNewsDetail.subscribe(new Action1<NewsDetails>() {
            @Override
            public void call(NewsDetails newsDetails) {
                if (null == newsDetails) {
                    return;
                }
                updateRead(newsDetails.getId());
                NewsDetailPresenter.this.newsDetails = newsDetails;
                if (null == newsDetails.getBody()) {
                    setNoBodyInfo(newsDetails);
                } else {
                    setNormalWebviewInfo(newsDetails);
                }
            }
        });
    }

    @Override
    public void favorite() {
        if (newsDetails == null) {
            throw new IllegalStateException("NewsDetail must not be null!");
        }
        domainService.saveToFavoriteDb(newsDetails);
    }

    @Override
    public void share() {

    }

    /**
     * 处理特殊的新闻页面
     *
     * @param newsDetails 新闻实体类
     */
    private void setNoBodyInfo(NewsDetails newsDetails) {
        String imageUrl;
        if (!TextUtils.isEmpty(newsDetails.getImage())) {
            imageUrl = newsDetails.getImage();
            newsDetailView.showHeaderImage(imageUrl);
        } else if (!TextUtils.isEmpty(newsDetails.getImages().get(0))) {
            imageUrl = newsDetails.getImages().get(0);
            newsDetailView.showHeaderImage(imageUrl);
        }
        String shareUrl = newsDetails.getShare_url();
        newsDetailView.showNobodyData(shareUrl);
        newsDetailView.showWebView(true);
    }

    /**
     * 根据是否为为夜间模式, 显示新闻页面
     *
     * @param newsDetails 新闻实体类
     */
    private void setNormalWebviewInfo(NewsDetails newsDetails) {
        if (Variable.isNight) {
            String js = "<script src=\"file:///android_asset/js/night.js\"></script>";
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
            String html = "<html><head>" + css + "</head><body>" + newsDetails.getBody() + "</body>" + js + "</html>";
            html = html.replace("<div class=\"img-place-holder\">", "");
            newsDetailView.showNormalData(html);

            showNightModel(newsDetails);
        } else {
            showDayModel(newsDetails);
        }
    }

    /**
     * 显示白天样式
     *
     * @param newsDetails
     */
    private void showDayModel(NewsDetails newsDetails) {
        newsDetailView.showHeaderImage(newsDetails.getImage());
        showDataSubscription = Observable.just(newsDetails).subscribe(new Action1<NewsDetails>() {
            @Override
            public void call(NewsDetails newsDetails) {
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + newsDetails.getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                newsDetailView.showNormalData(html);
                newsDetailView.showWebView(true);
            }
        });
    }

    /**
     * 显示夜间样式
     *
     * @param newsDetails
     */
    private void showNightModel(final NewsDetails newsDetails) {
        newsDetailView.showHeaderImage(newsDetails.getImage());
        showDataSubscription = Observable
                .timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        newsDetailView.showWebView(true);
                    }
                });
    }
    @Override
    public void updateRead(final int id) {
//        domainService.updateRead(id);
    }
}
