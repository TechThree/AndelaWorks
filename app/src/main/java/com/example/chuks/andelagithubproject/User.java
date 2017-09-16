package com.example.chuks.andelagithubproject;

import android.graphics.Bitmap;

/**
 * Created by chuks on 9/16/2017.
 */

public class User {
    /** This is the Github Username*/
    private String  userName;

    /** This is the Github profile URL*/
    private String  userURL;

    /** Github User Image*/
    private Bitmap userImage;

    /**
     * Public constructor
     * @param userName
     * @param userURL
     * @param userImage
     */
    public User(String userName, String userURL, Bitmap userImage){

        this.userName = userName;
        this.userURL = userURL;
        this.userImage = userImage;

    }

    /**
     * public accessor method that returns the Github User Name
     * @return
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * public accessor method that returns the Github profile URL
     * @return
     */
    public String getUserURL(){
        return this.userURL;
    }

    /**
     * public accessor method that returns the Github profile Image
     * @return
     */
    public Bitmap getUserImage(){return this.userImage;}
}
