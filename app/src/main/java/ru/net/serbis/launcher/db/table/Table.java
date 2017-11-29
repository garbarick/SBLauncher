package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.sqlite.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.db.action.*;

public abstract class Table
{
    protected DBHelper helper;

    public Table setHelper(DBHelper helper)
    {
        this.helper = helper;
		return this;
    }

    protected Context getContext()
    {
        return helper.getContext();
    }

    public abstract void createTable(SQLiteDatabase db, int oldVersion, int newVersion);

	private <T> T doAction(Action<T> action, boolean write)
	{
		SQLiteDatabase db = null;
		try
		{
			if (write)
			{
				db = helper.getWritableDatabase();
				try
				{
					startWrite(db);
					return action.call(db);
				}
				finally
				{
					stoptWrite(db);
				}
			}
			else
			{
				db = helper.getReadableDatabase();
				return action.call(db);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
			return action.onError();
		}
		finally
		{
			close(db);
		}
	}
	
	protected void startWrite(SQLiteDatabase db)
	{
		db.beginTransaction();
	}

	protected void stoptWrite(SQLiteDatabase db)
	{
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	protected <T> T read(Action<T> action)
	{
		return doAction(action, false);
	}

	protected <T> T write(Action<T> action)
	{
		return doAction(action, true);
	}

	private void close(SQLiteDatabase db)
	{
		try
		{
			if (db != null)
			{
				db.close();
			}
		}
		catch (Throwable e)
		{
		}
	}
	
	protected void dropTable(SQLiteDatabase db, String table)
	{
		db.execSQL("drop table if exists " + table);
	}
}
