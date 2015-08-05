/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2015 ownCloud Inc.
 *   
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *   
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *   
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.example.yudbet.owncloud;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.owncloud.android.lib.resources.files.RemoteFile;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupsArrayAdapter extends ArrayAdapter<RemoteFile> {

    private Context context;
    private int resource;

	public GroupsArrayAdapter(Context context, int resource) {
		super(context, resource);
        this.context = context;
        this.resource = resource;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
        RemoteFile file = getItem(position);
        String groupname = file.getRemotePath();
        groupname = groupname.substring(1, groupname.length() - 1);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        ImageView ivImage = (ImageView)convertView.findViewById(R.id.groupimg);
		TextView tvName = (TextView)convertView.findViewById(R.id.groupname);

        ivImage.setImageResource(R.drawable.shared_with_me_folder);
        tvName.setText(groupname);

	    return convertView;
	}		
}

