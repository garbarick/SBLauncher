package ru.net.serbis.launcher.db.table;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.set.*;

public class SettingsTable extends Table
{
    @Override
    public void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table settings(" +
            "    name text primary key," +
            "    value text" +
            ")");
    }

    public boolean loadParameterValue(Parameter parameter)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            return loadParameterValue(db, parameter);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get parameter " + parameter.getName().getValue(), e);
            return false;
        }
        finally
        {
            db.close();
        }
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

    public void loadParameterValues(List<Parameter> parameters)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            loadParameterValues(db, parameters);
        }
        catch (Exception e)
        {
            Log.info(this, "Error on get parameters", e);
        }
        finally
        {
            db.close();
        }
    }

    private void loadParameterValues(SQLiteDatabase db, List<Parameter> parameters)
    {
        for (Parameter parameter : parameters)
        {
            loadParameterValue(db, parameter);
        }
    }

    public boolean saveParameterValues(List<Parameter> parameters)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            saveParameterValues(db, parameters);
            return true;
        }
        catch (Exception e)
        {
            Log.info(this, "Error on save parameters", e);
            return false;
        }
        finally
        {
            db.close();
        }
    }

    public boolean saveParameterValue(Parameter parameter)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        try
        {
            saveParameterValue(db, parameter);
            return true;
        }
        catch (Exception e)
        {
            Log.info(this, "Error on save parameter", e);
            return false;
        }
        finally
        {
            db.close();
        }
    }

    private void saveParameterValues(SQLiteDatabase db, List<Parameter> parameters)
    {
        db.beginTransaction();

        for (Parameter parameter : parameters)
        {
            saveParameterValue(db, parameter);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
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
