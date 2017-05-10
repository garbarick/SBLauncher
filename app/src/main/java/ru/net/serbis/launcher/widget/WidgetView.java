package ru.net.serbis.launcher.widget;
import android.appwidget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.swipe.*;

public class WidgetView extends AppWidgetHostView
{
    private Widget widget;
    private Host host;
    private GestureDetector detector;
    
    private class WidgetGestureListener extends SwipeGestureListener
    {
        public WidgetGestureListener()
        {
            super(false);
        }
        
        @Override
        public void onLongPress(MotionEvent event)
        {
            startDrag();
        }

        @Override
        public void onSwipeLeft()
        {
            host.swipeLeft();
        }

        @Override
        public void onSwipeRight()
        {
            host.swipeRight();
        }

        @Override
        public void onSwipeTop()
        {
            host.swipeTop();
        }
    }
    
    public WidgetView(Context context, int id, AppWidgetProviderInfo info)
    {
        super(context);
        
        setFocusable(true);
        TypedValue background = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, background, true);
        setBackgroundResource(background.resourceId);
        
        setAppWidget(id, info);
        detector = new GestureDetector(context, new WidgetGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return detector.onTouchEvent(event);
    }
    
    public void setWidget(Widget widget)
    {
        this.widget = widget;
    }

    public Widget getWidget()
    {
        return widget;
    }
    
    public void setHost(Host host)
    {
        this.host = host;
    }

    private void startDrag()
    {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(this);
        ClipData data = ClipData.newPlainText(null, null);
        WidgetView.this.startDrag(data, builder, new DragItem(this, null), 0);
    }
}
