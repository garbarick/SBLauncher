package ru.net.serbis.launcher.tools;

import android.content.*;
import android.content.pm.*;
import ru.net.serbis.launcher.application.*;

public class DayDream extends Item
{
    public DayDream(Context context)
    {
        super(null, null, 
            "com.android.systemui.Somnambulator",
            "com.android.systemui");
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
