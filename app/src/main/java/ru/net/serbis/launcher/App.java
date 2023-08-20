package ru.net.serbis.launcher;

import android.app.*;
import ru.net.serbis.launcher.handler.*;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getApplicationContext()));
    }
}
