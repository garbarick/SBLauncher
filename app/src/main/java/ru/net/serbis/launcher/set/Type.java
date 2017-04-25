package ru.net.serbis.launcher.set;

import android.content.pm.*;
import java.util.*;
import ru.net.serbis.launcher.*;

public enum Type
{
    STRING(R.layout.type_string),
    INTEGER(R.layout.type_integer),
    BOOLEAN(R.layout.type_boolean),
    ORIENTATION(
        R.layout.type_list,
        Arrays.asList(
            new Value(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, R.string.unspecified),
            new Value(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, R.string.portrait),
            new Value(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, R.string.landscape)
        )
    );
    
    private int layout;
    private List<Value> values;

    private Type(int layout)
    {
        this.layout = layout;
    }

    private Type(int layout, List<Value> values)
    {
        this(layout);
        this.values = values;
    }
    
    public int getLayout()
    {
        return layout;
    }
    
    public List<Value> getValues()
    {
        return values;
    }
}
