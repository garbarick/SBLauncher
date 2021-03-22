package ru.net.serbis.launcher.tools;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;

public class SecureLock extends Activity
{
    private boolean done;

    @Override
    protected void onStart()
    {
        super.onStart();
        startKeyguard();
    }

    private void startKeyguard()
    {
        try
        {
            KeyguardManager manager = Tools.getService(this, Context.KEYGUARD_SERVICE);
            if (!manager.isKeyguardSecure())
            {
                Tools.toast(this, R.string.secureLockNotSetUp);
                done();
            }
            Intent intent = manager.createConfirmDeviceCredentialIntent(null, null);
            startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM);           
        }
        catch (Throwable e)
        {
            Log.info(this, e);
            Tools.toast(this, R.string.secureLockNotSupport);
            done();
        }
    }

    private void done()
    {
        done = true;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (Constants.REQUEST_CODE_CONFIRM == requestCode)
        {
            if (RESULT_OK == resultCode)
            {
                done();
            }
            else
            {
                startKeyguard();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        if (!done)
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
        super.onDestroy();
    }
}
