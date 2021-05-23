package ru.net.serbis.launcher.icon;

import android.widget.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.host.*;

public class AppIconView extends IconView<AppIcon>
{
    public AppIconView(Host host, AppIcon icon, int layoutId)
    {
        super(host, icon, layoutId);
    }

    @Override
    protected void initIconView(ImageView iconView)
    {
        iconView.setImageDrawable(icon.getItem().getIcon());
    }

    @Override
    protected void initLabelView(TextView labelView)
    {
        labelView.setText(icon.getItem().getLabel());
    }

    @Override
    protected void start()
    {
        icon.getItem().start(host);
    }

    @Override
    protected void update(DBHelper db)
    {
        db.appIcons.update(icon, host.getName(), host.getPlace());
        host.createAppIconView(icon);
    }

    @Override
    public void remove(DBHelper db)
    {
        db.appIcons.remove(icon.getId());
        super.remove(db);
    }
}
