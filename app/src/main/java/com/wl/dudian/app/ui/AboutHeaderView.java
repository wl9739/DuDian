package com.wl.dudian.app.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.wl.dudian.R;
import com.wl.dudian.framework.Util;

/**
 * 关于界面
 * Created by Qiushui on 2016/11/7.
 */

public class AboutHeaderView extends ImageView implements Runnable {

    private Path mClipPath;
    private Paint mPaint;
    private Bitmap mBackGound;
    private float positionX;
    private float positionY;

    public AboutHeaderView(Context context) {
        this(context, null);
    }

    public AboutHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboutHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClipPath = new Path();

        mBackGound = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dayu);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mClipPath.moveTo(0, 0);
        mClipPath.lineTo(getWidth(), 0);
        mClipPath.lineTo(getWidth(), Util.convertDpToPixel(280 - Util.convertPixelsToDp((float) (getWidth() * 3 / 7), getContext()), getContext()));
        mClipPath.lineTo(0, Util.convertDpToPixel(280, getContext()));
        mClipPath.close();
        canvas.clipPath(mClipPath);
        canvas.drawBitmap(mBackGound, positionX, positionY, mPaint);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (positionX > -800) {
                    positionX -= 1;
                    positionY -= 0.1;
                    postInvalidate();
                } else {
                    positionX = 0;
                    positionY = 0;
                }
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
