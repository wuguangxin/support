package com.wuguangxin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDao {

    private TestDaoHelper helper;

    public TestDao(Context context) {
        helper = new TestDaoHelper(context);
    }

    // 添加一条记录,返回id
    public int add(TestBean t) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String insertSql = "insert into transinfo(uno,bno,bnum) values(?,?,?)";
        db.execSQL(insertSql, new Object[]{t.getUno(), t.getBno(), t.getBnum()});

        String selectSql = "select last_insert_rowid() from transinfo";
        Cursor cursor = db.rawQuery(selectSql, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        return id;
    }

    // 修改一条记录
    public void update(TestBean t) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "update transinfo set uno=?,bno=?,bnum=? where _id=?";
        db.execSQL(sql, new Object[]{t.getUno(), t.getBno(), t.getBnum()});

    }

    // 删除一条记录
    public void delete(int id) {
        if (id < 0) {
            return;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "delete from transinfo where _id=?";
        db.execSQL(sql, new Object[]{id});
    }

    // 根据id查找信息
    public TestBean findById(int _id) {
        if (_id < 0) {
            return null;
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from transinfo where _id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{_id + ""});
        TestBean t = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String uno = cursor.getString(cursor.getColumnIndex("uno"));
            String bno = cursor.getString(cursor.getColumnIndex("bno"));
            String bnum = cursor.getString(cursor.getColumnIndex("bnum"));
            t = new TestBean(id, uno, bno, bnum);
        }
        return t;
    }


    // 根据指定条件查询所有数据
    public List<TestBean> findAll(String uno) {
        List<TestBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(uno)) {
            return list;
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from transinfo where uno=?";
        Cursor cursor = db.rawQuery(sql, new String[]{uno});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String _uno = cursor.getString(cursor.getColumnIndex("uno"));
            String bno = cursor.getString(cursor.getColumnIndex("bno"));
            String bnum = cursor.getString(cursor.getColumnIndex("bnum"));
            TestBean t = new TestBean(id, _uno, bno, bnum);
            list.add(t);
        }
        return list;
    }


}