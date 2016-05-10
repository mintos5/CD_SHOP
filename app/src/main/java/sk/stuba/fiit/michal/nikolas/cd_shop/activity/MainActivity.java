package sk.stuba.fiit.michal.nikolas.cd_shop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import sk.stuba.fiit.michal.nikolas.cd_shop.exception.ApiException;
import sk.stuba.fiit.michal.nikolas.cd_shop.fragment.AlbumView;
import sk.stuba.fiit.michal.nikolas.cd_shop.fragment.Albums;
import sk.stuba.fiit.michal.nikolas.cd_shop.fragment.Decades;
import sk.stuba.fiit.michal.nikolas.cd_shop.fragment.Genres;
import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.fragment.Regions;
import sk.stuba.fiit.michal.nikolas.data.api.ApiRequest2;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment oldfragment;
    public boolean offline;
    private Bitmap cover;

    private Socket socket;

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true); //set default

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Albums()).commit();
        }
        //vytvorenie socketu...
        IO.Options opts = new IO.Options();
        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;
        opts.reconnectionAttempts = 3;

        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1",opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("CONNECTED TO SANDBOX");
                ApiRequest2.open();
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                int retries = ApiRequest2.getRetries();
                retries++;
                ApiRequest2.setRetries(retries);
                System.out.println(args[0]);
                if (retries == 4) {
                    retries = 0;
                    ApiRequest2.setError(new ApiException(500));
                    ApiRequest2.open();
                    ApiRequest2.openEvent();
                }
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("DISCONECTED");
                ApiRequest2.setTest(false);
            }
        });
        socket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.list_menu, menu);
//        return true;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_albums) {
            fragment = new Albums();
        } else if (id == R.id.nav_decades) {
            fragment = new Decades();
        } else if (id == R.id.nav_genres) {
            fragment = new Genres();
        } else if (id == R.id.nav_regions) {
            fragment = new Regions();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment)
                .addToBackStack("tag");
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMe(View view) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        cover.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo = null;
        try {
            fo = openFileOutput("cover", Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, FullscreenAlbum.class);
        startActivity(intent);
    }

    public boolean connectionTest() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean test = cm.getActiveNetworkInfo() != null;
        if (test) {
            return test;
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Network error");
            dialog.setMessage("No internet connection");
            dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue to login
                }
            });
            dialog.show();
            return test;
        }
    }

    public void customDialog(String text) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error");
        dialog.setMessage(text);
        dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue to login
            }
        });
        dialog.show();
    }

    public void createAlbum(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("New Album name:");
        final EditText input = new EditText(this);
        dialog.setView(input);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = null;
                String albumName = input.getText().toString();
                Bundle transData = new Bundle();
                transData.putString("id",id);
                transData.putString("albumName",albumName);
                AlbumView frag = new AlbumView();
                frag.setArguments(transData);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, frag)
                        .addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
