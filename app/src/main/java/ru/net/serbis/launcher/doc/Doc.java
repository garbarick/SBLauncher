package ru.net.serbis.launcher.doc;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.drag.*;
import ru.net.serbis.launcher.host.*;
import ru.net.serbis.launcher.tab.*;

public class Doc extends Host
{
    public Doc()
    {
        host = "doc";
    }
    
    @Override
    protected int getLayoutId()
    {
        return R.layout.doc;
    }

    @Override
    protected int getHostId()
    {
        return R.id.doc;
    }

    @Override
    protected int getAppIconLayotId()
    {
        return R.layout.doc_application;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.applications:
                startActivity(new Intent(getActivity(), Tabs.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void initDeleteDragListener(ImageView deleteItem)
    {
    }

    @Override
    protected void initResizeDragListener(ImageView resizeItem)
    {
    }
   
    @Override
    public void swipeLeft()
    {
        Home home = (Home) getActivity();
        home.swipDoc(false);
    }

    @Override
    public void swipeRight()
    {
        Home home = (Home) getActivity();
        home.swipDoc(true);
    }

    @Override
    protected Point getAppIconPosition(DragEvent event, DragItem item)
    {
        Point result = super.getAppIconPosition(event, item);
        result.x = result.x < 0 ? 0 : result.x;
        result.x = (int) Math.ceil(result.x/5) * 5;
        result.y = 0;
        return result;
    }
}
