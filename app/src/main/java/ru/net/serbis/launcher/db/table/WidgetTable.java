package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import java.util.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.widget.*;

public class WidgetTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists widgets(" +
            "    id integer primary key," +
            "    x integer," +
            "    y integer," +
            "    w integer," +
            "    h integer," +
            "    host text," +
            "    place integer" +
            ")");
    }

    public void addWidget(final Widget widget, final String host, final int place)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					addWidget(db, widget, host, place);
					return null;
				}
			}
		);
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

    public Collection<Widget> getWidgets(final String host, final int place)
    {
		return read(
			new CollectionAction<Widget>()
			{
				@Override
				public Collection<Widget> call(SQLiteDatabase db)
				{
					return getWidgets(db, host, place);
				}
			}
		);
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

    public void removeWidget(final int id)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					removeWidget(db, id);
					return null;
				}
			}
		);
    }

    private void removeWidget(SQLiteDatabase db, Integer id)
    {
        db.delete("widgets", "id = ?", new String[]{id.toString()});
    }

    public void updateWidget(final Widget widget, final String host, final int place)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					updateWidget(db, widget, host, place);
					return null;
				}
			}
		);
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
