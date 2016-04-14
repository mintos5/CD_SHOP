package sk.stuba.fiit.michal.nikolas.data.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikolas on 31.3.2016.
 */
public class Provider extends SQLiteOpenHelper {

    public static final String LOG_NAME = "DATABASE_PROVIDER";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "albumManager";

    public Provider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(ActivityLogHelper.CREATE_SQL);
        db.execSQL(AlbumHelper.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE TABLE IF EXISTS " + AlbumHelper.TABLE_NAME);
        db.execSQL("DELETE TABLE IF EXISTS " + ActivityLogHelper.TABLE_NAME);
    }
}
