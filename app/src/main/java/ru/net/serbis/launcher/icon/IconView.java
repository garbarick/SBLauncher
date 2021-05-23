package ru.net.serbis.launcher.icon;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.swipe.*;

public abstract class IconView<T extends Icon> extends LinearLayout
{
    protected T icon;
    protected View view;
    protected Host host;

    public IconView(Host host, T icon, int layoutId)
    {
        super(host.getActivity());

        this.icon = icon;
        this.host = host;

        setId((int) icon.getId());

        initView(layoutId);
        initLongClickListener();
        initClickListener();
        initSwipeListener();
    }

    public T getIcon()
    {
        return icon;
    }

    public void setHost(Host host)
    {
        this.host = host;
    }

    private void initView(int layoutId)
    {
        view = inflate(getContext(), layoutId, null);

        ImageView icon = Tools.getView(view, R.id.icon);
        initIconView(icon);

        TextView label = Tools.getView(view, R.id.label);
        if (label != null)
        {
            initLabelView(label);
        }

        addView(view);
    }

    protected abstract void initIconView(ImageView iconView);
    protected abstract void initLabelView(TextView labelView);

    private void initLongClickListener()
    {
        view.setOnLongClickListener(
            new View.OnLongClickListener()
            {
                public boolean onLongClick(View view)
                {
                    startDrag();
                    return true;
                }
            }
        );
    }

    public void startDrag()
    {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
        ClipData data = ClipData.newPlainText(null, null);
        view.startDrag(data, builder, new DragItem(this, null), 0);
    }

    private void initClickListener()
    {
        view.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    start();
                }
            });        
    }

    protected abstract void start();

    protected void initSwipeListener()
    {
        view.setOnTouchListener(
            new SwipeListener(getContext(), false)
            {
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
        );
    }

    public void savePosition(DragEvent event, DragItem item, DBHelper db)
    {
        try
        {
            Point point = host.getPosition(event, item);
            host.removeFromParent(this);

            icon.setX(point.x);
            icon.setY(point.y);

            update(db);
        }
        catch (Exception e)
        {
            Log.info(this, "error on save ikon", e);
        }
    }

    protected abstract void update(DBHelper db);

    public void remove(DBHelper db)
    {
        host.removeFromParent(this);
    }
}
