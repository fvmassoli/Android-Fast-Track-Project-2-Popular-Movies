package com.example.android.fvmpopularmovieapp.data_persistance;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;


public class Provider extends ContentProvider {

    public static final int FAVORITE_MOVIES = 100;
    public static UriMatcher sUriMatcher = buidUriMatcher();
    private SQLiteOpenHelper mSqLiteOpenHelper;

    public static UriMatcher buidUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDataContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieDataContract.MOVIE_TABLE_NAME, FAVORITE_MOVIES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mSqLiteOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                cursor = mSqLiteOpenHelper.getReadableDatabase().query(
                        MovieDataContract.MOVIE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("Method not needed");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = mSqLiteOpenHelper.getWritableDatabase();
        Uri rUri;
        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                long id = sqLiteDatabase.insert(MovieDataContract.MOVIE_TABLE_NAME, null, values);
                if(id > 0) {
                    rUri = ContentUris.withAppendedId(MovieDataContract.Contract.CONTENT_URI, id);
                    if(getContext() != null)
                        getContext().getContentResolver().notifyChange(uri, null);
                } else
                    throw new SQLException("Failed to insert at uri " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        return rUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int nbDeletedRows = 0;
        final SQLiteDatabase sqLiteDatabase = mSqLiteOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case FAVORITE_MOVIES:
                nbDeletedRows = sqLiteDatabase.delete(MovieDataContract.MOVIE_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Failed to delete row at uri: " + uri);
        }

        if(nbDeletedRows > 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return nbDeletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Method not implemented");
    }
}
