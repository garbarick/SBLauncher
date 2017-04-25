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
import ru.net.serbis.launcher.set.*;

public class Tabs extends TabActivity
{  
    private DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Transparent.set(this);
        
        db = new DBHelper(this);

        initSettings();
        initTabs();
        intAnimationTabChange();
    }
    
    private void initSettings()
    {
        Parameters parameters = new Parameters();
        if (db.loadParameterValue(parameters.orientation))
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
        getTabHost().setCurrentTab(0);
    }

    private void setupTab(Group group)
    {
        TabSpec spec = getTabHost().newTabSpec(group.getName(this));
        spec.setIndicator(createTabIndicator(group.getName(this)));

        Intent intent = new Intent(this, Applications.class);
        intent.putExtra(Group.GROUP, group);

        spec.setContent(intent);
        getTabHost().addTab(spec);
    }  

    private View createTabIndicator(String name)
    {
        View view = getLayoutInflater().inflate(R.layout.tab, null);
        TextView text = (TextView) view.findViewById(R.id.tab_text);
        text.setText(name);
        return view;
    }

    private void intAnimationTabChange()
    {
        getTabHost().setOnTabChangedListener(
            new AnimatedTabChange(getTabHost())
        );
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
            {
                startActivityForResult(new Intent(this, Groups.class), 0);
                return true;
            }
                
            case R.id.hideSet:
            {
                Intent intent = new Intent(this, GroupEditor.class);
                intent.putExtra(Group.GROUP, Group.HIDDEN);
                startActivityForResult(intent, 0);
                return true;
            }
 
            case R.id.reload:
            {
                reload();
                return true;
            }
                
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
        getTabHost().clearAllTabs();
        initTabs();
    }
}
