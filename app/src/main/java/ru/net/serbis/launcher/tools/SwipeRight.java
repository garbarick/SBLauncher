package ru.net.serbis.launcher.tools;

import android.app.*;
import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.host.*;

public class SwipeRight extends Item
{
    public SwipeRight(Context context)
    {
        super(
            context.getResources().getString(R.string.swipeRight),
            context.getResources().getDrawable(android.R.drawable.ic_media_next),
            SwipeRight.class.getName(),
            context.getPackageName());
    }

    @Override
    public void start(Context context)
    {
    }

    @Override
    public void start(Host host)
    {
        host.swipeLeft();
    }
}
