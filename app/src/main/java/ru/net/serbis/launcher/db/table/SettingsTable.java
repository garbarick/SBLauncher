package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.set.*;

public class SettingsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(
            "create table if not exists settings(" +
            "    name text primary key," +
            "    value text" +
            ")");
    }

    public boolean loadParameterValue(final Parameter parameter)
    {
		return read(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					return loadParameterValue(db, parameter);
				}
			}
		);
    }

    private boolean loadParameterValue(SQLiteDatabase db, Parameter parameter)
    {
        Cursor cursor = db.query("settings", new String[]{"value"}, "name = ?", new String[]{parameter.getName().getValue()}, null, null, null);
        if (cursor.moveToFirst())
        {
            parameter.setValue(cursor.getString(0));
            return true;
        }
        return false;
    }

    public void loadParameterValues(final List<Parameter> parameters)
    {
		read(
			new Action<Void>()
			{
				@Override
				public Void call(SQLiteDatabase db)
				{
					loadParameterValues(db, parameters);
					return null;
				}
			}
		);
    }

    private void loadParameterValues(SQLiteDatabase db, List<Parameter> parameters)
    {
        for (Parameter parameter : parameters)
        {
            loadParameterValue(db, parameter);
        }
    }

    public boolean saveParameterValues(final List<Parameter> parameters)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					saveParameterValues(db, parameters);
					return true;
				}
			}
		);
    }

    public boolean saveParameterValue(final Parameter parameter)
    {
		return write(
			new BooleanAction()
			{
				@Override
				public Boolean call(SQLiteDatabase db)
				{
					saveParameterValue(db, parameter);
					return true;
				}
			}
		);
    }

    private void saveParameterValues(SQLiteDatabase db, List<Parameter> parameters)
    {
        for (Parameter parameter : parameters)
        {
            saveParameterValue(db, parameter);
        }
    }

    private void saveParameterValue(SQLiteDatabase db, Parameter parameter)
    {
        String name = parameter.getName().getValue();
        String value = parameter.getValue();
        if (value == null || value.isEmpty())
        {
            db.delete("settings", "name = ?", new String[]{name});
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put("value", value);
            int count = db.update("settings", values, "name = ?", new String[]{name});
            if (count == 0)
            {
                values.put("name", name);
                db.insert("settings", null, values);
            }
        }
    }
}
