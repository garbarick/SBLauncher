package ru.net.serbis.launcher.help;

import android.os.*;
import android.view.*;
import java.io.*;
import android.app.*;

public class Tools
{
    public static void close(Closeable o)
    {
        try
        {
            if (o != null)
            {
                o.close();
            }
        }
        catch (Exception e)
        {
        }
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str)
    {
        return str != null && str.length() > 0;
    }

    public static int toInt(String str)
    {
        return Integer.valueOf(str);
    }
    
    public static <T extends View> T getView(Object view, int id)
    {
        if (view instanceof View)
        {
            return (T) ((View)view).findViewById(id);
        }
        else if (view instanceof Activity)
        {
            return (T) ((Activity)view).findViewById(id);
        }
        return null;
    }

    public static File getToolDir()
    {
        File dir = Environment.getExternalStorageDirectory();
        File appDir = new File(dir, "SBLauncher");
        appDir.mkdirs();
        return appDir;
    }
}