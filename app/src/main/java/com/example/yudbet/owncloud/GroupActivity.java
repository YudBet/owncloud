package com.example.yudbet.owncloud;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class GroupActivity extends ActionBarActivity {

    private OwnCloudLibraryAdapter adapter;
    private FilesArrayAdapter filesAdapter;

    private String currentPath;

    private Toolbar toolbar;
    private ListView fileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentPath = extras.getString("GROUP_NAME");
            TextView tvCurPath = (TextView)findViewById(R.id.curpath);
            tvCurPath.setText(currentPath);

            adapter = MainActivity.getOwnCloudAdapter();

            initToolbar();
            initFileListView();
        }
    }

    public void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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


                Toast.makeText(GroupActivity.this, filename, Toast.LENGTH_SHORT).show();
            }
        });
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
