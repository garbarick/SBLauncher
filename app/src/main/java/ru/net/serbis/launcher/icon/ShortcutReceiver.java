package ru.net.serbis.launcher.icon;

import android.content.*;
import android.os.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import android.graphics.*;
import ru.net.serbis.launcher.db.*;
import android.sax.*;

public class ShortcutReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
            switch(intent.getAction())
            {
                case Constants.INSTALL_SHORTCUT:
                    installShortcut(context, intent);
                    break;
            }
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
	}

    private void installShortcut(Context context, Intent intent)
    {
        logExtra(intent);
        Intent shortcut = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        switch (shortcut.getAction())
        {
            case Intent.ACTION_VIEW:
                createViewIcon(context, intent, shortcut);
                break;

            default:
                createAppIcon(context, shortcut);
                break;
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

    private void createAppIcon(Context context, Intent intent)
    {
        ComponentName comp = intent.getComponent();
        if (comp != null)
        {
            Item item = Items.getIstance().addItem(context, comp);
            if (item != null)
            {
                addToDesktop(context, item);
            }
        }
    }

    private void createViewIcon(Context context, Intent intent, Intent shortcut)
    {
        Shortcut icon = new Shortcut(
            intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME),
            intent.<Bitmap>getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON),
            shortcut.getDataString(),
            shortcut.getPackage(),
            10, 10);
        DBHelper db = new DBHelper(context);
        db.shortcuts.add(icon, Constants.DESKTOP, 0);
        addToDesktop(context, icon);
    }

    private void addToDesktop(Context context, Shortcut icon)
    {
        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(Constants.SHORTCUT_ID, icon.getId());
        context.startActivity(intent);
    }

    private void logExtra(Intent intent)
    {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
        {
            return;
        }
        for (String key : bundle.keySet())
        {
            Log.info(this, key + "=" + intent.getExtras().get(key));
        }
    }
}
