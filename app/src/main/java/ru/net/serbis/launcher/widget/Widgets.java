package ru.net.serbis.launcher.widget;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.adapter.*;

public class Widgets extends ListActivity
{
    private WidgetAdapter adapter;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        adapter = new WidgetAdapter(this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);

        AppWidgetProviderInfo info = adapter.getItem(position);
        if (adapter.isAppLevel())
        {
            adapter.setPackageName(WidgetData.getPackageName(info));
            list.invalidate();
        }
        else
        {
            int widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            Intent intent =  new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider);
            startActivityForResult(intent, Constants.REQUEST_BIND_WIDGET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constants.REQUEST_BIND_WIDGET)
        {
            if (resultCode == RESULT_OK)
            {
                setResult(RESULT_OK, getIntent());
            }
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (adapter.isAppLevel())
        {
            super.onBackPressed();
        }
        else
        {
            adapter.setAppLevel();
            getListView().invalidate();
        }
    }
}
