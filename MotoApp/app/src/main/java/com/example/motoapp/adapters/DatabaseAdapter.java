package com.example.motoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.example.motoapp.MainActivity;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private SQLiteDatabase localdb;
    private Context context;

    public DatabaseAdapter(Context context)
    {
        this.context = context;
        File storagePath = context.getFilesDir();
        String myDbPath = storagePath + "/" + "mygarage";

        try {
            localdb = SQLiteDatabase.openDatabase(myDbPath, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);

        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        localdb.execSQL("create table if not exists garage ("
                + " vehID integer PRIMARY KEY autoincrement, "
                + " name text );" );
        //localdb.execSQL("DROP table garage");
    }

    public List<String> getNamesFromGarage()
    {
        Cursor cursor = localdb.rawQuery("select * from garage", null);
        cursor.moveToFirst();
        List<String> returnList = new ArrayList<String>();
        while (cursor.isAfterLast() == false)
        {
            String a = (String)cursor.getString(cursor.getColumnIndex("name"));
            returnList.add(a);
            cursor.moveToNext();
        }
        cursor.close();
        return returnList;
    }

    public void addVehicle(String name)
    {
        localdb.execSQL( "insert into garage(name) values (?);", new String[]{name} );
    }

    public void closeDatabase(){
        localdb.close();
    }
}
