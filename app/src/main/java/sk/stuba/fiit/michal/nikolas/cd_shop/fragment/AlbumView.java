package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.MainActivity;
import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.SpinnerItemAdapter;
import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.TestAdapter;
import sk.stuba.fiit.michal.nikolas.cd_shop.exception.ApiException;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.DecadeEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.RegionEnum;
import sk.stuba.fiit.michal.nikolas.data.api.ApiRequest;
import sk.stuba.fiit.michal.nikolas.data.model.Album;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumView extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.MultiChoiceModeListener{


    private View header;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Album albumDetail;
    private String albumId;
    private List<String> songListRemove;

    private Spinner spinnerRegion;
    private Spinner spinnerDecade;
    private Spinner spinnerGenre;
    private EditText editTextPrice,editTextArtist,editTextStock,editTextReleased;
    private CheckBox checkBox;
    private ImageView imageView;

    private DatePickerDialog dateDialog;
    private int year,month,day;
    private Menu menu;



    public AlbumView() {
        // Required empty public constructor
        songListRemove = new ArrayList<String>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        header = inflater.inflate(R.layout.fragment_album_header, null, false);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        albumId = args.getString("id");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().addHeaderView(header);
        getListView().setClickable(false);
        getListView().setLongClickable(false);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_album);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onRefresh();
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new AsyncGetDetail().execute(albumId);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_edit, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_modify) {
            Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT).show();

            menu.findItem(R.id.action_modify).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_add).setVisible(true);
            menu.findItem(R.id.action_modify_album).setVisible(true);

            enterText();
            return true;
        }

        if (id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add song");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.song_dialog,(ViewGroup)getView(), false);
            // Set up the input
            final EditText num = (EditText) viewInflated.findViewById(R.id.editTextNumber);
            final EditText input = (EditText) viewInflated.findViewById(R.id.editTextSong);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);
            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int order = Integer.parseInt(num.getText().toString());
                    if (order > albumDetail.getSongs().size()) {
                        ((MainActivity) getActivity()).customDialog("Enter correct song number.");
                        //albumDetail.getSongs().add(order, input.getText().toString());
                    } else {
                        albumDetail.getSongs().add(order, input.getText().toString());
                        dialog.dismiss();
                    }
                    //m_Text = input.getText().toString();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        if (id == R.id.action_save) {
            saveText();
            new AsyncUpdateDetail(this).execute(albumDetail);
        }

        if (id == R.id.action_modify_album) {
            final EditText albumName = new EditText(getContext());
            albumName.setText(albumDetail.getName());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Rename album name");
            builder.setView(albumName);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    albumDetail.setName(albumName.getText().toString());
                    MainActivity activity = (MainActivity)getActivity();
                    activity.getSupportActionBar().setTitle(albumDetail.getName());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void setViews() {
        dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                albumDetail.setReleaseDate(calendar.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                editTextReleased.setText(dateFormat.format(albumDetail.getReleaseDate()));
            }
        },year,month,day);
        spinnerRegion = (Spinner)header.findViewById(R.id.spinnerRegion);
        spinnerDecade = (Spinner)header.findViewById(R.id.spinnerDecade);
        spinnerGenre = (Spinner)header.findViewById(R.id.spinnerGenre);
        editTextArtist = (EditText)header.findViewById(R.id.editTextArtist);
        editTextStock = (EditText)header.findViewById(R.id.editTextStock);
        editTextPrice = (EditText)header.findViewById(R.id.editTextPrice);
        imageView = (ImageView)header.findViewById(R.id.imageViewCover);
        checkBox = (CheckBox)header.findViewById(R.id.checkBox);
        editTextReleased = (EditText)header.findViewById(R.id.editTextReleased);
        editTextReleased.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateDialog.updateDate(2000, 1, 1);
                    dateDialog.show();
                }
            }
        });
        editTextReleased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.updateDate(2000, 1, 1);
                dateDialog.show();
            }
        });
    }

    private void fillText() {
        setViews();
        MainActivity activity = (MainActivity)getActivity();
        activity.getSupportActionBar().setTitle(albumDetail.getName());
        activity.setCover(albumDetail.getPicture());
        imageView.setImageBitmap(albumDetail.getPicture());

        spinnerRegion.setEnabled(false);
        spinnerRegion.setClickable(false);
        spinnerRegion.setAdapter(new SpinnerItemAdapter(getActivity(), RegionEnum.getAllNames()));
        spinnerRegion.setSelection(albumDetail.getCountry());

        spinnerDecade.setEnabled(false);
        spinnerDecade.setClickable(false);
        spinnerDecade.setAdapter(new SpinnerItemAdapter(getActivity(), DecadeEnum.getAllNames()));
        spinnerDecade.setSelection(albumDetail.getDecade());

        spinnerGenre.setEnabled(false);
        spinnerGenre.setClickable(false);
        spinnerGenre.setAdapter(new SpinnerItemAdapter(getActivity(), GenresEnum.getAllNames()));
        spinnerGenre.setSelection(albumDetail.getGenre());

        editTextArtist.setText(albumDetail.getArtist());
        editTextPrice.setText(String.valueOf(albumDetail.getPrice() / 100.00));
        editTextStock.setText(String.valueOf(albumDetail.getCount()));
        checkBox.setEnabled(albumDetail.getSales());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        editTextReleased.setText(dateFormat.format(albumDetail.getReleaseDate()));

        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, albumDetail.getSongs());
        setListAdapter(adapter);

    }

    private void enterText() {
        getListView().setClickable(true);
        getListView().setLongClickable(true);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(this);

        spinnerRegion.setEnabled(true);
        spinnerRegion.setClickable(true);

        spinnerDecade.setEnabled(true);
        spinnerDecade.setClickable(true);

        spinnerGenre.setEnabled(true);
        spinnerGenre.setClickable(true);

        editTextPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextArtist.setEnabled(true);
        editTextStock.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextReleased.setEnabled(true);
        editTextReleased.setClickable(true);
        checkBox.setEnabled(true);
        checkBox.setClickable(true);
    }

    private void saveText() {
        Double price = Double.parseDouble(editTextPrice.getText().toString());
        price = price* 100;
        albumDetail.setArtist(editTextArtist.getText().toString());
        albumDetail.setPrice(price.intValue());
        albumDetail.setCount(Integer.parseInt(editTextStock.getText().toString()));
        albumDetail.setSales(checkBox.isSelected());

        albumDetail.setCountry(spinnerRegion.getSelectedItemPosition());
        albumDetail.setDecade(spinnerDecade.getSelectedItemPosition());
        albumDetail.setGenre(spinnerGenre.getSelectedItemPosition());

    }


    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int selectCount = getListView().getCheckedItemCount();
        switch (selectCount) {
            case 1:
                mode.setSubtitle("One item selected");
                break;
            default:
                mode.setSubtitle("" + selectCount + " items selected");
                break;
        }
        if (checked) {
            //counting from 1 not from 0
            songListRemove.add(albumDetail.getSongs().get(position-1));
        } else {
            songListRemove.remove(albumDetail.getSongs().get(position-1));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.cab, menu);
        mode.setTitle("Select Items");
        mode.setSubtitle("One item selected");
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
            int i =0;
            for (String song1 : songListRemove) {
                albumDetail.getSongs().remove(song1);
                i++;
            }
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private class AsyncGetDetail extends AsyncTask<String, Void, Album> {

        private Exception error;

        public  AsyncGetDetail() {
        }

        @Override
        protected Album doInBackground(String... params) {
            try {
                return ApiRequest.getDetailAlbum(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
                error = e;
            }
            return null;
        }

        protected void onPostExecute(Album result){
            super.onPostExecute(result);
            try {
                albumDetail = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (error != null) {
                System.out.println("Riesenie chyby");
                ((MainActivity)getActivity()).customDialog(error.toString());
                return;
            }
            fillText();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private class AsyncUpdateDetail extends AsyncTask<Album, Void, Void> {

        private Exception error;
        private Fragment fragment;

        public  AsyncUpdateDetail(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected Void doInBackground(Album... params) {
            try {
                ApiRequest.updateAlbum(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                error = e;
                e.printStackTrace();
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
