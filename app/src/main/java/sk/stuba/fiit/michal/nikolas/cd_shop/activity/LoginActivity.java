package sk.stuba.fiit.michal.nikolas.cd_shop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sk.stuba.fiit.michal.nikolas.cd_shop.R;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.action_login);

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void logMe(View view) {
        // Login function with password check
        EditText login = (EditText) activity.findViewById(R.id.editTextLogin);
        EditText pass = (EditText) activity.findViewById(R.id.editTextPass);
        if (login.getText().toString().equals(pass.getText().toString())) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Wrong login or password");
            dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue to login
                }
            });
            dialog.show();
        }
    }
}
