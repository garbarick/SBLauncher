package ru.net.serbis.launcher.tools;

import android.content.*;
import android.content.pm.*;
import ru.net.serbis.launcher.application.*;

public class ActionItem extends Item
{
	public ActionItem(String name, String packageName)
    {
		super(null, null, name, packageName);
    }
	
	@Override
    protected Intent getIntent()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(packageName, name);
        return intent;
    }

    @Override
    public boolean validate(PackageManager manager)
    {
        ActivityInfo info = getIntent().resolveActivityInfo(manager, 0);
        if (info != null)
        {
            label = info.loadLabel(manager).toString();
            icon = info.loadIcon(manager);
            return true;
        }
        return false;
    }
}
