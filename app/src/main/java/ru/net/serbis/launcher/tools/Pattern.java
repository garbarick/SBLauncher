package ru.net.serbis.launcher.tools;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.application.*;
import ru.net.serbis.launcher.db.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.set.*;
import ru.net.serbis.launcher.view.*;

public class Pattern extends Activity implements PatternView.Listener, View.OnClickListener
{
    private TextView text;
    private PatternView patternView;
    private Button reset;
    
    private DBHelper db;
    private Parameter value = new Parameters().patern;
    private Constants.PatternState state;
    private boolean done;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern);

        text = Tools.getView(this, R.id.text);

        patternView = Tools.getView(this, R.id.pattern);
        patternView.addListener(this);

        reset = Tools.getView(this, R.id.reset);
        reset.setOnClickListener(this);

        db = new DBHelper(this);
        db.settings.loadParameterValue(value);
        if (value.getValue() == null)
        {
            state = Constants.PatternState.NEW;
        }
        else
        {
            state = Constants.PatternState.CONFIRM;
        }
        init();
    }

    private void init()
    {
        switch(state)
        {
            case NEW:
                reset.setVisibility(View.GONE);
                text.setText(R.string.selectNewPattern);
                break;

            case CONFIRM:
                text.setText(R.string.confirmPattern);
                break;

            case CONFIRM_OLD:
                text.setText(R.string.confirmOldPattern);
                break;
        }
    }

    @Override
    public void onSelectPattern(String pattern)
    {
        switch(state)
        {
            case NEW:
                patternView.reset();
                value.setValue(pattern);
                text.setText(R.string.confirmNewPattern);
                state = Constants.PatternState.CONFIRM_NEW;
                break;

            case CONFIRM_NEW:
                if (pattern.equals(value.getValue()))
                {
                    db.settings.saveParameterValue(value);
                    done();
                    break;
                }
                wrong();
                break;

            case CONFIRM:
                if (pattern.equals(value.getValue()))
                {
                    done();
                    break;
                }
                wrong();
                break;
                
            case CONFIRM_OLD:
                if (pattern.equals(value.getValue()))
                {
                    patternView.reset();
                    value.setValue(null);
                    db.settings.saveParameterValue(value);
                    state = Constants.PatternState.NEW;
                    init();
                    break;
                }
                wrong();
                break;
        }
    }

    private void wrong()
    {
        patternView.reset();
        done = false;
        text.setText(R.string.patternWrong);
        text.setTextColor(Color.RED);
    }

    private void done()
    {
        done = true;
        finish();
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.reset:
                state = Constants.PatternState.CONFIRM_OLD;
                init();
                break;
        }
    }

    private void restart()
    {
        new Handler().postDelayed(
            new Runnable()
            {
                public void run()
                {
                    startActivity(getIntent());
                }
            }, 100
        );
    }

    @Override
    protected void onDestroy()
    {
        if (!done &&
            !Constants.PatternState.NEW.equals(state) &&
            !Constants.PatternState.CONFIRM_NEW.equals(state))
        {
            restart();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (Constants.PatternState.NEW.equals(state) ||
            Constants.PatternState.CONFIRM_NEW.equals(state))
        {
            super.onBackPressed();
        }
    }

    public static Item getItem(Context context)
    {
        return new ActivityItem(
            context,
            R.string.pattern,
            R.drawable.pattern, 
            Pattern.class);
    }
}
