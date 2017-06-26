package ru.net.serbis.launcher.drag;

import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.ikon.*;
import ru.net.serbis.launcher.widget.*;

public abstract class DragAndShowViewListener extends DragListener
{
    private View[] viewsForWidget;
    private View[] viewsForIkon;

    public void setViewsForWidget(View... viewsForWidget)
    {
        this.viewsForWidget = viewsForWidget;
    }

    public void setViewsForIkon(View... viewsForIkon)
    {
        this.viewsForIkon = viewsForIkon;
    }

    private void showViews(View[] views, int visible)
    {
        for (View view : views)
        {
            if (view != null)
            {
                view.setVisibility(visible);
            }
        }
    }
    
    private void viewDrag(DragItem item, int itemVisible, int viewsVisible)
    {
        View view = item.getView();
        if (view instanceof WidgetView)
        {
            showViews(viewsForWidget, viewsVisible);
        }
        else if (view instanceof IkonView)
        {
            showViews(viewsForIkon, viewsVisible);
        }
        else
        {
            return;
        }
        view.setVisibility(itemVisible);
    }
    
    @Override
    protected void startDrag(View view, DragEvent event, DragItem item)
    {
        viewDrag(item, View.INVISIBLE, View.VISIBLE);
    }

    @Override
    protected void endDrag(View view, DragEvent event, DragItem item)
    {
        viewDrag(item, View.VISIBLE, View.INVISIBLE);
    }
}
