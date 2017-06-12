package ru.net.serbis.launcher.set;

import java.io.*;

public class Value implements Serializable
{
    private String value;
    private int resource;

    public Value(String value)
    {
        this.value = value;
    }

    public Value(String value, int resource)
    {
        this(value);
        this.resource = resource;
    }

    public Value(Integer value, int resource)
    {
        this(value.toString(), resource);
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public String getValue()
    {
        return value;
    }

    public int getResource()
    {
        return resource;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Value)
        {
            Value that = (Value) object;
            if (value != null)
            {
                return value.equals(that.value);
            }
            return resource == that.resource;
        }
        return false;
    }
}
