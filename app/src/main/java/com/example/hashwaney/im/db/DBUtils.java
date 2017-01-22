package com.example.hashwaney.im.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HashWaney on 2017/1/21.
 */

public class DBUtils {
    private static Context sContext;
    private static boolean init;

    //进行数据库的初始化。不然会获取不到数据，因为使用该方法的前提是要数据库中有数据，而该数据应该是先从网络上获取，然后才能进行获取和更新数据中的数据


    public static void initDB(Context context) {
        sContext = context.getApplicationContext();
        //给一个标志位
        init = true;
    }


    //获取数据库中的联系人

    /**
     *根据登陆用户来获取当前用户的联系人
     */

    public static List<String> getContact(String username) {
        //判断一下是否已经进行了数据库的初始化,不然没有数据可以进行查询
        if (!init){

            throw new RuntimeException("数据库还没有进行初始化,暂无数据可以查询");
        }


        ContactSqliteOpenHelper openHelper = new ContactSqliteOpenHelper(sContext);
        SQLiteDatabase          database   = openHelper.getReadableDatabase();
//        database.beginTransaction();
        Cursor cursor = database.query(ContactSqliteOpenHelper.T_CONTACT,
                                      new String[]{ContactSqliteOpenHelper.CONTACT},
                                      ContactSqliteOpenHelper.USERNAME + " =?",
                                      new String[]{username},
                                      null,
                                      null,
                                      ContactSqliteOpenHelper.CONTACT);
        ArrayList<String> contactLists =new ArrayList<>();
        if (cursor !=null){
            while(cursor.moveToNext()){
                String contact = cursor.getString(0);
                contactLists.add(contact);
            }
            cursor.close();
        }
//        database.endTransaction();
//        database.setTransactionSuccessful();
        database.close();

        return contactLists;
    }


    //更新数据库中联系人
    /**
     * 1. 首先更新之前,要进行数据的清空,保证拿到的是最新的数据
     * 2. 在清空之后,将返回的数据contactList写入到数据库中
     */

    public static void updateContact(String username ,List<String> contactList){
        ContactSqliteOpenHelper openHelper = new ContactSqliteOpenHelper(sContext);
        SQLiteDatabase          writableDatabase = openHelper.getWritableDatabase();

        //这里不需要去判断了,因为数据已经传回了,说明数据库是已经进行了初始化的
        //为什么put两次,因为获取到的数据是当前用户的联系人,在put之前进行了数据的清空,首先需要将这个用户放入到表中,然后,在将联系人放入到对应的列
        //开启一个事务
        writableDatabase.beginTransaction();

        writableDatabase.delete(ContactSqliteOpenHelper.T_CONTACT,ContactSqliteOpenHelper.USERNAME+"=?",new String[]{username});
        ContentValues values =new ContentValues();
        values.put(ContactSqliteOpenHelper.USERNAME,username);
        for (int i = 0; i <contactList.size() ; i++) {
            //插入数据
            values.put(ContactSqliteOpenHelper.CONTACT,contactList.get(i));
            writableDatabase.insert(ContactSqliteOpenHelper.T_CONTACT, null, values);

        }
//
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        //关闭数据库
        writableDatabase.close();

    }




}
