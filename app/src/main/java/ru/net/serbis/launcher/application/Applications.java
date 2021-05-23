package ru.net.serbis.launcher.application;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.swipe.*;
import ru.net.serbis.launcher.tab.*;

public class Applications extends Activity implements ItemsHandler
{
    private GridView grid;
    private Tabs tabs;
    private DBHelper db;
    private Group group;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applications);

        grid = Tools.getView(this, R.id.applications);
        tabs = (Tabs)getParent();
        db = new DBHelper(this);
        
        initList();
        initClickListener();
        initSwipeListener();
        registerForContextMenu(grid);
    }

    private void initList()
    {
        Intent intent = getIntent();
        group = Tools.getExtra(intent, Constants.GROUP);
        List<Item> items = db.getItems(group);
        items.removeAll(db.getItems(Group.HIDDEN));
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
                    tabs.nextTab(false);
                }

                @Override
                public void onSwipeRight()
                {
                    tabs.nextTab(true);
                }
            }
        );
    }
    
    private void addToDesktop(Item item, View view)
    {
        Intent intent = new Intent(Applications.this, Home.class);
        intent.putExtra(Constants.ITEM_KEY, item.getKey());

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        intent.putExtra(Constants.ITEM_POS_X, rect.left);
        intent.putExtra(Constants.ITEM_POS_Y, rect.top);

        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        if (intent.hasExtra(Constants.GROUP))
        {
            initList();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (view.getId() == R.id.applications)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Item item = (Item) grid.getItemAtPosition(info.position);
            
            View headerView = LayoutInflater.from(this).inflate(R.layout.menu_header, null);
            ImageView icon = Tools.getView(headerView, R.id.icon);
            icon.setImageDrawable(item.getIcon());
            TextView label = Tools.getView(headerView, R.id.label);
            label.setText(item.getLabel());

            menu = menu.setHeaderView(headerView);

            getMenuInflater().inflate(R.menu.activity, menu);
            initMoveTo(menu, info);
        }
    }

    private void initMoveTo(ContextMenu menu, AdapterView.AdapterContextMenuInfo info)
    {
        MenuItem moveTo = menu.findItem(R.id.moveTo);
        SubMenu subMenu =moveTo.getSubMenu();
        addSubItem(subMenu, moveTo, info, Group.ALL);
        for(Group group : db.groups.getGroups())
        {
            addSubItem(subMenu, moveTo, info, group);
        }
    }

    private void addSubItem(SubMenu subMenu, MenuItem item, AdapterView.AdapterContextMenuInfo info, Group group)
    {
        if (!this.group.equals(group))
        {
            subMenu.add(info.position, item.getItemId(), 0, group.getName(this));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        String name = menuItem.getTitle().toString();
        Item item;
        boolean subMenu = false;
        if (info == null)
        {
            subMenu = true;
            item  = (Item) grid.getItemAtPosition(menuItem.getGroupId());
        }
        else
        {
            item = (Item) grid.getItemAtPosition(info.position);
        }
        switch(menuItem.getItemId())
        {
            case R.id.info:
                openInformation(item);
                return true;
            case R.id.addToDesktop:
                addToDesktop(item, info.targetView);
                return true;
            case R.id.hide:
                addToHiddenGroup(item);
                return true;
            case R.id.moveTo:
                if (subMenu)
                {
                    moveToGroup(item, name);
                    return true;
                }
        }
        return super.onContextItemSelected(menuItem);
    }

    private void openInformation(Item item)
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + item.getPackageName()));
        startActivity(intent);
    }

    private void addToHiddenGroup(Item item)
    {
        db.appsGroup.addItemInGroup(item, Group.HIDDEN);
        initList();
	}

    private void moveToGroup(Item item, String name)
    {
        db.appsGroup.moveItem(item, this.group, name);
        initList();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Items.getIstance().addHandler(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Items.getIstance().removeHandler(this);
    }

    @Override
    public void itemsUpdate()
    {
        initList();
    }
}
