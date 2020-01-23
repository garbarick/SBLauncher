package ru.net.serbis.launcher.group;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.drag.*;

public class Groups extends Activity
{
    private ListView listView;
    private GroupAdapter groupAdapter;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups);
        setResult(RESULT_OK);

        listView = (ListView) findViewById(R.id.groups);
        db = new DBHelper(this);

        initList();
        initClickList();
        initButtonNew();
        initLongClickListener();
        initDragListener();
    }

    private void initList()
    {
        groupAdapter = new GroupAdapter(this, R.layout.groups, R.layout.group, db.getGroups(false));
        listView.setAdapter(groupAdapter);
    }

    private void initClickList()
    {
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {
                    Group group = (Group) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Groups.this, GroupEditor.class);
                    intent.putExtra(Constants.GROUP, group);
                    intent.putExtra(Constants.POSITION, position);
                    Groups.this.startActivityForResult(intent, 0);
                }
            }
        );
    }

    private void initButtonNew()
    {
        Button button = (Button) findViewById(R.id.groupNew);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    new GroupNameDialog(Groups.this, groupAdapter).show();
                }
            }
        );
    }
    
    private void initLongClickListener()
    {
        listView.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener()
            {
                public boolean onItemLongClick(AdapterView adapter, View view, int position, long id)
                {
                    ClipData data = ClipData.newPlainText(null, null);
                    View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
                    view.startDrag(data, builder, new DragItem(view, position), 0);
                    return true;
                }
            }
        );
    }

    private void initDragListener()
    {
        listView.setOnDragListener(
            new DragListener()
            {
                @Override
                protected void moveItem(View view, DragEvent event, DragItem item)
                {
                    if (moveGroup(event, item))
                    {
                        savePositions();        
                    }
                }
            }
        );
    }

    private boolean moveGroup(DragEvent event, DragItem item)
    {
        int position = listView.pointToPosition(
            (int)event.getX(), 
            (int)event.getY());
        if (position < 0)
        {
            return false;
        }

        int oldPosition = item.getObject();
        Group group = groupAdapter.getItem(oldPosition);
        groupAdapter.remove(group);
        groupAdapter.insert(group, position);
        return true;
    }

    private void savePositions()
    {
        List<Group> groups = new ArrayList<Group>();
        for (int i = 0; i < groupAdapter.getCount(); i++)
        {
            groups.add(groupAdapter.getItem(i));
        }
        db.groups.saveGroupOrdering(groups);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (RESULT_OK == resultCode)
        {
            Group group = (Group) intent.getSerializableExtra(Constants.GROUP);
            int position = intent.getIntExtra(Constants.POSITION, -1);
            if (position > -1)
            {
                groupAdapter.getItem(position).setName(group.getName(this));
                groupAdapter.notifyDataSetChanged();
            }
        }
    }
}
