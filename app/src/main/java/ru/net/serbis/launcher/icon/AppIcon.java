package ru.net.serbis.launcher.icon;

import android.graphics.*;
import ru.net.serbis.launcher.application.*;

public class AppIcon extends Icon
{
    private Item item;
    
    private AppIcon(Item item, Rect rect)
    {
        super(0, rect);
        this.item = item;
    }

    public AppIcon(Item item, int x, int y)
    {
        super(0, x, y);
        this.item = item;
    }

    public Item getItem()
    {
        return item;
    }
}
