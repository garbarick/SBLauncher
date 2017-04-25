package ru.net.serbis.launcher.drag;

import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class DragItem
{
    private View view;
    private Object object;

    public DragItem(View view, Object object)
    {
        this.view = view;
        this.object = object;
    }

    public View getView()
    {
        return view;
    }

    public Object getObject()
    {
        return object;
    }
}
