package sk.stuba.fiit.michal.nikolas.cd_shop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.TestAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Decades extends Fragment {


    public Decades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_decades, container, false);
        TestAdapter adapter = new TestAdapter(getActivity());
        GridView grid = (GridView) view.findViewById(R.id.gridview);
        grid.setAdapter(adapter);


        return view;
    }

}
