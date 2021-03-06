package com.belajar.newportofolio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "portofolio";
    private static final String TAB_FRIENDS = "friends";

    //    column name
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String BIRTH = "birthday";
    public static final String GENDER = "gender";
    public static final String F_HOME = "home";
    public static final String F_SCH = "school";
    public static final String SOMETEXT = "sometext";
    public static final String PATH = "photo_path";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //    Create Table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "create table " + TAB_FRIENDS + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " + EMAIL + " , " + BIRTH + " TEXT, " +
                GENDER + " TEXT, " + F_HOME + " TEXT, " + F_SCH + " TEXT, " +
                SOMETEXT + " TEXT, " + PATH + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TAB_FRIENDS);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<String> getAllFriend() {
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TAB_FRIENDS, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            list.add(res.getString(Integer.parseInt(String.valueOf(res.getColumnIndex(ID))))+" - "+res.getString(Integer.parseInt(String.valueOf(res.getColumnIndex(NAME)))));
            res.moveToNext();
        }

        return list;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TAB_FRIENDS + " where id = " + id, null);
        return res;
    }

    public boolean addFriend(String name, String email, String birth, String gender, String home, String sch, String txt, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(NAME, name);
        val.put(EMAIL, email);
        val.put(BIRTH, birth);
        val.put(GENDER, gender);
        val.put(F_HOME, home);
        val.put(F_SCH, sch);
        val.put(SOMETEXT, txt);
        val.put(PATH, path);
        db.insert(TAB_FRIENDS, null, val);
        return true;
    }

    public boolean updateFriend(Integer id, String name, String email, String birth, String gender, String home, String sch, String txt, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(NAME, name);
        val.put(EMAIL, email);
        val.put(BIRTH, birth);
        val.put(GENDER, gender);
        val.put(F_HOME, home);
        val.put(F_SCH, sch);
        val.put(SOMETEXT, txt);
        val.put(PATH, path);
        db.update(TAB_FRIENDS, val, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Integer delData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete(TAB_FRIENDS, "id = ?", new String[] {Integer.toString(id)});
        return a;
    }
}
