package ru.net.serbis.launcher.db.table;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.db.action.*;
import ru.net.serbis.launcher.help.*;
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

    public void loadParameterValue(final Parameter parameter)
    {
		read(
			new VoidAction()
			{
				@Override
				public void voidCall(SQLiteDatabase db)
				{
				    loadParameterValue(db, parameter);
				}
			}
		);
    }

    private void loadParameterValue(SQLiteDatabase db, Parameter parameter)
    {
        Cursor cursor = db.query("settings", new String[]{"value"}, "name = ?", new String[]{parameter.getName().getValue()}, null, null, null);
        if (cursor.moveToFirst())
        {
            parameter.setValue(cursor.getString(0));
        }
    }

    public void loadParameterValues(final List<Parameter> parameters)
    {
		read(
			new VoidAction()
			{
				@Override
				protected void voidCall(SQLiteDatabase db)
				{
					loadParameterValues(db, parameters);
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

    public void saveParameterValues(final List<Parameter> parameters)
    {
		write(
			new VoidAction()
			{
				@Override
				public void voidCall(SQLiteDatabase db)
				{
					saveParameterValues(db, parameters);
				}
			}
		);
    }

    public void saveParameterValue(final Parameter parameter)
    {
		write(
			new VoidAction()
			{
				@Override
				public void voidCall(SQLiteDatabase db)
				{
					saveParameterValue(db, parameter);
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
        saveParameterValue(db, name, value);
    }

    public void saveParameterValue(final String name, final String value)
    {
        write(
            new VoidAction()
            {
                @Override
                protected void voidCall(SQLiteDatabase db)
                {
                    saveParameterValue(db, name, value);
                }
            }
        );
    }

    private void saveParameterValue(SQLiteDatabase db, String name, String value)
    {
        if (Tools.isEmpty(value))
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
