package ru.net.serbis.launcher.desktop;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.ikon.*;
import ru.net.serbis.launcher.widget.*;

public class Desktop extends Host
{
    public Desktop()
    {
        host = "desktop";
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.desktop;
    }

    @Override
    protected int getHostId()
    {
        return R.id.desktop;
    }

    @Override
    protected int getIkonLayotId()
    {
        return R.layout.doc_application;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addWidget:
                selectWidget();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectWidget()
    {
        int widgetId = widgetHost.allocateAppWidgetId();
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, new ArrayList<AppWidgetProviderInfo>());
        intent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, new ArrayList<Bundle>());
        startActivityForResult(intent, REQUEST_PICK_WIDGET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_PICK_WIDGET)
            {
                configureWidget(data);
            }
            else if (requestCode == REQUEST_CREATE_WIDGET)
            {
                createWidget(data);
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED && data != null)
        {
            int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (widgetId > AppWidgetManager.INVALID_APPWIDGET_ID)
            {
                widgetHost.deleteAppWidgetId(widgetId);
            }
        }
    }

    private void configureWidget(Intent data)
    {
        int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo widgetInfo = widgetManager.getAppWidgetInfo(widgetId);
        if (widgetInfo.configure != null)
        {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(widgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            startActivityForResult(intent, REQUEST_CREATE_WIDGET);
        }
        else
        {
            createWidget(data);
        }
    }

    private void createWidget(Intent data)
    {
        int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        Widget widget = new Widget(widgetId, 100, 100);
        db.addWidget(widget, host, place);
        createWidget(widget);
    }

    public void sendItem(String name)
    {
        Item item = db.getItem(name);
        int x = layout.getWidth() / 2 - 60;
        int y = layout.getHeight() / 2 - 60;
        Ikon ikon = new Ikon(0, item, x, y);
        db.addIkon(ikon, host, place);

        creatIkonView(ikon);
    }

    @Override
    public void swipeLeft()
    {
        Home home = (Home) getActivity();
        home.swipDesktop(false);
    }

    @Override
    public void swipeRight()
    {
        Home home = (Home) getActivity();
        home.swipDesktop(true);
    }
}
