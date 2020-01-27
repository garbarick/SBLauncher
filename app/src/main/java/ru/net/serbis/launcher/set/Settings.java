package ru.net.serbis.launcher.set;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.ei.*;

public class Settings extends Activity implements View.OnClickListener
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
        initButton(R.id.apply);
        initButton(R.id.cancel);
        initButton(R.id.doImport);
        initButton(R.id.doExport);
    }
    
    private void initList()
    {
        parameters = new Parameters().getParameters();
        db.settings.loadParameterValues(parameters);
        ParameterAdapter adapter = new ParameterAdapter(this, R.layout.settings, parameters);
        listView.setAdapter(adapter);
    }

    private void initButton(int id)
    {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
    }

    private void makeResultOk()
    {
        Intent intent = new Intent(getIntent());
        setResult(RESULT_OK, intent);
    }

    private void apply()
    {
        if (db.settings.saveParameterValues(parameters))
        {
            makeResultOk();
            finish();        
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.apply:
                apply();
                break;

            case R.id.cancel:
                finish();
                break;

            case R.id.doExport:
                doExport();
                break;

            case R.id.doImport:
                doImport();
                break;
        }
    }

    private void doExport()
    {
        new ExportTool(this)
        {
            @Override
            protected void onFinish()
            {
                finish();
            }
        }.executeDialog(R.string.doExport);
    }

    private void doImport()
    {
        new ImportTool(this)
        {
            @Override
            protected void onFinish()
            {
                makeResultOk();
                finish();
            }
        }.executeDialog(R.string.doImport);
    }
}
