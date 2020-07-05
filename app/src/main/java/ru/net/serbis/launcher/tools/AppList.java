package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.tab.*;

public class AppList extends Item
{
    public AppList(Context context)
    {
        super(
            context.getResources().getString(R.string.applications),
            context.getResources().getDrawable(R.drawable.all_apps),
            Tabs.class.getName(),
            context.getPackageName());
    }
}
