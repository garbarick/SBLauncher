package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.set.*;

public class DayDreamReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        DBHelper db = new DBHelper(context);
        Parameters parameters = new Parameters();
        db.settings.loadParameterValue(parameters.secureLockAfterDayDream);
        if (parameters.secureLockAfterDayDream.getBooleanValue())
        {
            db.settings.loadParameterValue(parameters.systemSecureLock);
            SecureLock.start(parameters.systemSecureLock.getBooleanValue(), context);
        }
        else
        {
            updateDesktop(context);
        }
    }

    private void updateDesktop(Context context)
    {
        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(Constants.UPDATE_DESKTOP, true);
        context.startActivity(intent);
    }
}
