package sk.stuba.fiit.michal.nikolas.cd_shop.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * Created by micha on 27.03.2016.
 */
public class TestAdapter extends BaseAdapter {

    private Context context;
    private List<Album> albumList;

    public TestAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }



    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckableLayout gridView;
        LayoutInflater inflater;
        inflater = (LayoutInflater) LayoutInflater.from(context);
        if (convertView == null) {
            gridView = new CheckableLayout(context);

            TextView txt = (TextView) gridView.findViewById(R.id.name);
            txt.setText(albumList.get(position).getName());
            TextView txt2 = (TextView) gridView.findViewById(R.id.artist);
            txt2.setText(albumList.get(position).getArtist());
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.comic_image);
            imageView.setImageResource(R.drawable.cd_case);

        }
        else
        {
            gridView = (CheckableLayout) convertView;
            TextView txt = (TextView) gridView.findViewById(R.id.name);
            txt.setText(albumList.get(position).getName());
            TextView txt2 = (TextView) gridView.findViewById(R.id.artist);
            txt2.setText(albumList.get(position).getArtist());
        }
        return gridView;
    }

    public class CheckableLayout extends FrameLayout implements Checkable {

        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
            inflate(context, R.layout.item, this);
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
            TextView name = (TextView) this.findViewById(R.id.name);
            TextView artist = (TextView) this.findViewById(R.id.artist);
            if (checked) {
                setBackgroundColor(getResources().getColor(R.color.colorAccent));
                name.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
                artist.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
            }
            else {
                setBackgroundColor(0);
                name.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransparent));
                artist.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTransparent));

            }
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }
}
