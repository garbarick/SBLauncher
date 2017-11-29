package ru.net.serbis.launcher.db.action;

public abstract class BooleanAction extends Action<Boolean>
{
	@Override
	public Boolean onError()
	{
		return false;
	}
}
