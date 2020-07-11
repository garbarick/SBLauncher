package ru.net.serbis.launcher.group;

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.help.*;

public class GroupAdapter extends ItemAdapter<Group>
{
    private DBHelper db;
    
    public GroupAdapter(Context context, int resourceId, int layoutId, List<Group> objects)
    {
        super(context, resourceId, layoutId, objects);
        db = new DBHelper(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getItemView();
        }

        final Group item = getItem(position);
       
        TextView label = Tools.getView(view, R.id.groupLabel);
        label.setText(item.getName(getContext()));
        
        ImageView delete = Tools.getView(view, R.id.groupDelete);
        delete.setOnClickListener(
            new OnClickListener()
            {
                public void onClick(View view)
                {
                    db.groups.deleteGroup(item);
                    remove(item);
                }
            }
        );

        return view;
    }
}
