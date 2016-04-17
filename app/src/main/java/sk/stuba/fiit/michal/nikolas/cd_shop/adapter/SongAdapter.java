package sk.stuba.fiit.michal.nikolas.cd_shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by micha on 17.04.2016.
 */
public class SongAdapter extends ArrayAdapter<String>
{
    Context context;
    List<String> songNames;


    public SongAdapter(Context context, List<String> songNames)
    {
        super(context, android.R.layout.simple_list_item_activated_1,songNames);
        this.context = context;
        this.songNames =songNames;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = vi.inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        TextView songName = (TextView) row.findViewById(android.R.id.text1);
        int pos = position+1;
        songName.setText(pos + ". " + songNames.get(position));
        pos++;
        return row;
    }

}
