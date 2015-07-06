package com.example.yudbet.owncloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OwnManagerActivity extends ActionBarActivity implements OnRemoteOperationListener {

    private String url, username, password;

    private OwnCloudClient client;
    private FilesArrayAdapter filesAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownmanager);

        Intent intent = this.getIntent();
        setInfo(intent);

        buildClient(url, username, password);

        TextView tvUsername = (TextView)findViewById(R.id.username);
        tvUsername.setText(username);

        filesAdapter = new FilesArrayAdapter(this, R.layout.file_in_list);
        ((ListView)findViewById(R.id.lvGroup)).setAdapter(filesAdapter);

        handler = new Handler();
        ReadRemoteFolderOperation refreshOperation = new ReadRemoteFolderOperation(FileUtils.PATH_SEPARATOR);
        refreshOperation.execute(client, this, handler);
    }

    public void setInfo(Intent intent) {

        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
        username = bundle.getString("username");
        password = bundle.getString("password");
    }

    public void buildClient(String url, String username, String password) {

        Uri serverUri = Uri.parse(url);
        client = OwnCloudClientFactory.createOwnCloudClient(serverUri, this, true);
        client.setCredentials(
                OwnCloudCredentialsFactory.newBasicCredentials(username, password)
        );
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

    @Override
    public void onRemoteOperationFinish(RemoteOperation operation, RemoteOperationResult result) {
        String LOG_TAG = MainActivity.class.getCanonicalName();
        if (!result.isSuccess()) {
            Toast.makeText(this, R.string.todo_operation_finished_in_fail, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, result.getLogMessage(), result.getException());
        }
        else if (operation instanceof ReadRemoteFolderOperation) {
            onSuccessfulRefresh((ReadRemoteFolderOperation)operation, result);
        }
        else {
            Toast.makeText(this, R.string.todo_operation_finished_in_success, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccessfulRefresh(ReadRemoteFolderOperation operation, RemoteOperationResult result) {
        filesAdapter.clear();
        List<RemoteFile> files = new ArrayList<RemoteFile>();

        for (Object obj : result.getData()) files.add((RemoteFile)obj);
        if (files != null) {

            Iterator<RemoteFile> it = files.iterator();
            while (it.hasNext()) filesAdapter.add(it.next());
            filesAdapter.remove(filesAdapter.getItem(0));
        }
        filesAdapter.notifyDataSetChanged();
    }
}
