package ru.net.serbis.launcher.tools;

import android.app.*;
import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.host.*;

public class SwipeTop extends Item
{
    public SwipeTop(Context context)
    {
        super(
            context.getResources().getString(R.string.swipeTop),
            context.getResources().getDrawable(android.R.drawable.ic_menu_more),
            SwipeTop.class.getName(),
            context.getPackageName());
    }

    @Override
    public void start(Context context)
    {
    }

    @Override
    public void start(Host host)
    {
        host.swipeTop();
    }
}
