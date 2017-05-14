package ru.net.serbis.launcher.application;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.tab.*;

/**
 * SEBY0408
 */
public class Applications extends Activity
{
    private GridView grid;
    private Tabs tabs;
    private DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applications);

        grid = (GridView) findViewById(R.id.applications);
        tabs = (Tabs)getParent();
        db = new DBHelper(this);
        
        initList();
        initClickListener();
        initSwipeListener();
        initLongClickListener();
    }

    private void initList()
    {
        Intent intent = getIntent();
        Group group = (Group)intent.getSerializableExtra(Group.GROUP);
        List<Item> items = db.getItems(group);
        Collections.sort(items);
        ApplicationAdapter adapter = new ApplicationAdapter(this, R.layout.applications, R.layout.application, items);
        grid.setAdapter(adapter);
    }

    private void initClickListener()
    {
        grid.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {
                    Item item = (Item) parent.getItemAtPosition(position);
                    item.start(Applications.this);
                }
            });        
    }

    private void initSwipeListener()
    {
        grid.setOnTouchListener(
            new SwipeListener(this, false)
            {
                @Override
                public void onSwipeLeft()
                {
                    TabHost host = tabs.getTabHost();
                    int count = host.getTabWidget().getTabCount() - 1;
                    int next = host.getCurrentTab() + 1;
                    next = next > count ? 0 : next;
                    host.setCurrentTab(next);
                }

                @Override
                public void onSwipeRight()
                {
                    TabHost host = tabs.getTabHost();
                    int count = host.getTabWidget().getTabCount() - 1;
                    int next = host.getCurrentTab() - 1;
                    next = next < 0 ? count : next;
                    host.setCurrentTab(next);
                }
            }
        );
    }
    
    private void initLongClickListener()
    {
        grid.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView parent, View view, int position, long id)
                {
                    Item item = (Item) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Applications.this, Home.class);
                    intent.putExtra(Item.ITEM_NAME, item.getName());
                    startActivity(intent);
                    return true;
                }
            }
        );
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        if (intent.hasExtra(Group.GROUP))
        {
            initList();
        }
    }
}
