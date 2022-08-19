package com.wuguangxin.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

/**
 * 本地验证码工具类
 *
 * <p>Created by wuguangxin on 14/4/14 </p>
 */
public class VerifyCodeUtil{
	// 默认数据
	private static final int DEFAULT_CODE_LENGTH = 4;
	private static final int DEFAULT_FONT_SIZE = 20;
	private static final int DEFAULT_LINE_NUMBER = 4; // 干扰线条数
	private static final int DEFAULT_WIDTH = 80;
	private static final int DEFAULT_HEIGHT = 45;
	private static final int BASE_PADDING_LEFT = 10;
	private static final int BASE_PADDING_TOP = 20;
	private static final int RANGE_PADDING_LEFT = 10;
	private static final int RANGE_PADDING_TOP = 10;
	// 图片宽高
	private static int width = DEFAULT_WIDTH;
	private static int height = DEFAULT_HEIGHT;
	// 随机数间隔和顶部距离
	private static int base_padding_left = BASE_PADDING_LEFT;
	private static int base_padding_top = BASE_PADDING_TOP;
	private static int range_padding_left = RANGE_PADDING_LEFT;
	private static int range_padding_top = RANGE_PADDING_TOP;
	// 数字大小和字体
	private static int codeLength = DEFAULT_CODE_LENGTH;
	private static int line_number = DEFAULT_LINE_NUMBER;
	private static int font_size = DEFAULT_FONT_SIZE;
	private static int padding_left, padding_top;
	private static Random random = new Random();
	private static boolean isRunning = false;
	private static final char[] CHARS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

//	 private static final char[] CHARS = {
//		 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//		 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//		 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
//	 };
	/**
	 * 创建图片验证码
	 * @param callBackHandler 回调
	 */
	public static void createImage(CallBackHandler callBackHandler){
		createImage(getRandomCode(), callBackHandler);
	}

	/**
	 * 创建图片验证码
	 * @param code 自己传入的验证码字符串
	 * @param callBackHandler 回调
	 */
	public static void createImage(String code, CallBackHandler callBackHandler){
		createBitmap(code, callBackHandler);
	}

	final private static synchronized void createBitmap(final String code, final CallBackHandler callBackHandler){
		if (isRunning) {
			Log.w("LOG", "上一次任务未结束");
			return;
		}
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					isRunning = true;
					padding_left = 0;
					Bitmap verifyCodeImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
					Canvas canvas = new Canvas(verifyCodeImage);
					canvas.drawColor(Color.parseColor("#00000000"));
					Paint paint = new Paint();
					paint.setTextSize(font_size);
					for (int i = 0; i < code.length(); i++) {
						randomTextStyle(paint);
						randomPadding();
						canvas.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
					}
					for (int i = 0; i < line_number; i++) {
						drawLine(canvas, paint);
					}
//					canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
					canvas.save();// 保存
					canvas.restore();// 恢复
					Object[] objs = {
						verifyCodeImage, code
					};
					Message msg = new Message();
					msg.what = 0;
					msg.obj = objs;
					callBackHandler.sendMessage(msg);
				} catch (Exception e) {
					isRunning = false;
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 获取随机字符串
	 * @return 随机字符串
	 */
	final public static String getRandomCode(){
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}

	/**
	 * 画线
	 * @param canvas 画板
	 * @param paint 画笔
	 */
	final private static void drawLine(Canvas canvas, Paint paint){
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	/**
	 * 随机颜色
	 * @return 颜色
	 */
	final private static int randomColor(){
		return randomColor(1);
	}

	/**
	 * 随机颜色
	 * @param rate int
	 * @return 颜色
	 */
	private static int randomColor(int rate){
		int red = random.nextInt(150) / rate;
		int green = random.nextInt(150) / rate;
		int blue = random.nextInt(150) / rate;
		return Color.rgb(red, green, blue);
	}

	/**
	 * 随机文字样式
	 * @param paint Paint
	 */
	final private static void randomTextStyle(Paint paint){
		int color = randomColor();
		paint.setColor(color);
		// paint.setFakeBoldText(random.nextBoolean()); //随机boolean值
		// true为粗体，false为非粗体
		paint.setFakeBoldText(true); // true为粗体，false为非粗体
		float skewX = random.nextInt(11) / 10; // X轴倾斜度
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX); // 设置文字倾斜方向 float类型参数，负数表示右斜，整数左斜
		// paint.setUnderlineText(true); //true为下划线，false为非下划线
		// paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}

	/**
	 * 随机Padding
	 */
	final private static void randomPadding(){
		padding_left += base_padding_left + random.nextInt(range_padding_left);
		padding_top = base_padding_top + random.nextInt(range_padding_top);
	}

	/**
	 * 创建验证码回调
	 *
	 * <p>Created by wuguangxin on 15/3/19 </p>
	 */
	public static class CallBackHandler extends Handler{
		@Override
		final public void handleMessage(Message msg){
			super.handleMessage(msg);
			if (msg.what == 0) {
				Object[] objs = (Object[]) msg.obj;
				Bitmap img = (Bitmap) objs[0];
				String code = (String) objs[1];
				isRunning = false;
				onCreate(img, code);
			}
		}

		/**
		 * 当创建验证码图片成功时回调
		 * @param verifyImg 返回的验证码图片
		 * @param code 验证码字符串
		 */
		public void onCreate(Bitmap verifyImg, String code){};
	}
}