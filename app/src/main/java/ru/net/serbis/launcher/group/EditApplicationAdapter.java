package ru.net.serbis.launcher.group;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;

public class EditApplicationAdapter extends ApplicationAdapter
{
    private Set<Integer> checked = new HashSet<Integer>();

    public EditApplicationAdapter(Context context, int resourceId, int layoutId, List<Item> objects)
    {
        super(context, resourceId, layoutId, objects);
    }

    public List<Item> getChecked()
    {
        List<Item> result = new ArrayList<Item>();
        for(int i : checked)
        {
            result.add(getItem(i));
        }
        return result;
    }

    public List<Item> getAll()
    {
        List<Item> result = new ArrayList<Item>();
        for(int i = 0; i < getCount(); i ++)
        {
            result.add(getItem(i));
        }
        return result;
    }
    
    public void setChecked(List<Item> items)
    {
        for (Item item : items)
        {
            int position = getPosition(item);
            if (position > -1)
            {
                checked.add(position);
            }
        }
    }

    @Override
    protected void initLabel(int position, View view, Item item)
    {
        CheckedTextView label = (CheckedTextView) view.findViewById(R.id.label);
        label.setText(item.getLabel());
        label.setChecked(checked.contains(position));
    }

    public void toggle(int position)
    {
        if (checked.contains(position))
        {
            checked.remove(position);
        }
        else
        {
            checked.add(position);
        }
        notifyDataSetChanged();
    }
}
