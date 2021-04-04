package ru.net.serbis.launcher.view;

import android.graphics.*;
import java.util.*;

public class Point
{
    private static int POINT_RAD = 30;
    private static int POINT_DOT_RAD = 15;
    private static int POINT_CLOUD_RAD = 60;

    public float x;
    public float y;
    public boolean filled;
    public int i;

    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Point)
        {
            Point that = (Point) o;
            return equal(that.x, that.y);
        }
        return false;
    }

    public boolean equal(float x, float y)
    {
        return this.x == x && this.y == y;
    }

    public boolean inCloud(float x, float y)
    {
        return this.x - POINT_CLOUD_RAD < x &&
            this.x + POINT_CLOUD_RAD > x &&
            this.y - POINT_CLOUD_RAD < y &&
            this.y + POINT_CLOUD_RAD > y;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x, y, POINT_RAD, PatternView.PAINT);
        if (filled)
        {
            canvas.drawCircle(x, y, POINT_DOT_RAD, PatternView.PAINT_FILL);
        }
    }
}
