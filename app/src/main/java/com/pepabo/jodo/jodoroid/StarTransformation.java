package com.pepabo.jodo.jodoroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.Shape;

import com.squareup.picasso.Transformation;

public class StarTransformation implements Transformation {
    boolean mStar;
    static final Shape sStarShape;

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
        sStarShape = new PathShape(path, 24f, 24f);
    }

    static ShapeDrawable createDrawable() {
        return new ShapeDrawable(sStarShape);
    }

    public StarTransformation(boolean star) {
        mStar = star;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (!mStar) {
            return source;
        } else {
            final Bitmap bitmap = source.copy(source.getConfig(), true);
            source.recycle();

            final Canvas canvas = new Canvas(bitmap);
            final ShapeDrawable drawable = createDrawable();
            final int size = (int) (Math.min(canvas.getHeight(), canvas.getWidth()) * 0.3);
            drawable.getPaint().setAntiAlias(true);
            drawable.setBounds(0, 0, size, size);

            drawable.getPaint().setColor(Color.rgb(255, 215, 0));
            drawable.getPaint().setStyle(Paint.Style.FILL);
            drawable.draw(canvas);

            drawable.getPaint().setColor(Color.WHITE);
            drawable.getPaint().setStrokeWidth(1f);
            drawable.getPaint().setStyle(Paint.Style.STROKE);
            drawable.draw(canvas);

            return bitmap;
        }
    }

    @Override
    public String key() {
        return String.format("star=%s", mStar);
    }
}
