package ru.net.serbis.launcher.tab;

import android.content.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.set.*;

public class AnimatedTabChange implements TabHost.OnTabChangeListener
{
    private Context context;
    private DBHelper db;
    private TabHost host;
    
    private View previous;
    private View current;
    private int currentTab;
    private boolean disable;

    public AnimatedTabChange(Context context, DBHelper db, TabHost host)
    {
        this.context = context;
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
        int type = previous == null ? 0 : host.getCurrentTab() - currentTab;
        switch (type)
        {
            case 1:
                setAnimation(previous, AnimationUtils.loadAnimation(context, R.anim.to_left));
                setAnimation(current, AnimationUtils.loadAnimation(context, R.anim.from_right));
                break;

            case -1:
                setAnimation(previous, AnimationUtils.loadAnimation(context, R.anim.to_right));
                setAnimation(current, AnimationUtils.loadAnimation(context, R.anim.from_left));
                break;

            default:
                setAnimation(previous, AnimationUtils.loadAnimation(context, R.anim.to_bottom));
                setAnimation(current, AnimationUtils.loadAnimation(context, R.anim.from_top));
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
}
