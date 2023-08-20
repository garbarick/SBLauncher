package ru.net.serbis.launcher.tools;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.host.*;

public class CheckError extends Item
{
    public CheckError(Context context)
    {
        super(
            context.getResources().getString(R.string.checkError),
            context.getResources().getDrawable(android.R.drawable.ic_dialog_alert),
            CheckError.class.getName(),
            context.getPackageName());
    }

    @Override
    public void start(Context context)
    {
        throw new RuntimeException(label);
    }

    @Override
    public void start(Host host)
    {
        start(host.getActivity());
    }
}
