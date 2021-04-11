package ru.net.serbis.launcher.set;

import android.content.pm.*;
import ru.net.serbis.launcher.*;

public class Parameters
{
    public Parameter orientation = new Parameter(new Value("orientation", R.string.orientation), Type.ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    public Parameter desktopCount = new Parameter(new Value("desktopCount", R.string.desktopCount), Type.INTEGER, 3, 1, 10);
    public Parameter docCount = new Parameter(new Value("docCount", R.string.docCount), Type.INTEGER, 3, 1, 10);
    public Parameter lastTab = new Parameter(new Value("lastTab"), Type.STRING);
    public Parameter unbadgedIcon = new Parameter(new Value("unbadgedIcon", R.string.unbadgedIcon), Type.BOOLEAN, 0);
    public Parameter systemWidgetSelector = new Parameter(new Value("systemWidgetSelector", R.string.systemWidgetSelector), Type.BOOLEAN, 1);
    public Parameter systemBarTransparency = new Parameter(new Value("systemBarTransparency", R.string.systemBarTransparency), Type.INTEGER, 100, 0, 100);
    public Parameter searchDefaultActivities = new Parameter(new Value("searchDefaultActivities", R.string.searchDefaultActivities), Type.BOOLEAN, 0);
    public Parameter secureLockOnStart = new Parameter(new Value("secureLockOnStart", R.string.secureLockOnStart), Type.BOOLEAN, 0);
    public Parameter patern = new Parameter(new Value("pattern"), Type.STRING);
    public Parameter systemSecureLock = new Parameter(new Value("systemSecureLock", R.string.systemSecureLock), Type.BOOLEAN, 0);
    public Parameter secureLockAfterDayDream = new Parameter(new Value("secureLockAfterDayDream", R.string.secureLockAfterDayDream), Type.BOOLEAN, 0);

    public Parameter[] getParameters()
    {
        return new Parameter[]{
            orientation,
            desktopCount,
            docCount,
            unbadgedIcon,
            systemWidgetSelector,
            systemBarTransparency,
            searchDefaultActivities,
            secureLockOnStart,
            systemSecureLock,
            secureLockAfterDayDream};
    }

    public Parameter[] getItemsParameters()
    {
        return new Parameter[]{
            unbadgedIcon,
            searchDefaultActivities};
    }
}
