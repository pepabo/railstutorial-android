package com.pepabo.jodo.jodoroid;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;

public class StarTransformation implements Transformation {
    boolean mStar;
    static final Path sStarPath;
    static final float sBaseSize;
    static final Paint sBorderPaint;

    static {
        // https://raw.githubusercontent.com/google/material-design-icons/master/toggle/svg/design/ic_star_24px.svg
        // M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z

        final Path path = new Path();
        path.moveTo(12f, 17.27f);
        path.lineTo(18.18f, 21f);
        path.rLineTo(-1.64f, -7.03f);
        path.lineTo(22f, 9.24f);
        path.rLineTo(-7.19f, -0.61f);
        path.lineTo(12f, 2f);
        path.lineTo(9.19f, 8.63f);
        path.lineTo(2f, 9.24f);
        path.rLineTo(5.46f, 4.73f);
        path.lineTo(5.82f, 21f);
        path.close();
        sStarPath = path;
        sBaseSize = 24f;
    }

    static {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(255, 215, 0));
        paint.setStrokeWidth(4f);
        paint.setMaskFilter(new BlurMaskFilter(8f, BlurMaskFilter.Blur.SOLID));
        paint.setAntiAlias(true);
        sBorderPaint = paint;
    }

    static Path getPath(float width, float height) {
        final Matrix scale = new Matrix();
        scale.setScale(width / sBaseSize, height / sBaseSize);

        final Path path = new Path();
        sStarPath.transform(scale, path);
        return path;
    }

    public StarTransformation(boolean star) {
        mStar = star;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (!mStar) {
            return source;
        } else {
            final Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), source.getConfig());

            final Canvas canvas = new Canvas(bitmap);
            final Path star = getPath(canvas.getWidth(), canvas.getHeight());
            canvas.drawPath(star, sBorderPaint);
            canvas.clipPath(star);
            canvas.drawBitmap(source, 0f, 0f, null);

            source.recycle();
            return bitmap;
        }
    }

    @Override
    public String key() {
        return String.format("star=%s", mStar);
    }
}
