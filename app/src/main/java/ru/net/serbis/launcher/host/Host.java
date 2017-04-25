package ru.net.serbis.launcher.host;
import android.app.*;
import android.appwidget.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.ikon.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.widget.*;
import java.util.*;

public abstract class Host extends Fragment
{
    protected static final int REQUEST_PICK_WIDGET = 9;
    protected static final int REQUEST_CREATE_WIDGET = 5;

    protected AppWidgetManager widgetManager;
    protected WidgetHost widgetHost;
    protected RelativeLayout layout;
    protected List<View> showOnDragViews = new ArrayList<View>(2);

    protected DBHelper db;

    protected String host;
    protected int place;

    protected abstract int getLayoutId();
    protected abstract int getHostId();
    protected abstract int getIkonLayotId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = inflater.inflate(getLayoutId(), null);

        setHasOptionsMenu(true);

        layout = (RelativeLayout) view;

        ImageView deleteItem = (ImageView) layout.findViewById(R.id.deleteItem);
        if (deleteItem != null)
        {
            showOnDragViews.add(deleteItem);
        }
        ImageView resizeItem = (ImageView) layout.findViewById(R.id.resizeItem);
        if (resizeItem != null)
        {
            showOnDragViews.add(resizeItem);
        }

        widgetManager = AppWidgetManager.getInstance(getActivity());
        widgetHost = new WidgetHost(getActivity(), getHostId());
        db = new DBHelper(getActivity());

        restoreWidgets();
        restoreIkons();

        initDragListener();
        initDeleteDragListener(deleteItem);
        initResizeDragListener(resizeItem);
        initSwipeListener();

        return view;
    }
    
    public void setPlace(int place)
    {
        this.place = place;
    }

    protected void restoreWidgets()
    {
        for (Widget widget : db.getWidgets(host, place))
        {
            createWidget(widget);
        }
    }

    protected void restoreIkons()
    {
        for (Ikon ikon : db.getIkons(host, place))
        {
            creatIkonView(ikon);
        }
    }

    protected void initDragListener()
    {
        layout.setOnDragListener(
            new DragAndShowViewListener(showOnDragViews)
            {
                @Override
                protected void moveItem(View view, DragEvent event, DragItem item)
                {
                    View object = item.getView();
                    if (object instanceof WidgetView)
                    {
                        saveWidgetPosition(event, item);
                    }
                    else if (object instanceof IkonView)
                    {
                        saveIkonPosition(event, item);
                    }
                }
            }
        );
    }

    protected void initDeleteDragListener(ImageView deleteItem)
    {
        deleteItem.setOnDragListener(
            new DragAndShowViewListener(showOnDragViews)
            {
                @Override
                protected void moveItem(View view, DragEvent event, DragItem item)
                {
                    View object = item.getView();
                    if (object instanceof WidgetView)
                    {
                        removeWidget(item);
                    }
                    else if (object instanceof IkonView)
                    {
                        removeIkon(item);
                    }
                }
            }
        );
    }

    protected void initResizeDragListener(ImageView resizeItem)
    {
        resizeItem.setOnDragListener(
            new DragAndShowViewListener(showOnDragViews)
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

    protected void createWidget(Widget widget)
    {
        AppWidgetProviderInfo widgetInfo = widgetManager.getAppWidgetInfo(widget.getId());
        WidgetView view = widgetHost.createView(this, widget, widgetInfo);
        layout.addView(view, createLayoutParams(widget.getRect()));
    }

    protected void creatIkonView(Ikon ikon)
    {
        IkonView view = new IkonView(this, ikon, getIkonLayotId());
        layout.addView(view, createLayoutParams(ikon.getRect()));
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

    protected Point getIkonPosition(DragEvent event, DragItem item)
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

            db.updateWidget(widget, host, place);

            view.setHost(this);
            layout.addView(view, createLayoutParams(widget.getRect()));
        }
        catch (Exception e)
        {
            Log.info(this, "error on save widget", e);
        }
    }

    protected void saveIkonPosition(DragEvent event, DragItem item)
    {
        try
        {
            IkonView view = (IkonView) item.getView();
            Point point = getIkonPosition(event, item);
            removeFromParent(view);

            Ikon ikon = view.getIkon();
            ikon.setX(point.x);
            ikon.setY(point.y);

            db.updateIkon(ikon, host, place);

            view = new IkonView(this, ikon, getIkonLayotId());
            layout.addView(view, createLayoutParams(ikon.getRect()));
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
        db.removeWidget(view.getWidget().getId());
        widgetHost.deleteAppWidgetId(view.getAppWidgetId());
        removeFromParent(view);
    }

    protected void removeIkon(DragItem item)
    {
        IkonView view = (IkonView) item.getView();
        db.removeIkon(view.getIkon().getId());
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
        db.updateWidget(widget, host, place);
        layout.addView(view, createLayoutParams(widget.getRect()));
    }
}
