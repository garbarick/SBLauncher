package ru.net.serbis.launcher.application;

import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import ru.net.serbis.launcher.host.*;

/**
 * SEBY0408
 */
public class Item implements Comparable
{
    public static final String ITEM_NAME = "itemName";
    protected String label;
    protected Drawable icon;
    protected String name;
    protected String packageName;

    public Item(String label, Drawable icon, String name, String packageName)
    {
        this.label = label;
        this.icon = icon;
        this.name =  name;
        this.packageName = packageName;
    }
    
    public Item(String label, Drawable icon, ActivityInfo info)
    {
        this(label, icon, info.name, info.applicationInfo.packageName);
    }

    public String getLabel()
    {
        return label;
    }
    
    public Drawable getIcon()
    {
        return icon;
    }

    public String getName()
    {
        return name;
    }

    public String getPackageName()
    {
        return packageName;
    }
    
    protected Intent getIntent()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(new ComponentName(packageName, name));
        return intent;
    }
    
    public void start(Context context)
    {
        context.startActivity(getIntent());
    }
    
    public void start(Host host)
    {
        host.getActivity().startActivity(getIntent());
    }
    
    @Override
    public int compareTo(Object o)
    {
        Item that = (Item) o;
        return label.compareTo(that.label);
    }

    @Override
    public boolean equals(Object that)
    {
        return name.equals(((Item)that).name);
    }
   
    public boolean validate(PackageManager manager)
    {
        return true;
    }
}
