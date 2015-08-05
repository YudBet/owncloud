package com.example.yudbet.owncloud;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.DownloadRemoteFileOperation;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OwnCloudLibraryAdapter implements OnRemoteOperationListener, OnDatatransferProgressListener {

    public static final int REFRESH_GROUPS = 0;
    public static final int REFRESH_FILES = 1;
    private int refreshType;

    private GroupsArrayAdapter groupsAdapter;
    private FilesArrayAdapter filesAdapter;

    private OwnCloudClient client;

    private Context context;
    private Handler handler;

    public OwnCloudLibraryAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.groupsAdapter = new GroupsArrayAdapter(context, R.layout.list_groups);
        this.filesAdapter = new FilesArrayAdapter(context, R.layout.list_files);
    }


    public void login(String url, String username, String password) {
        Uri serverUri = Uri.parse(url);
        client = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
        client.setCredentials(
                OwnCloudCredentialsFactory.newBasicCredentials(username, password)
        );
    }

    public void refresh(int refreshType) {
        this.refreshType = refreshType;

        ReadRemoteFolderOperation refreshOperation = new ReadRemoteFolderOperation(FileUtils.PATH_SEPARATOR);
        refreshOperation.execute(client, this, handler);
    }

    public void createGroup(String groupname) {
        // Create shared dir
    }

    public void deleteGroup(String groupname) {
        // Delete shared dir
    }
/*
    public void uploadFile(String filename) {
        File upFolder = new File(context.getCacheDir(), filename);
        File fileToUpload = upFolder.listFiles()[0];
        String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName();
        String mimeType = getString(R.string.sample_file_mimetype); // self
        UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
        uploadOperation.addDatatransferProgressListener(this);
        uploadOperation.execute(client, this, handler);
    }
*/
    public void downloadFile(String filename) {
        File downFolder = new File(context.getCacheDir(), context.getString(R.string.download_folder_path));
        downFolder.mkdir();
        File upFolder = new File(context.getCacheDir(), filename);
        File fileToUpload = upFolder.listFiles()[0];
        String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName();
        DownloadRemoteFileOperation downloadOperation = new DownloadRemoteFileOperation(remotePath, downFolder.getAbsolutePath());
        downloadOperation.addDatatransferProgressListener(this);
        downloadOperation.execute(client, this, handler);
    }

    public void readFile(String filename) {
        // read file info.
        // contains uploader(owncloud), upload list(owncloud), checkpoint list(DB), Thread(DB)
        // create class store list of these items, class name: FileInfo
        // return list of FileInfo
        // can use RemoteFile class in android-library, maybe
    }

    public GroupsArrayAdapter getGroupsAdapter() {
        return groupsAdapter;
    }

    public FilesArrayAdapter getFilesAdapter() {
        return filesAdapter;
    }

    @Override
    public void onTransferProgress(long progressRate, long totalTransferredSoFar, long totalToTransfer, String fileAbsoluteName) {

    }

    @Override
    public void onRemoteOperationFinish(RemoteOperation operation, RemoteOperationResult result) {
        String LOG_TAG = MainActivity.class.getCanonicalName();
        if (!result.isSuccess()) {
            Toast.makeText(context, R.string.todo_operation_finished_in_fail, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, result.getLogMessage(), result.getException());
        }
        else if (operation instanceof ReadRemoteFolderOperation) {
            onSuccessfulRefresh((ReadRemoteFolderOperation)operation, result);
        }
        else {
            Toast.makeText(context, R.string.todo_operation_finished_in_success, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccessfulRefresh(ReadRemoteFolderOperation operation, RemoteOperationResult result) {
        ArrayAdapter<RemoteFile> adapter = new ArrayAdapter<>(context, R.layout.list_groups);

        switch (refreshType) {
            case REFRESH_GROUPS: adapter = groupsAdapter; break;
            case REFRESH_FILES: adapter = filesAdapter; break;
            default: break;
        }

        adapter.clear();
        List<RemoteFile> files = new ArrayList<>();

        for (Object obj : result.getData()) files.add((RemoteFile)obj);
        if (files != null) {
            Iterator<RemoteFile> it = files.iterator();
            while (it.hasNext()) adapter.add(it.next());
            adapter.remove(adapter.getItem(0));
        }
        adapter.notifyDataSetChanged();
    }

}
