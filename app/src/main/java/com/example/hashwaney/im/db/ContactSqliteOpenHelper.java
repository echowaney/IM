package com.example.hashwaney.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactSqliteOpenHelper
        extends SQLiteOpenHelper
{
    public static final String CONTACTDB = "contact.db";
    public static final int VERSION      = 1;
    public static final String T_CONTACT = "t_contact"; //表名
    public static final String USERNAME  = "username"; //用户
    public static final String CONTACT      = "contact";//用户联系人

    public ContactSqliteOpenHelper(Context context)
    {
        super(context, CONTACTDB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table " + T_CONTACT + "(_id integer primary key," + USERNAME + " varchar(20)," + CONTACT + " varchar(20))");
        String sql ="create table "+ T_CONTACT + "(_id integer primary key autoincrement, " + USERNAME + " varchar(20),"+ CONTACT + " varchar(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
