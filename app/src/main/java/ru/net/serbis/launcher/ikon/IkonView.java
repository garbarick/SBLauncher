package ru.net.serbis.launcher.ikon;

import android.content.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.application.*;

public class IkonView extends LinearLayout
{
    private Ikon ikon;
    private View view;
    private Host host;

    public IkonView(Host host, Ikon ikon, int layoutId)
    {
        super(host.getActivity());

        this.ikon = ikon;
        this.host = host;

        initView(layoutId);
        initLongClickListener();
        initClickListener();
        initSwipeListener();
    }

    public Ikon getIkon()
    {
        return ikon;
    }
    
    private void initView(int layoutId)
    {
        view = inflate(getContext(), layoutId, null);
        
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(ikon.getItem().getIcon());
        
        TextView label = (TextView) view.findViewById(R.id.label);
        if (label != null)
        {
            label.setText(ikon.getItem().getLabel());
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

    private void startDrag()
    {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
        ClipData data = ClipData.newPlainText(null, null);
        view.startDrag(data, builder, new DragItem(IkonView.this, null), 0);
    }

    private void initClickListener()
    {
        view.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ikon.getItem().start(host);
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
