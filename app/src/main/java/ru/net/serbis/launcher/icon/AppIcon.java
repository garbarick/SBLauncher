package ru.net.serbis.launcher.icon;

import android.graphics.*;
import ru.net.serbis.launcher.application.*;
import android.graphics.drawable.*;

public class AppIcon
{
    private long id;
    private Item item;
    private Rect rect;
    
    private AppIcon(long id, Item item, Rect rect)
    {
        this.id = id;
        this.item = item;
        this.rect = rect;
    }

    public AppIcon(long id, Item item, int x, int y)
    {
        this(id, item, new Rect(x, y, 0, 0));
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public Item getItem()
    {
        return item;
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
