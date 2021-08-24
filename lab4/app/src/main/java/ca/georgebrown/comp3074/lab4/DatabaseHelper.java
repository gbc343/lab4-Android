package ca.georgebrown.comp3074.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+Note.TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note);
        long id = db.insert(Note.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Note note = null;

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
                Note.COLUMN_ID+"=?",
                new String[]{String.valueOf(id)},
                null,null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            note = new Note(
                    cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                    cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
            );
           cursor.close();
        }
        db.close();
        return note;
    }

    public List<Note> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> list = new ArrayList<>();

        String selectQueary = "SELECT * FROM " + Note.TABLE_NAME
                + " ORDER BY " + Note.COLUMN_TIMESTAMP + " DESC";

        Cursor cursor = db.rawQuery(selectQueary, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                        cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
                );
                list.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }

    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getNote());

        int c= db.update(Note.TABLE_NAME, values, Note.COLUMN_ID+"=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return c;
    }

    public int deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Note.TABLE_NAME,
                Note.COLUMN_ID+"=?",
                new String[]{String.valueOf(note.getId())});

    }
}
