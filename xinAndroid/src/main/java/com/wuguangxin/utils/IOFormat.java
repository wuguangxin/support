package com.wuguangxin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Bitmap、Drawable、byte[]、InputStream之间的转换工具类
 * 
 */
public class IOFormat{
	private static final String TAG = "IOFormat";
	private static IOFormat tools = new IOFormat();

	public static IOFormat getInstance(){
		if (tools == null) {
			tools = new IOFormat();
		}
		return tools;
	}

	/**
	 * 将byte[]转换成InputStream
	 * @param b
	 * @return
	 */
	public InputStream byte2InputStream(byte[] b){
		return new ByteArrayInputStream(b);
	}

	/**
	 * 将InputStream转换成byte[]
	 * @param is
	 * @return
	 */
	public byte[] inputStream2Bytes(InputStream is){
		byte[] readByte = new byte[1024];
		StringBuilder sb = new StringBuilder();
		try {
			while (is.read(readByte, 0, 1024) != -1) {
				sb.append(new String(readByte));
			}
			return sb.toString().getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Bitmap转换成InputStream
	 * @param bm
	 * @return
	 */
	public InputStream bitmap2InputStream(Bitmap bm){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, out); // 参数2是压缩质量0-100，如果是PNG格式，将忽略压缩质量。按无损压缩
		InputStream is = new ByteArrayInputStream(out.toByteArray());
		return is;
	}

	/**
	 * 将Bitmap转换成InputStream,并指定压缩质量
	 * @param bm
	 * @param quality 质量(0-100)
	 * @return
	 */
	public InputStream bitmap2InputStream(Bitmap bm, int quality){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 将InputStream转换成Bitmap
	 * @param is
	 * @return
	 */
	public Bitmap inputStream2Bitmap(InputStream is){
		if (is == null) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			android.util.Log.i(TAG, e.toString());
		}
		return bitmap;
	}

	/**
	 * Drawable转换成InputStream
	 * @param d
	 * @return
	 */
	public InputStream drawable2InputStream(Drawable d){
		if (d == null) {
			return null;
		}
		Bitmap bitmap = this.drawable2Bitmap(d);
		return this.bitmap2InputStream(bitmap);
	}

	/**
	 * InputStream转换成Drawable
	 * @param is
	 * @return
	 */
	public Drawable inputStream2Drawable(InputStream is){
		if (is != null) {
			Bitmap bitmap = inputStream2Bitmap(is);
			return bitmap2Drawable(bitmap);
		}
		return null;
	}

	/**
	 * Drawable转换成byte[]
	 * @param drawable
	 * @return
	 */
	public byte[] drawable2Bytes(Drawable drawable){
		if (drawable != null) {
			Bitmap bitmap = this.drawable2Bitmap(drawable);
			return this.bitmap2Bytes(bitmap);
		}
		return null;
	}

	/**
	 * byte[]转换成Drawable
	 * @param bytes
	 * @return
	 */
	public Drawable bytes2Drawable(byte[] bytes){
		if (bytes != null) {
			Bitmap bitmap = this.bytes2Bitmap(bytes);
			return this.bitmap2Drawable(bitmap);
		}
		return null;
	}

	/**
	 * Bitmap转换成byte[]
	 * @param bitmap
	 * @return
	 */
	public byte[] bitmap2Bytes(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * Bitmap转换成byte[]
	 * @param bitmap
	 * @param quality 质量 （0-100）
	 * @return
	 */
	public byte[] bitmap2Bytes(Bitmap bitmap, int quality){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[]转换成Bitmap
	 * @param b
	 * @return
	 */
	public Bitmap bytes2Bitmap(byte[] b){
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**
	 * Drawable转换成Bitmap
	 * @param drawable
	 * @return
	 */
	public Bitmap drawable2Bitmap(Drawable drawable){
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Config config = Bitmap.Config.RGB_565;
		// 如果drawable不是透明的，则压缩为不透明的
//		if(drawable.getOpacity() != PixelFormat.OPAQUE){
//			config = Bitmap.Config.ARGB_8888;
//		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转换成Drawable
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Drawable bitmap2Drawable(Bitmap bitmap){
		if(bitmap == null) return null;
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 将绑定的URI转换为Bitmap返回
	 * @param uri
	 * @return
	 */
	public Bitmap uri2Bitmap(Context context, Uri uri){
		InputStream is = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
			if (is != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				if (bitmap != null) return bitmap;
				byte[] data = readStream(is);
				if (data.length > 0) {
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					if (bitmap != null) {
						return bitmap;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 生成与原图同样大小的Bitmap，不作压缩
	 * @param imageUrl 图片URL
	 * @return
	 */
	public static Bitmap url2Bitmap(String imageUrl){
		try {
			return BitmapFactory.decodeFile(imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到图片字节流 数组大小
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception{
		int len;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 简单获取网落图片资源
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(final String url){
		if(TextUtils.isEmpty(url)){
			return null;
		}
		InputStream is = null;
		URL imgUrl;
		try {
			imgUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
			// conn.setConnectionTiem(0); // 表示没有时间限制
			conn.setConnectTimeout(6000); // 超时时间
			conn.setDoInput(true); 
			conn.setUseCaches(false); // 不使用缓存
			// conn.connect(); // 这句可有可无，没有影响
			is = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}