package com.example.motoapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
                + " producent text, "
                + " model text, "
                + " year text, "
                + " inService integer);" );
        //localdb.execSQL("DROP table garage");
    }

    public static class GarageAnswer {

        public List<Integer> indexes;
        public List<String> names;
        public List<String> producents;
        public List<String> models;
        public List<String> years;


        /**
         * @param names
         * @param indexes
         */
        public GarageAnswer(List<Integer> indexes,
                            List<String> names,
                            List<String> producents,
                            List<String> models,
                            List<String> years) {
            this.indexes = indexes;
            this.names = names;
            this.producents = producents;
            this.models = models;
            this.years = years;
        }
    }

    public GarageAnswer getVehicles(boolean inService)
    {
        String inServiceString;
        if(inService) inServiceString = "1";
        else inServiceString = "0";

        Cursor cursor = localdb.rawQuery("select * from garage where inService = ?;", new String[] {inServiceString});
        cursor.moveToFirst();
        List<Integer> returnListIndexes = new ArrayList<Integer>();
        List<String> returnListNames = new ArrayList<String>();
        List<String> returnListProducents = new ArrayList<String>();
        List<String> returnListModels = new ArrayList<String>();
        List<String> returnListYears = new ArrayList<String>();


        while (cursor.isAfterLast() == false)
        {
            int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("vehID"));
            returnListIndexes.add(indexTemp);
            String nameTemp = (String)cursor.getString(cursor.getColumnIndex("name"));
            returnListNames.add(nameTemp);
            String producentTemp = (String)cursor.getString(cursor.getColumnIndex("producent"));
            returnListProducents.add(producentTemp);
            String modelTemp = (String)cursor.getString(cursor.getColumnIndex("model"));
            returnListModels.add(modelTemp);
            String yearTemp = (String)cursor.getString(cursor.getColumnIndex("year"));
            returnListYears.add(yearTemp);
            cursor.moveToNext();
        }
        cursor.close();
        return new GarageAnswer(
                returnListIndexes,
                returnListNames,
                returnListProducents,
                returnListModels,
                returnListYears);
    }

    public void moveToService(int idx)
    {
        localdb.execSQL("update garage set inService = 1 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    public void moveToGarage(int idx)
    {
        localdb.execSQL("update garage set inService = 1 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    public void addVehicle(Bundle data)
    {
        Log.i("ADD: ", "Iam in");
        String name = data.getString("vresult");
        String producent = data.getString("Producent");
        String model = data.getString("Model");
        String year = data.getString("Year");
        String path = data.getString("Path");

        localdb.execSQL(
                "insert into garage(name, producent, model, year, inService) values (?, ?, ?, ?, ?);",
                new Object[] {name, producent, model, year, 0} );

        Toast.makeText(this.context, name + " added",
                Toast.LENGTH_LONG).show();

        //Creating folders

        Cursor cursor = localdb.rawQuery("select MAX(vehID) as MAX_ITER from garage;", new String[] {});
        cursor.moveToFirst();
        int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("MAX_ITER"));

        //Folder for pictures
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String newPathPic = picturesDirectory.getAbsolutePath() + "/moto" + String.valueOf(indexTemp);
        File dirToCreatePic = new File(newPathPic);
        Log.i("PATH: ", dirToCreatePic.getAbsolutePath());
        if(dirToCreatePic.exists() && dirToCreatePic.isDirectory()) {
            //erase
            deleteFolder(dirToCreatePic);
        }
        dirToCreatePic.mkdir();

        //Folder for PDF
        File documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String newPathDoc = documentsDirectory.getAbsolutePath() + "/moto" + String.valueOf(indexTemp);
        File dirToCreateDoc = new File(newPathDoc);
        Log.i("PATH: ", dirToCreateDoc.getAbsolutePath());
        if(dirToCreateDoc.exists() && dirToCreateDoc.isDirectory()) {
            //erase
            deleteFolder(dirToCreateDoc);
        }
        dirToCreateDoc.mkdir();

        //Copying PDF file to directory
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void deleteVehicle(int idx)
    {
        localdb.execSQL("DELETE from garage where vehID == ?", new Object[] {idx});
    }

    public void closeDatabase(){
        localdb.close();
    }
}
