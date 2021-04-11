package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.set.*;

public class DayDreamReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        DBHelper db = new DBHelper(context);
        Parameters parameters = new Parameters();
        db.settings.loadParameterValue(parameters.systemSecureLock);
        SecureLock.start(parameters.systemSecureLock.getBooleanValue(), context);
    }
}
