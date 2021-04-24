package ru.net.serbis.launcher.view;

import android.graphics.*;
import java.util.*;

public class Point
{
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
        float radius = PaintHolder.getInstance().getPointCloudRadius();
        return this.x - radius < x &&
            this.x + radius > x &&
            this.y - radius < y &&
            this.y + radius > y;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x, y, PaintHolder.getInstance().getPointRadius(), PaintHolder.getInstance().getPaint());
        if (filled)
        {
            canvas.drawCircle(x, y, PaintHolder.getInstance().getPointDotRadius(), PaintHolder.getInstance().getPaintFill());
        }
    }
}
