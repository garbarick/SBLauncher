package ru.net.serbis.launcher.ei;

public class Host implements Comparable
{
    private String type;
    private int place;
    private int x;
    private int y;

    public Host(String type, int place, int x, int y)
    {
        this.type = type;
        this.place = place;
        this.x = x;
        this.y = y;
    }

    public String getType()
    {
        return type;
    }

    public int getPlace()
    {
        return place;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return type + "_" + place + "_" + x + "_" + y;
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public int compareTo(Object that)
    {
        return this.toString().compareTo(that.toString());
    }
}
