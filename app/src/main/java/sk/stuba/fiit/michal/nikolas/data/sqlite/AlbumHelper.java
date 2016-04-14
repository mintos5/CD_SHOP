package sk.stuba.fiit.michal.nikolas.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;
import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by Nikolas on 31.3.2016.
 */
public abstract class AlbumHelper {

    public static final String TABLE_NAME = "album";

    public static final String KEY_ID = "id_album";
    public static final String KEY_ARTIST = "alb_artist";
    public static final String KEY_COUNTRY = "alb_country";
    public static final String KEY_DECADE = "alb_decade";
    public static final String KEY_DESCRIPTION = "alb_description";
    public static final String KEY_NAME = "alb_name";
    public static final String KEY_PRICE = "alb_price";
    public static final String KEY_RATING = "alb_rating";
    public static final String KEY_RELEASE_DATE = "alb_releaseDate";
    public static final String KEY_SONGS = "alb_songs";
    public static final String KEY_GENRE = "alb_genre";
    public static final String KEY_RECORD_HASH = "alb_recordHash";
    public static final String KEY_SALES = "alb_sales";

    public static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_ARTIST + " TEXT, "
            + KEY_COUNTRY + " INTEGER, "
            + KEY_DECADE + " INTEGER, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_NAME + " TEXT, "
            + KEY_PRICE + " INTEGER, "
            + KEY_RATING + " INTEGER, "
            + KEY_RELEASE_DATE + " DATETIME, "
            + KEY_SONGS + " TEXT, "
            + KEY_GENRE + " INTEGER, "
            + KEY_RECORD_HASH + " TEXT, "
            + KEY_SALES + " BOOLEAN "
            + ");";

    public static Album saveAlbum(Provider provider, Album album) {
        SQLiteDatabase db = provider.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ARTIST, album.getArtist());
        values.put(KEY_COUNTRY, album.getCountry());
        values.put(KEY_DECADE, album.getDecade());
        values.put(KEY_DESCRIPTION, album.getDescription());
        values.put(KEY_NAME, album.getName());
        values.put(KEY_PRICE, album.getPrice());
        values.put(KEY_RATING, album.getCount());

        if (album.getReleaseDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            values.put(KEY_RELEASE_DATE, dateFormat.format(album.getReleaseDate()));
        }

        if (album.getSongs() != null)
            values.put(KEY_SONGS, album.getSongs().toString());
        values.put(KEY_GENRE, album.getGenre());
        values.put(KEY_RECORD_HASH, album.getRecordHash());
        values.put(KEY_SALES, album.getSales());

        db.update(TABLE_NAME, values, "alb_recordHash LIKE ?", new String[]{album.getRecordHash()});

        db.close();

        return getAlbum(provider, album.getRecordHash());
    }

    public static Album getAlbum(Provider provider, long id){

        SQLiteDatabase db = provider.getReadableDatabase();

        String selectQuery = String.format("SELECT %s.*, DATE(%s) FROM %s WHERE %s = %d", TABLE_NAME, KEY_RELEASE_DATE, TABLE_NAME, KEY_ID, id);

        Log.e(Provider.LOG_NAME, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null)
            c.moveToFirst();

        Album td = new Album();

        try {
            td.setId(c.getLong(c.getColumnIndex(KEY_ID)));
            td.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
            td.setCountry(c.getInt(c.getColumnIndex(KEY_COUNTRY)));
            td.setDecade(c.getInt(c.getColumnIndex(KEY_DECADE)));
            td.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            td.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            td.setPrice(c.getInt(c.getColumnIndex(KEY_PRICE)));
            td.setCount(c.getInt(c.getColumnIndex(KEY_RATING)));
            //TODO
            //td.setReleaseDateFromString(c.getString(c.getColumnIndex(KEY_RELEASE_DATE)), "yyyy-MM-dd");
            td.setGenre(c.getInt(c.getColumnIndex(KEY_GENRE)));
            td.parseSongsFromString(c.getString(c.getColumnIndex(KEY_SONGS)));
            td.setRecordHash(c.getString(c.getColumnIndex(KEY_RECORD_HASH)));
            td.setSales(c.getInt(c.getColumnIndex(KEY_SALES)) != 0);
        }
        catch (NullPointerException e) {
            return null;
        }
        db.close();
        return td;
    }

    public static Album getAlbum(Provider provider, String hash){

        SQLiteDatabase db = provider.getReadableDatabase();

        String selectQuery = String.format("SELECT %s.*, DATE(%s) FROM %s WHERE %s = '%s'", TABLE_NAME, KEY_RELEASE_DATE, TABLE_NAME, KEY_RECORD_HASH, hash);

        Log.e(Provider.LOG_NAME, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null)
            c.moveToFirst();

        Album td = new Album();

        try {
            td.setId(c.getLong(c.getColumnIndex(KEY_ID)));
            td.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
            td.setCountry(c.getInt(c.getColumnIndex(KEY_COUNTRY)));
            td.setDecade(c.getInt(c.getColumnIndex(KEY_DECADE)));
            td.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            td.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            td.setPrice(c.getInt(c.getColumnIndex(KEY_PRICE)));
            td.setCount(c.getInt(c.getColumnIndex(KEY_RATING)));
            //TODO
            //td.setReleaseDateFromString(c.getString(c.getColumnIndex(KEY_RELEASE_DATE)), "yyyy-MM-dd");
            td.setGenre(c.getInt(c.getColumnIndex(KEY_GENRE)));
            td.parseSongsFromString(c.getString(c.getColumnIndex(KEY_SONGS)));
            td.setRecordHash(c.getString(c.getColumnIndex(KEY_RECORD_HASH)));
            td.setSales(c.getInt(c.getColumnIndex(KEY_SALES)) != 0);
        }
        catch (NullPointerException e) {
            return null;
        }
        db.close();
        return td;
    }

    public static List<Album> getAll (Provider provider, Map<String, Integer> filter){

        String sql;
        List<Album> albumList = new ArrayList<Album>();

        if (filter.containsKey("genre")) {
            sql = String.format("SELECT %s FROM %s WHERE %s = %d;", KEY_ID, TABLE_NAME, KEY_GENRE, filter.get("genre"));
        }
        else if (filter.containsKey("decade")){
            sql = String.format("SELECT %s FROM %s WHERE %s = %d;", KEY_ID, TABLE_NAME, KEY_DECADE, filter.get("decade"));
        }
        else if (filter.containsKey("country")){
            sql = String.format("SELECT %s FROM %s WHERE %s = %d;", KEY_ID, TABLE_NAME, KEY_COUNTRY, filter.get("country"));
        }
        else {
            sql = String.format("SELECT %s FROM %s;", KEY_ID, TABLE_NAME);
        }

        SQLiteDatabase db = provider.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                // TODO: pozera ci nie som null
                albumList.add(getAlbum(provider, cursor.getLong(cursor.getColumnIndex(KEY_ID))));
            } while (cursor.moveToNext());
        }

        db.close();
        return albumList;
    }

    public static boolean removeAlbum(Provider provider, Album album){
        SQLiteDatabase db = provider.getWritableDatabase();
        db.delete(TABLE_NAME, "id LIKE ?", new String[]{String.valueOf(album.getId())});
        db.close();
        return true;
    }

}
