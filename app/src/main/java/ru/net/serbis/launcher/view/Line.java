package ru.net.serbis.launcher.view;

import android.graphics.*;

public class Line
{
    public Point start;
    public Point end;

    public Line(float sx, float sy, float ex, float ey)
    {
        this.start = new Point(sx, sy);
        this.end = new Point(ex, ey);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawLine(start.x, start.y, end.x, end.y, PatternView.PAINT);
    }
}
