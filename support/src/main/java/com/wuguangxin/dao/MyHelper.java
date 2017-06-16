package com.wuguangxin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wuguangxin.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MyHelper {
	public static final String TAG = "MyHelper";
    public static final String DB_NAME = "xin_city.db";
    private SQLiteDatabase db;
	private File file;
	private Context context;

    public MyHelper(Context context) {
    	this.context = context;
    }

    public void openDatabase() {
    	if(file == null){
    		file = new File(context.getCacheDir(), DB_NAME);
    		if(!file.exists() || file.length() == 0){
          		copyAssetsFile(context);
          	}
    	}
      	db = SQLiteDatabase.openOrCreateDatabase(file,null);
    }

    public SQLiteDatabase getDatabase(){
    	if(db == null){
    		openDatabase();
    	}
    	return db;
    }

    public void close() {
    	if(db!=null){
    		db.close();
    	}
    }

    /**
     * 把文件从RAW下复制到data/data/下
     * @param context 上下文
     */
	public void copyAssetsFile(Context context){
		try {
			InputStream is = null;
			/*
			 * 注意，当db文件处于library工程中assets里时，对复制到data/data/下的复本数据库进行操作会报错，目前未查明原因；
			 * 处理方法是，如果需要把数据库文件放在库工程里，则把db文件移到res下的raw中，即可
			 * context.getAssets().open(filename); // 会报错
			 */
			is = context.getResources().openRawResource(R.raw.xin_city);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}