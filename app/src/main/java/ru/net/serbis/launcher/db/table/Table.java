package ru.net.serbis.launcher.db.table;

import android.database.sqlite.*;
import ru.net.serbis.launcher.db.*;
import android.content.*;

public abstract class Table
{
    protected DBHelper helper;

    public void setHelper(DBHelper helper)
    {
        this.helper = helper;
    }
    
    protected Context getContext()
    {
        return helper.getContext();
    }

    public abstract void createTable(SQLiteDatabase db);
}
