package com.example.yudbet.owncloud;


import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private static final int STATUS_LOGIN = 0;
    private static final int STATUS_MAIN = 1;
    private int statusType = STATUS_LOGIN;

    private OwnCloudLibraryAdapter adapter;
    private String url, username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        statusType = STATUS_LOGIN;

        adapter = new OwnCloudLibraryAdapter(this, new Handler());
        prepareLogin();
    }


    public void prepareLogin() {
        Button btnLogin = (Button)findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUrl = (EditText)findViewById(R.id.url);
                EditText etUsername = (EditText)findViewById(R.id.username);
                EditText etPassword = (EditText)findViewById(R.id.password);
                String url = etUrl.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                MainActivity.this.setClientInfo(url, username, password);
                MainActivity.this.startLogin();
            }
        });
    }

    public void startLogin() {
        adapter.login(url, username, password);
        endLogin();
    }

    public void endLogin() {
        setContentView(R.layout.activity_main);

        statusType = STATUS_MAIN;

        adapter.refresh(OwnCloudLibraryAdapter.REFRESH_GROUPS);

        GroupsArrayAdapter groupsAdapter = adapter.getGroupsAdapter();
        ListView lvGroup = (ListView)findViewById(R.id.lvGroup);
        lvGroup.setAdapter(groupsAdapter);
    }


    public void setClientInfo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        switch (statusType) {
            case STATUS_LOGIN:
                getMenuInflater().inflate(R.menu.menu_main_login, menu);
                break;
            case STATUS_MAIN:
                getMenuInflater().inflate(R.menu.menu_main_actions, menu);
                break;
            default: break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_profileChecker:
                showProfileChecker();
                break;
            case R.id.action_todoRemainder:
                showTodoRemainder();
                break;
            case R.id.action_groupAdder:
                showGroupAdder();
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProfileChecker() {}
    public void showTodoRemainder() {}
    public void showGroupAdder() {}
}