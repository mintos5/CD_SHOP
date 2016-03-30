package sk.stuba.fiit.michal.nikolas.cd_shop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;

/**
 * Created by micha on 27.03.2016.
 */
public class TestAdapter extends BaseAdapter {

    private Context context;

    public TestAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 25;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        LayoutInflater inflater;
        inflater = (LayoutInflater) LayoutInflater.from(context);
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.item,null);

            TextView txt = (TextView) gridView.findViewById(R.id.name);
            txt.setText("Test text");

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.comic_image);
            imageView.setImageResource(R.drawable.lol_guy);

        }
        else
        {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
