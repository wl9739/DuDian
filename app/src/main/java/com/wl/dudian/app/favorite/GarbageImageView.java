package com.wl.dudian.app.favorite;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 图片扩展类
 * Created by Qiushui on 2016/11/15.
 */

public class GarbageImageView extends ImageView {

    public GarbageImageView(Context context) {
        super(context);
    }

    public GarbageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GarbageImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isContains(float touchX, float touchY) {
        int[] position = new int[2];
        this.getLocationOnScreen(position);
        float x = position[0];
        float y = position[1];
        float w = this.getWidth();
        float h = this.getHeight();

        return touchX > x && touchX < x + w && touchY > y && touchY < y + h;
    }
}
