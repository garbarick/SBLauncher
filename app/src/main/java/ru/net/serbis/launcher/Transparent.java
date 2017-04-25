package ru.net.serbis.launcher;
import android.app.*;
import android.os.*;
import android.view.*;

public class Transparent
{
    public static void set(Activity activity)
    {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }    
}
