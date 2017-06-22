package ru.net.serbis.launcher.application;

import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.tab.*;
import ru.net.serbis.launcher.tools.*;

public class Items extends BroadcastReceiver
{
    private static Items instance = new Items();
    
    private Map<String, Item> items;
    
    public static Items getIstance()
    {
        return instance;
    }
    
    public void init(Context context)
    {
        items = new HashMap<String, Item>();
        PackageManager manager = context.getPackageManager();
        
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        findActivities(manager, intent);
        
        intent = new Intent(context, Tabs.class);
        findActivities(manager, intent);
        
        addItem(new SwipeRight(context), manager);
        addItem(new SwipeLeft(context), manager);
        addItem(new SwipeTop(context), manager);
        addItem(new DayDream(context), manager);
    }
    
    private void addItem(Item item, PackageManager manager)
    {
        if (item.validate(manager))
        {
            items.put(item.getName(), item);
        }
    }

    private void findActivities(PackageManager manager, Intent intent)
    {
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : availableActivities)
        {
            Item item = new Item(
                resolveInfo.loadLabel(manager).toString().trim(),
                getActivityIcon(manager, resolveInfo.activityInfo),
                resolveInfo.activityInfo);
            
            items.put(item.getName(), item);
        }
    }
  
    private Drawable getActivityIcon(PackageManager manager, ActivityInfo info)
    {
        return info.loadIcon(manager);
    }
    
    public Map<String, Item> getItems(Context context)
    {
        if (items == null)
        {
            init(context);
        }
        return items;        
    }
    
    public Item getItem(Context context, String name)
    {
        return getItems(context).get(name);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        String action = intent.getAction();
        Log.info(this, packageName + " - " + action);
        instance.items = null;
    }
}
