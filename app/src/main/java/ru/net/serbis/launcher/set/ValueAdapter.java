package ru.net.serbis.launcher.set;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;

public class ValueAdapter extends ItemAdapter<Value>
{
    public ValueAdapter(Context context, int resourceId, int layoutId, List<Value> objects)
    {
        super(context, resourceId, layoutId, objects);
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getItemView();
        }

        Value value = getItem(position);
        TextView name = (TextView)view.findViewById(R.id.value);
        name.setText(value.getResource());

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent)
    {
        return getView(position, view, parent);
    }
}
