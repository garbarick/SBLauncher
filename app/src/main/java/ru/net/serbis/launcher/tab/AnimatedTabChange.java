package ru.net.serbis.launcher.tab;

import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.set.*;

public class AnimatedTabChange implements TabHost.OnTabChangeListener
{
    private static final int DURATION = 250;

    private DBHelper db;
    private TabHost host;
    
    private View previous;
    private View current;
    private int currentTab;
    private boolean disable;

    public AnimatedTabChange(DBHelper db, TabHost host)
    {
        this.db = db;
        this.host = host;
    }

    public void setDisable(boolean disable)
    {
        this.disable = disable;
    }

    @Override
    public void onTabChanged(String tab)
    {
        if (disable)
        {
            return;
        }

        Parameter lastTab = new Parameters().lastTab;
        lastTab.setValue(tab);
        db.settings.saveParameterValue(lastTab);
        
        current = host.getCurrentView();
        switch (host.getCurrentTab() - currentTab)
        {
            case 1:
                setAnimation(previous, outToLeft());
                setAnimation(current, inFromRight());
                break;

            case -1:
                setAnimation(previous, outToRight());
                setAnimation(current, inFromLeft());
                break;

            default:
                setAnimation(previous, outToTop());
                setAnimation(current, inFromTop());
                break;
        }
        previous = current;
        currentTab = host.getCurrentTab();
        
        new Handler().postDelayed(
            new Runnable()
            {
                public void run()
                {
                    scroll();
                }
            }, 100
        );
    }

    private void setAnimation(View view, Animation animation)
    {
        if (view == null)
        {
            return;
        }
        view.setAnimation(animation);
    }

    public void scroll()
    {
        HorizontalScrollView scroll = (HorizontalScrollView) host.getTabWidget().getParent();
        View view = host.getCurrentTabView();
        int position = view.getLeft() - (scroll.getWidth() - view.getWidth()) / 2;
        scroll.scrollTo(position, 0);
    }
    
    private Animation inFromRight()
    {
        return getAnination(1, 0, 0, 0);
    }

    private Animation outToRight()
    {
        return getAnination(0, 1, 0, 0);
    }

    private Animation inFromLeft()
    {
        return getAnination(-1, 0, 0, 0);
    }

    private Animation outToLeft()
    {
        return getAnination(0, -1, 0, 0);
    }
    
    private Animation inFromTop()
    {
        return getAnination(0, 0, -1, 0);
    }

    private Animation outToTop()
    {
        return getAnination(0, 0, 0, -1);
    }

    private Animation getAnination(int fromX, int toX, int fromY, int toY)
    {
        Animation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, fromX,
            Animation.RELATIVE_TO_PARENT, toX,
            Animation.RELATIVE_TO_PARENT, fromY,
            Animation.RELATIVE_TO_PARENT, toY
        );
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }
}
