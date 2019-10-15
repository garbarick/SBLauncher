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

	private Map<String, Item> items = new HashMap<String, Item>();
    private boolean init;

    public static Items getIstance()
    {
        return instance;
    }

    public synchronized void init(Context context)
    {
        findActivities(context);
        
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(context, Tabs.class);
        findActivities(manager, intent);

        addItem(new SwipeRight(context), manager);
        addItem(new SwipeLeft(context), manager);
        addItem(new SwipeTop(context), manager);
        addItem(new DayDream(), manager);
        
        init = true;
    }
    
    public synchronized void findActivities(Context context)
    {
        findActivities(context, null);
    }

    private boolean addItem(Item item, PackageManager manager)
    {
        if (item.validate(manager))
        {
			addItem(item);
			return true;
        }
		return false;
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
            addItem(item);
        }
    }

	private void addItem(Item item)
	{
		items.put(item.getKey(), item);
	}

	public Item addItem(Context context, ComponentName comp)
	{
		return getItem(context, comp.getClassName(), comp.getPackageName());
	}

    private Drawable getActivityIcon(PackageManager manager, ActivityInfo info)
    {
        return info.loadIcon(manager);
    }

	public Map<String, Item> getItems(Context context)
    {
        if (!init)
        {
            init(context);
        }
        return items;        
    }

	public Item getItem(Context context, String itemKey)
    {
        return getItems(context).get(itemKey);
    }

	public Item getItem(Context context, String name, String packageName)
    {
		Item item = getItem(context, Item.getKey(name, packageName));
		if (item == null)
		{
			item = new ActionItem(name, packageName);
			PackageManager manager = context.getPackageManager();
			if (!addItem(item, manager))
			{
				item = null;
			}
		}
		return item;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        String action = intent.getAction();
        Log.info(this, packageName + " - " + action);
        if (Intent.ACTION_PACKAGE_REMOVED.equals(action))
        {
            getIstance().removeItems(context, packageName);
        }
        else if (Intent.ACTION_PACKAGE_ADDED.equals(action)  ||
                 Intent.ACTION_PACKAGE_CHANGED.equals(action) ||
                 Intent.ACTION_PACKAGE_REPLACED.equals(action))
        {
            getIstance().findActivities(context, packageName);
        }
    }

    private synchronized void removeItems(Context context, String packageName)
    {
        List<String> keys = new ArrayList<String>();
        for (Item item : items.values())
        {
            if (packageName.equals(item.getPackageName()))
            {
                Log.info(this, "remove item " + item.getKey());
                keys.add(item.getKey());
            }
        }
        items.keySet().removeAll(keys);
    }

    private synchronized void findActivities(Context context, String packageName)
    {
        findActivities(context, Intent.CATEGORY_LAUNCHER, packageName);
        findActivities(context, Intent.CATEGORY_HOME, packageName);
    }
    
    private synchronized void findActivities(Context context, String category, String packageName)
    {
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(category);
        if (packageName != null)
        {
            intent.setPackage(packageName);
        }
        findActivities(manager, intent);
    }
}
