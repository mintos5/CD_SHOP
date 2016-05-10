package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import io.socket.client.Socket;
import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.MainActivity;
import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.TestAdapter;
import sk.stuba.fiit.michal.nikolas.cd_shop.exception.ApiException;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.DecadeEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.RegionEnum;
import sk.stuba.fiit.michal.nikolas.data.api.ApiRequest;
import sk.stuba.fiit.michal.nikolas.data.api.ApiRequest2;
import sk.stuba.fiit.michal.nikolas.data.model.Album;


/**
 * A simple {@link Fragment} subclass.
 */
public class Albums extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.MultiChoiceModeListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter adapter;
    private List<Album> albumList;
    private List<Album> albumListRemove;

    private String title;
    private String enumType;
    private int enumNum;

    public Albums() {
        // Required empty public constructor
        albumListRemove = new ArrayList<Album>();
    }

    private GridView grid;



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
                Bundle transData = new Bundle();
                transData.putString("id", albumList.get(position).getRecordHash());
                AlbumView frag = new AlbumView();
                frag.setArguments(transData);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, frag)
                        .addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });

        Bundle args = getArguments();
        if (args!= null) {
            enumType = args.getString("enumType");
            enumNum = args.getInt("enumNum");
            if (enumType.equals("genre")) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(GenresEnum.getByValue(enumNum).getStringName());
            }
            if (enumType.equals("decade")) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(DecadeEnum.getByValue(enumNum).getStringName());
            }
            if (enumType.equals("country")) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(RegionEnum.getByValue(enumNum).getStringName());
            }
        }


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onRefresh();
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
        swipeRefreshLayout.setRefreshing(true);
        MainActivity mainActivity = (MainActivity)getActivity();
        if ( mainActivity.connectionTest() ){
            if (enumType== null) {
                new AsyncGet(mainActivity.getSocket()).execute("");
            }
            else {
                new AsyncGet(mainActivity.getSocket()).execute(enumType, new Integer(enumNum).toString());
            }
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.cab, menu);
        mode.setTitle("Select Items");
        mode.setSubtitle("One item selected");
        albumListRemove.clear();
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
            String[] hashArray = new String[albumListRemove.size()];
            int i =0;
            for (Album alb1 : albumListRemove) {
                hashArray[i]=alb1.getRecordHash();
                i++;
            }
            MainActivity mainActivity = (MainActivity)getActivity();
            if ( mainActivity.connectionTest() ){
                new AsyncDelete(mainActivity.getSocket(), this).execute(hashArray);
            }
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
            albumListRemove.add(albumList.get(position));
        } else {
            albumListRemove.remove(albumList.get(position));
        }
    }

    private class AsyncGet extends AsyncTask<String, Void, List<Album>> {
        private Exception error;
        private Socket socket;
        public  AsyncGet(Socket socket) {
            this.socket = socket;
        }

        @Override
        protected List<Album> doInBackground(String... params) {

            try {
                if (params[0].equals("")) {
                    return ApiRequest2.getList(socket);
                }
                if (params[0].equals("country")) {
                    return ApiRequest2.getList(socket);
                    //return ApiRequest.getSortList(params[0]+"="+params[1]);
                }
                if (params[0].equals("decade")) {
                    return ApiRequest2.getList(socket);
                    //return ApiRequest.getSortList(params[0]+"="+params[1]);
                }
                if (params[0].equals("genre")) {
                    return ApiRequest2.getList(socket);
                    //return ApiRequest.getSortList(params[0]+"="+params[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
                error = e;
            } catch (ApiException e) {
                e.printStackTrace();
                error = e;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(List<Album> result){
            super.onPostExecute(result);
            if (error != null) {
                System.out.println("Riesenie chyby");
                ((MainActivity)getActivity()).customDialog(error.toString());
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            albumList = result;
            TestAdapter adapter = new TestAdapter(getContext(),result);
            GridView grid = (GridView)getView().findViewById(R.id.gridview);
            grid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            //tu by som mohol pridat vytvorenie adaptera pre listView a nabindovat ho tam
        }

    }

    private class AsyncDelete extends AsyncTask<String, Void, Void> {

        private Exception error;
        private Fragment fragment;
        private Socket socket;

        public  AsyncDelete(Socket socket,Fragment fragment) {
            this.fragment = fragment;
            this.socket = socket;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                for (String param1 : params) {
                    ApiRequest2.deletAlbum(socket,param1);
                }
            } catch (ApiException e) {
                e.printStackTrace();
                error = e;
            } catch (InterruptedException e) {
                e.printStackTrace();
                error = e;
            }
            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (error != null) {
                System.out.println("Riesenie chyby");
                ((MainActivity)getActivity()).customDialog(error.toString());
                return;
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(fragment).attach(fragment).commit();
        }

    }
}
