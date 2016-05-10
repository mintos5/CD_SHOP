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
public class AlbumHelper2 {

    public static List<Album> getList(JSONObject parentObject) throws JSONException {
        List<Album> array = new ArrayList<Album>();
        JSONArray parentArray = parentObject.getJSONArray("data");
        //albumList = new ArrayList<>();
        for (int i=0; i < parentArray.length(); i++){
            JSONObject finalObject = parentArray.getJSONObject(i).getJSONObject("data");
            Album album = new Album();
            album.setRecordHash(parentArray.getJSONObject(i).getString("id"));

            album.setName(finalObject.getString("album_name"));
            album.setArtist(finalObject.getString("artist"));
            array.add(album);
        }
        return array;
    }

    public static Album getAlbum(JSONObject fullJson) throws JSONException {
        Album album = new Album();
        JSONObject parentObject = fullJson.getJSONObject("data");
        album.setName(parentObject.getString("album_name"));
        album.setArtist(parentObject.getString("artist"));
        album.setCount(parentObject.getInt("count"));
        album.setCountry(parentObject.getInt("country"));
        album.setDecade(parentObject.getInt("decade"));
        album.setDescription(parentObject.getString("description"));
        album.setGenre(parentObject.getInt("genre"));
        album.setPrice(parentObject.getInt("price"));
        album.setRecordHash(fullJson.getString("id"));
        album.setSales(parentObject.getBoolean("sales"));;
        album.setUrl(parentObject.getString("album_url"));
        album.setPicture(image(album.getUrl()));
        album.parseSongsFromString(parentObject.getString("songs"));
        album.setReleaseDateFromString(parentObject.getString("release_date"));
        return album;
    }

    public static JSONObject getJSON(Album album) {
        JSONObject json = new JSONObject();
        try {
            json.put("album_name",album.getName());
            json.put("artist",album.getArtist());
            json.put("count", album.getCount());
            json.put("country", album.getCountry());
            json.put("decade", album.getDecade());
            json.put("description", album.getDescription());
            json.put("genre",album.getGenre());
            json.put("price",album.getPrice());
            json.put("sales",album.getSales());
            json.put("album_url",album.getUrl());
            json.put("songs",album.getSongstoString());
            //DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss", Locale.US);
            json.put("release_date", album.getReleaseDate().getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static Bitmap image(String url) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
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
