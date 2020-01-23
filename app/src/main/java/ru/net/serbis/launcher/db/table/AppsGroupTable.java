package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.db.table.migrate.*;
import ru.net.serbis.launcher.group.*;

public class AppsGroupTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists apps_group(" +
            "    app_id integer," +
            "    group_id integer," +
            "    foreign key(group_id) references groups(id) on delete cascade," +
			"    foreign key(app_id) references apps(id)" +
            ")");
			
		new ApplicationsTable().setHelper(helper).createTable(db, oldVersion, newVersion);
    }
    
    public Collection<String> getItemKeys(final Group group)
    {
		return read(
			new CollectionAction<String>()
			{
				@Override
				public Collection<String> call(SQLiteDatabase db)
				{
					return getItemKeys(db, group);
				}
			}
		);
    }
    
    private Collection<String> getItemKeys(SQLiteDatabase db, Group group)
    {
		String where = "g.app_id = a.id";
		String[] binds = null;
		if (group != null)
		{
			where += " and g.group_id = ?";
			binds = new String[]{group.getId().toString()};
		}
		
        Set<String> names = new HashSet<String>();
        Cursor cursor = db.query("apps_group g, apps a", new String[]{"a.name", "a.package"}, where, binds, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                names.add(Item.getKey(cursor.getString(0), cursor.getString(1)));
            }
            while(cursor.moveToNext());
        } 
        return names;
    }
    
    public boolean saveItemsInGroup(final List<Item> items, final Group group)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					return saveItemsInGroup(db, items, group);
				}
			}
		);
    }
    
    private boolean saveItemsInGroup(SQLiteDatabase db, List<Item> items, Group group)
    {
        db.delete("apps_group", "group_id = ?", new String[]{group.getId().toString()});
        return addItemsInGroup(db, items, group);
    }

	public boolean addItemInGroup(final Item item, final Group group)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					return addItemInGroup(db, item, group);
				}
			}
		);
    }

    private boolean addItemInGroup(SQLiteDatabase db, Item item, Group group)
    {
		Long id = helper.addApplication(db, item);
        excludeItemFromGroup(db, id, group);
		addItemInGroup(db, id, group);
        return true;
    }

	private void addItemInGroup(SQLiteDatabase db, Long itemId, Group group)
    {
		ContentValues values = new ContentValues();
        values.put("app_id", itemId);
        values.put("group_id", group.getId());
        db.insert("apps_group", null, values);
    }

    public boolean addItemsInGroup(final List<Item> items, final Group group)
    {
        return write(
            new BooleanAction()
            {
                @Override
                public Boolean call(SQLiteDatabase db)
                {
                    return addItemsInGroup(db, items, group);
                }
            }
        );
    }

    private boolean addItemsInGroup(SQLiteDatabase db, List<Item> items, Group group)
    {
        for (Item item : items)
        {
            Long id = helper.addApplication(db, item);
            addItemInGroup(db, id, group);
        }
        return true;
    }

    private void excludeItemFromGroup(SQLiteDatabase db, Long id, Group group)
    {
        db.delete("apps_group", "app_id = ? and group_id = ?", new String[]{id.toString(), group.getId().toString()});
    }

    public boolean excludeItemsFromGroup(final List<Item> items, final Group group)
    {
        return write(
            new BooleanAction()
            {
                @Override
                public Boolean call(SQLiteDatabase db)
                {
                    return excludeItemsFromGroup(db, items, group);
                }
            }
        );
    }

    private boolean excludeItemsFromGroup(SQLiteDatabase db, List<Item> items, Group group)
    {
        for (Item item : items)
        {
            Long id = helper.addApplication(db, item);
            excludeItemFromGroup(db, id, group);
        }
        return true;
    }
}
