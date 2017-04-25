package ru.net.serbis.launcher.widget;

import android.graphics.*;

public class Widget
{
    private int id;
    private Rect rect;

    public Widget(int id, Rect rect)
    {
        this.id = id;
        this.rect = rect;
    }
    
    public Widget(int id, int x, int y)
    {
        this(id, new Rect(x, y, 0, 0));
    }

    public int getId()
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
    
    public void setW(int w)
    {
        rect.right = w;
    }

    public int getW()
    {
        return rect.right;
    }

    public void setH(int h)
    {
        rect.bottom = h;
    }

    public int getH()
    {
        return rect.bottom;
    }
    
    public Rect getRect()
    {
        return rect;
    }
}
