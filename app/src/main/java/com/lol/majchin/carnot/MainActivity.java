package com.lol.majchin.carnot;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // verious textviews for all the timestamps
    TextView tv_start_1 , tv_end_1 , tv_start_save_1 , tv_end_save_1 ;
    TextView tv_start_2 , tv_end_2 , tv_start_save_2 , tv_end_save_2 ;
    TextView tv_start_3 , tv_end_3 , tv_start_save_3 , tv_end_save_3 ;
    TextView tv_start_4 , tv_end_4 , tv_start_save_4 , tv_end_save_4 ;
    TextView tv_cts ;

    // database helper object
    DB_helper DBH = null ;

    // URLs
    String URL_comments = "https://jsonplaceholder.typicode.com/comments";
    String URL_photos = "https://jsonplaceholder.typicode.com/photos";
    String URL_todos = "https://jsonplaceholder.typicode.com/todos";
    String URL_posts = "https://jsonplaceholder.typicode.com/posts";

    // volley request objects
    JsonArrayRequest jsonReq_comments ;
    JsonArrayRequest jsonReq_photos ;
    JsonArrayRequest jsonReq_todos ;
    JsonArrayRequest jsonReq_posts ;


    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;

    // returns string version of current timestamp
    String GetCurrentTimeStamp(){
        long date = System.currentTimeMillis();
        return Long.toString(date);
    }

    // the onclick listener functions
    // for button 1
    public void refresh_1(View target) {
        tv_start_1.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_comments);
    }

    // for button 2
    public void refresh_2(View target) {
        tv_start_2.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_photos);
    }

    // for button 3
    public void refresh_3(View target) {
        tv_start_3.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_todos);
    }

    // for button 4
    public void refresh_4(View target) {
        tv_start_4.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_posts);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // database helper
        DBH = new DB_helper(this);

        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(this);


        // Casts all into the TextView found within the main layout XML
        tv_start_1 = (TextView) findViewById(R.id.txt_v_1_start);
        tv_end_1 = (TextView) findViewById(R.id.txt_v_1_end);
        tv_start_save_1 = (TextView) findViewById(R.id.txt_v_1_start_save);
        tv_end_save_1 = (TextView) findViewById(R.id.txt_v_1_end_save);

        tv_start_2 = (TextView) findViewById(R.id.txt_v_2_start);
        tv_end_2 = (TextView) findViewById(R.id.txt_v_2_end);
        tv_start_save_2 = (TextView) findViewById(R.id.txt_v_2_start_save);
        tv_end_save_2 = (TextView) findViewById(R.id.txt_v_2_end_save);

        tv_start_3 = (TextView) findViewById(R.id.txt_v_3_start);
        tv_end_3 = (TextView) findViewById(R.id.txt_v_3_end);
        tv_start_save_3 = (TextView) findViewById(R.id.txt_v_3_start_save);
        tv_end_save_3 = (TextView) findViewById(R.id.txt_v_3_end_save);

        tv_start_4 = (TextView) findViewById(R.id.txt_v_4_start);
        tv_end_4 = (TextView) findViewById(R.id.txt_v_4_end);
        tv_start_save_4 = (TextView) findViewById(R.id.txt_v_4_start_save);
        tv_end_save_4 = (TextView) findViewById(R.id.txt_v_4_end_save);

        tv_cts = (TextView) findViewById(R.id.txt_v_cts);



        // for current time stamp
        // contineously changes the value of current time stamp field
        Thread cts_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_cts.setText(GetCurrentTimeStamp());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        cts_thread.start();

        // volley json request for comments
        jsonReq_comments = new JsonArrayRequest(URL_comments,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // json request received / completed for comments
                        tv_end_1.setText( "End : " + GetCurrentTimeStamp());
                        try {
                            // here we started with saving
                            tv_start_save_1.setText( "Start Save : " + GetCurrentTimeStamp());
                            for(int l=0; l< response.length(); l++){
                                JSONObject commentObj = response.getJSONObject(l);
                                int id = commentObj.getInt("id") ;
                                int pid = commentObj.getInt("postId") ;
                                String name = commentObj.getString("name");
                                String email = commentObj.getString("email");
                                String body = commentObj.getString("body");
                                DBH.insertComment(id ,pid , name ,email , body);
                            }
                            // here we ended saving
                            tv_end_save_1.setText( "End Save : " + GetCurrentTimeStamp());

                            // for testing purpose
                            Toast.makeText( getApplicationContext() , DBH.test_1() , Toast.LENGTH_LONG).show();
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

        // // volley json request for photos
        jsonReq_photos = new JsonArrayRequest(URL_photos,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // json request received / completed for photos
                        tv_end_2.setText( "End : " + GetCurrentTimeStamp());
                        try {
                            // here we started with saving
                            tv_start_save_2.setText( "Start Save : " + GetCurrentTimeStamp());
                            for(int l=0; l< response.length(); l++){
                                JSONObject photoObj = response.getJSONObject(l);
                                int id = photoObj.getInt("id") ;
                                int aid = photoObj.getInt("albumId") ;
                                String title = photoObj.getString("title");
                                String url = photoObj.getString("url");
                                String turl = photoObj.getString("thumbnailUrl");
                                DBH.insertPhotos(id ,aid , title ,url , turl);
                            }
                            // here we ended saving
                            tv_end_save_2.setText( "End Save : " + GetCurrentTimeStamp());

                            // for testing purpose
                            Toast.makeText( getApplicationContext() , DBH.test_2() , Toast.LENGTH_LONG).show();

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

        // volley json request for todos
        jsonReq_todos = new JsonArrayRequest(URL_todos,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // json request received / completed for todos
                        tv_end_3.setText( "End : " + GetCurrentTimeStamp());
                        try {
                            // here we started with saving
                            tv_start_save_3.setText( "Start Save : " + GetCurrentTimeStamp());
                            for(int l=0; l< response.length(); l++){
                                JSONObject todoObj = response.getJSONObject(l);
                                int id = todoObj.getInt("id") ;
                                int uid = todoObj.getInt("userId") ;
                                String title = todoObj.getString("title");
                                String completed = todoObj.getString("completed");
                                DBH.insertTodos(id ,uid , title ,completed);
                            }
                            // here we ended saving
                            tv_end_save_3.setText( "End Save : " + GetCurrentTimeStamp());

                            // for testing purpose
                            Toast.makeText( getApplicationContext() , DBH.test_3() , Toast.LENGTH_LONG).show();
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

        // volley json request for posts
        jsonReq_posts = new JsonArrayRequest(URL_posts,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // json request received / completed for posts
                        tv_end_4.setText( "End : " + GetCurrentTimeStamp());
                        try {
                            // here we started with saving
                            tv_start_save_4.setText( "Start Save : " + GetCurrentTimeStamp());
                            for(int l=0; l< response.length(); l++){
                                JSONObject photoObj = response.getJSONObject(l);
                                int id = photoObj.getInt("id") ;
                                int uid = photoObj.getInt("userId") ;
                                String title = photoObj.getString("title");
                                String body = photoObj.getString("body");
                                DBH.insertPosts(id ,uid , title ,body);
                            }
                            // here we ended saving
                            tv_end_save_4.setText( "End Save : " + GetCurrentTimeStamp());

                            // for testing purpose
                            Toast.makeText( getApplicationContext() , DBH.test_4() , Toast.LENGTH_LONG).show();
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

        // startrd the json request for comments
        tv_start_1.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_comments);

        // startrd the json request for photos
        tv_start_2.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_photos);

        // startrd the json request todos
        tv_start_3.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_todos);

        // startrd the json request posts
        tv_start_4.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue.add(jsonReq_posts);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DBH.close();
    }

}