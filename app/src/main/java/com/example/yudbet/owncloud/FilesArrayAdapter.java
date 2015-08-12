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

        int imageRes = selectImageResource(file);
        ivImage.setImageResource(imageRes);
        tvName.setText(filename);

        return convertView;
	}

    public int selectImageResource(RemoteFile file) {
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
}

