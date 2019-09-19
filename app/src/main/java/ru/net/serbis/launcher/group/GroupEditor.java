package ru.net.serbis.launcher.group;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;

public class GroupEditor extends Activity
{
    private ListView listView;
    private EditApplicationAdapter adapter;
    private EditText input;
    private DBHelper db;
    private Group group;
  
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_edit);
        setResult(RESULT_CANCELED);

        listView = (ListView) findViewById(R.id.applications);
        input = (EditText) findViewById(R.id.name);
        db = new DBHelper(this);

        Intent intent = getIntent();
        group = (Group)intent.getSerializableExtra(Group.GROUP);
        input.setText(group.getName(this));
        input.setEnabled(group.getId() >= 0);

        initList();
        initClickListener();
        initOk();
        initCancel();
    }

    private void initList()
    {
        List<Item> items;
        List<Item> checked = db.getItems(group);
      
		if (Group.HIDDEN.equals(group))
		{
			items = new ArrayList<Item>(Items.getIstance().getItems(this).values());
		}
		else
		{
			items = db.getItems(Group.ALL);
			items.addAll(checked);
		}
		
        Collections.sort(items);
        adapter = new EditApplicationAdapter(this, R.layout.group_edit, R.layout.edit_application, items);
        adapter.setChecked(checked);
        listView.setAdapter(adapter);
    }

    private void initClickListener()
    {
        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {
                    adapter.toggle(position);
                }
            });        
    }
    
    private void initOk()
    {
        Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    String name = input.getText().toString();
                    group.setName(name);
                    if (saveGroup() && saveChecked())
                    {
                        Intent intent = new Intent(getIntent());
                        intent.putExtra(Group.GROUP, group);
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
            }
        );
    }

    private void initCancel()
    {
        Button button = (Button) findViewById(R.id.cancel);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    finish();
                }
            }
        );
    }

    private boolean saveGroup()
    {
        return db.updateGroup(group);
    }
    
    private boolean saveChecked()
    {
        return db.saveItemsInGroup(adapter.getChecked(), group);
    }
}
