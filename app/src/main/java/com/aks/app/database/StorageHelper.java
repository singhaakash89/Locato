package com.aks.app.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public interface StorageHelper {

    public long insertToDB(String table_name, ContentValues contentValues);

    public long updateDB();

    public Cursor queryFromDB(String tableName, String[] columns);

//    public Cursor queryForSelectedCol(String tableName, String[] columns, String whereCol, int arg1);
//
//    public Cursor queryForSelectedCols(String tableName, String[] columns, String whereCol1, String whereCol2, int arg1, int arg2);
//
//    public Cursor queryForWeek1(String tableName, String[] columns, String whereCol1, int arg1);
//
//    public Cursor queryForWeek2and3(String tableName, String[] columns, String whereCol1, int arg1, int arg2, int arg3);
//
//    public Cursor queryForWeek4(String tableName, String[] columns, String whereCol1, int arg1);

    public int deleteFromDB();

}
