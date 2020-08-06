package com.wuguangxin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 继承SQLiteOpenHelper类
public class TestDaoHelper extends SQLiteOpenHelper {
    private final String DB_NAME = "bankers.db"; // 数据库名称
    private final int DB_VERSION = 1;  // 数据库版本

    /**
     * Context 上下文
     * name 数据库名
     * CursorFactory 游标工厂模式 当为null时 使用默认值
     * version 数据库版本 版本号从1开始
     */
    public TestDaoHelper(Context context) {
        super(context, "bankers.db", null, 1);
    }

    /**
     * 创建数据库。
     * 1、在第一次打开数据库的时候才会走
     * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
     * 3、没有清除数据，不会走这个方法
     * 4、数据库升级的时候这个方法不会走
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构
        String sql = "create table transinfo(_id integer primary key autoincrement,uno varchar(20),bno varchar(20),bnum int(10))";
        db.execSQL(sql);

    }

    /**
     * 数据库升级。
     * 1、第一次创建数据库的时候，这个方法不会走
     * 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
     * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 数据库降级。
     * 1、只有新版本比旧版本低的时候才会执行
     * 2、如果不执行降级操作，会抛出异常
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

}