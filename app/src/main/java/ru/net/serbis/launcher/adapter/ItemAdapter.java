package ru.net.serbis.launcher.adapter;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public abstract class ItemAdapter<T> extends ArrayAdapter<T> 
{
    protected int layoutId;
    
    public ItemAdapter(Context context, int resourceId, int layoutId, List<T> objects)
    {
        super(context, resourceId, objects);
        this.layoutId = layoutId;
    }

    protected Activity getActivity()
    {
        return (Activity) getContext();
    }
    
    protected View getItemView()
    {
        return getActivity().getLayoutInflater().inflate(layoutId, null);
    }
}
