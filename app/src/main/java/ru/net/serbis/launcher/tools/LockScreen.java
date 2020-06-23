package ru.net.serbis.launcher.tools;

import android.app.*;
import android.app.admin.*;
import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.host.*;

public class LockScreen extends Item
{
    public LockScreen(Context context)
    {
        super(
            context.getResources().getString(R.string.lockScreen),
            context.getResources().getDrawable(android.R.drawable.ic_lock_lock),
            LockScreen.class.getName(),
            context.getPackageName());
    }

    @Override
    public void start(Activity context)
    {
        DevicePolicyManager manager = Tools.getService(context, Context.DEVICE_POLICY_SERVICE);
        ComponentName comp = new ComponentName(context, AdminReceiver.class);
        if (manager.isAdminActive(comp))
        {
            manager.lockNow();
        }
        else
        {
            provideAccess(context, comp);
        }
    }

    @Override
    public void start(Host host)
    {
        start(host.getActivity());
    }

    private void provideAccess(Activity context, ComponentName comp)
    {
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", comp);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", context.getResources().getString(R.string.deviceAdmin));
        context.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }
}
