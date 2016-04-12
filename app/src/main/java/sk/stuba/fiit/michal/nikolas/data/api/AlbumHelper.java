package sk.stuba.fiit.michal.nikolas.data.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            array.add(album);
        }
        return array;
    }
}
