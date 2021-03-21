package ru.net.serbis.launcher.application;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.host.*;

public class Item implements Comparable
{
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
	
	public String getKey()
	{
		return getKey(name, packageName);
	}
	
	public static String getKey(String name, String packageName)
	{
		return packageName + "/" + name;
	}
    
    protected Intent getIntent()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(new ComponentName(packageName, name));
        return intent;
    }
    
    public void start(Activity context)
    {
        try
        {
            context.startActivity(getIntent());
        }
        catch(Throwable e)
        {
            Log.info(this, e);
        }
    }
    
    public void start(Host host)
    {
        start(host.getActivity());
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
