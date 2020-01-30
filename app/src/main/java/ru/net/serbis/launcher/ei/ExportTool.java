package ru.net.serbis.launcher.ei;

import android.content.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import ru.net.serbis.launcher.group.*;
import ru.net.serbis.launcher.help.*;
import ru.net.serbis.launcher.set.*;

public abstract class ExportTool extends Tool
{
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private JsonWriter writer;

    public ExportTool(Context context)
    {
        super(context);
    }

    @Override
    public void execute() throws Exception
    {
        try
        {
            File appDir = Tools.getToolDir();
            String name = "set-android-" + Build.VERSION.SDK_INT + "-" + format.format(new Date()) + ".json";
            File file = new File(appDir, name);
            
            writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("  ");
            write();
            result = file.getAbsolutePath();
        }
        finally
        {
            Tools.close(writer);
        }
    }

    private void write() throws IOException
    {
        writer.beginObject();
        writeSettings();
        writeGroups();
        writeApplications();
        writer.endObject();
    }

    private void writeSettings() throws IOException
    {
        Parameters parameters = new Parameters();
        db.settings.loadParameterValues(parameters.getParameters());
        writer.name(Names.SETTINGS);
        writer.beginObject();
        for (Parameter parameter : parameters.getParameters())
        {
            writer
                .name(parameter.getName().getValue())
                .value(parameter.getValue());
        }
        writer.endObject();
    }

    private void writeGroups() throws IOException
    {
        writer.name(Names.GROUPS);
        writer.beginArray();
        for(Group group : db.groups.getGroups())
        {
            writer.value(group.getName(context));
        }
        writer.endArray();
    }

    private void writeApplications() throws IOException
    {
        writer.name(Names.APPLICATIONS);
        writer.beginArray();
        for (ExportData data : db.appsGroup.getExportData())
        {
            writer
                .beginObject()
                .name(Names.NAME).value(data.getName())
                .name(Names.PACKAGE).value(data.getPackageName());
            if (data.isHidden())
            {
                writer.name(Names.HIDDEN).value(true);
            }
            String group = data.getGroup();
            if (Tools.isNotEmpty(group))
            {
                writer.name(Names.GROUP).value(group);
            }
            Set<Host> hosts = data.getHosts();
            if (hosts != null)
            {
                writer.name(Names.HOSTS).beginArray();
                for (Host host : hosts)
                {
                    writer.value(host.toString());
                }
                writer.endArray();
            }
            writer.endObject();
        }
        writer.endArray();
    }
}
