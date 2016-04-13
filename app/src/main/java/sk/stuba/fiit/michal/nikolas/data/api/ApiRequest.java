package sk.stuba.fiit.michal.nikolas.data.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

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

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.TestAdapter;
import sk.stuba.fiit.michal.nikolas.data.model.Album;
import sk.stuba.fiit.michal.nikolas.data.sqlite.*;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class ApiRequest {


    public static List<Album> getList(String... params) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = null;
        try {
            url = new URL("https://api.backendless.com/v1/data/cds?props=album_name%2Cartist%2CobjectId");
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

            return AlbumHelper.getList(finalJson);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw e;
            //e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void deletAlbum(Album album){
        HttpURLConnection connection = null;
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL (String.format("https://api.backendless.com/v1/data/cds/%s",album.getRecordHash()));
            System.out.println(String.format("https://api.backendless.com/v1/data/cds/%s",album.getRecordHash()));
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("application-id",
                    "F9615D38-AE50-A389-FF5E-8BD658331900");
            connection.addRequestProperty("secret-key",
                    "A4082182-4C7A-E9E8-FFF4-2D69B1025700");
            connection.setRequestMethod("DELETE");
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }
            String finalJson = buffer.toString();
            System.out.println(finalJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Album getDetailAlbum(Album album) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = null;
        try {
            url = new URL(String.format("https://api.backendless.com/v1/data/cds/%s",album.getRecordHash()));
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

            return AlbumHelper.getAlbum(finalJson);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw e;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

}
