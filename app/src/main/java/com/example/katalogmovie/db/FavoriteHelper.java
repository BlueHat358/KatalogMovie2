package com.example.katalogmovie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteHelper {
    private static String DATABASE_TABLE = DatabaseContract.TABLE_MOVIE;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase sqliteDatabase;


    public FavoriteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        sqliteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqliteDatabase.close();
    }

    public Cursor queryProvider() {
        return sqliteDatabase.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.MovieColumns._ID + " DESC"
        );
    }

    public Cursor queryByIdProvider(String id) {
        return sqliteDatabase.query(DATABASE_TABLE, null
                , DatabaseContract.MovieColumns.ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public long insertProvider(ContentValues values) {
        return sqliteDatabase.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return sqliteDatabase.update(DATABASE_TABLE, values,
                DatabaseContract.MovieColumns.ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return sqliteDatabase.delete(DATABASE_TABLE,
                DatabaseContract.MovieColumns.ID + " = ?", new String[]{id});
    }
}
