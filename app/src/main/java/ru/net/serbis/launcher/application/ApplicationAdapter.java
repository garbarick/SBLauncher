package ru.net.serbis.launcher.application;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;
import ru.net.serbis.launcher.help.*;

public class ApplicationAdapter extends ItemAdapter<Item>
{
    public ApplicationAdapter(Context context, int resourceId, int layoutId, List<Item> objects)
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

        Item item = getItem(position);

        initImage(position, view, item);
        initLabel(position, view, item);

        return view;
    }

    protected void initImage(int position, View view, Item item)
    {
        ImageView icon = Tools.getView(view, R.id.icon);
        icon.setImageDrawable(item.getIcon());
    }
    
    protected void initLabel(int position, View view, Item item)
    {
        TextView label = Tools.getView(view, R.id.label);
        label.setText(item.getLabel());
    }
}
