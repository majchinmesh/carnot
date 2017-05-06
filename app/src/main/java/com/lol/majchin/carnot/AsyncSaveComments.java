package com.lol.majchin.carnot;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by majch on 05-05-2017.
 */
public class AsyncSaveComments extends AsyncTask< DB_Task_Parameters , Void, TextView > {

    // returns string version of current timestamp
    String GetCurrentTimeStamp(){
        long date = System.currentTimeMillis();
        return Long.toString(date);
    }

    public AsyncSaveComments(TextView tv_start_save , TextView tv_end_save ) {
        super();
        // do stuff
        tv_start_save.setText( "Start Save : " + GetCurrentTimeStamp() );
        tv_end_save.setText("---");
    }

    @Override
    protected void onPreExecute()
    {
        Log.i("AsyncSaveComments "," reached pre ");
        super.onPreExecute();

    }
    @Override
    protected TextView doInBackground(DB_Task_Parameters... data)
    {
        Log.i("AsyncSaveComments "," reached Do in back ");

        JSONArray comments_arr = data[0].jsonArray ;
        DB_helper DBH = data[0].dbHepler ;
        TextView tv_end_save = data[0].tv_end_save ;
        final TextView tv_rows_in_db = data[0].tv_rows_in_db ;
        MainActivity myact = data[0].myact ;
        JSONObject commentObj ;
        int id , pid ;
        String name , email , body ;

        //Activity myact = new MainActivity() ;

        try {
            for (int l = 0; l < comments_arr.length(); l++) {

                // for Rows in DB field
                final int i = l+1 ;

                commentObj = comments_arr.getJSONObject(l);
                id = commentObj.getInt("id");
                pid = commentObj.getInt("postId");
                name = commentObj.getString("name");
                email = commentObj.getString("email");
                body = commentObj.getString("body");
                DBH.insertComment(id, pid, name, email, body);

                myact.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_rows_in_db.setText("Rows In DB : " + Integer.toString(i));
                    }
                });
            }
        }catch (JSONException e) {
            // If an error occurs, this prints the error to the log
            e.printStackTrace();
        }

        //Record method
        return tv_end_save  ;
    }

    @Override
    protected void onPostExecute(TextView tv_end_save )
    {
        Log.i("AsyncSaveComments "," reached post ");
        super.onPostExecute(tv_end_save);
        tv_end_save.setText("End Save : " + GetCurrentTimeStamp());
        Log.i("Async Task", "Comments Done!");
    }

}

