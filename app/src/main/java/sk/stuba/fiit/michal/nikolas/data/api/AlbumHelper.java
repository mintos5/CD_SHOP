package sk.stuba.fiit.michal.nikolas.data.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class AlbumHelper {

    public static List<Album> getList(String jsonString) throws JSONException {
        List<Album> array = new ArrayList<Album>();
        JSONObject parentObject = new JSONObject(jsonString);
        JSONArray parentArray = parentObject.getJSONArray("data");
        //albumList = new ArrayList<>();
        for (int i=0; i < parentArray.length(); i++){
            JSONObject finalObject = parentArray.getJSONObject(i);
            Album album = new Album();
            album.setName(finalObject.getString("album_name"));
            album.setArtist(finalObject.getString("artist"));
            album.setRecordHash(finalObject.getString("objectId"));
            array.add(album);
        }
        return array;
    }

    public static Album getAlbum(String jsonString) throws JSONException {
        Album album = new Album();
        JSONObject parentObject = new JSONObject(jsonString);
        album.setName(parentObject.getString("album_name"));
        album.setArtist(parentObject.getString("artist"));
        album.setCount(parentObject.getInt("count"));
        album.setCountry(parentObject.getInt("country"));
        album.setDecade(parentObject.getInt("decade"));
        album.setDescription(parentObject.getString("description"));
        album.setGenre(parentObject.getInt("genre"));
        album.setPrice(parentObject.getInt("price"));
        album.setRecordHash(parentObject.getString("objectId"));
        album.setSales(parentObject.getBoolean("sales"));;
        album.setUrl(parentObject.getString("album_url"));
        album.setPicture(image(album.getUrl()));
        return album;
    }

    private static Bitmap image(String link) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ERROR");
        return null;
    }
}
