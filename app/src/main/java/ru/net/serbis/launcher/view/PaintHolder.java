package ru.net.serbis.launcher.view;

import android.content.*;
import android.graphics.*;

public class PaintHolder
{
    private static PaintHolder instance;

    private Paint paint;
    private Paint paintFill;
    private float density;
    private float pointRadius;
    private float pointDotRadius;
    private float pointCloudRadius;

    public static PaintHolder getInstance()
    {
        return instance;
    }

    public static void createInstance(Context context)
    {
        instance = new PaintHolder(context);
    }

    private PaintHolder(Context context)
    {
        density = context.getResources().getDisplayMetrics().density;
        paint = newPaint(2, Color.WHITE, Paint.Style.STROKE);
        paintFill = newPaint(2, Color.WHITE, Paint.Style.FILL_AND_STROKE);
        pointRadius = 10 * density;
        pointDotRadius = 5 * density;
        pointCloudRadius = 20 * density;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public Paint getPaintFill()
    {
        return paintFill;
    }

    public float getDensity()
    {
        return density;
    }

    public float getPointRadius()
    {
        return pointRadius;
    }

    public float getPointDotRadius()
    {
        return pointDotRadius;
    }

    public float getPointCloudRadius()
    {
        return pointCloudRadius;
    }

    private Paint newPaint(float width, int color, Paint.Style style)
    {
        Paint paint = new Paint();
        paint.setStrokeWidth(width * density);
        paint.setColor(color);
        paint.setStyle(style);
        return paint;
    }
}
