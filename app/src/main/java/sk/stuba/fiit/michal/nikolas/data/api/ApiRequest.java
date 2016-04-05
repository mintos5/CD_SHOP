package sk.stuba.fiit.michal.nikolas.data.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class ApiRequest extends AsyncTask<String, Void, List<Album>> {
    private List<Album> albumList;

    public ApiRequest(List<Album> albumList) {
        this.albumList = albumList;
    }


    @Override
    protected List<Album> doInBackground(String... params) {
        Log.i("as","asdas2");
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL("https://api.backendless.com/v1/data/cds?props=album_name%2Cartist");
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("application-id",
                    "F9615D38-AE50-A389-FF5E-8BD658331900");
            connection.addRequestProperty("secret-key",
                    "A4082182-4C7A-E9E8-FFF4-2D69B1025700");
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }
            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject(finalJson);
            JSONArray parentArray = parentObject.getJSONArray("data");
            //albumList = new ArrayList<>();
            for (int i=0; i < parentArray.length(); i++){
                JSONObject finalObject = parentArray.getJSONObject(i);
                Album album = new Album();
                album.setName(finalObject.getString("album_name"));
                album.setArtist(finalObject.getString("artist"));
                albumList.add(album);
            }
            return albumList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;

    }

    protected void onPostExecute(List<Album> result){
        super.onPostExecute(result);
        for (int i=0; i <result.size();i++ )
            System.out.println("albums_name: "+ result.get(i).getName() + "artist: "+ result.get(i).getArtist());

        //tu by som mohol pridat vytvorenie adaptera pre listView a nabindovat ho tam
    }

}
