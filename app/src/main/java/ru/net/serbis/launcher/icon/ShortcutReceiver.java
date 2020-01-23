package ru.net.serbis.launcher.icon;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;

public class ShortcutReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			Intent extra = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
			Item item = Items.getIstance().addItem(context, extra.getComponent());
			if (item != null)
			{
				addToHiddenGroup(context, item);
				addToDesktop(context, item);
			}
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
	}
	
	private void addToDesktop(Context context, Item item)
	{
		Intent intent = new Intent(context, Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.putExtra(Constants.ITEM_KEY, item.getKey());

		intent.putExtra(Constants.ITEM_POS_X, 10);
		intent.putExtra(Constants.ITEM_POS_Y, 10);

		context.startActivity(intent);
	}
	
	private void addToHiddenGroup(Context context, Item item)
	{
		DBHelper db = new DBHelper(context);
		db.appsGroup.addItemInGroup(item, Group.HIDDEN);
	}
}
