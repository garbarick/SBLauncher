package ru.net.serbis.launcher.swipe;

import android.view.*;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener
{
    private static final int THRESHOLD = 100;
    private static final int VELOCITY = 100;

    private boolean onDown;

    public SwipeGestureListener(boolean onDown)
    {
        this.onDown = onDown;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        float difX = e2.getX() - e1.getX();
        float difY = e2.getY() - e1.getY();

        if (Math.abs(difX) > Math.abs(difY) &&
            Math.abs(difX) > THRESHOLD &&
            Math.abs(velocityX) > VELOCITY)
        {
            if (difX > 0)
            {
                onSwipeRight();
            }
            else
            {
                onSwipeLeft();
            }
            return true;
        }
        if (Math.abs(difY) > Math.abs(difX) &&
            Math.abs(difY) > THRESHOLD &&
            Math.abs(velocityY) > VELOCITY)
        {
            if (difY > 0)
            {
                onSwipeBottom();
            }
            else
            {
                onSwipeTop();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        return onDown;
    }
    
    public void onSwipeRight()
    {
    }

    public void onSwipeLeft()
    {
    }

    public void onSwipeBottom()
    {
    }

    public void onSwipeTop()
    {
    }
}
