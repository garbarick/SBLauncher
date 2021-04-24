package ru.net.serbis.launcher.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.security.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.Log;

public class PatternView extends View
{
    public interface Listener
    {
        void onSelectPattern(String pattern);
    }

    private Points points = new Points();
    private Lines lines = new Lines();
    private List<Listener> listeners = new ArrayList<Listener>();

    public PatternView(Context context)
    {
        super(context);
    }

    public PatternView(Context context, AttributeSet attr)
    {
        super(context, attr);
    }

    public PatternView(Context context, AttributeSet attr, int style)
    {
        super(context, attr, style);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (PaintHolder.getInstance() == null)
        {
            PaintHolder.createInstance(getContext());
        }
        
        float w = canvas.getWidth();
        float h = canvas.getHeight();

        drawBorder(w, h, canvas);
        if (!points.ready())
        {
            points.init(w, h);
        }
        points.draw(canvas);
        lines.draw(canvas);
    }

    private void drawBorder(float w, float h, Canvas canvas)
    {
        canvas.drawRect(1, 1, w - 1, h - 1, PaintHolder.getInstance().getPaint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                onTouchMove(x, y);
                break;

            case MotionEvent.ACTION_UP:
                onTouchUp(x, y);
                break;
        }
        return true;
    }

    private boolean startLine(float x, float y)
    {
        Point point = points.findNearestPoint(x, y);
        if (point != null)
        {
            point.filled = true;
            lines.add(point.x, point.y, x, y);
            return true;
        }
        return false;
    }

    private boolean finishLine(float x, float y)
    {
        Point point = points.findNearestPoint(x, y);
        if (point != null && !lines.isStart(point))
        {
            point.filled = true;
            lines.progress(point.x, point.y);
            return true;
        }
        return false;
    }

    private void onTouchDown(float x, float y)
    {
        if (lines.inProgress())
        {
            lines.clear();
            points.clearFilled();
        }
        startLine(x, y);
        invalidate();
    }

    private void onTouchMove(float x, float y)
    {
        if (lines.inProgress())
        {
            if (finishLine(x, y))
            {
                lines.add(x, y);
            }
            else
            {
                lines.progress(x, y);
            }
        }
        else
        {
            startLine(x, y);
            return;
        }
        invalidate();
    }

    private void onTouchUp(float x, float y)
    {
        if (lines.inProgress())
        {
            if (!finishLine(x, y))
            {
                lines.clearLast();
            }
        }
        else
        {
            lines.stop();
        }
        invalidate();

        if (lines.isFilled())
        {
            onSelect(md5(lines.toPattern(points)));
        }
    }

    private String md5(String str)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte bytes[] = digest.digest();
            StringBuilder data = new StringBuilder();
            for (byte item : bytes) 
            {
                data.append(String.format("%02x", item));
            }
            return data.toString();
        } 
        catch (Exception e)
        {
            Log.info(this, e);
        }
        return null;
    }

    private void onSelect(String pattern)
    {
        for (Listener listener : listeners)
        {
            listener.onSelectPattern(pattern);
        }
    }

    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    public void reset()
    {
        points.clearFilled();
        lines.clear();
        invalidate();
    }
}
