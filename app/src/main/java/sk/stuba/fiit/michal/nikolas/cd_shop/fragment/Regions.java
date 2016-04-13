package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.RegionEnum;


/**
 * A simple {@link Fragment} subclass.
 */
public class Regions extends ListFragment implements AdapterView.OnItemClickListener {

    private String title;

    public Regions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        title = getResources().getString(R.string.regions);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regions, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, RegionEnum.getAllNames());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        //getListView().addHeaderView(header);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
