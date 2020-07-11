package ru.net.serbis.launcher.widget;

import android.appwidget.*;
import android.content.*;
import android.content.pm.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;

public class WidgetData extends ArrayList<AppWidgetProviderInfo>
{
    public WidgetData(AppWidgetProviderInfo info)
    {
        add(info);
    }

    public String getPackageName()
    {
        return getPackageName(get(0));
    }

    public static String getPackageName(AppWidgetProviderInfo info)
    {
        return info.provider.getPackageName();
    }

    public void initAppData(PackageManager manager, TextView label, ImageView icon)
    {
        try
        {
            ApplicationInfo app = manager.getApplicationInfo(getPackageName(), 0);
            label.setText(manager.getApplicationLabel(app) + " (" + size() + ")");
            icon.setImageDrawable(manager.getApplicationIcon(app));
        }
        catch (Exception e)
        {
            Log.info(this, e);
        }
    }

    public static void initLabel(AppWidgetProviderInfo info, TextView label)
    {
        label.setText(info.label);
    }

    public static void initLabelIcon(AppWidgetProviderInfo info, PackageManager manager, TextView label, ImageView icon)
    {
        initLabel(info, label);
        icon.setImageDrawable(manager.getDrawable(getPackageName(info), info.icon, null));
    }

    public static void initLabelPreview(AppWidgetProviderInfo info, PackageManager manager, TextView label, ImageView icon)
    {
        initLabel(info, label);
        icon.setImageDrawable(manager.getDrawable(getPackageName(info), info.previewImage, null));
    }
}
