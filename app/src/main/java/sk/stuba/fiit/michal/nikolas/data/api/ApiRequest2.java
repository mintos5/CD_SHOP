package sk.stuba.fiit.michal.nikolas.data.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.Socket;
import sk.stuba.fiit.michal.nikolas.cd_shop.exception.ApiException;
import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class ApiRequest2 {

    private static int retries = 0;
    private static ApiException error;
    private static final String user = "a19e80b8-ee92-4827-af5d-bc9ef6e30517";

    private static boolean connectTest = false;
    private static final Object connectObj = new Object();
    private static boolean eventTest = false;
    private static final Object eventObj = new Object();

    public static int getRetries() {
        return retries;
    }

    public static void setRetries(int retries) {
        ApiRequest2.retries = retries;
    }

    public static ApiException getError() {
        return error;
    }

    public static void setError(ApiException error) {
        ApiRequest2.error = error;
    }

    public synchronized static boolean isTest() {
        return connectTest;
    }

    public synchronized static void setTest(boolean test) {
        ApiRequest2.connectTest = test;
    }

    public synchronized static boolean isEventTest() {
        return eventTest;
    }

    public synchronized static void setEventTest(boolean eventTest) {
        ApiRequest2.eventTest = eventTest;
    }

    public static void send(Socket sock,String event,JSONObject jObj,Ack ack) throws InterruptedException {

        synchronized(connectObj) {
            while(ApiRequest2.isTest() == false) {
                System.out.println("Waiting for connection");
                if (!sock.connected()){
                    sock.connect();
                }
                connectObj.wait();
            }
        }
        if (error == null) {
            sock.emit(event, jObj,ack);
            synchronized(eventObj) {
                while(ApiRequest2.isEventTest() == false) {
                    System.out.println("Waiting for ACK");
                    eventObj.wait();
                }
                ApiRequest2.setEventTest(false);
            }
        }
    }

    public static void open() {
        synchronized(connectObj) {
            ApiRequest2.setTest(true);
            connectObj.notifyAll();
        }
    }

    public static void openEvent() {
        synchronized(eventObj) {
            ApiRequest2.setEventTest(true);
            eventObj.notifyAll();
        }
    }

    public static List<Album> getList(Socket socket) throws IOException, ApiException, InterruptedException {
        JSONObject getQuery = new JSONObject();
        final JSONObject[] data = new JSONObject[1];
        try {
            getQuery.put("url", "/data/" + user );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ack ack = new Ack(){
            @Override
            public void call(Object... os) {
                if (os[0] != null) {
                    //System.out.println(os[0]);
                    JSONObject jsonAll = null;
                    try {
                        jsonAll = new JSONObject(os[0].toString());
                        int statusCode = jsonAll.getInt("statusCode");
                        if (statusCode != 200) {
                            error = new ApiException(statusCode);
                        }
                        else {
                            data[0] = jsonAll.getJSONObject("body");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ApiRequest2.openEvent();
            }
        };

        ApiRequest2.send(socket,"get",getQuery,ack);
        if (error == null) {
            try {
                return AlbumHelper2.getList(data[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            ApiException out = error;
            error = null;
            throw out;
        }
        return null;
    }

    public static void deletAlbum(Socket socket,String hash) throws ApiException, InterruptedException {
        JSONObject delQuery = new JSONObject();
        try {
            delQuery.put("url", "/data/" + user + "/" + hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ack ack = new Ack(){
            @Override
            public void call(Object... os) {
                if (os[0] != null) {
                    //System.out.println(os[0]);
                    JSONObject jsonAll = null;
                    try {
                        jsonAll = new JSONObject(os[0].toString());
                        int statusCode = jsonAll.getInt("statusCode");
                        if (statusCode != 200) {
                            error = new ApiException(statusCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ApiRequest2.openEvent();
            }
        };

        ApiRequest2.send(socket,"delete",delQuery,ack);
        if (error != null) {
            ApiException out = error;
            error = null;
            throw out;
        }
    }

    public static Album getDetailAlbum(Socket socket,String hash) throws IOException, ApiException, InterruptedException {
        JSONObject getQuery = new JSONObject();
        final JSONObject[] data = new JSONObject[1];
        try {
            getQuery.put("url", "/data/" + user + "/" + hash );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ack ack = new Ack(){
            @Override
            public void call(Object... os) {
                if (os[0] != null) {
                    //System.out.println(os[0]);
                    JSONObject jsonAll = null;
                    try {
                        jsonAll = new JSONObject(os[0].toString());
                        int statusCode = jsonAll.getInt("statusCode");
                        if (statusCode != 200) {
                            error = new ApiException(statusCode);
                        }
                        else {
                            data[0] = jsonAll.getJSONObject("body");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ApiRequest2.openEvent();
            }
        };

        ApiRequest2.send(socket,"get",getQuery,ack);
        if (error == null) {
            try {
                return AlbumHelper2.getAlbum(data[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            ApiException out = error;
            error = null;
            throw out;
        }
        return null;
    }

    public static void updateAlbum(Socket socket,Album album) throws IOException, ApiException, InterruptedException {
        JSONObject putQuery = new JSONObject();
        JSONObject newData;
        try {
            putQuery.put("url", "/data/" + user + "/" + album.getRecordHash());
            newData = new JSONObject().put("data", AlbumHelper2.getJSON(album));
            putQuery.put("data", newData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ack ack = new Ack(){
            @Override
            public void call(Object... os) {
                if (os[0] != null) {
                    //System.out.println(os[0]);
                    JSONObject jsonAll = null;
                    try {
                        jsonAll = new JSONObject(os[0].toString());
                        int statusCode = jsonAll.getInt("statusCode");
                        if (statusCode != 200) {
                            error = new ApiException(statusCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ApiRequest2.openEvent();
            }
        };

        ApiRequest2.send(socket,"put",putQuery,ack);
        if (error != null) {
            ApiException out = error;
            error = null;
            throw out;
        }
    }

    public static String createAlbum(Socket socket,Album album) throws IOException, ApiException, InterruptedException {
        JSONObject postQuery = new JSONObject();
        JSONObject newData;
        final String[] data = new String[1];
        try {
            postQuery.put("url", "/data/" + user);
            newData = new JSONObject().put("data", AlbumHelper2.getJSON(album));
            postQuery.put("data", newData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ack ack = new Ack(){
            @Override
            public void call(Object... os) {
                if (os[0] != null) {
                    //System.out.println(os[0]);
                    JSONObject jsonAll = null;
                    try {
                        jsonAll = new JSONObject(os[0].toString());
                        int statusCode = jsonAll.getInt("statusCode");
                        if (statusCode != 200) {
                            error = new ApiException(statusCode);
                        }
                        else {
                            data[0] = jsonAll.getJSONObject("body").getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ApiRequest2.openEvent();
            }
        };

        ApiRequest2.send(socket,"post",postQuery,ack);
        if (error == null) {
            return data[0];
        }
        else {
            ApiException out = error;
            error = null;
            throw out;
        }
    }

}
