package ru.net.serbis.launcher.dialog;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;

public abstract class FileDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener
{
    private FileAdapter adapter;

    public FileDialog(Context context, File dir, String ext)
    {
        super(context);
        setTitle(R.string.selectFile);

        adapter = new FileAdapter(context, dir, ext);
        setAdapter(adapter, null);
        setPositiveButton(android.R.string.ok, this);
        setNegativeButton(android.R.string.cancel, this);

        AlertDialog dialog = show();

        ListView list = dialog.getListView();
        list.setOnItemClickListener(adapter);
    }

    @Override
    public void onClick(DialogInterface dialig, int id)
    {
        switch(id)
        {
            case Dialog.BUTTON_POSITIVE:
                onPositive();
                break;

            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

    private void onPositive()
    {
        int position = adapter.getSelected();
        if (position == -1)
        {
            return;
        }
        onSelect(adapter.getItem(position));
    }

    protected abstract void onSelect(File file);
}
