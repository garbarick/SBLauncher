package ru.net.serbis.launcher.host;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.widget.*;

public class ResizeDialog extends AlertDialog.Builder
{
    private EditText widthEdit;
    private EditText heightEdit;
    
    public ResizeDialog(Context context, Host host, WidgetView view)
    {
        super(context);
        
        setTitle(R.string.resize);

        initLayout(host, view);
        initPositive(host, view);
        initNegative();
    }

    private void initLayout(Host host, WidgetView view)
    {
        View layout = host.getActivity().getLayoutInflater().inflate(R.layout.resize, null);
        widthEdit = Tools.getView(layout, R.id.width);
        heightEdit = Tools.getView(layout, R.id.height);
        
        Integer width = view.getWidget().getW();
        Integer height = view.getWidget().getH();
        width = width == 0 ? 400 : width;
        height = height == 0 ? 400 : height;
        
        widthEdit.setText(width.toString());
        heightEdit.setText(height.toString());
        
        setView(layout);
    }

    private void initPositive(final Host host, final WidgetView view)
    {
        setPositiveButton(
            android.R.string.ok,
            new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    int width = Integer.valueOf(widthEdit.getText().toString());
                    int height = Integer.valueOf(heightEdit.getText().toString());
                    view.getWidget().setW(width);
                    view.getWidget().setH(height);
                    host.saveWidgetSize(view);
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
