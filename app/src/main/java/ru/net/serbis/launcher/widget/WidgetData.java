package ru.net.serbis.launcher.widget;

import android.appwidget.*;
import android.content.pm.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;

public class WidgetData
{
    private List<AppWidgetProviderInfo> data = new ArrayList<AppWidgetProviderInfo>();

    public WidgetData(AppWidgetProviderInfo info)
    {
        data.add(info);
    }

    public List<AppWidgetProviderInfo> getData()
    {
        return data;
    }

    public void add(AppWidgetProviderInfo info)
    {
        data.add(info);
    }

    public String getPackageName()
    {
        return getPackageName(data.get(0));
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
            label.setText(manager.getApplicationLabel(app) + " (" + data.size() + ")");
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
