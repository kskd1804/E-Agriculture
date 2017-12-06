package com.example.lekha.stickpost;

/**
 * Created by LEKHA on 09-02-2017.
 */

import android.database.Cursor;
import android.database.sqlite.*;
import android.content.*;

import java.io.StringWriter;

public class db  extends SQLiteOpenHelper{

    private static final int dbversion=1;
    private static final  String database="msgsdb";
    private static final String tablename="data";
    private static final String msgid="msgid";
    private static final String loginid="loginid";
    private static final String msg="msg";

    public db(Context context)//, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, database, null, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="CREATE TABLE"+tablename+"("+msgid+"INTEGER PRIMARY KEY AUTOINCREMENT,"+loginid+"integer,"+msg+"text"+")";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists"+tablename);
        onCreate(sqLiteDatabase);
    }

    public void insertion(String msg,int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="insert into"+tablename+"values('"+id+"','"+msg+"')";
        db.execSQL(query);
    }
    public Cursor read() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select"  + msg + " from" + tablename, null);
        return c;
    }
}
