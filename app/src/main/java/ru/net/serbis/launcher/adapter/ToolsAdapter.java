package ru.net.serbis.launcher.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.help.*;

public class ToolsAdapter extends ItemAdapter<Item>
{
    public ToolsAdapter(Context context)
    {
        super(context, 0, R.layout.line_application, Items.getIstance().getItemTools());
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getItemView();
        }

        Item item = getItem(position);
        TextView label = Tools.getView(view, R.id.label);
        label.setText(item.getLabel());
        ImageView icon = Tools.getView(view, R.id.icon);
        icon.setImageDrawable(item.getIcon());

        return view;
    }
}
