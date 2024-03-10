package ru.net.serbis.launcher.db.table.migrate;

import android.database.sqlite.*;
import ru.net.serbis.launcher.db.table.*;

public class ApplicationsTable extends Table
{
	@Override
	public void createTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion == 1 && newVersion > 1)
		{
			db.execSQL(
				" insert into apps(name, package)" +
				" select name, package from applications"
			);
			
			db.execSQL(
				" insert into apps_group(app_id, group_id)" +
				" select app.id, old.group_id" +
				"   from applications old," +
				"        apps app" +
				"  where app.name = old.name" +
				"    and app.package = old.package"
			);
			
			dropTable(db, "applications");
		}
	}
}
