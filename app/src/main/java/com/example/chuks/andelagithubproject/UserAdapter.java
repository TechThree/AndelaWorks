package com.example.chuks.andelagithubproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chuks on 9/16/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {

    /**
     * Constructs a new {@link UserAdapter}
     * @param context
     * @param data
     */

    int resource;
    public UserAdapter(Context context, int _resourceList, List<User> data){
        super(context,_resourceList,data);
        resource = _resourceList;
    }

    /**
     * Returns a list item view that displays information about the user at the
     * given position
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Check if there is an existing list item view (called convertView) that
        //we can reuse, otherwise, it convertView is null, then inflate a new list item
        //layout

        View listItemView = convertView;

        if(listItemView == null){
            //Inflate new view if this is not an update
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.java_dev_list_item, parent, false);

            //Find the needed data at the given position in the list
            User currentUser = getItem(position);

            //Find the TextView with the view ID user_name
            TextView userName = (TextView) listItemView.findViewById(R.id.user_name);

            //Display the user name in the current text view
            userName.setText(currentUser.getUserName());

            //Display the user image in the current image view
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.user_image);
            imageView.setImageBitmap(currentUser.getUserImage());

        }

        return listItemView;
    }
}
