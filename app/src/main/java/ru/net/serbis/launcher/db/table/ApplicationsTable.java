package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;

public class ApplicationsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table applications(" +
            "    name text primary key," +
            "    package text," +
            "    group_id integer," +
            "    foreign key(group_id) references groups(id) on delete cascade" +
            ")");
    }
    
    public Set<String> getItemNames(String where, String[] binds)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            return getItemNames(db, where, binds);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get items names", e);
            return Collections.<String>emptySet();
        }
        finally
        {
            db.close();
        }
    }
    
    private Set<String> getItemNames(SQLiteDatabase db, String where, String[] binds)
    {
        Set<String> names = new HashSet<String>();
        Cursor cursor = db.query("applications", new String[]{"name"}, where, binds, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                names.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        } 
        return names;
    }
    
    public boolean saveItemsInGroup(List<Item> items, Group group)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            return saveItemsInGroup(db, items, group);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on save items into group " + group.getName(getContext()), e);
            return false;
        }
        finally
        {
            db.close();
        }
    }
    
    private boolean saveItemsInGroup(SQLiteDatabase db, List<Item> items, Group group)
    {
        db.beginTransaction();

        db.delete("applications", "group_id = ?", new String[]{group.getId().toString()});
        for (Item item : items)
        {
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("package", item.getPackageName());
            values.put("group_id", group.getId());
            db.insert("applications", null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }
}
