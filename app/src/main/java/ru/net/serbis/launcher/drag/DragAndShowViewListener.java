package ru.net.serbis.launcher.drag;

import android.view.*;
import java.util.*;

public abstract class DragAndShowViewListener extends DragListener
{
    private List<View> showViews;

    public DragAndShowViewListener(List<View> showViews)
    {
        this.showViews = showViews;
    }
    
    @Override
    protected void startDrag(View view, DragEvent event, DragItem item)
    {
        for (View showView : showViews)
        {
            showView.setVisibility(View.VISIBLE);
        }
        super.startDrag(view, event, item);
    }

    @Override
    protected void endDrag(View view, DragEvent event, DragItem item)
    {
        for (View showView : showViews)
        {
            showView.setVisibility(View.INVISIBLE);
        }
        super.endDrag(view, event, item);
    }
}
