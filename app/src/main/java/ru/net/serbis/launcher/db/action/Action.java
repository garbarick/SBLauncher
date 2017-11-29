package ru.net.serbis.launcher.db.action;

import android.database.sqlite.*;

public abstract class Action<T>
{
	public abstract T call(SQLiteDatabase db);
	
	public T onError()
	{
		return null;
	}
}

