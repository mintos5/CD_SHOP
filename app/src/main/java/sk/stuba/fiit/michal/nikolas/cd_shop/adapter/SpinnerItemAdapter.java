package sk.stuba.fiit.michal.nikolas.cd_shop.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by micha on 14.04.2016.
 */
public class SpinnerItemAdapter extends ArrayAdapter{
    public SpinnerItemAdapter(Context context, Object[] objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
