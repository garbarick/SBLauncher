package ru.net.serbis.launcher;

public interface Constants
{
    String ITEM_KEY = "itemKey";
    String ITEM_POS_X = "itemPosX";
    String ITEM_POS_Y = "itemPosY";

    String GROUP = "group";
    String POSITION = "position";

    int REQUEST_CODE_CONFIRM = 1;
    int REQUEST_CHANGE_SETTINGS = 2;
    int REQUEST_CHANGE_GROUP = 3;
    int REQUEST_BIND_WIDGET = 4;
    int REQUEST_CREATE_WIDGET = 5;
    int REQUEST_CHANGE_GROUPS = 6;
    int REQUEST_CHANGE_HIDDEN = 7;
    int REQUEST_PICK_WIDGET = 9;

    String DESKTOP = "desktop";
    String DOC = "doc";

    enum PatternState
    {
        NEW,
        CONFIRM_NEW,
        CONFIRM_OLD,
        CONFIRM
    };

    String INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    String SHORTCUT_ID = "shortcutId";
}
