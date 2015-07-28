package com.example.yudbet.owncloud;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

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
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;

import java.io.File;


/**
 * Created by Mist on 2015/7/25.
 */
public class OwnCloudLibraryAdapter implements OnRemoteOperationListener, OnDatatransferProgressListener {

    private OwnCloudClient client;

    private Context context;
    private Handler handler;

    public OwnCloudLibraryAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void login(String url, String username, String password) {
        Uri serverUri = Uri.parse(url);
        client = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
        client.setCredentials(
                OwnCloudCredentialsFactory.newBasicCredentials(username, password)
        );
    }

    public void refresh() {
        ReadRemoteFolderOperation refreshOperation = new ReadRemoteFolderOperation(FileUtils.PATH_SEPARATOR);
        refreshOperation.execute(client, this, handler);
    }

    public void createGroup(String groupname) {
        // Create shared dir
    }

    public void deleteGroup(String groupname) {
        // Delete shared dir
    }

    public void uploadFile(String filename) {
        File upFolder = new File(context.getCacheDir(), filename);
        File fileToUpload = upFolder.listFiles()[0];
        String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName();
        String mimeType = getString(R.string.sample_file_mimetype);
        UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
        uploadOperation.addDatatransferProgressListener(this);
        uploadOperation.execute(client, this, handler);
    }

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


    @Override
    public void onTransferProgress(long progressRate, long totalTransferredSoFar, long totalToTransfer, String fileAbsoluteName) {

    }

    @Override
    public void onRemoteOperationFinish(RemoteOperation caller, RemoteOperationResult result) {

    }




}
