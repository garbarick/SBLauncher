package ru.net.serbis.launcher.db.action;

import android.database.sqlite.*;

public abstract class VoidAction extends Action<Void>
{
	@Override
	public Void call(SQLiteDatabase db)
	{
		voidCall(db);
		return null;
	}
	
	protected abstract void voidCall(SQLiteDatabase db)
}
