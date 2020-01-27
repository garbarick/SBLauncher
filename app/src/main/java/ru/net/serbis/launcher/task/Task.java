package ru.net.serbis.launcher.task;

import android.os.*;

public class Task extends AsyncTask<Object, Object, Object>
{
    private Async async;

    public Task(Async async)
    {
        this.async = async;
    }

    @Override
    protected void onPreExecute()
    {
        async.preExecute();
    }

    @Override
    protected void onPostExecute(Object o)
    {
        async.postExecute();
    }

    @Override
    protected Object doInBackground(Object... o)
    {
        async.background();
        return null;
    }
}
