package ru.net.serbis.launcher.help;

import android.view.*;
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
    
    public static <T extends View> T getView(View view, int id)
    {
        return (T) view.findViewById(id);
    }
}
