package ru.net.serbis.launcher.set;

import android.content.*;

public class Name extends Value
{
    public Name(String value, int resource)
    {
        super(value, resource);
    }
    
    public Name(int resource)
    {
        this((String) null, resource);
    }

    public String getValue(Context context)
    {
        if (getValue() != null)
        {
            return getValue();
        }
        else if (getResource() > 0)
        {
            return context.getResources().getString(getResource());
        }
        return null;
    }
}
