package com.lol.majchin.carnot;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import android.os.Handler;

import java.util.concurrent.CyclicBarrier;

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
    JsonArrayRequest jsonReq_comments , jsonReq_photos , jsonReq_todos , jsonReq_posts ;


    // Defining the Volley request queue that handles the URL request concurrently
    //RequestQueue requestQueue ;

    RequestQueue requestQueue1;
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;
    RequestQueue requestQueue4;

    // returns string version of current timestamp
    String GetCurrentTimeStamp(){
        long date = System.currentTimeMillis();
        return Long.toString(date);
    }

    // the onclick listener functions
    // for button 1
    public void refresh_1(View target) {
        tv_start_1.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue1.add(jsonReq_comments);

    }

    // for button 2
    public void refresh_2(View target) {
        tv_start_2.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue2.add(jsonReq_photos);
    }

    // for button 3
    public void refresh_3(View target) {
        tv_start_3.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue3.add(jsonReq_todos);
    }

    // for button 4
    public void refresh_4(View target) {
        tv_start_4.setText( "Start : " + GetCurrentTimeStamp());
        requestQueue4.add(jsonReq_posts);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // database helper
        DBH = new DB_helper(this);

        // Creates the Volley request queue
        //requestQueue = Volley.newRequestQueue(this);

        requestQueue1 = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        requestQueue3 = Volley.newRequestQueue(this);
        requestQueue4 = Volley.newRequestQueue(this);

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
                        tv_end_1.setText("End : " + GetCurrentTimeStamp());
                        // collect required parameters to pass to the async task of inserting data into the DB
                        DB_Task_Parameters comments_para = new DB_Task_Parameters(DBH, response, tv_end_save_1);
                        // start the async task to insert data into DB
                        //new AsyncSaveComments(tv_start_save_1 , tv_end_save_1).execute(comments_para);
                        new AsyncSaveComments(tv_start_save_1, tv_end_save_1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, comments_para);
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
                        tv_end_2.setText("End : " + GetCurrentTimeStamp());
                        // collect required parameters to pass to the async task of inserting data into the DB
                        DB_Task_Parameters photos_para = new DB_Task_Parameters(DBH, response, tv_end_save_2);
                        // start the async task to insert data into DB
                        //new AsyncSavePhotos(tv_start_save_2 , tv_end_save_2).execute(photos_para);
                        new AsyncSavePhotos(tv_start_save_2, tv_end_save_2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, photos_para);
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
                        tv_end_3.setText("End : " + GetCurrentTimeStamp());
                        // collect required parameters to pass to the async task of inserting data into the DB
                        DB_Task_Parameters todos_para = new DB_Task_Parameters(DBH, response, tv_end_save_3);
                        // start the async task to insert data into DB
                        //new AsyncSaveTodos(tv_start_save_3 , tv_end_save_3).execute(todos_para);
                        new AsyncSaveTodos(tv_start_save_3, tv_end_save_3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, todos_para);
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
                        tv_end_4.setText("End : " + GetCurrentTimeStamp());
                        // collect required parameters to pass to the async task of inserting data into the DB
                        DB_Task_Parameters posts_para = new DB_Task_Parameters(DBH, response, tv_end_save_4);
                        // start the async task to insert data into DB
                        // new AsyncSavePosts(tv_start_save_4 , tv_end_save_4).execute(posts_para);
                        new AsyncSavePosts(tv_start_save_4, tv_end_save_4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, posts_para);
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

        // each of these functions content will be called after 5 seconds
        /*
        start(tv_start_1,requestQueue1,jsonReq_comments);
        start(tv_start_2,requestQueue2,jsonReq_photos);
        start(tv_start_3,requestQueue3,jsonReq_todos);
        start(tv_start_4,requestQueue4,jsonReq_posts);
        */

        WAITSTART();

    }


    // THIS FUNCTION STARTS ALL THE DOWNLOADS AT THE SAME TIME AS SOON AS THE FUNCTION IS CALLED
    void START(){
        final CyclicBarrier gate = new CyclicBarrier(5);

        Thread t1 = new Thread() {
            public void run() {
                try {
                    gate.await();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_start_1.setText("Start : " + GetCurrentTimeStamp());
                        }
                    });
                    requestQueue1.add(jsonReq_comments);
                } catch (java.util.concurrent.BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };


        Thread t2 = new Thread() {
            public void run() {
                try {
                    gate.await();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_start_2.setText("Start : " + GetCurrentTimeStamp());
                        }
                    });
                    requestQueue2.add(jsonReq_photos);
                } catch (java.util.concurrent.BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };


        Thread t3 = new Thread() {
            public void run() {
                try {
                    gate.await();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_start_3.setText("Start : " + GetCurrentTimeStamp());
                        }
                    });
                    requestQueue3.add(jsonReq_todos);
                } catch (java.util.concurrent.BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };


        Thread t4 = new Thread() {
            public void run() {
                try {
                    gate.await();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_start_4.setText("Start : " + GetCurrentTimeStamp());
                        }
                    });
                    requestQueue4.add(jsonReq_posts);
                } catch (java.util.concurrent.BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };


        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            gate.await();
        } catch (java.util.concurrent.BrokenBarrierException e) {
            e.printStackTrace();
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }

    }

    // THIS FUNCTION CALLS THE START FUNVTION 5 SECONDS AFTER IT IS CALLED
    // AND IT IS CALLED AS SOON AS THE ONCREATE IS LOADED
    public void WAITSTART(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                START();

            }
        }, 5000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        DBH.close();
    }

}
