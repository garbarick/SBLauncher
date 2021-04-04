package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.application.*;

public class ActivityItem extends Item
{
    public ActivityItem(Context context, int textId, int iconId, Class clazz)
    {
        super(
            context.getResources().getString(textId),
            context.getResources().getDrawable(iconId),
            clazz.getName(),
            context.getPackageName());
    }
    
    @Override
    protected Intent getIntent()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(packageName, name);
        return intent;
    }
}
