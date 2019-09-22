package ru.net.serbis.launcher.db;

import android.content.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.table.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.icon.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.widget.*;

public class DBHelper extends SQLiteOpenHelper
{
    private Context context;

    private GroupsTable groups = new GroupsTable();
    private AppsGroupTable appsGroup = new AppsGroupTable();
    private SettingsTable settings = new SettingsTable();
    private WidgetTable widgets = new WidgetTable();
    private AppIconsTable appIcons = new AppIconsTable();
	private AppsTable apps = new AppsTable();

    private List<Table> tables = Arrays.asList(new Table[] {
        groups,
        apps,
        appsGroup,
        settings,
        widgets,
        appIcons});

    public DBHelper(Context context)
    {
        super(context, "db", null, 2);
        this.context = context;

        initTables();
    }

    public Context getContext()
    {
        return context;
    }

    private void initTables()
    {
        for (Table table : tables)
        {
            table.setHelper(this);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db, 1, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        createTables(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void createTables(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (Table table : tables)
        {
            try
            {
                table.createTable(db, oldVersion, newVersion);
            }
            catch (Exception e)
            {
                Log.info(this, "Error on create tables: " + e.getMessage());
            }
        }
    }

    public List<Group> getGroups(boolean withAll)
    {
        List<Group> result = new ArrayList<Group>();
        if (withAll)
        {
            result.add(Group.ALL);
        }
        result.addAll(groups.getGroups());
        return result;
    }

    public List<Item> getItems(Group group)
    {
        Map<String, Item> items = Items.getIstance().getItems(context);
        if (Group.ALL.equals(group))
        {
            return filterItemForAll(items);
        }
        else
        {
            return filterItemForGroup(items, group);
        }
    }

    private List<Item> filterItemForAll(Map<String, Item> items)
    {
        Map<String, Item> result = new HashMap<String, Item>(items);
        result.keySet().removeAll(appsGroup.getItemKeys(null));
        return new ArrayList<Item>(result.values());
    }

    private List<Item> filterItemForGroup(Map<String, Item> items, Group group)
    {
        List<Item> result = new ArrayList<Item>();
        for (String name : appsGroup.getItemKeys(group))
        {
            if (items.containsKey(name))
            {
                result.add(items.get(name));
            }
        }
        return result;
    }

    public Item getItem(String name, String packageName)
    {
        return Items.getIstance().getItem(context, name, packageName);
    }
	
	public Item getItem(String itemKey)
    {
        return Items.getIstance().getItem(context, itemKey);
    }

    public boolean deleteGroup(Group group)
    {
        return groups.deleteGroup(group);
    }

    public Group createGroup(String name)
    {
        return groups.createGroup(name);
    }

    public void saveGroupOrdering(List<Group> groupList)
    {
        groups.saveGroupOrdering(groupList);
    }

    public boolean updateGroup(Group group)
    {
        return groups.updateGroup(group);
    }

    public boolean saveItemsInGroup(List<Item> items, Group group)
    {
        return appsGroup.saveItemsInGroup(items, group);
    }

	public boolean addItemInGroup(Item item, Group group)
    {
        return appsGroup.addItemInGroup(item, group);
    }
	
    public boolean loadParameterValue(Parameter parameter)
    {
        return settings.loadParameterValue(parameter);
    }

    public void loadParameterValues(List<Parameter> parameters)
    {
        settings.loadParameterValues(parameters);
    }

    public boolean saveParameterValues(List<Parameter> parameters)
    {
        return settings.saveParameterValues(parameters);
    }

    public boolean saveParameterValue(Parameter parameter)
    {
        return settings.saveParameterValue(parameter);
    }

    public void addWidget(Widget widget, String host, int place)
    {
        widgets.addWidget(widget, host, place);
    }

    public Collection<Widget> getWidgets(String host, int place)
    {
        return widgets.getWidgets(host, place);
    }

    public void removeWidget(int id)
    {
        widgets.removeWidget(id);
    }

    public void updateWidget(Widget widget, String host, int place)
    {
        widgets.updateWidget(widget, host, place);
    }

    public void addAppIcon(AppIcon appIcon, String host, int place)
    {
        appIcons.add(appIcon, host, place);
    }

    public Collection<AppIcon> getAppIcons(String host, int place)
    {
        return appIcons.getIcons(host, place);
    }

    public void removeAppIcon(long id)
    {
        appIcons.remove(id);
    }

    public void updateAppIcon(AppIcon appIcon, String host, int place)
    {
        appIcons.update(appIcon, host, place);
    }
	
	public long addApplication(SQLiteDatabase db, Item item)
    {
		return apps.add(db, item);
	}
}
