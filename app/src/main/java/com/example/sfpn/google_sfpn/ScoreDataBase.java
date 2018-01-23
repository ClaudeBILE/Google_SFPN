package com.example.sfpn.google_sfpn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by alexis on 23/01/18.
 */

public class ScoreDataBase extends SQLiteOpenHelper {
    private static ScoreDataBase sInstance;

    //Database info
    private static final String DATABASE_NAME= "ScoresDataBase";
    private static final int DATABASE_VERSION = 1;

    // Table
    private static final String TABLE_SCORE = "scores";

    //Score table columns
    private static final String KEY_SCORE_POINTS = "points";
    private static final String KEY_SCORE_LEVEL = "level";
    private static final String KEY_SCORE_TIME = "time";

    public ScoreDataBase(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    public static synchronized ScoreDataBase getInstance(Context context){
        if(sInstance == null){
            sInstance = new ScoreDataBase(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE +
                "(" +
                    KEY_SCORE_POINTS + " INT," +
                    KEY_SCORE_LEVEL + " INT," +
                    KEY_SCORE_TIME + " TEXT PRIMARY KEY" +
                ")";
        db.execSQL(CREATE_SCORE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        }
    }

    public void addScore(Score score){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_SCORE_POINTS, score.getPoints());
            values.put(KEY_SCORE_TIME, score.getTimeStamp());
            values.put(KEY_SCORE_LEVEL, score.getNiveauInt());

            db.insertOrThrow(TABLE_SCORE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to add score to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Score> getAllScores(){
        ArrayList<Score> scores = new ArrayList<Score>();

        String SCORES_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_SCORE);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SCORES_SELECT_QUERY,null);
        try {
            if (cursor.moveToFirst()){
                do {
                    Score newScore = new Score(cursor.getInt(cursor.getColumnIndex(KEY_SCORE_POINTS)),
                            cursor.getInt(cursor.getColumnIndex(KEY_SCORE_LEVEL)),
                            cursor.getString(cursor.getColumnIndex(KEY_SCORE_TIME)));
                    scores.add(newScore);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error while trying to get scores from database");
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return scores;
    }

    public void deleteAllScores(){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SCORE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to delete all scores from database");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteScore(Score score){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SCORE, "time=?", new String[]{score.getTimeStamp()});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to delete one score from database");
        } finally {
            db.endTransaction();
        }
    }
}
