package ru.net.serbis.launcher.view;

import android.app.*;
import android.view.*;
import ru.net.serbis.launcher.help.*;

public class NoMoveView implements View.OnLayoutChangeListener
{
    private Activity activity;
    private int startTop;

    public NoMoveView(Activity activity, int id)
    {
        this.activity = activity;
        View view = Tools.getView(activity, id);
        view.addOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View view,
                               int left,    int top,    int right,    int bottom,
                               int leftWas, int topWas, int rightWas, int bottomWas)
    {
        if (topWas == 0 && top > 0)
        {
            startTop = top;
        }
        else if (topWas > 0 && top > topWas)
        {
            view.setTop(startTop);
        }
    }
}
