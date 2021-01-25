package com.example.motoapp.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.motoapp.MainActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.LatLng;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static androidx.core.app.ActivityCompat.requestPermissions;

public class DatabaseAdapter {
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL = 4192;
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
                + " mth integer, "
                + " inService integer);" );

        localdb.execSQL("create table if not exists services ("
                + " serviceID integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " description text, "
                + " date_time text, "
                + " vehID integer);" );

        localdb.execSQL("create table if not exists runs ("
                + " runID integer PRIMARY KEY autoincrement, "
                + " vehID integer, "
                + " allocation text, "
                + " date_time text);" );

        localdb.execSQL("create table if not exists triggers ("
                + " triggerID integer PRIMARY KEY autoincrement, "
                + " vehID integer, "
                + " whatToDo text, "
                + " mth integer, "
                + " done integer);" );
        //localdb.execSQL("DROP table garage");
    }

    public String getCurrentTable() {
        Cursor cursor = localdb.rawQuery("select MAX(runID) as MAX_ITER from runs;", new String[] {});
        cursor.moveToFirst();
        int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("MAX_ITER"));
        Cursor cursorForString = localdb.rawQuery("select * from runs where runID = ?;",
                new String[] {String.valueOf(indexTemp)});
        cursorForString.moveToFirst();
        return (String)cursorForString.getString(cursorForString.getColumnIndex("allocation"));
    }

    public void addPointToCurrentRun(String table, double latitude, double longitude) {
        String command = "insert into " + table +" (latitude, longitude) values (?, ?);";
        localdb.execSQL(
                command,
                new Object[] {latitude, longitude} );
    }

    public void addNewRun(Integer vehID) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        localdb.execSQL(
                "insert into runs(vehID, allocation, date_time) values (?, ?, ?);",
                new Object[] {vehID, null, formattedDate} );
        Cursor cursor = localdb.rawQuery("select MAX(runID) as MAX_ITER from runs;", new String[] {});
        cursor.moveToFirst();
        int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("MAX_ITER"));
        String newTableForRun = "run" + String.valueOf(indexTemp);
        localdb.execSQL("update runs set allocation = ? where runID = ?;",
                new Object[] {newTableForRun, indexTemp});
        String createTableCommand = "create table if not exists " + newTableForRun + " ("
                + " pointID integer PRIMARY KEY autoincrement, "
                + " latitude real, "
                + " longitude real);";
        localdb.execSQL(createTableCommand);
    }

    public void updateMth(Integer vehID, Integer newMth)
    {
        localdb.execSQL("update garage set mth = ? where vehID = ?;", new Object[] {newMth, vehID});
    }

    public void addService(String name, String description, Integer vehID) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        localdb.execSQL(
                "insert into services(name, description, date_time, vehID) values (?, ?, ?, ?);",
                new Object[] {name, description, formattedDate, vehID} );
    }

    public void addTrigger(int vehID, String whatToDo, int mth)
    {
        localdb.execSQL(
                "insert into triggers(vehID, whatToDo, mth, done) values (?, ?, ?, ?);",
                new Object[] {vehID, whatToDo, mth, 0} );
    }

    public void triggerCompleted(int idx)
    {
        localdb.execSQL("update triggers set done = 1 where triggerID = ?;", new Object[] {Integer.toString(idx)});
    }

    public static class TriggersAnswer {
        public List<Integer> indexes;
        public List<String> whatToDo;
        public List<Integer> mth;
        public List<Integer> done;

        public TriggersAnswer(List<Integer> indexes, List<String> whatToDo, List<Integer> mth, List<Integer> done) {
            this.indexes = indexes;
            this.whatToDo = whatToDo;
            this.mth = mth;
            this.done = done;
        }
    }

    public TriggersAnswer getTriggers(Integer vehID)
    {
        Cursor cursor = localdb.rawQuery("select * from triggers where vehID = ? and done = 0;",
                new String[] {String.valueOf(vehID)});
        cursor.moveToFirst();
        List<Integer> returnListIndexes = new ArrayList<Integer>();
        List<String> returnListWhatToDo = new ArrayList<String>();
        List<Integer> returnListMth = new ArrayList<Integer>();
        List<Integer> returnListDone = new ArrayList<Integer>();


        while (cursor.isAfterLast() == false)
        {
            Integer idxTemp = (Integer) cursor.getInt(cursor.getColumnIndex("triggerID"));
            returnListIndexes.add(idxTemp);
            String whatTemp = (String) cursor.getString(cursor.getColumnIndex("whatToDo"));
            returnListWhatToDo.add(whatTemp);
            Integer mthTemp = (Integer) cursor.getInt(cursor.getColumnIndex("mth"));
            returnListMth.add(mthTemp);
            Integer doneTemp = (Integer) cursor.getInt(cursor.getColumnIndex("done"));
            returnListDone.add(doneTemp);

            cursor.moveToNext();
        }
        cursor.close();
        return new TriggersAnswer(
                returnListIndexes,
                returnListWhatToDo,
                returnListMth,
                returnListDone);
    }

    public List<com.google.android.gms.maps.model.LatLng> getLocations(String allocation)
    {
        String command = "select * from " + allocation + ";";
        Cursor cursor = localdb.rawQuery(command, null);
        cursor.moveToFirst();
        List<com.google.android.gms.maps.model.LatLng> returnListLatLng = new ArrayList<>();

        while (cursor.isAfterLast() == false)
        {
            Double latitude = (Double) cursor.getDouble(cursor.getColumnIndex("latitude"));
            Double longitude = (Double) cursor.getDouble(cursor.getColumnIndex("longitude"));
            com.google.android.gms.maps.model.LatLng tempLatLng = new com.google.android.gms.maps.model.LatLng(latitude, longitude);
            returnListLatLng.add(tempLatLng);
            cursor.moveToNext();
        }
        cursor.close();
        return returnListLatLng;
    }

    public static class RunsAnswer {
        public List<Integer> indexes;
        public List<String> allocations;
        public List<String> dates;

        public RunsAnswer(List<Integer> indexes, List<String> allocations, List<String> dates) {
            this.indexes = indexes;
            this.allocations = allocations;
            this.dates = dates;
        }
    }

    public RunsAnswer getRuns(Integer vehID)
    {
        Cursor cursor = localdb.rawQuery("select * from runs where vehID = ?;",
                new String[] {String.valueOf(vehID)});
        cursor.moveToFirst();
        List<Integer> returnListIndexes = new ArrayList<Integer>();
        List<String> returnListAllocations = new ArrayList<String>();
        List<String> returnListDates = new ArrayList<String>();


        while (cursor.isAfterLast() == false)
        {
            Integer idxTemp = (Integer) cursor.getInt(cursor.getColumnIndex("runID"));
            returnListIndexes.add(idxTemp);
            String allocationTemp = (String) cursor.getString(cursor.getColumnIndex("allocation"));
            returnListAllocations.add(allocationTemp);
            String dateTemp = (String)cursor.getString(cursor.getColumnIndex("date_time"));
            returnListDates.add(dateTemp);

            cursor.moveToNext();
        }
        cursor.close();
        return new RunsAnswer(
                returnListIndexes,
                returnListAllocations,
                returnListDates);
    }

    public static class GarageAnswer {

        public List<Integer> indexes;
        public List<String> names;
        public List<String> producents;
        public List<String> models;
        public List<String> years;
        public List<Integer> mths;
        public List<Integer> activeTriggers;


        /**
         * @param names
         * @param indexes
         */
        public GarageAnswer(List<Integer> indexes,
                            List<String> names,
                            List<String> producents,
                            List<String> models,
                            List<String> years,
                            List<Integer> mths,
                            List<Integer> activeTriggers) {

            this.mths = mths;
            this.indexes = indexes;
            this.names = names;
            this.producents = producents;
            this.models = models;
            this.years = years;
            this.activeTriggers = activeTriggers;
        }
    }
    
    public static class ServicesAnswer {
        public List<Integer> indexes;
        public List<String> names;
        public List<String> dates;
        public List<String> descriptions;

        public ServicesAnswer(List<Integer> indexes,
                              List<String> names,
                              List<String> dates,
                              List<String> descriptions) {
            this.indexes = indexes;
            this.names = names;
            this.dates = dates;
            this.descriptions = descriptions;
        }
    }

    public ServicesAnswer getServices(Integer vehID)
    {
        Cursor cursor = localdb.rawQuery("select * from services where vehID = ?;",
                new String[] {String.valueOf(vehID)});
        cursor.moveToFirst();
        List<Integer> returnListIndexes = new ArrayList<Integer>();
        List<String> returnListNames = new ArrayList<String>();
        List<String> returnListDates = new ArrayList<String>();
        List<String> returnListDescriptions = new ArrayList<String>();


        while (cursor.isAfterLast() == false)
        {
            Integer idxTemp = (Integer) cursor.getInt(cursor.getColumnIndex("serviceID"));
            returnListIndexes.add(idxTemp);
            String nameTemp = (String) cursor.getString(cursor.getColumnIndex("name"));
            returnListNames.add(nameTemp);
            String descTemp = (String)cursor.getString(cursor.getColumnIndex("description"));
            returnListDescriptions.add(descTemp);
            String dateTemp = (String)cursor.getString(cursor.getColumnIndex("date_time"));
            returnListDates.add(dateTemp);

            cursor.moveToNext();
        }
        cursor.close();
        return new ServicesAnswer(
                returnListIndexes,
                returnListNames,
                returnListDates,
                returnListDescriptions);
    }

    public GarageAnswer getVehicles(boolean inService)
    {
        String inServiceString;
        if(inService) inServiceString = "1";
        else inServiceString = "0";

        Cursor cursor = localdb.rawQuery("select * from garage where inService = ?;", new String[] {inServiceString});
        cursor.moveToFirst();
        List<Integer> returnListIndexes = new ArrayList<Integer>();
        List<Integer> returnListMths = new ArrayList<Integer>();
        List<String> returnListNames = new ArrayList<String>();
        List<String> returnListProducents = new ArrayList<String>();
        List<String> returnListModels = new ArrayList<String>();
        List<String> returnListYears = new ArrayList<String>();
        List<Integer> returnActiveTriggers = new ArrayList<>();


        while (cursor.isAfterLast() == false)
        {
            int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("vehID"));
            returnListIndexes.add(indexTemp);
            int mthTemp = (Integer)cursor.getInt(cursor.getColumnIndex("mth"));
            returnListMths.add(mthTemp);
            String nameTemp = (String)cursor.getString(cursor.getColumnIndex("name"));
            returnListNames.add(nameTemp);
            String producentTemp = (String)cursor.getString(cursor.getColumnIndex("producent"));
            returnListProducents.add(producentTemp);
            String modelTemp = (String)cursor.getString(cursor.getColumnIndex("model"));
            returnListModels.add(modelTemp);
            String yearTemp = (String)cursor.getString(cursor.getColumnIndex("year"));
            returnListYears.add(yearTemp);
            cursor.moveToNext();
            int trigger = 0;
            TriggersAnswer tempTriggers = getTriggers(indexTemp);
            for (int i = 0; i < tempTriggers.indexes.size(); i++) {
                if (tempTriggers.mth.get(i) <= mthTemp) trigger = 1;
                break;
            }
            returnActiveTriggers.add(trigger);
        }
        cursor.close();
        return new GarageAnswer(
                returnListIndexes,
                returnListNames,
                returnListProducents,
                returnListModels,
                returnListYears,
                returnListMths,
                returnActiveTriggers);
    }

    public void moveToService(int idx)
    {
        localdb.execSQL("update garage set inService = 1 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    public void moveToGarage(int idx)
    {
        localdb.execSQL("update garage set inService = 0 where vehID = ?;", new Object[] {Integer.toString(idx)});
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addVehicle(Bundle data)
    {
        Log.i("ADD: ", "Iam in");
        String name = data.getString("vresult");
        String producent = data.getString("Producent");
        String model = data.getString("Model");
        String year = data.getString("Year");
        String path = data.getString("Path");

        localdb.execSQL(
                "insert into garage(name, producent, model, year, mth, inService) values (?, ?, ?, ?, ?, ?);",
                new Object[] {name, producent, model, year, 0, 0} );

        Cursor cursor = localdb.rawQuery("select MAX(vehID) as MAX_ITER from garage;", new String[] {});
        cursor.moveToFirst();
        int indexTemp = (Integer)cursor.getInt(cursor.getColumnIndex("MAX_ITER"));
        Log.i("MAX: ", String.valueOf(indexTemp));
        //Creating folders
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

    public void deleteService(int idx)
    {
        localdb.execSQL("DELETE from services where serviceID == ?", new Object[] {idx});
    }

    public void deleteVehicle(int idx)
    {
        localdb.execSQL("DELETE from garage where vehID == ?", new Object[] {idx});
    }

    public void closeDatabase(){
        localdb.close();
    }
}
