package ru.net.serbis.launcher.db.action;

import java.util.*;

public abstract class CollectionAction<T> extends Action<Collection<T>>
{
	@Override
	public Collection<T> onError()
	{
		return Collections.<T>emptyList();
	}
}
