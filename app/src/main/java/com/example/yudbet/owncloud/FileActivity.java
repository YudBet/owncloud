package com.example.yudbet.owncloud;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.resources.files.RemoteFile;

import org.w3c.dom.Text;

public class FileActivity extends ActionBarActivity {

    private OwnCloudLibraryAdapter adapter;

    private String filename;
    private int fileposition;
    private int fileresid;

    private Toolbar toolbar;
    private ListView versionListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filename = extras.getString("FILE_NAME");
            fileposition = extras.getInt("FILE_POSITION");

            adapter = MainActivity.getOwnCloudAdapter();
            adapter.readFile(fileposition);

            fileresid = adapter.getFileresid();

            initToolbar();
            initFileCheckerDrawer();
            initFileHeader();
            initVersionListView();
            // initialize swipe view with old file record list & thread view
        }
    }

    public void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toc_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void initFileCheckerDrawer() {
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

    public void initFileHeader() {
        ImageView ivFile = (ImageView)findViewById(R.id.fileimg);
        TextView tvFile = (TextView)findViewById(R.id.filename);
        ivFile.setImageResource(fileresid);
        tvFile.setText(filename);
    }

    public void initVersionListView() {
        versionListView = (ListView)findViewById(R.id.lvVersion);

        // item contains
        //      file image(with fileresid),
        //      update time(RemoteFIle.getModifiedTimesstamp()),
        //      commit msg(from DB),
        //      buttom(adapter.fileDownload(filename))

        // set adapter
        // set it's onitemclick listener
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

        getMenuInflater().inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
