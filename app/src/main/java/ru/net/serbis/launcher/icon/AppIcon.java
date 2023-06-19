package ru.net.serbis.launcher.icon;

import android.graphics.*;
import ru.net.serbis.launcher.application.*;

public class AppIcon extends Icon
{
    private Item item;
    private String command;

    private AppIcon(Item item, Rect rect)
    {
        super(0, rect);
        this.item = item;
    }

    public AppIcon(Item item, int x, int y, String command)
    {
        super(0, x, y);
        this.item = item;
        this.command = command;
    }

    public Item getItem()
    {
        return item;
    }

    public String getCommand()
    {
        return command;
    }
}
