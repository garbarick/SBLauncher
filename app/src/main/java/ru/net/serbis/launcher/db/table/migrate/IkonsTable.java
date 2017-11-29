package ru.net.serbis.launcher.db.table.migrate;

import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.table.*;

public class IkonsTable extends Table
{
	@Override
	public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion == 1 && newVersion > 1)
		{
			migrateAppIcons(db);
			
			db.execSQL(
				" insert into app_icons(app_id, x, y, host, place)" +
				" select app.id, old.x, old.y, old.host, old.place" +
				"   from ikons old," +
				"        apps app" +
				"  where app.name = old.name"
			);
			
			dropTable(db, "ikons");
		}
	}

	private void migrateAppIcons(SQLiteDatabase db)
	{
		startWrite(db);
	    Cursor cursor = db.query("ikons", new String[]{"name"}, "name not in(select name from apps)", null, null, null, null);
		if (cursor.moveToFirst())
        {
			Map<String, Item> items = getItems();
            do
            {
                String name = cursor.getString(0);
				if (items.containsKey(name))
				{
					helper.addApplication(db, items.get(name));
				}
            }
            while(cursor.moveToNext());
        }
		stoptWrite(db);
	}

	private Map<String, Item> getItems()
	{
		Map<String, Item> result = new HashMap<String, Item>();
		for(Item item : Items.getIstance().getItems(helper.getContext()).values())
		{
			result.put(item.getName(), item);
		}
		return result;
	}
}
