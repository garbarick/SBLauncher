package ru.net.serbis.launcher;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import java.util.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.desktop.*;
import ru.net.serbis.launcher.doc.*;
import ru.net.serbis.launcher.set.*;

public class Home extends Activity
{
    private DBHelper db;
    private List<Desktop> desktops;
    private int desktop;
    private List<Doc> docs;
    private int doc;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Transparent.set(this);
        
        db = new DBHelper(this);
        initSettings();
    }

    private void initSettings()
    {
        Parameters parameters = new Parameters();
        db.loadParameterValues(parameters.getDesktopParameters());

        setRequestedOrientation(parameters.orientation.getIntValue());
        initDesktops(parameters.desktopCount.getIntValue());
        initDocs(parameters.docCount.getIntValue());
        
        setContentView(R.layout.home);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.desktop, desktops.get(desktop));
        transaction.replace(R.id.doc, docs.get(doc));
        transaction.commit();
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
                startActivityForResult(new Intent(this, Settings.class), 0);
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
        if (RESULT_OK == resultCode)
        {
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
        desktop = swipCurrent(right, desktop, desktops.size() - 1);
            
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        setAnimation(right, transaction);
        transaction.replace(R.id.desktop, desktops.get(desktop));
        transaction.commit();
    }

    public void swipDoc(boolean right)
    {
        doc = swipCurrent(right, doc, docs.size() - 1);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        setAnimation(right, transaction);
        transaction.replace(R.id.doc, docs.get(doc));
        transaction.commit();
    }
    
    @Override
    protected void onNewIntent(Intent intent)
    {
        if (intent.hasExtra(Item.ITEM_NAME))
        {
            String name = intent.getStringExtra(Item.ITEM_NAME);
            desktops.get(desktop).sendItem(name);
        }
    }

    @Override
    public void onBackPressed()
    {
    }
}
