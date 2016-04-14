package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

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
import sk.stuba.fiit.michal.nikolas.cd_shop.exception.ApiException;
import sk.stuba.fiit.michal.nikolas.data.api.ApiRequest;
import sk.stuba.fiit.michal.nikolas.data.model.Album;


/**
 * A simple {@link Fragment} subclass.
 */
public class Albums extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.MultiChoiceModeListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter adapter;
    private String title;

    public Albums() {
        // Required empty public constructor
    }

    private GridView grid;

    public void background() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        title = getResources().getString(R.string.albums);
        final View view =  inflater.inflate(R.layout.fragment_albums, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        setHasOptionsMenu(true);

        //TestAdapter adapter = new TestAdapter(getActivity(),null);
        grid = (GridView) view.findViewById(R.id.gridview);
        //grid.setAdapter(adapter);
        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        grid.setMultiChoiceModeListener(this);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Clicking", Toast.LENGTH_SHORT).show();
            }
        });




        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        background();
                                        new AsyncGet().execute("");
                                    }
                                }
        );
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            onRefresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        background();
        new AsyncGet().execute("");
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.cab, menu);
        mode.setTitle("Select Items");
        mode.setSubtitle("One item selected");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            Toast.makeText(getActivity(), "Deleting", Toast.LENGTH_SHORT).show();
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int selectCount = grid.getCheckedItemCount();
        switch (selectCount) {
            case 1:
                mode.setSubtitle("One item selected");
                break;
            default:
                mode.setSubtitle("" + selectCount + " items selected");
                break;
        }
        if (checked) {
            Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Nonchecked", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncGet extends AsyncTask<String, Void, List<Album>> {

        private Exception error;

        public  AsyncGet() {
        }

        @Override
        protected List<Album> doInBackground(String... params) {
            try {
                return ApiRequest.getList(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                error = e;
            } catch (ApiException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(List<Album> result){
            super.onPostExecute(result);
            if (error != null) {
                System.out.println("Riesenie chyby");
            }

            for (int i=0; i <result.size();i++ )
                System.out.println("albums_name: "+ result.get(i).getName() + "artist: "+ result.get(i).getArtist());
            TestAdapter adapter = new TestAdapter(getContext(),result);
            GridView grid = (GridView)getView().findViewById(R.id.gridview);
            grid.setAdapter(adapter);
            //tu by som mohol pridat vytvorenie adaptera pre listView a nabindovat ho tam
        }

    }
}
