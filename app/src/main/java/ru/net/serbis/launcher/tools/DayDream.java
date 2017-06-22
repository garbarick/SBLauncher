package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import android.content.pm.*;

public class DayDream extends Item
{
    public DayDream(Context context)
    {
        super(
            context.getResources().getString(R.string.dayDream),
            context.getResources().getDrawable(R.drawable.day_dream),
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
        return getIntent().resolveActivityInfo(manager, 0) != null;
    }
}
