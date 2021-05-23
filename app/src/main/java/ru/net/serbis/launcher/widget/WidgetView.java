package ru.net.serbis.launcher.widget;

import android.appwidget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.swipe.*;

import ru.net.serbis.launcher.Log;

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

    public void startDrag()
    {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(this);
        ClipData data = ClipData.newPlainText(null, null);
        startDrag(data, builder, new DragItem(this, null), 0);
    }

    public void savePosition(DragEvent event, DragItem item, DBHelper db)
    {
        try
        {
            Point point = host.getPosition(event, item);
            host.removeFromParent(this);

            widget.setX(point.x);
            widget.setY(point.y);

            db.widgets.updateWidget(widget, host.getName(), host.getPlace());

            host.createWidget(widget);
        }
        catch (Exception e)
        {
            Log.info(this, "error on save widget", e);
        }
    }

    public void remove(DragItem item, WidgetHost widgetHost, DBHelper db)
    {
        WidgetView view = (WidgetView) item.getView();
        db.widgets.removeWidget(view.getWidget().getId());
        widgetHost.deleteAppWidgetId(view.getAppWidgetId());
        host.removeFromParent(view);
    }
}
