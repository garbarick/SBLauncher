package ru.net.serbis.launcher;

import android.os.*;
import java.io.*;
import ru.net.serbis.launcher.help.*;

public class Log
{
    public static void info(Object o, BufferedReader input, String prefix) throws Exception
    {
        String line;
        while (input.ready() && (line = input.readLine()) != null)
        {
            info(o, prefix + ":" + line);
        }
    }

    public static void info(Object o, String message)
    {
        android.util.Log.i(o.getClass().getName(), message);
    }

    public static void info(Object o, String message, Throwable e)
    {
        android.util.Log.i(o.getClass().getName(), message, e);
    }
	
	public static void info(Object o, Throwable e)
    {
        info(o, "Error", e);
    }
    
    public static synchronized void toFile(String message)
    {
        File dir = Environment.getExternalStorageDirectory();
        File logFile = new File(dir , "SBLauncher.log");
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter(new FileWriter(logFile, true));
            writer.printf("%s\n", message);
        }
        catch (Exception e)
        {
        }
        finally
        {
            Tools.close(writer);
        }
    }
}
