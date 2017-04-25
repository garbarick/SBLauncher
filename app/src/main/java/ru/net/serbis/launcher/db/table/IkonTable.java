package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.ikon.*;

public class IkonTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table ikons(" +
            "    id integer primary key autoincrement," +
            "    name text," +
            "    x integer," +
            "    y integer," +
            "    host text," +
            "    place integer" +
            ")");
    }
    
    public void addIkon(Ikon ikon, String host, int place)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            addIkon(db, ikon, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on add ikon", e);
        }
        finally
        {
            db.close();
        }
    }

    private void addIkon(SQLiteDatabase db, Ikon ikon, String host, int place)
    {
        ContentValues values = new ContentValues();
        values.put("name", ikon.getItem().getName());
        values.put("x", ikon.getX());
        values.put("y", ikon.getY());
        values.put("host", host);
        values.put("place", place);
        long id = db.insert("ikons", null, values);
        ikon.setId(id);
    }

    public List<Ikon> getIkons(String host, int place)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            return getIkons(db, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get ikons for place " + host + "." + place, e);
            return Collections.<Ikon>emptyList();
        }
        finally
        {
            db.close();
        }
    }

    private List<Ikon> getIkons(SQLiteDatabase db, String host, Integer place)
    {
        List<Ikon> result = new ArrayList<Ikon>();
        Cursor cursor = db.query("ikons", new String[]{"id", "name", "x", "y"}, "host = ? and place = ?", new String[]{host, place.toString()}, null, null, "id");
        if (cursor.moveToFirst())
        {
            do
            {
                String name = cursor.getString(1);
                Item item = helper.getItem(name);
                Ikon ikon = new Ikon(cursor.getLong(0), item, cursor.getInt(2), cursor.getInt(3));
                result.add(ikon);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public void removeIkon(long id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            removeIkon(db, id);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on remove ikon", e);
        }
        finally
        {
            db.close();
        }
    }

    private void removeIkon(SQLiteDatabase db, Long id)
    {
        db.delete("ikons", "id = ?", new String[]{id.toString()});
    }

    public void updateIkon(Ikon ikon, String host, int place)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            updateIkon(db, ikon, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on update ikon", e);
        }
        finally
        {
            db.close();
        }
    }

    private void updateIkon(SQLiteDatabase db, Ikon ikon, String host, int place)
    {
        ContentValues values = new ContentValues();
        values.put("x", ikon.getX());
        values.put("y", ikon.getY());
        values.put("host", host);
        values.put("place", place);
        db.update("ikons", values, "id = ?", new String[]{String.valueOf(ikon.getId())});   
    }
}
