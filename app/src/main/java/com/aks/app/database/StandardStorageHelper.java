package com.aks.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aakash Singh on 24-11-2016.
 */

public class StandardStorageHelper implements StorageHelper {

    private PrimaryDBProvider primaryDBProvider;
    private static StandardStorageHelper standardStorageHelper;

    private StandardStorageHelper(Context mContext) {
        if (mContext != null) {
            primaryDBProvider = PrimaryDBProvider.getInstance();
        } else {
            throw new NullPointerException("Context is null in StandardStorageHelper");
        }
    }

    public static void createInstance(Context mContext) {
        if (standardStorageHelper == null) {
            standardStorageHelper = new StandardStorageHelper(mContext);
        } else {
            throw new IllegalStateException("primaryDBProvider is already present");
        }
    }

    public static StandardStorageHelper getInstance() {
        return standardStorageHelper;
    }

    @Override
    public long insertToDB(String table_name, ContentValues contentValues) {
        if (table_name == null || contentValues == null) {
            throw new SQLException("Table name OR ContentValues is null");
        } else {
            SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
            long id = db.insert(table_name, null, contentValues);
            return id;
        }
    }

    @Override
    public long updateDB() {
        return 0;
    }

    @Override
    public Cursor queryFromDB(String tableName, String[] columns) {
        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
        return cursor;
    }

//    @Override
//    public Cursor queryForSelectedCol(String tableName, String[] columns, String whereCol, int arg1) {
//        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
//        String whereClause = whereCol + " = ?";
//        String[] whereArgs = {String.valueOf(arg1)};
//        //db.update(String table, ContentValues values, String whereClause, String[] whereArgs);
//        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, null, null, null);
//        return cursor;
//    }
//
//    @Override
//    public Cursor queryForSelectedCols(String tableName, String[] columns, String whereCol1, String whereCol2, int arg1, int arg2) {
//        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
//        String whereClause = whereCol1 + "=? and " + whereCol2 + "=?";
//        String[] whereArgs = {String.valueOf(arg1), String.valueOf(arg2)};
//        //db.update(String table, ContentValues values, String whereClause, String[] whereArgs);
//        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, null, null, null);
//        return cursor;
//    }
//
//    @Override
//    public Cursor queryForWeek1(String tableName, String[] columns, String whereCol1, int arg1) {
//        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
//        String whereClause = WeightAccessor.DATE + "<=7 and " + whereCol1 + "=?";
//        String[] whereArgs = {String.valueOf(arg1)};
//        //db.update(String table, ContentValues values, String whereClause, String[] whereArgs);
//        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, null, null, null);
//        return cursor;
//    }
//
//    @Override
//    public Cursor queryForWeek2and3(String tableName, String[] columns, String whereCol1, int arg1, int arg2, int arg3) {
//        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
//        String whereClause = WeightAccessor.DATE + ">=? and " + WeightAccessor.DATE + "<=? and " + whereCol1 + "=?";
//        String[] whereArgs = {String.valueOf(arg2), String.valueOf(arg3), String.valueOf(arg1)};
//        //db.update(String table, ContentValues values, String whereClause, String[] whereArgs);
//        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, null, null, null);
//        return cursor;
//    }
//
//    @Override
//    public Cursor queryForWeek4(String tableName, String[] columns, String whereCol1, int arg1) {
//        SQLiteDatabase db = this.primaryDBProvider.getWritableDatabase();
//        String whereClause = WeightAccessor.DATE + ">21 and " + whereCol1 + "=?";
//        String[] whereArgs = {String.valueOf(arg1)};
//        //db.update(String table, ContentValues values, String whereClause, String[] whereArgs);
//        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, null, null, null);
//        return cursor;
//    }
//

    @Override
    public int deleteFromDB() {
        return 0;
    }
}
