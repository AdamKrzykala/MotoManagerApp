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
                + " name text, "
                + " inService integer);" );
        //localdb.execSQL("DROP table garage");
    }

    public static class GarageAnswer {
        public List<String> names;
        public List<Integer> indexes;

        /**
         * @param names
         * @param indexes
         */
        public GarageAnswer(List<String> names, List<Integer> indexes) {
            this.names = names;
            this.indexes = indexes;
        }
    }

    public GarageAnswer getVehicles(boolean inService)
    {
        String inServiceString;
        if(inService) inServiceString = "1";
        else inServiceString = "0";

        Cursor cursor = localdb.rawQuery("select * from garage where inService = ?;", new String[] {inServiceString});
        cursor.moveToFirst();
        List<String> returnListNames = new ArrayList<String>();
        List<Integer> returnListIndexes = new ArrayList<Integer>();

        while (cursor.isAfterLast() == false)
        {
            String nameTemp = (String)cursor.getString(cursor.getColumnIndex("name"));
            returnListNames.add(nameTemp);
            int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("vehID"));
            returnListIndexes.add(indexTemp);
            cursor.moveToNext();
        }
        cursor.close();
        return new GarageAnswer(returnListNames, returnListIndexes);
    }

    public void moveToService(int idx)
    {
        localdb.execSQL("update garage set inService = 1 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    public void moveToGarage(int idx)
    {
        localdb.execSQL("update garage set inService = 1 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    public void addVehicle(String name)
    {
        localdb.execSQL( "insert into garage(name, inService) values (?, ?);", new Object[] {name, 0} );
    }

    public void deleteVehicle(int idx)
    {
        localdb.execSQL("DELETE from garage where vehID == ?", new Object[] {idx});
    }

    public void closeDatabase(){
        localdb.close();
    }
}
