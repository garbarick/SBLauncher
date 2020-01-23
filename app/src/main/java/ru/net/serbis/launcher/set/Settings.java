package ru.net.serbis.launcher.set;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;

public class Settings extends Activity
{
    private ListView listView;
    private DBHelper db;
    private List<Parameter> parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setResult(RESULT_CANCELED);

        listView = (ListView) findViewById(R.id.settings);
        db = new DBHelper(this);

        initList();
        initOk();
        initCancel();
    }
    
    private void initList()
    {
        parameters = new Parameters().getParameters();
        db.settings.loadParameterValues(parameters);
        ParameterAdapter adapter = new ParameterAdapter(this, R.layout.settings, parameters);
        listView.setAdapter(adapter);
    }
    
    private void initOk()
    {
        Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    if (db.settings.saveParameterValues(parameters))
                    {
                        Intent intent = new Intent(getIntent());
                        setResult(RESULT_OK, intent);
                        finish();        
                    }
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
}
