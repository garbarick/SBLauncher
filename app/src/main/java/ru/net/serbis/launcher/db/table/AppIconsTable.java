package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.db.table.migrate.*;
import ru.net.serbis.launcher.ei.*;
import ru.net.serbis.launcher.icon.*;

public class AppIconsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists app_icons(" +
            "    id integer primary key autoincrement," +
            "    app_id integer," +
            "    x integer," +
            "    y integer," +
            "    host text," +
            "    place integer," +
            "    command text," +
			"    foreign key(app_id) references apps(id)" +
            ")");
			
		new IkonsTable().setHelper(helper).createTable(db, oldVersion, newVersion);
        if (oldVersion == 3 && newVersion > 3)
        {
            db.execSQL(
                "alter table app_icons" +
                "  add column command text"
            );
        }
    }
    
    public void add(final AppIcon appIcon, final String host, final int place)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					add(db, appIcon, host, place);
					return null;
				}
			}
		);
    }

    public void add(SQLiteDatabase db, AppIcon appIcon, String host, int place)
    {
        long appId = helper.addApplication(db, appIcon.getItem());
        long id = add(db, appId, host, place, appIcon.getX(), appIcon.getY(), appIcon.getCommand());
        appIcon.setId(id);
    }

    public void add(SQLiteDatabase db, long appId, Host host)
    {
        add(db, appId, host.getType(), host.getPlace(), host.getX(), host.getY(), null);
    }

    private long add(SQLiteDatabase db, long appId, String host, int place, int x, int y, String command)
    {
        ContentValues values = new ContentValues();
        values.put("app_id", appId);
        values.put("x", x);
        values.put("y", y);
        values.put("host", host);
        values.put("place", place);
        values.put("command", command);
        return db.insert("app_icons", null, values);
    }

    public Collection<AppIcon> getIcons(final String host, final int place)
    {
		return read(
			new CollectionAction<AppIcon>()
			{
				@Override
				public Collection<AppIcon> call(SQLiteDatabase db)
				{
					return getIcons(db, host, place);
				}
			}
		);
    }

    private List<AppIcon> getIcons(SQLiteDatabase db, String host, Integer place)
    {
        List<AppIcon> result = new ArrayList<AppIcon>();
        Cursor cursor = db.query("app_icons i, apps a", new String[]{"i.id", "a.name", "a.package", "i.x", "i.y", "i.command"}, "i.app_id = a.id and i.host = ? and i.place = ?", new String[]{host, place.toString()}, null, null, "i.id");
        if (cursor.moveToFirst())
        {
            do
            {
                Item item = helper.getItem(cursor.getString(1), cursor.getString(2));
                if (item != null)
                {
                    AppIcon appIcon = new AppIcon(item, cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
                    appIcon.setId(cursor.getLong(0));
                    result.add(appIcon);
                }
            }
            while(cursor.moveToNext());
        }
        return result;
    }

    public void removeIcons(final List<Long> ids)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					remove(db, ids);
					return null;
				}
			}
		);
    }

    public void remove(long id)
    {
        removeIcons(Arrays.asList(id));
    }

    private void remove(SQLiteDatabase db, List<Long> ids)
    {
        for (long id : ids)
        {
            remove(db, id);
        }
    }
    
    private void remove(SQLiteDatabase db, Long id)
    {
        db.delete("app_icons", "id = ?", new String[]{id.toString()});
    }

    public void update(final AppIcon appIcon, final String host, final int place)
    {
		write(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					update(db, appIcon, host, place);
					return null;
				}
			}
		);
    }

    private void update(SQLiteDatabase db, AppIcon appIcon, String host, int place)
    {
        update(db, appIcon.getId(), host, place, appIcon.getX(), appIcon.getY());
    }

    public void update(SQLiteDatabase db, long id, Host host)
    {
        update(db, id, host.getType(), host.getPlace(), host.getX(), host.getY());
    }

    private void update(SQLiteDatabase db, Long id, String host, int place, int x, int y)
    {
        ContentValues values = new ContentValues();
        values.put("x", x);
        values.put("y", y);
        values.put("host", host);
        values.put("place", place);
        db.update("app_icons", values, "id = ?", new String[]{id.toString()});   
    }

    public long findIcon(SQLiteDatabase db, Long appId, String host, Integer place)
    {
        Cursor cursor = db.query("app_icons", new String[]{"id"}, "app_id = ? and host = ? and place = ?", new String[]{appId.toString(), host, place.toString()}, null, null, null);
        if (cursor.moveToFirst())
        {
            return cursor.getLong(0);
        }
        return 0;
    }

    public long findIcon(SQLiteDatabase db, Long appId, Host host)
    {
        return findIcon(db, appId, host.getType(), host.getPlace());
    }
}
