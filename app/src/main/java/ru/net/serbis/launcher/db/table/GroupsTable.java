package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.group.*;

public class GroupsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists groups(" +
            "    id integer primary key autoincrement," + 
            "    name text," +
            "    ordering integer" +
            ")");
			
		if (!groupExists(db, Group.HIDDEN.getId()))
		{
        	createGroup(db, Group.HIDDEN);
		}
    }   

    public Collection<Group> getGroups()
    {
		return read(
			new CollectionAction<Group>()
			{
				@Override
				public Collection<Group> call(SQLiteDatabase db)
				{
					return getGroups(db);
				}
			}
		);
    }

    private List<Group> getGroups(SQLiteDatabase db)
    {
        List<Group> result = new ArrayList<Group>();
        Cursor cursor = db.query("groups", new String[]{"id", "name"}, "id != -1", null, null, null, "ordering");
        if (cursor.moveToFirst())
        {
            do
            {
                Group group = new Group(
                    cursor.getLong(0),
                    cursor.getString(1));
                result.add(group);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public boolean deleteGroup(final Group group)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					return deleteGroup(db, group);
				}
			}
		);
    }
    
    private boolean deleteGroup(SQLiteDatabase db, Group group)
    {
        int count = db.delete("groups", "id = ?", new String[]{group.getId().toString()});
        return count > 0;
    }

    public Group createGroup(String name)
    {
        return createGroup(null, name);
    }
    
    public Group createGroup(Group group)
    {
        return createGroup(group.getId(), group.getName(getContext()));
    }

    public Group createGroup(final Long id, final String name)
    {
		return write(
			new Action<Group>()
			{
				@Override
				public Group call(SQLiteDatabase db)
				{
					return createGroup(db, id, name);
				}
			}
		);
    }
    
    private Group createGroup(SQLiteDatabase db, Group group)
    {
        return createGroup(db, group.getId(), group.getName(getContext()));
    }
    
    private Group createGroup(SQLiteDatabase db, Long id, String name)
    {
        ContentValues values = new ContentValues();
        if (id != null)
        {
            values.put("id", id);
        }
        values.put("name", name);
        values.put("ordering", getMaxOrder(db));
        id = db.insert("groups", null, values);
        return new Group(id, name);
    }

    private int getMaxOrder(SQLiteDatabase db)
    {
        Cursor cursor = db.query("groups", new String[]{"max(ordering) + 1"}, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return 0;
    }

    public void saveGroupOrdering(final List<Group> groups)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					saveGroupOrdering(db, groups);
					return null;
				}
			}
		);
    }
    
    private void saveGroupOrdering(SQLiteDatabase db, List<Group> groups)
    {
        int order = 0;
        for (Group group : groups)
        {
            ContentValues values = new ContentValues();
            values.put("ordering", order++);
            db.update("groups", values, "id = ?", new String[]{group.getId().toString()});
        }
    }

    public boolean updateGroup(final Group group)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					return updateGroup(db, group);
				}
			}
		);
    }
    
    private boolean updateGroup(SQLiteDatabase db, Group group)
    {
        ContentValues values = new ContentValues();
        values.put("name", group.getName(getContext()));
        int count = db.update("groups", values, "id = ?", new String[]{group.getId().toString()});
        return count > 0;
    }
	
	private boolean groupExists(SQLiteDatabase db, Long id)
	{
		Cursor cursor = db.query("groups", new String[]{"1"}, "id = ?", new String[]{id.toString()}, null, null, null);
        return cursor.moveToFirst();
	}
}
