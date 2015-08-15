package com.example.yudbet.owncloud;


import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owncloud.android.lib.resources.files.RemoteFile;

import org.w3c.dom.Text;

public class GroupActivity extends ActionBarActivity {

    private OwnCloudLibraryAdapter adapter;
    private FilesArrayAdapter filesAdapter;

    private String currentPath;

    private Toolbar toolbar;
    private ListView fileListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String groupname = extras.getString("GROUP_NAME");
            currentPath = groupname;

            TextView tvCurPath = (TextView)findViewById(R.id.curpath);
            tvCurPath.setText(currentPath);

            adapter = MainActivity.getOwnCloudAdapter();

            initToolbar();
            initGroupCheckerDrawer();
            initGroupHeader(groupname);
            initFileListView();
        }
    }

    public void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void initGroupCheckerDrawer() {
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

    public void initGroupHeader(String groupname) {
        if (groupname.equals("own")) {
            ImageView ivGroup = (ImageView)findViewById(R.id.groupimg);
            ivGroup.setImageResource(R.drawable.folder);
        }
    }

    public void initFileListView() {
        adapter.refresh(OwnCloudLibraryAdapter.REFRESH_FILES);

        filesAdapter = adapter.getFilesAdapter();
        fileListView = (ListView)findViewById(R.id.lvFile);
        fileListView.setAdapter(filesAdapter);

        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view.findViewById(R.id.filename);
                String filename = tv.getText().toString();

                RemoteFile file = filesAdapter.getItem(i);
                boolean isDir = (file.getMimeType()).equals("DIR");

                if (isDir) {
                    Toast.makeText(GroupActivity.this, "is dir " + adapter.getCurpath(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(GroupActivity.this, FileActivity.class);
                    intent.putExtra("FILE_NAME", filename);
                    startActivity(intent);
                }
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: // will implement drawer later.
                break;
            case R.id.action_chatroom:
                enterChatroom();
                break;
            case R.id.action_refresh:
                refreshGroup();
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enterChatroom() {}
    public void refreshGroup() {
        initFileListView();
        Toast.makeText(GroupActivity.this, "refresh successed", Toast.LENGTH_SHORT).show();
    }
}
