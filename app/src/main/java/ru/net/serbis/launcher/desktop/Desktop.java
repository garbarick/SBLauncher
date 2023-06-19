package ru.net.serbis.launcher.desktop;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.icon.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.widget.*;

public class Desktop extends Host
{
    private boolean systemWidgetSelector;
    private int lastWidgetId;

    public Desktop()
    {
        host = Constants.DESKTOP;
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
    protected int getAppIconLayotId()
    {
        return R.layout.desktop_icon;
    }

    @Override
    protected int getAppIconCloseLayotId()
    {
        return R.layout.close_icon;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View view = super.onCreateView(inflater, container, bundle);
        
        Parameters parameters = new Parameters();
        db.settings.loadParameterValue(parameters.systemWidgetSelector);
        systemWidgetSelector = parameters.systemWidgetSelector.getBooleanValue();
        
        return view;
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
        if (systemWidgetSelector)
        {
            selectWidgetBySystem();
        }
        else
        {
            selectWidgetCustom();
        }
    }

    private void selectWidgetCustom()
    {
        Intent intent = new Intent(getActivity(), Widgets.class);
        int widgetId = widgetHost.allocateAppWidgetId();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        startActivityForResult(intent, Constants.REQUEST_PICK_WIDGET);
    }

    private void selectWidgetBySystem()
    {
        int widgetId = widgetHost.allocateAppWidgetId();
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, new ArrayList<AppWidgetProviderInfo>());
        intent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, new ArrayList<Bundle>());
        startActivityForResult(intent, Constants.REQUEST_PICK_WIDGET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.info(this, "activity result, requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch(requestCode)
            {
                case Constants.REQUEST_PICK_WIDGET:
                    configureWidget(data);
                    break;
                case Constants.REQUEST_CREATE_WIDGET:
                    createWidget(data);
                    break;
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            if (requestCode == Constants.REQUEST_CREATE_WIDGET && data == null)
            {
                if (lastWidgetId > AppWidgetManager.INVALID_APPWIDGET_ID)
                {
                    createWidget(lastWidgetId);
                    lastWidgetId = 0;
                }
            }
            else if (data != null)
            {
                int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                if (widgetId > AppWidgetManager.INVALID_APPWIDGET_ID)
                {
                    widgetHost.deleteAppWidgetId(widgetId);
                }                
            }
        }
    }

    private void configureWidget(Intent data)
    {
        int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo info = widgetManager.getAppWidgetInfo(widgetId);
        if (info.configure != null)
        {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(info.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            
            if (!startWidgetConfigure1(intent))
            {
                startWidgetConfigure2(widgetId);
            }
        }
        else
        {
            createWidget(data);
        }
    }

    private boolean startWidgetConfigure1(Intent intent)
    {
        try
        {
            startActivityForResult(intent, Constants.REQUEST_CREATE_WIDGET);
            return true;
        }
        catch (Exception e)
        {
            Log.error(this, e);
            return false;
        }
    }

    private boolean startWidgetConfigure2(int widgetId)
    {
        try
        {
            widgetHost.startAppWidgetConfigureActivityForResult(
                getActivity(),
                widgetId,
                0,
                Constants.REQUEST_CREATE_WIDGET,
                null);
            lastWidgetId = widgetId;
            return true;
        }
        catch (Exception e)
        {
            Log.error(this, e);
            return false;
        }
    }

    private void createWidget(Intent data)
    {
        int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        createWidget(widgetId);
    }

    private void createWidget(int widgetId)
    {
        Widget widget = new Widget(widgetId, 100, 100);
        db.widgets.addWidget(widget, host, place);
        createWidget(widget);
    }

    public void sendItem(String itemKey, int x, int y, String command)
    {
        Item item = db.getItem(itemKey);
        AppIcon appIcon = new AppIcon(item, x, y, command);
        db.appIcons.add(appIcon, host, place);

        createAppIconView(appIcon);
    }

    public void sendShortcut(long id)
    {
        Shortcut shortcut = db.shortcuts.getShortcut(id);
        db.shortcuts.update(shortcut, host, place);
        createShortcutView(shortcut);
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
