package ru.net.serbis.launcher.ei;

import android.content.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.dialog.*;

public abstract class Tool
{
    protected Context context;
    protected DBHelper db;
    protected String result;
    
    public Tool(Context context)
    {
        this.context = context;
        db = new DBHelper(context);
    }
    
    public void executeDialog(int title)
    {
        new ProgressDialog(context, title)
        {
            @Override
            public void background()
            {
                try
                {
                    Thread.sleep(2000);
                    execute();
                }
                catch(Exception e)
                {
                    Log.info(Tool.this, e);
                    result = e.getMessage();
                }
            }

            @Override
            protected String getResult()
            {
                return result;
            }
            
            @Override
            protected void onPositive()
            {
                onFinish();
            }
        };
    }

    public abstract void execute() throws Exception;
    
    protected abstract void onFinish()
}
