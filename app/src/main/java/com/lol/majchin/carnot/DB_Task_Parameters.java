package com.lol.majchin.carnot;

import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

/**
 * Created by majch on 05-05-2017.
 */
public class DB_Task_Parameters {

    DB_helper dbHepler ;
    JSONArray jsonArray ;
    TextView tv_end_save ;
    TextView tv_rows_in_db;
    MainActivity myact ;

    DB_Task_Parameters(DB_helper dbHepler, JSONArray jsonArray, TextView tv_end_save,TextView tv_rows_in_db, MainActivity myact ) {
        this.dbHepler = dbHepler ;
        this.jsonArray = jsonArray ;
        this.tv_end_save = tv_end_save ;
        this.tv_rows_in_db = tv_rows_in_db ;
        this.myact = myact ;
    }

}

