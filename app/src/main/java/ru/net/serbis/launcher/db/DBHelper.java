package ru.net.serbis.launcher.db;

import android.content.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.table.*;
import ru.net.serbis.launcher.group.*;

public class DBHelper extends SQLiteOpenHelper
{
    private Context context;

    public GroupsTable groups = new GroupsTable();
    public AppsGroupTable appsGroup = new AppsGroupTable();
    public SettingsTable settings = new SettingsTable();
    public WidgetTable widgets = new WidgetTable();
    public AppIconsTable appIcons = new AppIconsTable();
	public AppsTable apps = new AppsTable();

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
	
	public long addApplication(SQLiteDatabase db, Item item)
    {
		return apps.add(db, item);
	}
}
