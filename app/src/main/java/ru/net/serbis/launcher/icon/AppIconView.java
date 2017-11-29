package ru.net.serbis.launcher.icon;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.application.*;

public class AppIconView extends LinearLayout
{
    private AppIcon appIcon;
    private View view;
    private Host host;

    public AppIconView(Host host, AppIcon appIcon, int layoutId)
    {
        super(host.getActivity());

        this.appIcon = appIcon;
        this.host = host;
        
        setId((int) appIcon.getId());

        initView(layoutId);
        initLongClickListener();
        initClickListener();
        initSwipeListener();
    }

    public AppIcon getAppIcon()
    {
        return appIcon;
    }
    
    private void initView(int layoutId)
    {
        view = inflate(getContext(), layoutId, null);
        
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(appIcon.getItem().getIcon());
        
        TextView label = (TextView) view.findViewById(R.id.label);
        if (label != null)
        {
            label.setText(appIcon.getItem().getLabel());
        }
        
        addView(view);
    }

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
        view.startDrag(data, builder, new DragItem(AppIconView.this, null), 0);
    }
    
    private void initClickListener()
    {
        view.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    appIcon.getItem().start(host);
                }
            });        
    }
    
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
}
