package ru.net.serbis.launcher.dialog;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.task.*;

public abstract class ProgressDialog extends AlertDialog.Builder implements Async, DialogInterface.OnClickListener
{
    protected View view;
    protected Task task;
    protected AlertDialog dialog;
    
    public ProgressDialog(Context context, int title)
    {
        super(context);
        setTitle(title);
        view = LayoutInflater.from(context).inflate(R.layout.progress, null);
        setView(view);
        
        setPositiveButton(android.R.string.ok, this);
        setNegativeButton(android.R.string.cancel, this);
        setCancelable(false);
        
        dialog = show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        
        task = new Task(this);
        task.execute();
    }

    @Override
    public void preExecute()
    {
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
                task.cancel(true);
                break;
        }
    }

    @Override
    public void postExecute()
    {
        ProgressBar progress = Tools.getView(view, R.id.progress);
        progress.setVisibility(View.GONE);
        
        TextView result = Tools.getView(view, R.id.result);
        result.setText(getResult());
        result.setVisibility(View.VISIBLE);
        
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
    }
    
    protected abstract String getResult();
    
    protected abstract void onPositive();
}
