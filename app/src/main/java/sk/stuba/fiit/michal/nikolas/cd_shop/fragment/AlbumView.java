package sk.stuba.fiit.michal.nikolas.cd_shop.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.SimpleTimeZone;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.FullscreenAlbum;
import sk.stuba.fiit.michal.nikolas.cd_shop.activity.MainActivity;
import sk.stuba.fiit.michal.nikolas.cd_shop.adapter.SpinnerItemAdapter;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.DecadeEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;
import sk.stuba.fiit.michal.nikolas.cd_shop.model.RegionEnum;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumView extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.MultiChoiceModeListener{


    private View header;
    private String title;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerRegion;
    private Spinner spinnerDecade;
    private Spinner spinnerGenre;
    private EditText editText;
    private DatePickerDialog test;
    private Menu menu;



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
        System.out.println("Test1");
        return inflater.inflate(R.layout.fragment_album_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, GenresEnum.getAllNames());
        getListView().addHeaderView(header);
        setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(this);


        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_album);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onRefresh();
                                    }
                                }
        );
        System.out.println("Test2");
        fillText();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify) {
            Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT).show();
            spinnerRegion.setEnabled(true);
            spinnerRegion.setClickable(true);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            test.updateDate(2000, 1, 1);
            //test.show();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add song");
            // I'm using fragment here so I'm using getView() to provide ViewGroup
            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.song_dialog, (ViewGroup) getView(), false);
            // Set up the input
            final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNumber);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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



            return true;
        }

        if (id == R.id.action_add) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            //menu.findItem(R.id.action_save).setVisible(true);
            //menu.findItem(R.id.action_add).setVisible(true);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setViews() {
        int y = 0,m = 0,d = 0;
        spinnerRegion = (Spinner)header.findViewById(R.id.spinnerRegion);
        spinnerDecade = (Spinner)header.findViewById(R.id.spinnerDecade);
        spinnerGenre = (Spinner)header.findViewById(R.id.spinnerGenre);
        editText = (EditText)header.findViewById(R.id.editTextPrice);

        test = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        },y,m,d);
    }

    private void fillText() {
        setViews();

        spinnerRegion.setEnabled(false);
        spinnerRegion.setClickable(false);
        spinnerRegion.setAdapter(new SpinnerItemAdapter(getActivity(),RegionEnum.getAllNames()));

        spinnerDecade.setEnabled(false);
        spinnerDecade.setClickable(false);
        spinnerDecade.setAdapter(new SpinnerItemAdapter(getActivity(),DecadeEnum.getAllNames()));

        spinnerGenre.setEnabled(false);
        spinnerGenre.setClickable(false);
        spinnerGenre.setAdapter(new SpinnerItemAdapter(getActivity(), GenresEnum.getAllNames()));

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
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
