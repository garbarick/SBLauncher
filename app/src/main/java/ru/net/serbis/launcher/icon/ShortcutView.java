package ru.net.serbis.launcher.icon;

import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.host.*;

public class ShortcutView extends IconView<Shortcut>
{
    public ShortcutView(Host host, Shortcut icon, int layoutId)
    {
        super(host, icon, layoutId);
    }

    @Override
    protected void initIconView(ImageView iconView)
    {
        iconView.setImageBitmap(icon.getBitmap());
    }

    @Override
    protected void initLabelView(TextView labelView)
    {
        labelView.setText(icon.getName());
        labelView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void start()
    {
        icon.start(host.getActivity());
    }

    @Override
    protected void update(DBHelper db)
    {
        db.shortcuts.update(icon, host.getName(), host.getPlace());
        host.createShortcutView(icon);
    }

    @Override
    public void remove(DBHelper db)
    {
        db.shortcuts.remove(icon.getId());
        super.remove(db);
    }
}
