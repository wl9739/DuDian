package com.wl.dudian.framework;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import rx.Subscription;

/**
 * Created by Qiushui on 16/6/22.
 */

public class BusinessUtil {

    /**
     * 判断是否有网络链接
     *
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 下载图片
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl) || context == null) {
            return;
        }
        Glide.with(context)
             .load(imageUrl)
             .diskCacheStrategy(DiskCacheStrategy.RESULT)
             .into(imageView);
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
