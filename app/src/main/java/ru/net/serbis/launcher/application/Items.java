package ru.net.serbis.launcher.application;

import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.tools.*;

public class Items extends BroadcastReceiver
{
    private static Items instance = new Items();

	private Map<String, Item> items = new HashMap<String, Item>();
    private List<String> tools = new ArrayList<String>();
    private boolean init;
    private List<ItemsHandler> handlers = new ArrayList<ItemsHandler>();
    private boolean unbadgedIcon;
    private boolean searchDefaultActivities;

    public void reInit()
    {
        init = false;
    }

    public static Items getIstance()
    {
        return instance;
    }

    public synchronized void init(Context context)
    {
        items.clear();
        tools.clear();
        initParameters(context);
        findActivities(context);
        
        PackageManager manager = context.getPackageManager();

        addItem(new AppList(context), manager, true);
        addItem(new SwipeRight(context), manager, true);
        addItem(new SwipeLeft(context), manager, true);
        addItem(new SwipeTop(context), manager, true);
        addItem(new DayDream(), manager, true);
        addItem(new LockScreen(context), manager, true);
        addItem(SecureLock.getItem(context), manager, true);
        addItem(Pattern.getItem(context), manager, true);
        addItem(new CheckError(context), manager, true);

        init = true;
    }

    private void initParameters(Context context)
    {
        Parameters parameters = new Parameters();
        new DBHelper(context).settings.loadParameterValues(parameters.getItemsParameters());
        this.unbadgedIcon = parameters.unbadgedIcon.getBooleanValue();
        this.searchDefaultActivities = parameters.searchDefaultActivities.getBooleanValue();
    }

    public synchronized void findActivities(Context context)
    {
        findActivities(context, null);
    }

    private boolean addItem(Item item, PackageManager manager)
    {
        return addItem(item, manager, false);
    }

    private boolean addItem(Item item, PackageManager manager, boolean tool)
    {
        if (item.validate(manager))
        {
			addItem(item);
            if (tool)
            {
                tools.add(item.getKey());
            }
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
        if (unbadgedIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            return info.loadUnbadgedIcon(manager);
        }
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
    public void onReceive(final Context context, final Intent intent)
    {
        new Handler().postDelayed(
            new Runnable()
            {
                public void run()
                {
                    getIstance().onReceiveInternal(context, intent);
                }
            }, 100
        );
    }

    private void onReceiveInternal(Context context, Intent intent)
    {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        String action = intent.getAction();
        Log.info(this, packageName + " - " + action);
        if (Intent.ACTION_PACKAGE_REMOVED.equals(action))
        {
            removeItems(context, packageName);
            updateHandlers();
        }
        else if (Intent.ACTION_PACKAGE_ADDED.equals(action)  ||
                 Intent.ACTION_PACKAGE_CHANGED.equals(action) ||
                 Intent.ACTION_PACKAGE_REPLACED.equals(action))
        {
            findActivities(context, packageName);
            updateHandlers();
        }
        else if (Intent.ACTION_MEDIA_MOUNTED.equals(action))
        {
            findActivities(context);
            updateHandlers();
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
        if (searchDefaultActivities)
        {
            findActivities(context, Intent.CATEGORY_DEFAULT, packageName);
        }
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

    public synchronized void addHandler(ItemsHandler handler)
    {
        handlers.add(handler);
    }

    public synchronized void removeHandler(ItemsHandler handler)
    {
        handlers.remove(handler);
    }

    private void updateHandlers()
    {
        for (ItemsHandler handler : handlers)
        {
            handler.itemsUpdate();
        }
    }

    public List<String> getTools()
    {
        return tools;
    }

    public List<Item> getItemTools()
    {
        List<Item> result = new ArrayList<Item>();
        for (String key : tools)
        {
            result.add(items.get(key));
        }
        return result;
    }

    public Intent getDesktopIntent(Context context, Item item, int x, int y, String command)
    {
        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(Constants.ITEM_KEY, item.getKey());

        intent.putExtra(Constants.ITEM_POS_X, x);
        intent.putExtra(Constants.ITEM_POS_Y, y);

        if (command != null)
        {
            intent.putExtra(Constants.ITEM_COMMAND, command);
        }

        return intent;
	}

    public Intent getDesktopIntent(Context context, Item item)
    {
        Point size = Tools.getDisplaySize();
        return getDesktopIntent(context, item, size.x / 4, size.y / 4, null);
    }

    public Intent getDesktopIntent(Context context, Item item, View view)
    {
        return getDesktopIntent(context, item, view, null);
    }

    public Intent getDesktopIntent(Context context, Item item, View view, String comnand)
    {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return getDesktopIntent(context, item, rect.left, rect.top, comnand);
    }
}
