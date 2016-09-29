package com.wl.dudian.framework;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * 截屏
 * <p>
 * Created by Qiushui on 16/6/22.
 */

public class ScreenShotUtils {

    /**
     * 截屏
     *
     * @param activity
     * @return
     */

    public static Bitmap captureScreen(Activity activity) {

        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);

        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();

        return bmp;

    }
}
