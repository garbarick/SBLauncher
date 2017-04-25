package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.widget.*;
import android.graphics.*;

public class WidgetTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table widgets(" +
            "    id integer primary key," +
            "    x integer," +
            "    y integer," +
            "    w integer," +
            "    h integer," +
            "    host text," +
            "    place integer" +
            ")");
    }

    public void addWidget(Widget widget, String host, int place)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            addWidget(db, widget, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on add widget", e);
        }
        finally
        {
            db.close();
        }
    }

    private void addWidget(SQLiteDatabase db, Widget widget, String host, int place)
    {
        ContentValues values = new ContentValues();
        values.put("id", widget.getId());
        values.put("x", widget.getX());
        values.put("y", widget.getY());
        values.put("host", host);
        values.put("place", place);
        db.insert("widgets", null, values);
    }

    public List<Widget> getWidgets(String host, int place)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            return getWidgets(db, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get widgets for place " + place, e);
            return Collections.<Widget>emptyList();
        }
        finally
        {
            db.close();
        }
    }

    private List<Widget> getWidgets(SQLiteDatabase db, String host, Integer place)
    {
        List<Widget> result = new ArrayList<Widget>();
        Cursor cursor = db.query("widgets", new String[]{"id", "x", "y", "w", "h"}, "host = ? and place = ?", new String[]{host, place.toString()}, null, null, "id");
        if (cursor.moveToFirst())
        {
            do
            {
                Widget widget = new Widget(
                    cursor.getInt(0),
                    new Rect(
                        cursor.getInt(1), 
                        cursor.getInt(2),
                        cursor.getInt(3), 
                        cursor.getInt(4)));
                result.add(widget);
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public void removeWidget(int id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            removeWidget(db, id);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on remove widget", e);
        }
        finally
        {
            db.close();
        }
    }

    private void removeWidget(SQLiteDatabase db, Integer id)
    {
        db.delete("widgets", "id = ?", new String[]{id.toString()});
    }

    public void updateWidget(Widget widget, String host, int place)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            updateWidget(db, widget, host, place);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on update widget", e);
        }
        finally
        {
            db.close();
        }
    }

    private void updateWidget(SQLiteDatabase db, Widget widget, String host, int place)
    {
        ContentValues values = new ContentValues();
        values.put("x", widget.getX());
        values.put("y", widget.getY());
        values.put("w", widget.getW());
        values.put("h", widget.getH());
        values.put("host", host);
        values.put("place", place);
        db.update("widgets", values, "id = ?", new String[]{String.valueOf(widget.getId())});   
    }
}
