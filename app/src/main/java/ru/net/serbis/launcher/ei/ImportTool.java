package ru.net.serbis.launcher.ei;

import android.content.*;
import android.util.*;
import java.io.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.dialog.*;
import ru.net.serbis.launcher.help.*;

public abstract class ImportTool extends Tool
{
    private File file;
    private JsonReader reader;

    public ImportTool(Context context)
    {
        super(context);
    }

    @Override
    public void executeDialog(final int title)
    {
        new FileDialog(context, Tools.getToolDir(), ".json")
        {
            @Override
            protected void onSelect(File file)
            {
                ImportTool.this.file = file;
                baseExecuteDialog(title);
            }
        };
    }

    protected void baseExecuteDialog(int title)
    {
        super.executeDialog(title);
    }

    @Override
    public void execute() throws Exception
    {
        try
        {
            reader = new JsonReader(new FileReader(file));
            read();
        }
        finally
        {
            Tools.close(reader);
            result = context.getResources().getString(R.string.successfully);
        }
    }

    private void read() throws Exception
    {
        reader.beginObject();
        readSettings();
        readGroups();
        readApplications();
        reader.endObject();
    }

    private void readSettings() throws Exception
    {
        if (reader.hasNext() &&
            Names.SETTINGS.equals(reader.nextName()))
        {
            reader.beginObject();
            while (reader.hasNext())
            {
                String name =reader.nextName();
                String value =reader.nextString();
                db.settings.saveParameterValue(name, value);
            }
            reader.endObject();
        }
    }

    private void readGroups() throws Exception
    {
        if (reader.hasNext() &&
            Names.GROUPS.equals(reader.nextName()))
        {
            reader.beginArray();
            int order = 0;
            while (reader.hasNext())
            {
                String value =reader.nextString();
                db.groups.updateGroup(value, order);
                order ++;
            }
            reader.endArray();
        }
    }

    private void readApplications() throws Exception
    {
        if (reader.hasNext() &&
            Names.APPLICATIONS.equals(reader.nextName()))
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                ExportData data = new ExportData();
                reader.beginObject();
                while (reader.hasNext())
                {
                    String name = reader.nextName();
                    switch(name)
                    {
                        case Names.NAME:
                            data.setName(reader.nextString());
                            break;
                        case Names.PACKAGE:
                            data.setPackageName(reader.nextString());
                            break;
                        case Names.GROUP:
                            data.setGroup(reader.nextString());
                            break;
                        case Names.HIDDEN:
                            data.setHidden(reader.nextBoolean());
                            break;
                        case Names.HOSTS:
                            reader.beginArray();
                            while (reader.hasNext())
                            {
                                data.addHost(reader.nextString());
                            }
                            reader.endArray();
                            break;
                    }
                }
                reader.endObject();
                db.appsGroup.updateData(data);
            }
            reader.endArray();
        }
    }
}
