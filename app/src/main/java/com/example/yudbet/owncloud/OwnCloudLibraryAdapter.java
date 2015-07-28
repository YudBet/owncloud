package com.example.yudbet.owncloud;

import android.content.Context;
import android.net.Uri;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;

/**
 * Created by Mist on 2015/7/25.
 */
public class OwnCloudLibraryAdapter {

    private OwnCloudClient client;
    private Context context;


    public OwnCloudLibraryAdapter(Context context) {
        this.context = context;
    }


    public void login(String url, String username, String password) {
        Uri serverUri = Uri.parse(url);
        client = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
        client.setCredentials(
                OwnCloudCredentialsFactory.newBasicCredentials(username, password)
        );
    }

    /** create shared dir(group)
     *      create a group
     *      Require:
     *          group name
     **/
    /** delete shared dir(group)
     *      delete a group
     *      Require:
     *          group name
     **/

    /** upload file
     *      upload file to owncloud server
     *      Require:
     *          file name
     **/
    /** download file
     *      download file to local phone
     *      Require:
     *          file name
     **/

    /** read file
     *      read file info
     *      Require:
     *          file name
     **/

    /** read dir
     *      read dir info
     *      Require:
     *          dir name
     **/

}
