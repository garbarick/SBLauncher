package ru.net.serbis.launcher.tab;

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
        previous = host.getCurrentView();
        currentTab = host.getCurrentTab();
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
        db.saveParameterValue(lastTab);
        
        current = host.getCurrentView();
        if (host.getCurrentTab() > currentTab)
        {
            previous.setAnimation(outToLeft());
            current.setAnimation(inFromRight());
        }
        else
        {
            previous.setAnimation(outToRight());
            current.setAnimation(inFromLeft());
        }
        previous = current;
        currentTab = host.getCurrentTab();
        
        scroll();
    }

    private void scroll()
    {
        HorizontalScrollView scroll = (HorizontalScrollView) host.getTabWidget().getParent();
        View view = host.getCurrentTabView();
        int position = view.getLeft();
        scroll.scrollTo(position, 0);
    }

    private Animation inFromRight()
    {
        Animation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 1,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0
        );
        return setProperties(animation);
    }

    private Animation outToRight()
    {
        Animation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 1,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0
        );
        return setProperties(animation);
    }

    private Animation inFromLeft()
    {
        Animation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0
        );
        return setProperties(animation);
    }

    private Animation outToLeft()
    {
        Animation animation = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, -1,
            Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0
        );
        return setProperties(animation);
    }

    private Animation setProperties(Animation animation)
    {
        animation.setDuration(DURATION);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }
}
