package ru.net.serbis.launcher.ei;

import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.help.*;

public class ExportData extends Item
{
    private String group;
    private Set<Host> hosts;
    private boolean hidden;
    
    public ExportData()
    {
        super(null, null, null, null);
    }

    public ExportData(String name, String packageName)
    {
        super(null, null, name, packageName);
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public void setGroup(String group)
    {
        if (Tools.isEmpty(group))
        {
            return;
        }
        this.group = group;
    }
    
    public String getGroup()
    {
        return group;
    }
    
    public void addHost(String type, int place, int x, int y)
    {
        if (Tools.isEmpty(type))
        {
            return;
        }
        if (hosts == null)
        {
            hosts = new TreeSet<Host>();
        }
        hosts.add(new Host(type, place, x, y));
    }

    public void addHost(String host)
    {
        if (Tools.isEmpty(host))
        {
            return;
        }
        String[] items = host.split("_");
        if (items.length == 4)
        {
            String type = items[0];
            int place = Tools.toInt(items[1]);
            int x = Tools.toInt(items[2]);
            int y = Tools.toInt(items[3]);
            addHost(type, place, x, y);
        }
    }

    public Set<Host> getHosts()
    {
        return hosts;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public boolean isHidden()
    {
        return hidden;
    }
}
