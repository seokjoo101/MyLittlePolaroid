package com.example.sj.mylittlepolaroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SJ on 2015-12-05.
 */
public class DbOpenHelper {

    private static final String DATABASE_NAME = "mypolaroid.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;





    private class DatabaseHelper extends SQLiteOpenHelper {

        // ??????
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);

        }

        // ???? DB?? ???? ????? ?????.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);

        }

        // ?????? ??????? ????? ??? DB?? ??? ????? ???.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn( String image, String date, String address, String explain,double x ,double y){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGE, image);
        values.put(DataBases.CreateDB.DATE, date);
        values.put(DataBases.CreateDB.ADDRESS, address);
        values.put(DataBases.CreateDB.EXPLAIN, explain);
        values.put(DataBases.CreateDB.X, x);
        values.put(DataBases.CreateDB.Y, y);
        return mDB.insert(DataBases.CreateDB._TABENAME, null, values);
    }

    // Update DB
    public boolean updateColumn(long id , String image){
        ContentValues values = new ContentValues();
//        values.put(DataBases.CreateDB.MOK, mok);
//        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.IMAGE, image);
//        values.put(DataBases.CreateDB.ADDRESS, address);
//        values.put(DataBases.CreateDB.FEATURE, feature);
        return mDB.update(DataBases.CreateDB._TABENAME, values, "_id="+id, null) > 0;
    }

    // Delete ID
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABENAME, "_id=" + id, null) > 0;
    }

    // Delete Contact
    public boolean deleteColumn(String number){
        return mDB.delete(DataBases.CreateDB._TABENAME, "contact="+number, null) > 0;
    }

    // Select All
    public Cursor getAllColumns(){
        return mDB.query(DataBases.CreateDB._TABENAME, null,null,null, null, null, null, null);
    }

    // ID �÷� ��� ����
    public Cursor getColumn(long id){
        Cursor c = mDB.query(DataBases.CreateDB._TABENAME, null,
                "_id="+id, null,null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // �̸� �˻� �ϱ� (rawQuery)
//    public Cursor getMatchName(String name){
//        Cursor c = mDB.rawQuery("select * from address where name=" + "'" + name + "'", null);
//        return c;
//    }


    // ���� �˻� �ϱ�
    public Cursor getMatchAll(String explain){
        Cursor c = mDB.rawQuery("select * from polaroid where explain LIKE" + "'%" + explain + "%' OR address LIKE" + "'%" + explain + "%'", null);
        return c;
    }
    public Cursor getMatchAddress(String explain){
        Cursor c = mDB.rawQuery("select * from polaroid where address LIKE" + "'%" + explain + "%'", null);
        return c;
    }
    public Cursor getMatchMemo(String explain){
        Cursor c = mDB.rawQuery("select * from polaroid where explain LIKE" + "'%" + explain + "%'", null);
        return c;
    }

    //DB ����
    public void delete(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABENAME);

    }


}
