package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import java.io.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.icon.*;

public class ShortcutsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists shortcuts(" +
            "    id integer primary key autoincrement," +
            "    name text," +
            "    bitmap blob," +
            "    data text," +
            "    package text," +
            "    x integer," +
            "    y integer," +
            "    host text," +
            "    place integer" +
            ")");
    }

    private void add(SQLiteDatabase db, Shortcut icon, String host, int place)
    {
        ContentValues values = new ContentValues();
        values.put("name", icon.getName());
        values.put("bitmap", getBytes(icon.getBitmap()));
        values.put("data", icon.getData());
        values.put("package", icon.getPackage());
        values.put("x", icon.getX());
        values.put("y", icon.getY());
        values.put("host", host);
        values.put("place", place);
        icon.setId(db.insert("shortcuts", null, values));        
    }

    private byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap getBitmap(byte[] bytes)
    {
        return BitmapFactory.decodeStream(new ByteArrayInputStream(bytes));
    }

    public void add(final Shortcut icon, final String host, final int place)
    {
        write(
            new Action<Void>()
            {
                @Override
                public Void call(SQLiteDatabase db)
                {
                    add(db, icon, host, place);
                    return null;
                }
            }
        );
    }

    private List<Shortcut> getShortcuts(SQLiteDatabase db, String host, Integer place)
    {
        List<Shortcut> result = new ArrayList<Shortcut>();
        Cursor cursor = db.query("shortcuts", new String[]{"id", "name", "bitmap", "data", "package", "x", "y"}, "host = ? and place = ?", new String[]{host, place.toString()}, null, null, "id");
        if (cursor.moveToFirst())
        {
            do
            {
                result.add(getShortcut(cursor));
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    private Shortcut getShortcut(Cursor cursor)
    {
        Shortcut result = new Shortcut(
            cursor.getString(1),
            getBitmap(cursor.getBlob(2)),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getInt(5),
            cursor.getInt(6));
        result.setId(cursor.getLong(0));
        return result;
    }

    public Collection<Shortcut> getShortcuts(final String host, final int place)
    {
        return read(
            new CollectionAction<Shortcut>()
            {
                @Override
                public Collection<Shortcut> call(SQLiteDatabase db)
                {
                    return getShortcuts(db, host, place);
                }
            }
        );
    }

    private Shortcut getShortcut(SQLiteDatabase db, Long id)
    {
        Cursor cursor = db.query("shortcuts", new String[]{"id", "name", "bitmap", "data", "package", "x", "y"}, "id = ?", new String[]{id.toString()}, null, null, null);
        if (cursor.moveToFirst())
        {
            return getShortcut(cursor);
        }
        return null;
    }

    public Shortcut getShortcut(final Long id)
    {
        return read(
            new Action<Shortcut>()
            {
                @Override
                public Shortcut call(SQLiteDatabase db)
                {
                    return getShortcut(db, id);
                }
            }
        );
    }

    private void remove(SQLiteDatabase db, Long id)
    {
        db.delete("shortcuts", "id = ?", new String[]{id.toString()});
    }

    public void remove(final Long id)
    {
        write(
            new Action<Void>()
            {
                @Override
                public Void call(SQLiteDatabase db)
                {
                    remove(db, id);
                    return null;
                }
            }
        );
    }

    private void update(SQLiteDatabase db, Long id, String host, int place, int x, int y)
    {
        ContentValues values = new ContentValues();
        values.put("x", x);
        values.put("y", y);
        values.put("host", host);
        values.put("place", place);
        db.update("shortcuts", values, "id = ?", new String[]{id.toString()});   
    }

    public void update(final Shortcut shortcut, final String host, final int place)
    {
        write(
            new Action<Void>()
            {
                @Override
                public Void call(SQLiteDatabase db)
                {
                    update(db, shortcut.getId(), host, place, shortcut.getX(), shortcut.getY());
                    return null;
                }
            }
        );
    }
}
