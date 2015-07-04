package com.example.yudbet.owncloud;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;


public class MainActivity extends ActionBarActivity {

    private EditText etOwncloudURL, etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etOwncloudURL = (EditText)findViewById(R.id.owncloud_url);
        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);

        btnLogin = (Button)findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = MainActivity.this.getUrl();
                String username = MainActivity.this.getUsername();
                String password = MainActivity.this.getPassword();

                MainActivity.this.openOwnManagerActivity(url, username, password);
            }
        });
    }

    public void openOwnManagerActivity(String url, String username, String password) {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, OwnManagerActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("username", username);
        bundle.putString("password", password);
        intent.putExtras(bundle);

        startActivity(intent);
        MainActivity.this.finish();
    }

    public String getUrl() {
        return etOwncloudURL.getText().toString();
    }

    public String getUsername() {
        return etUsername.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}