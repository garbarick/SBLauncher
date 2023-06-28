package ru.net.serbis.launcher.application;

import android.app.*;
import android.content.*;
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
import ru.net.serbis.launcher.sh.*;
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

            if (Tools.isSupportHeaderView())
            {
                menu.setHeaderView(getMenuHeader(item));
            }
            else
            {
                menu.setHeaderTitle(item.getLabel());
            }
            getMenuInflater().inflate(R.menu.activity, menu);
            initMoveTo(menu, info);
        }
    }

    private View getMenuHeader(Item item)
    {
        View result = View.inflate(this, R.layout.menu_header, null);
        ImageView icon = Tools.getView(result, R.id.icon);
        icon.setImageDrawable(item.getIcon());
        TextView label = Tools.getView(result, R.id.label);
        label.setText(item.getLabel());
        return result;
    }

    private void initMoveTo(ContextMenu menu, AdapterView.AdapterContextMenuInfo info)
    {
        MenuItem moveTo = menu.findItem(R.id.moveTo);
        SubMenu subMenu = moveTo.getSubMenu();
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
            case R.id.stop:
                stop(item);
                return true;
            case R.id.addToDesktop:
                startActivity(Items.getIstance().getDesktopIntent(this, item, info.targetView));
                return true;
            case R.id.addStopToDesktop:
                startActivity(Items.getIstance().getDesktopIntent(this, item, info.targetView, Constants.COMMAND_STOP));
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
    
    private void stop(Item item)
    {
        Tools.killBackgroundProcesses(this, item.getPackageName());
        new Shell().stop(item.getPackageName());
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
