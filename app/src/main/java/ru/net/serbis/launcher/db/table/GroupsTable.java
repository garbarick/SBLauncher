package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;

public class GroupsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table groups(" +
            "    id integer primary key autoincrement," + 
            "    name text," +
            "    ordering integer" +
            ")");
        createGroup(db, Group.HIDDEN);
    }   

    public List<Group> getGroups()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            return getGroups(db);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get groups", e);
            return Collections.<Group>emptyList();
        }
        finally
        {
            db.close();
        }
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

    public boolean deleteGroup(Group group)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            return deleteGroup(db, group);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on delete group " + group.getName(getContext()), e);
            return false;
        }
        finally
        {
            db.close();
        }
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

    public Group createGroup(Long id, String name)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            return createGroup(db, id, name);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on create group " + name, e);
            return null;
        }
        finally
        {
            db.close();
        }
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

    public void saveGroupOrdering(List<Group> groups)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            saveGroupOrdering(db, groups);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on save group ordering", e);
        }
        finally
        {
            db.close();
        }
    }
    
    private void saveGroupOrdering(SQLiteDatabase db, List<Group> groups)
    {
        db.beginTransaction();

        int order = 0;
        for (Group group : groups)
        {
            ContentValues values = new ContentValues();
            values.put("ordering", order++);
            db.update("groups", values, "id = ?", new String[]{group.getId().toString()});
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public boolean updateGroup(Group group)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            return updateGroup(db, group);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on update group " + group.getName(getContext()), e);
            return false;
        }
        finally
        {
            db.close();
        }
    }
    
    private boolean updateGroup(SQLiteDatabase db, Group group)
    {
        ContentValues values = new ContentValues();
        values.put("name", group.getName(getContext()));
        int count = db.update("groups", values, "id = ?", new String[]{group.getId().toString()});
        return count > 0;
    }
}
