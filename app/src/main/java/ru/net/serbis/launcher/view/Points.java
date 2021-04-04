package ru.net.serbis.launcher.view;

import android.graphics.*;
import java.util.*;

public class Points
{
    private List<Point> points = new ArrayList<Point>();

    public boolean ready()
    {
        return points.size() > 0;
    }

    public void init(float w, float h)
    {
        points.clear();
        float step = Math.min(w / 6, h / 6);
        float startX = (w - step * 4) / 2;
        float startY = (h - step * 4) / 2;
        int i = 1;
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                add(startX + x * 2 * step, startY + y * 2 * step, i++);
            }
        }
    }

    private void add(float x, float y, int i)
    {
        Point point = new Point(x, y);
        point.i = i;
        points.add(point);
    }

    public void draw(Canvas canvas)
    {
        for (Point point : points)
        {
            point.draw(canvas);
        }
    }

    public Point findNearestPoint(float x, float y)
    {
        for (Point point : points)
        {
            if (point.inCloud(x, y))
            {
                return point;   
            }
        }
        return null;
    }

    public Point findPoint(float x, float y)
    {
        for (Point point : points)
        {
            if (point.equal(x, y))
            {
                return point;   
            }
        }
        return null;
    }

    public void clearFilled()
    {
        for (Point point : points)
        {
            point.filled = false;
        }
    }
}
