package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.FullscreenAlbum;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.MainActivity;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumView extends ListFragment {


    private View header;
    private String title;

    public AlbumView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        title = "Nazov albumu";
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        header = inflater.inflate(R.layout.fragment_album_header, null, false);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, GenresEnum.getAllNames());
        getListView().addHeaderView(header);
        setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify) {
            Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT).show();
            fillText();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillText() {
        EditText test= (EditText)header.findViewById(R.id.editTextArtist);
        test.setText("asass");
    }

}
