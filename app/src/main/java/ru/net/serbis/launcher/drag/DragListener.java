package ru.net.serbis.launcher.drag;

import android.view.*;

public abstract class DragListener implements View.OnDragListener
{    
    public boolean onDrag(View view, DragEvent event)
    { 
        DragItem item = (DragItem) event.getLocalState();

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
        View itemView = item.getView();
        itemView.setVisibility(View.INVISIBLE);
    }
    
    protected void endDrag(View view, DragEvent event, DragItem item)
    {
        View itemView = item.getView();
        itemView.setVisibility(View.VISIBLE);
    }
    
    protected abstract void moveItem(View view, DragEvent event, DragItem item);
}
