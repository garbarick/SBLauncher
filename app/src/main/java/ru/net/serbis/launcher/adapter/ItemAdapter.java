package ru.net.serbis.launcher.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public abstract class ItemAdapter<T> extends ArrayAdapter<T> 
{
    protected int layoutId;

    public ItemAdapter(Context context, int resourceId, int layoutId)
    {
        super(context, resourceId);
        this.layoutId = layoutId;
    }
    
    public ItemAdapter(Context context, int resourceId, int layoutId, List<T> objects)
    {
        this(context, resourceId, layoutId);
        addAll(objects);
    }

    protected View getItemView()
    {
        return getItemView(layoutId);
    }

    protected View getItemView(int layoutId)
    {
        return LayoutInflater.from(getContext()).inflate(layoutId, null);
    }
}
