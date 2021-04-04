package ru.net.serbis.launcher;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.desktop.*;
import ru.net.serbis.launcher.doc.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.view.*;
import ru.net.serbis.launcher.tools.*;

public class Home extends Activity
{
    private DBHelper db;
    private List<Desktop> desktops;
    private int desktop;
    private List<Doc> docs;
    private int doc;
    private boolean secureLock = true;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        db = new DBHelper(this);
        initSettings();
    }

    private void initSettings()
    {
        Parameters parameters = new Parameters();
        db.settings.loadParameterValues(parameters.getParameters());

        setTransparency(parameters.systemBarTransparency.getIntValue());
        setRequestedOrientation(parameters.orientation.getIntValue());
        initDesktops(parameters.desktopCount.getIntValue());
        initDocs(parameters.docCount.getIntValue());
        
        setContentView(R.layout.home);
        new NoMoveView(this, R.id.doc);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (desktops.size() > 0)
        {
            desktop = getPosition(desktop, desktops);
            transaction.replace(R.id.desktop, desktops.get(desktop));
        }
        if (docs.size() > 0)
        {
            doc = getPosition(doc, docs);
            transaction.replace(R.id.doc, docs.get(doc));
        }
        transaction.commit();
        
        if (secureLock && parameters.secureLockOnStart.getBooleanValue())
        {
            secureLock = false;
            if (parameters.systemSecureLock.getBooleanValue())
            {
                SecureLock.getItem(this).start(this);
            }
            else
            {
                Pattern.getItem(this).start(this);
            }
        }
    }
    
    private int getPosition(int position, List positions)
    {
        return position > positions.size() - 1 ? positions.size() - 1 : position;
    }
    
    private void initDesktops(int count)
    {
        desktops = new ArrayList<Desktop>(count);
        for (int i = 0; i < count; i++)
        {
            Desktop desktop = new Desktop();
            desktop.setPlace(i);
            desktops.add(desktop);
        }
    }
    
    private void initDocs(int count)
    {
        docs = new ArrayList<Doc>(count);
        for (int i = 0; i < count; i++)
        {
            Doc doc = new Doc();
            doc.setPlace(i);
            docs.add(doc);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.androidSet:
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                return true;
           
            case R.id.launcherSet:
                startActivityForResult(new Intent(this, Settings.class), Constants.REQUEST_CHANGE_SETTINGS);
                return true;
                
            case R.id.setWallpaper:
                startActivity(new Intent(Intent.ACTION_SET_WALLPAPER));
                return true;
        
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (Constants.REQUEST_CHANGE_SETTINGS == requestCode &&
            RESULT_OK == resultCode)
        {
            Items.getIstance().reInit();
            initSettings();
        }
    }
    
    private int swipCurrent(boolean right, int value, int max)
    {
        value = right ? value - 1 : value + 1;
        value = value > max ? 0 : value;
        value = value < 0 ? max : value;
        return value;
    }
    
    private void setAnimation(boolean right, FragmentTransaction transaction)
    {
        if (right)
        {
            transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
        }
        else
        {
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        }
    }
    
    public void swipDesktop(boolean right)
    {
        if (desktops.size() == 1)
        {
            return;
        }
        desktop = swipCurrent(right, desktop, desktops.size() - 1);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        setAnimation(right, transaction);
        transaction.replace(R.id.desktop, desktops.get(desktop));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void swipDoc(boolean right)
    {
        if (docs.size() == 1)
        {
            return;
        }
        doc = swipCurrent(right, doc, docs.size() - 1);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        setAnimation(right, transaction);
        transaction.replace(R.id.doc, docs.get(doc));
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        if (intent.hasExtra(Constants.ITEM_KEY))
        {
            String itemKey = intent.getStringExtra(Constants.ITEM_KEY);
            int x = intent.getIntExtra(Constants.ITEM_POS_X, 0);
            int y = intent.getIntExtra(Constants.ITEM_POS_Y, 0);
            desktops.get(desktop).sendItem(itemKey, x, y);
        }
    }

    @Override
    public void onBackPressed()
    {
    }
    
    private void setTransparency(int transparency)
    {
        int value = 255 - (int)(255 * transparency / 100.);
        int color = Color.argb(value, 0, 0, 0);
        Tools.setStatusBarColor(this, color);
    }
}
