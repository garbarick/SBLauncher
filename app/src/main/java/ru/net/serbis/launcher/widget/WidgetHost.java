package ru.net.serbis.launcher.widget;
import android.view.inputmethod.*;
import android.appwidget.*;
import android.content.*;
import ru.net.serbis.launcher.host.*;

public class WidgetHost extends AppWidgetHost
{
    public WidgetHost(Context context, int id)
    {
        super(context, id);
    }

    public WidgetView createView(Host host, Widget widget, AppWidgetProviderInfo info)
    {
        WidgetView view = (WidgetView) super.createView(host.getActivity(), widget.getId(), info);
        view.setWidget(widget);
        view.setHost(host);
        return view;
    }

    @Override
    protected AppWidgetHostView onCreateView(Context context, int id, AppWidgetProviderInfo info)
    {
        return new WidgetView(context, id, info);
    }
}
