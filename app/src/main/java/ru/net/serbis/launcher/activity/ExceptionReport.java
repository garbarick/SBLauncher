package ru.net.serbis.launcher.activity;

import android.content.*;
import android.os.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;

public class ExceptionReport extends TextActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.THROWABLE))
        {
            Throwable error = (Throwable) intent.getSerializableExtra(Constants.THROWABLE);
            edit.setText(Tools.errorToText(error));
        }
    }

    @Override
    protected void onOk()
    {
        Intent intent = new Intent(this, Home.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        super.onOk();
    }
}
