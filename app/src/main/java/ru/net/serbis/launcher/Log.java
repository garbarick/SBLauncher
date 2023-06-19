package ru.net.serbis.launcher;

import java.io.*;

public class Log
{
    public static void info(Object o, String message)
    {
        android.util.Log.i(o.getClass().getName(), message);
    }

    public static void error(Object o, String message)
    {
        android.util.Log.e(o.getClass().getName(), message);
    }

    public static void error(Object o, String message, Throwable e)
    {
        android.util.Log.e(o.getClass().getName(), message, e);
    }
	
	public static void error(Object o, Throwable e)
    {
        error(o, "Error", e);
    }

    public static void error(Object o, BufferedReader input) throws Exception
    {
        String line;
        while (input.ready() && (line = input.readLine()) != null)
        {
            error(o, line);
        }
    }
}
