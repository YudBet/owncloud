package com.example.yudbet.owncloud;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private static final int STATUS_LOGIN = 0;
    private static final int STATUS_MAIN = 1;
    private int statusType = STATUS_LOGIN;

    private String url, username, password;
    private static OwnCloudLibraryAdapter adapter;
    private GroupsArrayAdapter groupsAdapter;

    private Toolbar toolbar;
    private ListView groupListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

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
                MainActivity.this.login();
            }
        });
    }

    public void login() {
        adapter.login(url, username, password);
        startMainActivity();
    }

    public void startMainActivity() {
        setContentView(R.layout.activity_main);
        statusType = STATUS_MAIN;

        initToolbar();
        initGroupListView();
        initProfileCheckerDrawer();
    }

    public void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void initGroupListView() {
        adapter.refresh(OwnCloudLibraryAdapter.REFRESH_GROUPS);

        groupsAdapter = adapter.getGroupsAdapter();
        groupListView = (ListView)findViewById(R.id.lvGroup);
        groupListView.setAdapter(groupsAdapter);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view.findViewById(R.id.groupname);
                String groupname = tv.getText().toString();
                adapter.setCurPath("/" + groupname);

                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("GROUP_NAME", groupname);
                startActivity(intent);
            }
        });
    }

    public void initProfileCheckerDrawer() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_profile_checker,
                R.string.close_profile_checker) {
            @Override
            public void onDrawerOpened(View drawerView) {
            }
            @Override
            public void onDrawerClosed(View drawerView) {
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void setClientInfo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void refreshGroupListView() {
        initGroupListView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        switch (statusType) {
            case STATUS_LOGIN:
                getMenuInflater().inflate(R.menu.menu_main_login, menu);
                break;
            case STATUS_MAIN:
                getMenuInflater().inflate(R.menu.menu_main, menu);
                break;
            default: break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // Profile checker, implemented by drawer
                // and initProfileCheckerDrawer().
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

    public void showTodoRemainder() {
        // To complete util other functions compeleted.
    }
    public void showGroupAdder() {
        // Create a group, complete soon.
        adapter.createGroup("GGG");
        refreshGroupListView();
    }

    public static OwnCloudLibraryAdapter getOwnCloudAdapter() {
        return adapter;
    }
}