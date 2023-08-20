package ru.net.serbis.launcher.group;

import android.app.*;
import android.content.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;

public class GroupNameDialog extends AlertDialog.Builder
{
    private EditText input;
    
    public GroupNameDialog(Context context, GroupAdapter groupAdapter)
    {
        super(context);
        
        setTitle(R.string.groupNew);
        
        initInput();
        initPositive(groupAdapter);
        initNegative();
    }

    private void initInput()
    {
        input = new EditText(getContext());
        input.setLines(1);
        setView(input);
    }
    
    private void initPositive(final GroupAdapter groupAdapter)
    {
        setPositiveButton(
            android.R.string.ok,
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    DBHelper db = new DBHelper(getContext());
                    Group group = db.groups.createGroup(input.getText().toString());
                    if (group != null)
                    {
                        groupAdapter.add(group);
                    }
                }
            }
        );
    }
    
    private void initNegative()
    {
        setNegativeButton(
            android.R.string.cancel,
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            }
        );
    }
}
