package com.example.favorite.Support;

import android.database.Cursor;

import com.example.favorite.db.DatabaseContract;
import com.example.favorite.model.Favorite;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Favorite> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Favorite> favorites = new ArrayList<>();
        Favorite favorite;
        while (cursor.moveToNext()) {
            favorite = new Favorite(cursor.getString(cursor.getColumnIndexOrThrow(
                    DatabaseContract.MovieColumns.ID)));
            favorites.add(favorite);
        }
        return favorites;
    }
}
