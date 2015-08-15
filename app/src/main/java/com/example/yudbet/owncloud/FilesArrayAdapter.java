package com.example.yudbet.owncloud;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.RemoteFile;


public class FilesArrayAdapter extends ArrayAdapter<RemoteFile> {

    private Context context;
    private int resource;

	public FilesArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        RemoteFile file = getItem(position);
        String filename = file.getRemotePath();

        String[] pathcontent = filename.split(FileUtils.PATH_SEPARATOR);
        filename = pathcontent[pathcontent.length - 1];

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        ImageView ivImage = (ImageView)convertView.findViewById(R.id.fileimg);
        TextView tvName = (TextView)convertView.findViewById(R.id.filename);

        int imageRes = OwnCloudLibraryAdapter.selectImageResource(file);
        ivImage.setImageResource(imageRes);
        tvName.setText(filename);

        return convertView;
	}
}

