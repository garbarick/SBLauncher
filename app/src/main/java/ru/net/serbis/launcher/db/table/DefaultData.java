package ru.net.serbis.launcher.db.table;

import android.database.sqlite.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.icon.*;
import ru.net.serbis.launcher.tools.*;

public class DefaultData extends Table
{
    private int step = 175;
    private int x;

    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion != newVersion)
        {
            return;
        }

        addAllApps(db);
        iter();
    }

    private void iter()
    {
        x += step;
    }

    private void addAppIcon(SQLiteDatabase db, Item item, int pos)
    {
        addAppIcon(db, item, pos, null);
    }

    private void addAppIcon(SQLiteDatabase db, Item item, int pos, String command)
    {
        AppIcon appsIcon = new AppIcon(item, x, 0, command);
        helper.appIcons.add(db, appsIcon, Constants.DOC, pos);
    }

    private void addAllApps(SQLiteDatabase db)
    {
        AppList item = new AppList(getContext());
        addAppIcon(db, item, 0);
        addAppIcon(db, item, 1);
        addAppIcon(db, item, 2);
    }
}
