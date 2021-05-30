package ru.net.serbis.launcher.dialog;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;
import ru.net.serbis.launcher.application.*;

public class ToolsDialog extends AlertDialog.Builder implements AdapterView.OnItemClickListener
{
    private ToolsAdapter adapter;
    private AlertDialog dialog;

    public ToolsDialog(Context context)
    {
        super(context);
        setTitle(R.string.addTool);

        adapter = new ToolsAdapter(context);
        setAdapter(adapter, null);

        dialog = show();

        ListView list = dialog.getListView();
        list.setOnItemClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        getContext().startActivity(
            Items.getIstance().getDesktopIntent(
                getContext(),
                adapter.getItem(position),
                view));
        dialog.dismiss();
    }
}
