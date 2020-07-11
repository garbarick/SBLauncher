package ru.net.serbis.launcher.host;

import android.app.*;
import android.appwidget.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.lang.reflect.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.icon.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.widget.*;

public abstract class Host extends Fragment
{
    protected AppWidgetManager widgetManager;
    protected WidgetHost widgetHost;
    protected RelativeLayout layout;

    protected DBHelper db;

    protected String host;
    protected int place;

    protected abstract int getLayoutId();
    protected abstract int getHostId();
    protected abstract int getAppIconLayotId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = inflater.inflate(getLayoutId(), null);

        setHasOptionsMenu(true);

        layout = (RelativeLayout) view;

        widgetManager = AppWidgetManager.getInstance(getActivity());
        widgetHost = new WidgetHost(getActivity(), getHostId());
        db = new DBHelper(getActivity());

        restoreWidgets();
        restoreAppIcons();

        initDragListener();
        initSwipeListener();

        return view;
    }

    public void setPlace(int place)
    {
        this.place = place;
    }

    protected void restoreWidgets()
    {
        for (Widget widget : db.widgets.getWidgets(host, place))
        {
            createWidget(widget);
        }
    }

    protected void restoreAppIcons()
    {
        for (AppIcon appIcon : db.appIcons.getIcons(host, place))
        {
            createAppIconView(appIcon);
        }
    }

    protected void initDragListener()
    {
        ImageView deleteItem = Tools.getView(layout, R.id.deleteItem);
        ImageView resizeItem = Tools.getView(layout, R.id.resizeItem);

        DragAndShowViewListener dragListener = new DragAndShowViewListener()
        {
            @Override
            protected void moveItem(View view, DragEvent event, DragItem item)
            {
                View object = item.getView();
                if (object instanceof WidgetView)
                {
                    saveWidgetPosition(event, item);
                }
                else if (object instanceof AppIconView)
                {
                    saveAppIconPosition(event, item);
                }
            }
        };
        dragListener.setViewsForWidget(deleteItem, resizeItem);
        dragListener.setViewsForAppIcon(deleteItem);
        layout.setOnDragListener(dragListener);

        if (deleteItem != null)
        {
            initDeleteDragListener(deleteItem);
        }
        if (resizeItem != null)
        {
            initResizeDragListener(resizeItem);
        }
    }

    protected void initDeleteDragListener(ImageView deleteItem)
    {
        deleteItem.setOnDragListener(
            new DragListener()
            {
                @Override
                protected void moveItem(View view, DragEvent event, DragItem item)
                {
                    View object = item.getView();
                    if (object instanceof WidgetView)
                    {
                        removeWidget(item);
                    }
                    else if (object instanceof AppIconView)
                    {
                        removeAppIcon(item);
                    }
                }
            }
        );
    }

    protected void initResizeDragListener(ImageView resizeItem)
    {
        resizeItem.setOnDragListener(
            new DragListener()
            {
                @Override
                protected void moveItem(View view, DragEvent event, DragItem item)
                {
                    View object = item.getView();
                    if (object instanceof WidgetView)
                    {
                        resizeWidget(item);
                    }
                }
            }
        );
    }

    public WidgetView createWidget(Widget widget)
    {
        AppWidgetProviderInfo widgetInfo = widgetManager.getAppWidgetInfo(widget.getId());
        WidgetView view = widgetHost.createView(this, widget, widgetInfo);
        layout.addView(view, createLayoutParams(widget.getRect()));
        return view;
    }

    public AppIconView createAppIconView(AppIcon appIcon)
    {
        AppIconView view = new AppIconView(this, appIcon, getAppIconLayotId());
        layout.addView(view, createLayoutParams(appIcon.getRect()));
        return view;
    }

    protected RelativeLayout.LayoutParams createLayoutParams(Rect rect)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            rect.right > 0 ? rect.right : RelativeLayout.LayoutParams.WRAP_CONTENT,
            rect.bottom > 0 ? rect.bottom : RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.leftMargin = rect.left;
        params.topMargin = rect.top;
        return params;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        widgetHost.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        widgetHost.stopListening();
    }

    protected Point getPosition(DragEvent event, DragItem item)
    {
        View view = item.getView();
        Point point = new Point();
        point.x = (int) event.getX() - view.getWidth() / 2;
        point.y = (int) event.getY() - view.getHeight() / 2;
        return point;
    }

    protected Point getWidgetPosition(DragEvent event, DragItem item)
    {
        return getPosition(event, item);
    }

    protected Point getAppIconPosition(DragEvent event, DragItem item)
    {
        return getPosition(event, item);
    }

    protected void saveWidgetPosition(DragEvent event, DragItem item)
    {
        try
        {
            WidgetView view = (WidgetView) item.getView();
            Point point = getWidgetPosition(event, item);
            removeFromParent(view);

            Widget widget = view.getWidget();
            widget.setX(point.x);
            widget.setY(point.y);

            db.widgets.updateWidget(widget, host, place);

            createWidget(widget);
        }
        catch (Exception e)
        {
            Log.info(this, "error on save widget", e);
        }
    }

    protected void saveAppIconPosition(DragEvent event, DragItem item)
    {
        try
        {
            AppIconView view = (AppIconView) item.getView();
            Point point = getAppIconPosition(event, item);
            removeFromParent(view);

            AppIcon appIcon = view.getAppIcon();
            appIcon.setX(point.x);
            appIcon.setY(point.y);

            db.appIcons.update(appIcon, host, place);

            createAppIconView(appIcon);
        }
        catch (Exception e)
        {
            Log.info(this, "error on save ikon", e);
        }
    }

    protected void removeFromParent(View view)
    {
        RelativeLayout parent = (RelativeLayout) view.getParent();
        parent.removeView(view);
    }

    protected void removeWidget(DragItem item)
    {
        WidgetView view = (WidgetView) item.getView();
        db.widgets.removeWidget(view.getWidget().getId());
        widgetHost.deleteAppWidgetId(view.getAppWidgetId());
        removeFromParent(view);
    }

    protected void removeAppIcon(DragItem item)
    {
        AppIconView view = (AppIconView) item.getView();
        db.appIcons.remove(view.getAppIcon().getId());
        removeFromParent(view);
    }

    protected void initSwipeListener()
    {
        layout.setOnTouchListener(
            new SwipeListener(getActivity(), true)
            {
                @Override
                public void onSwipeLeft()
                {
                    swipeLeft();
                }

                @Override
                public void onSwipeRight()
                {
                    swipeRight();
                }

                @Override
                public void onSwipeTop()
                {
                    swipeTop();
                }
            }
        );
    }

    public void swipeLeft()
    {
    }

    public void swipeRight()
    {
    }

    public void swipeTop()
    {
        getActivity().openOptionsMenu();
    }

    protected void resizeWidget(DragItem item)
    {
        WidgetView view = (WidgetView) item.getView();
        new ResizeDialog(getActivity(), this, view).show();
    }

    public void saveWidgetSize(WidgetView view)
    {
        removeFromParent(view);
        Widget widget = view.getWidget();
        db.widgets.updateWidget(widget, host, place);
        layout.addView(view, createLayoutParams(widget.getRect()));
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        try
        {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        }
        catch (Throwable e)
        {
        }
    }
}
