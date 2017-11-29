package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.action.*;

public class AppsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists apps(" +
			"    id integer primary key autoincrement," +
            "    name text," +
            "    package text," +
			"    unique (name, package) on conflict ignore" +
            ")");
    }
	
	public long add(final Item item)
    {
		return write(
			new Action<Long>()
			{
				@Override
				public Long call(SQLiteDatabase db)
				{
					return add(db, item);
				}
			}
		);
    }

    public long add(SQLiteDatabase db, Item item)
    {
		long id = getId(db, item);
		if (id > 0)
		{
			return id;
		}
		
    	ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("package", item.getPackageName());
        return db.insert("apps", null, values);
    }
	
	private long getId(SQLiteDatabase db, Item item)
	{
		Cursor cursor = db.query("apps", new String[]{"id"}, "name = ? and package = ?", new String[]{item.getName(), item.getPackageName()}, null, null, null);
		if (cursor.moveToFirst())
        {
            return cursor.getLong(0);
		}
		return 0;
	}
}
