package ru.net.serbis.launcher.help;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;

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

    public static <T> T getService(Context context, String name)
    {
        return (T) context.getSystemService(name);
    }

    public static <T> T getExtra(Intent intent, String name)
    {
        return (T) intent.getSerializableExtra(name);
    }
    
    public static void setStatusBarColor(Activity activity, int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
    }

    public static void toast(Context context, int resource)
    {
        Toast.makeText(context, resource, Toast.LENGTH_LONG).show();
    }

    public static void toast(Context context, String resource)
    {
        Toast.makeText(context, resource, Toast.LENGTH_LONG).show();
    }

    public static boolean isSupportHeaderView()
    {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }

    public static Point getDisplaySize()
    {
        DisplayMetrics metrics =Resources.getSystem().getDisplayMetrics();
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }
    
    public static void killBackgroundProcesses(Context context, String packageName)
    {
        ActivityManager manager = getService(context, Context.ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(packageName);
    }
    
    public static String errorToText(Throwable error)
    {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
