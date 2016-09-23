package com.onnuridmc.sample.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;

public class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
    public DefaultRoundedBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override
    public void setMipMap(boolean mipMap) {
        if (mBitmap != null) {
            BitmapCompat.setHasMipMap(mBitmap, mipMap);
            invalidateSelf();
        }
    }

    @Override
    public boolean hasMipMap() {
        return mBitmap != null && BitmapCompat.hasMipMap(mBitmap);
    }

    @Override
    void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight,
                            Rect bounds, Rect outRect) {
        GravityCompat.apply(gravity, bitmapWidth, bitmapHeight,
                bounds, outRect, ViewCompat.LAYOUT_DIRECTION_LTR);
    }
}