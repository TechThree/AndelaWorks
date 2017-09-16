package com.example.chuks.andelagithubproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Tag for Log Messages*/
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** URL that gets the list of Java developers in Lagos*/
    private final static String GITHUB_QUERY_URL = "https://api.github.com/search/users?q=language:java+location:lagos";


    /** making the UserAdapter object a global variable, so it is easily accessible*/
    private  UserAdapter adapter;

    private TextView emptyStateTextView;

    private ProgressBar myProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG,"The onCreate Activity Has started!!");

        myProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        //Begin AsyncTask
        UserAsyncTask task = new UserAsyncTask();
        task.execute(GITHUB_QUERY_URL);
    }


    public  class UserAsyncTask extends AsyncTask<String, Void, List<User>> implements AdapterView.OnItemClickListener {


        @Override
        protected List<User> doInBackground(String... urls) {

            //Perform HTTP request to get users' github data
            List<User> userDetails = QueryUtils.getGithubData(urls[0]);

            return userDetails;
        }

        @Override
        protected void onPostExecute(List<User> data){

            updateUi(data);
            myProgressBar.setVisibility(View.GONE);


        }

        private void updateUi(List<User> data){

            //Reference to the {@link ListView} in the layout
            ListView usersListview = (ListView) findViewById(R.id.list);
            emptyStateTextView = (TextView) findViewById(R.id.empty_state_textview);
            usersListview.setEmptyView(emptyStateTextView);

            //Get a reference to the ConnectivityManager to check state of the network connection
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            //Get details on the currently active default data network
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //create a new adapter that takes the list of user as input
            adapter = new UserAdapter(MainActivity.this, R.layout.java_dev_list_item, data);

            //if there is a network connection, fetch data
           if(data == null || networkInfo == null || !networkInfo.isConnected()){
                emptyStateTextView.setText("List of Java Developers Could Not Be Found!");
                adapter.clear();
                return;

            }

            //set the adapter on the {@link ListView}
            //so the list can be populated in the user interface
            usersListview.setAdapter(adapter);

            //set an item click listener on the ListView, which sends an intent to a
            //web browser to open a website with more information about the selected user
            usersListview.setOnItemClickListener(this);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Find the current user that was clicked on
            User currentUser = adapter.getItem(position);

            //Intent to take me to the next screen
            Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
            myIntent.putExtra("User Name", currentUser.getUserName());
            myIntent.putExtra("User Profile URL", currentUser.getUserURL());
            //myIntent.putExtra("User Image", currentUser.getUserImage());
            //launches the activity
            startActivity(myIntent);

        }
    }
}
