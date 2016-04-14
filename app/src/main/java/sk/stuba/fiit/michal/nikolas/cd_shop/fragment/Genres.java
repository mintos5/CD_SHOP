package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;


/**
 * A simple {@link Fragment} subclass.
 */
public class Genres extends ListFragment implements AdapterView.OnItemClickListener {

    private String title;

    public Genres() {
        // Required empty public constructor
    }

    private View header;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //header = inflater.inflate(R.layout.fragment_album_header, null, false);
        title = getResources().getString(R.string.genres);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);

        return inflater.inflate(R.layout.fragment_genres, container, false);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, GenresEnum.getAllNames());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        //getListView().addHeaderView(header);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment nextFra = new Albums();
        Bundle transData = new Bundle();
        transData.putString("enumType", "genre");
        transData.putInt("enumNum",position);
        nextFra.setArguments(transData);
        fragmentTransaction.replace(R.id.frame,nextFra)
                .addToBackStack("tag");
        fragmentTransaction.commit();
    }
}
