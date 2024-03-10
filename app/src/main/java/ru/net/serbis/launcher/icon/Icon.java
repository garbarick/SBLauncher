package ru.net.serbis.launcher.icon;

import android.graphics.*;

public class Icon
{
    private long id;
    private Rect rect;

    public Icon(long id, Rect rect)
    {
        this.id = id;
        this.rect = rect;
    }

    public Icon(long id, int x, int y)
    {
        this(id, new Rect(x, y, 0, 0));
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public void setX(int x)
    {
        rect.left = x;
    }

    public int getX()
    {
        return rect.left;
    }

    public void setY(int y)
    {
        rect.top = y;
    }

    public int getY()
    {
        return rect.top;
    }

    public Rect getRect()
    {
        return rect;
    }
}
