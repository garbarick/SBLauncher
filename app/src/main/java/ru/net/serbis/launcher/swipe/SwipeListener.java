package ru.net.serbis.launcher.swipe;

import android.content.*;
import android.view.*;

public class SwipeListener extends SwipeGestureListener implements View.OnTouchListener
{
    private GestureDetector detector;

    public SwipeListener(Context context, boolean onDown)
    {
        super(onDown);
        detector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        return detector.onTouchEvent(event);
    }
}
