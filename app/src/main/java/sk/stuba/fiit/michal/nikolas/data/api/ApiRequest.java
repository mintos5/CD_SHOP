package sk.stuba.fiit.michal.nikolas.data.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class ApiRequest extends AsyncTask<ApiRoute, String, Album> {

    private static final String BASE_URL = "https://myfuckingapi.com/v1";

    @Override
    protected Album doInBackground(ApiRoute... params) {

        URL url;
        HttpURLConnection connection;
        BufferedReader bufferedReader;

        try {
            url = new URL(String.format("%s%s", params[0].getRoute()));
        }
        catch (MalformedURLException e) {
            Log.e("MyApp", e.getMessage());
        }

        return null;
    }
}
