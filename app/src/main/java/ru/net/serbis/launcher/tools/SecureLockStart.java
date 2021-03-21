package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;

public class SecureLockStart extends Item
{
    public SecureLockStart(Context context)
    {
        super(
            context.getResources().getString(R.string.securelock),
            context.getResources().getDrawable(R.drawable.secure_lock),
            SecureLock.class.getName(),
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
