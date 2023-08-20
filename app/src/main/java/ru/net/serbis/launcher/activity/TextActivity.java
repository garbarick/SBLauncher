package ru.net.serbis.launcher.activity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;

public class TextActivity extends Activity implements View.OnClickListener
{
    protected EditText edit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);

        edit = Tools.getView(this, R.id.text);
        edit.setKeyListener(null);
        edit.setTextIsSelectable(true);

        initButtons(R.id.ok, R.id.cancel);
    }

    public void initButtons(int ... ids)
    {
        for (int id : ids)
        {
            View button = Tools.getView(this, id);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.ok:
                onOk();
                break;

            case R.id.cancel:
                onCancel();
                break;
        }
    }

    protected void onOk()
    {
        finish();
    }

    protected void onCancel()
    {
        finish();
    }
}
