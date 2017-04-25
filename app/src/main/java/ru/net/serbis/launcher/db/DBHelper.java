package ru.net.serbis.launcher.db;
import android.content.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.table.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.ikon.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.widget.*;

public class DBHelper extends SQLiteOpenHelper
{
    private Context context;
    private GroupsTable groups = new GroupsTable();
    private ApplicationsTable applications = new ApplicationsTable();
    private SettingsTable settings = new SettingsTable();
    private WidgetTable widgets = new WidgetTable();
    private IkonTable ikons = new IkonTable();
    
    private List<Table> tables = Arrays.asList(
        groups, 
        applications, 
        settings,
        widgets,
        ikons);

    public DBHelper(Context context)
    {
        super(context, "db", null, 1);
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
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        createTables(db);
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

    private void createTables(SQLiteDatabase db)
    {
        for (Table table : tables)
        {
            try
            {
                table.createTable(db);
            }
            catch (Exception e)
            {
                Log.info(this, "Error on create tables", e);
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
        result.keySet().removeAll(applications.getItemNames(null, null));
        return new ArrayList<Item>(result.values());
    }

    private List<Item> filterItemForGroup(Map<String, Item> items, Group group)
    {
        List<Item> result = new ArrayList<Item>();
        for (String name : applications.getItemNames("group_id = ?", new String[]{group.getId().toString()}))
        {
            if (items.containsKey(name))
            {
                result.add(items.get(name));
            }
        }
        return result;
    }
    
    public Item getItem(String name)
    {
        return Items.getIstance().getItem(context, name);
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
        return applications.saveItemsInGroup(items, group);
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
    
    public void addWidget(Widget widget, String host, int place)
    {
        widgets.addWidget(widget, host, place);
    }
    
    public List<Widget> getWidgets(String host, int place)
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
    
    public void addIkon(Ikon ikon, String host, int place)
    {
        ikons.addIkon(ikon, host, place);
    }

    public List<Ikon> getIkons(String host, int place)
    {
        return ikons.getIkons(host, place);
    }

    public void removeIkon(long id)
    {
        ikons.removeIkon(id);
    }

    public void updateIkon(Ikon ikon, String host, int place)
    {
        ikons.updateIkon(ikon, host, place);
    }
}
