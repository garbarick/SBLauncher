package ru.net.serbis.launcher.tab;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.TabHost.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.set.*;

public class Tabs extends TabActivity
{
    private DBHelper db;
	private AnimatedTabChange animationTab;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        db = new DBHelper(this);

        initSettings();
        initTabs();
        initAnimationTabChange();
        initMenuButton();
    }

    private void initSettings()
    {
        Parameters parameters = new Parameters();
        if (db.settings.loadParameterValue(parameters.orientation))
        {
            setRequestedOrientation(parameters.orientation.getIntValue());
        }
        setContentView(R.layout.tabs);
    }

    private void initTabs()
    {        
        for (Group group : db.getGroups(true))
        {
            setupTab(group);
        }

        Parameter lastTab = new Parameters().lastTab;
        db.settings.loadParameterValue(lastTab);
        getTabHost().setCurrentTabByTag(lastTab.getValue());
    }

    private void setupTab(Group group)
    {
        TabSpec spec = getTabHost().newTabSpec(group.getName(this));
        spec.setIndicator(createTabIndicator(group.getName(this)));

        Intent intent = new Intent(this, Applications.class);
        intent.putExtra(Constants.GROUP, group);

        spec.setContent(intent);
        getTabHost().addTab(spec);
    }  

    private View createTabIndicator(String name)
    {
        View view = getLayoutInflater().inflate(R.layout.tab, null);
        TextView text = Tools.getView(view, R.id.tab_text);
        text.setText(name);
        return view;
    }

    private void initAnimationTabChange()
    {
        animationTab = new AnimatedTabChange(db, getTabHost());
        getTabHost().setOnTabChangedListener(animationTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.tabsSet:
                startActivityForResult(new Intent(this, Groups.class), 0);
                return true;

            case R.id.hideSet:
                Intent intent = new Intent(this, GroupEditor.class);
                intent.putExtra(Constants.GROUP, Group.HIDDEN);
                intent.putExtra(Constants.POSITION, getTabHost().getCurrentTab());
                startActivityForResult(intent, 0);
                return true;

            case R.id.reload:
                reload();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (RESULT_OK == resultCode)
        {
            reload();
        }
    }

    private void reload()
    {
        animationTab.setDisable(true);
        Items.getIstance().findActivities(this);
        getTabHost().clearAllTabs();
        initTabs();
        animationTab.setDisable(false);
    }

    private void initMenuButton()
    {
        ImageButton menu = Tools.getView(this, R.id.menu);
        menu.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    openOptionsMenu();
                }
            }
        );
    }

    public void nextTab(boolean left)
    {
        TabHost host = getTabHost();
        int count = host.getTabWidget().getTabCount() - 1;
        int next = host.getCurrentTab();
        if (left)
        {
            next--;
            next = next < 0 ? count : next;
        }
        else
        {
            next++;
            next = next > count ? 0 : next;
        }
        host.setCurrentTab(next);
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event)
    {
        TabHost host = getTabHost();
        int count = host.getTabWidget().getTabCount() - 1;
        int current = host.getCurrentTab();

        if (count > 0)
        {
            switch (code)
            {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (current == count)
                    {
                        nextTab(false);
                        return true;
                    }
                    break;

                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (current == 0)
                    {
                        nextTab(true);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}
