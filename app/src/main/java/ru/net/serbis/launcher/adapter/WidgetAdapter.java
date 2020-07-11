package ru.net.serbis.launcher.adapter;

import android.appwidget.*;
import android.content.*;
import android.content.pm.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.widget.*;

public class WidgetAdapter extends ItemAdapter<AppWidgetProviderInfo>
{
    private PackageManager manager;
    private boolean appLevel;
    private Map<String, WidgetData> datas = new TreeMap<String, WidgetData>();

    public WidgetAdapter(Context context)
    {
        super(context, 0, R.layout.line_application);
        manager = context.getPackageManager();
        initItems();
        setAppLevel();
    }

    public void setAppLevel()
    {
        appLevel = true;
        clear();
        for(WidgetData data : datas.values())
        {
            add(data.get(0));
        }
    }
    
    public void setPackageName(String packageName)
    {
        appLevel = false;
        clear();
        addAll(datas.get(packageName));
    }

    public boolean isAppLevel()
    {
        return appLevel;
    }

    private void initItems()
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(getContext());
        for(AppWidgetProviderInfo info : manager.getInstalledProviders())
        {
            String packageName = WidgetData.getPackageName(info);
            if (datas.containsKey(packageName))
            {
                datas.get(packageName).add(info);
            }
            else
            {
                WidgetData data = new WidgetData(info);
                datas.put(packageName, data);
            }
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        AppWidgetProviderInfo info = getItem(position);
        if (appLevel || info.previewImage == 0)
        {
            view = getItemView();
        }
        else
        {
            view = getItemView(R.layout.preview_widget);
        }

        TextView label = Tools.getView(view, R.id.label);
        ImageView icon = Tools.getView(view, R.id.icon);
        if (appLevel)
        {
            String packageName = WidgetData.getPackageName(info);
            WidgetData data = datas.get(packageName);
            data.initAppData(manager, label, icon);
        }
        else if (info.previewImage > 0)
        {
            WidgetData.initLabelPreview(info, manager, label, icon);
        }
        else if (info.icon > 0)
        {
            WidgetData.initLabelIcon(info, manager, label, icon);
        }
        else
        {
            WidgetData.initLabel(info, label);
        }

        return view;
    }
}
