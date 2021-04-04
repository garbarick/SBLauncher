package ru.net.serbis.launcher.view;

import android.graphics.*;
import java.util.*;

public class Lines
{
    private List<Line> lines = new ArrayList<Line>();
    private Line line;

    public void draw(Canvas canvas, Paint paint)
    {
        for (Line line : lines)
        {
            line.draw(canvas);
        }
    }

    public void add(float sx, float sy, float ex, float ey)
    {
        line = new Line(sx, sy, ex, ey);
        lines.add(line);
    }

    public void add(float ex, float ey)
    {
        line = new Line(line.end.x, line.end.y, ex, ey);
        lines.add(line);
    }

    public void progress(float x, float y)
    {
        line.end.x = x;
        line.end.y = y;
    }

    public boolean inProgress()
    {
        return line != null;
    }

    public void stop()
    {
        line = null;
    }

    public boolean isStart(Point point)
    {
        return point.equals(line.start);
    }

    public void clear()
    {
        stop();
        lines.clear();
    }

    public void clearLast()
    {
        lines.remove(line);
        stop();
    }
    
    public boolean isFilled()
    {
        return !lines.isEmpty();
    }

    public String toPattern(Points points)
    {
        StringBuilder data = new StringBuilder();
        Iterator<Line> iter = lines.iterator();
        while(iter.hasNext())
        {
            line = iter.next();
            data.append(points.findPoint(line.start.x, line.start.y).i);
            if (!iter.hasNext())
            {
                data.append(points.findPoint(line.end.x, line.end.y).i);
            }
        }
        return data.toString();
    }
}
