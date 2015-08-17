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
import com.owncloud.android.lib.resources.files.CreateRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.DownloadRemoteFileOperation;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.ReadRemoteFileOperation;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;
import com.owncloud.android.lib.resources.files.RemoveRemoteFileOperation;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OwnCloudLibraryAdapter implements OnRemoteOperationListener, OnDatatransferProgressListener {

    public static final int REFRESH_GROUPS = 0;
    public static final int REFRESH_FILES = 1;
    private int refreshType;

    private String curpath;

    private GroupsArrayAdapter groupsAdapter;
    private FilesArrayAdapter filesAdapter;

    private OwnCloudClient client;
    private RemoteFile file;
    private int fileresid;

    private Context context;
    private Handler handler;

    public OwnCloudLibraryAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.curpath = FileUtils.PATH_SEPARATOR;
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

        ReadRemoteFolderOperation refreshOperation = new ReadRemoteFolderOperation(curpath);
        refreshOperation.execute(client, this, handler);
    }

    public void uploadFile(String filename) {
        /*
        File upFolder = new File(context.getCacheDir(), curpath);
        File fileToUpload = upFolder.listFiles()[0];
        String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName();
        String mimeType = getString(R.string.sample_file_mimetype);
        UploadRemoteFileOperation uploadOperation = new UploadRemoteFileOperation(fileToUpload.getAbsolutePath(), remotePath, mimeType);
        uploadOperation.addDatatransferProgressListener(this);
        uploadOperation.execute(client, this, handler);
        */
    }

    public void downloadFile(String filename) {
        /*
        File downFolder = new File(context.getCacheDir(), curpath);
        downFolder.mkdir();
        File upFolder = new File(context.getCacheDir(), filename);
        File fileToUpload = upFolder.listFiles()[0];
        String remotePath = FileUtils.PATH_SEPARATOR + fileToUpload.getName();
        DownloadRemoteFileOperation downloadOperation = new DownloadRemoteFileOperation(remotePath, downFolder.getAbsolutePath());
        downloadOperation.addDatatransferProgressListener(this);
        downloadOperation.execute(client, this, handler);
        */
    }

    public RemoteFile readFile(int fileposition) {
        return file = filesAdapter.getItem(fileposition);
    }


    public void createGroup(String groupname) {
        // Create shared dir
        CreateRemoteFolderOperation createOperation = new CreateRemoteFolderOperation(
                FileUtils.PATH_SEPARATOR + groupname, false);
        createOperation.execute(client, this, handler);
    }

    public void deleteGroup(String groupname) {
        // Delete shared dir
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
            onSuccessfulRefresh((ReadRemoteFolderOperation) operation, result);
        }
        else if (operation instanceof  UploadRemoteFileOperation) {
            onSuccessfulUpload((UploadRemoteFileOperation) operation, result);
        }
        else if (operation instanceof  DownloadRemoteFileOperation) {
            onSuccessfulDownload((DownloadRemoteFileOperation) operation, result);
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

    private void onSuccessfulUpload(UploadRemoteFileOperation operation, RemoteOperationResult result) {
        refresh(refreshType);
    }

    private void onSuccessfulDownload(DownloadRemoteFileOperation operation, RemoteOperationResult result) {

    }


    public static int selectImageResource(RemoteFile file) {
        if (file == null) return R.drawable.file;
        String mimetype = file.getMimeType();

        if (mimetype.equals("DIR")) return R.drawable.folder;
        if (mimetype.startsWith("text")) return R.drawable.file;
        if (mimetype.endsWith("msword")) return R.drawable.file_doc;
        if (mimetype.startsWith("image")) return R.drawable.file_image;
        if (mimetype.startsWith("video")) return R.drawable.file_movie;
        if (mimetype.endsWith("pdf")) return R.drawable.file_pdf;
        if (mimetype.endsWith("mspowerpoint")) return R.drawable.file_ppt;
        if (mimetype.endsWith("msexcel")) return R.drawable.file_xls;
        if (mimetype.startsWith("sudio")) return R.drawable.file_sound;
        if (mimetype.endsWith("zip")) return R.drawable.file_zip;

        return R.drawable.file;
    }


    public GroupsArrayAdapter getGroupsAdapter() {
        return groupsAdapter;
    }

    public FilesArrayAdapter getFilesAdapter() {
        return filesAdapter;
    }

    public RemoteFile getFile() {
        return file;
    }

    public int getFileresid() {
        return fileresid = selectImageResource(file);
    }

    public String getCurpath() {
        return curpath;
    }

    public void setCurPath(String curpath) {
        this.curpath = curpath;
    }

}
