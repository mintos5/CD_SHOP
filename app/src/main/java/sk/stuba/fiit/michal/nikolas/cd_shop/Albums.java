package sk.stuba.fiit.michal.nikolas.cd_shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;


/**
 * A simple {@link Fragment} subclass.
 */
public class Albums extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.MultiChoiceModeListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    public Albums() {
        // Required empty public constructor
    }

    public void background() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Log.i("test","Prvy");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("test","Druhy");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        thread.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_albums, container, false);
        setHasOptionsMenu(true);

        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, GenresEnum.getAllNames());
        ListView listok = (ListView) view.findViewById(R.id.listView);
        listok.setAdapter(adapter);
        listok.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listok.setMultiChoiceModeListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        background();
                                    }
                                }
        );
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        background();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Nonchecked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.cab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
