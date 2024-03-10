package ru.net.serbis.launcher.drag;

import android.view.*;

public class DragListener implements View.OnDragListener
{    
    public boolean onDrag(View view, DragEvent event)
    { 
        DragItem item = (DragItem) event.getLocalState();
        if (item == null)
        {
            return false;
        }

        switch(event.getAction())
        {
            case DragEvent.ACTION_DRAG_STARTED:
                startDrag(view, event, item);
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                endDrag(view, event, item);
                break;

            case DragEvent.ACTION_DROP:
                moveItem(view, event, item);
                break;
        }
        return true;
    }
    
    protected void startDrag(View view, DragEvent event, DragItem item)
    {
    }
    
    protected void endDrag(View view, DragEvent event, DragItem item)
    {
    }
    
    protected void moveItem(View view, DragEvent event, DragItem item)
    {
    }
}
