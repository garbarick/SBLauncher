package ru.net.serbis.launcher.set;

public class Parameter
{
    private Value name;
    private Type type;
    private String value;
    private String defaultValue;
    private int min;
    private int max;

    public Parameter(Value name, Type type)
    {
        this.name = name;
        this.type = type;
    }
    
    public Parameter(Value name, Type type, String defaultValue)
    {
        this(name, type);
    }
    
    public Parameter(Value name, Type type, Integer defaultValue)
    {
        this(name, type, defaultValue.toString());
    }
    
    public Parameter(Value name, Type type, Integer defaultValue, int min, int max)
    {
        this(name, type, defaultValue);
        this.min = min;
        this.max = max;
    }
    
    public Value getName()
    {
        return name;
    }

    public Type getType()
    {
        return type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    public void setIntValue(Integer value)
    {
        this.value = value.toString();
    }

    public void setBooleanValue(boolean value)
    {
        setIntValue(value ? 1 : 0);
    }
    
    public String getValue()
    {
        return value != null? value : defaultValue;
    }
    
    public int getIntValue()
    {
        try
        {
            return Integer.valueOf(getValue());
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    public boolean getBooleanValue()
    {
        return getIntValue() == 1;
    }
    
    public int getMin()
    {
        return min;
    }
    
    public int getMax()
    {
        return max;
    }
}
