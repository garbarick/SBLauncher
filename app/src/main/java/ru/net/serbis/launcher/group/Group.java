package ru.net.serbis.launcher.group;

import android.content.*;
import java.io.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.set.*;

public class Group implements Serializable
{
    public static final String GROUP = "group";
    public static final String POSITION = "position";
    
    public static final Group ALL = new Group(-2L, R.string.allApps);
    public static final Group HIDDEN = new Group(-1L, R.string.hideSet);
    
    private Long id;
    private Name name;

    public Group(Long id, String name)
    {
        this.id = id;
        this.name = new Name(name, 0);
    }
    
    public Group(Long id, int resource)
    {
        this.id = id;
        this.name = new Name(resource);
    }

    public Long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name.setValue(name);
    }
    
    public String getName(Context context)
    {
        return name.getValue(context);
    }

    @Override
    public boolean equals(Object that)
    {
        if (that instanceof Group)
        {
            return id.equals(((Group) that).id);
        }
        return false;
    }
}
