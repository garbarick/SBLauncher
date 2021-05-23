package ru.net.serbis.launcher.icon;

import android.content.*;
import android.graphics.*;
import android.net.*;

public class Shortcut extends Icon
{
    private String name;
    private Bitmap bitmap;
    private String data;
    private String packageName;

    public Shortcut(String name, Bitmap bitmap, String data, String packageName, int x, int y)
    {
        super(0, x, y);
        this.name = name;
        this.bitmap = bitmap;
        this.data = data;
        this.packageName = packageName;
    }

    public String getName()
    {
        return name;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public String getData()
    {
        return data;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void start(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
        intent.setPackage(packageName);
        context.startActivity(intent);
    }
}
