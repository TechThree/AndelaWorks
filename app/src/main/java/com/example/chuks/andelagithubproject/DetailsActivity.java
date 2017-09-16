package com.example.chuks.andelagithubproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    /** User Name TextView object*/
    TextView userNameText;

    /** User Name Profile URL*/
    TextView userProfileURLText;

    /** User Image*/
    ImageView userImageView;

    String userURL;

    String message = "Check out this awesome developer";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        userNameText = (TextView) findViewById(R.id.user_name);
        userProfileURLText = (TextView)findViewById(R.id.user_profile_textview);
        userImageView = (ImageView)findViewById(R.id.user_imageview);

        Bundle getExtras = getIntent().getExtras();
        final String userName = getExtras.getString("User Name");
        userURL = getExtras.getString("User Profile URL");

        //TODO: Figure out a way to pass Bitmaps between activities (properly)
        Bitmap userImage = getExtras.getParcelable("User Image");


        userNameText.setText(userName);
        userProfileURLText.setText(userURL);
        //userImageView.setImageBitmap(userImage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(userURL));
                shareIntent.putExtra(Intent.EXTRA_TEXT, message + " @" + userName + "," + " @" + userURL);
                startActivity(shareIntent);
            }
        });



    }
}
