package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wuguangxin.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 上下切换滚动的TextView
 *
 * @author wuguangxin
 * @date: 2016-3-2 下午3:18:18
 */
@SuppressLint("HandlerLeak")
public class NoticeView extends TextSwitcher implements ViewSwitcher.ViewFactory, android.view.View.OnClickListener{
	private static final int DEFAULT_TEXT_COLOR = Color.BLACK; // 文字颜色
	private static final float DEFAULT_TEXT_SIZE = 24; // 文字大小
	private static final long DEFAULT_ANIM_DURATION = 500; // 动画延时
	private static final String DEFAULT_TEXT = "暂无信息"; // 动画延时
	private static long SCROLL_DURATION = 5000; // 滚动延时
	private Context mContext;
	private Animation mTopIn;
	private Animation mTopOut;
	private Animation mBottomIn;
	private Animation mBottomOut;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private TextAdapter mAdapter;
	private CharSequence mDefaultText = DEFAULT_TEXT; // 默认显示的文本
	private long mScrollDuration = SCROLL_DURATION; // 滚动延时
	private long mAnimDuration = DEFAULT_ANIM_DURATION; // 动画延时
	private boolean isRunning;
	private int mIndex; // 自增号
	private int mCount; // 数据总数
	private int mPosition; // 当前位置
	private int mAnimMode; // 动画模式
	private int mOrientation; // 滚动方向
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if(msg.what == 0){
				scroll();
			}
		}
	};

	public NoticeView(Context context){
		this(context, null);
	}

	public NoticeView(Context context, AttributeSet attrs){
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init(){
		setFactory(this);
		setOnClickListener(this);
		initAnim();
		initTimeTask();
	}

	private void initAnim(){
		if(mAnimMode == 0){
			mTopIn = AnimationUtils.loadAnimation(mContext, R.anim.xin_anim_top_in);
			mTopOut = AnimationUtils.loadAnimation(mContext, R.anim.xin_anim_top_out);
			mBottomIn = AnimationUtils.loadAnimation(mContext, R.anim.xin_anim_bottom_in);
			mBottomOut = AnimationUtils.loadAnimation(mContext, R.anim.xin_anim_bottom_out);
		} else {
			mTopIn = createAnim(90, 0 , true , false);
			mTopOut = createAnim(0, 90, false, true);
			mBottomIn = createAnim(-90, 0 , true, true);
			mBottomOut = createAnim(0, -90, false, false);
		}
	}
	
	/**
	 * 设置切换动画模式（2D或3D效果）
	 * @param animMode
	 */
	public void setAnimMode(int animMode){
		this.mAnimMode = animMode;
		initAnim();
	}
	
	/**
	 * 设置滚动方向
	 * @param orientation
	 */
	public void setOrientation(int orientation){
		this.mOrientation = orientation;
	}
	
	public static interface AnimMode {
		/** 2D平移 */
		int MODE_2D = 0;
		/** 3D翻转 */
		int MODE_3D = 1;
	}
	
	public static interface Orientation {
		/** 由上至下滚动 */
		int DOWN = 0;
		/** 由下至上滚动 */
		int TOP = 1;
	}
	
	private void initTimeTask(){
		mIndex = 0;
		mPosition = 0;
		isRunning = true;
		mTimerTask = new TimerTask(){
			@Override
			public void run(){
				if(isRunning){
					mHandler.sendEmptyMessage(0);
				}
			}
		};
		mTimer = new Timer(true);
		mTimer.schedule(mTimerTask, mScrollDuration, mScrollDuration);
	}
	
	/**
	 * 滚动
	 */
	private void scroll(){
		if(mAdapter != null){
			int count = mAdapter.getCount();
			if(count != mCount){
				notifyDataChangeed();
				mCount = count;
			}
			if(mCount <= 0){
				setCurrentText(mDefaultText);
				return;
			}
			mPosition = mIndex++ % mCount;
			setText(mAdapter.getText(mPosition));
			startAnim();
		}
	}
	
	/**
	 * 启动
	 */
	public void start(){
		isRunning = true;
	}
	
	/**
	 * 暂停
	 */
	public void pause(){
		isRunning = false;
	}
	
	/**
	 * 销毁定时器
	 */
	public void cancel(){
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
		mTimerTask = null;
		isRunning = false;
		mIndex = 0;
	}
	
	public void next(){
		scroll();
	}
	
	@Override
	public void onClick(View v){
		if(mListener != null){
			mListener.onItemClick(mAdapter, NoticeView.this, mPosition);
		}
	}
	
	public CharSequence getDefaultText(){
		return mDefaultText;
	}

	public void setDefaultText(CharSequence text){
		this.mDefaultText = text;
	}
	
	public void setAdapter(TextAdapter textAdapter){
		this.mAdapter = textAdapter;
		reset();
		if(mAdapter != null){
			removeAllViews();
			addView(mAdapter.getView());
			addView(mAdapter.getView());
		}
	}
	
	public TextAdapter getAdapter(){
		return mAdapter;
	}

	public static interface TextAdapter{
		Object getData();
		int getCount();
		CharSequence getText(int position);
		TextView getView();
	}
	
	private OnItemClickListener mListener;
	
	public void setOnItemClickListener(OnItemClickListener listener){
		this.mListener = listener;
	}
	
	public static interface OnItemClickListener{
		void onItemClick(TextAdapter adapter, NoticeView view, int position);
	}
	
	private void notifyDataChangeed(){
		super.reset();
		mIndex = 0;
		mPosition = 0;
	}
	
	public int getPosition(){
		return mIndex;
	}
	
	//这里返回的TextView，就是我们看到的View，改方法只执行两次
	@Override
	public View makeView(){
		TextView view = new TextView(mContext);
		view.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
		view.setGravity(Gravity.CENTER_VERTICAL);
		view.setTextSize(px2dip(DEFAULT_TEXT_SIZE));
		view.setTextColor(DEFAULT_TEXT_COLOR);
		int px = dip2px(15);
		view.setPadding(px, 0, px, 0);
		view.setMaxLines(1);
		return view;
	}

	/**
	 * 执行切换动画
	 */
	private void startAnim(){
		if(mOrientation == Orientation.DOWN){
			setInAnimation(mTopIn);
			setOutAnimation(mBottomOut);
		} else {
			setInAnimation(mBottomIn);
			setOutAnimation(mTopOut);
		}
	}
	
	private int px2dip(float pxValue){
		float scale = mContext.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	
	private int dip2px(float dipValue){
		float density = mContext.getResources().getDisplayMetrics().density;
		return (int)(dipValue * density + 0.5f);
	}
	
	private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp){
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
		rotation.setDuration(mAnimDuration);
		rotation.setFillAfter(false);
		rotation.setInterpolator(new AccelerateInterpolator());
		return rotation;
	}
	
	class Rotate3dAnimation extends Animation{
		private final float mFromDegrees;
		private final float mToDegrees;
		private float mCenterX;
		private float mCenterY;
		private final boolean mTurnIn;
		private final boolean mTurnUp;
		private Camera mCamera;

		public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp){
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mTurnIn = turnIn;
			mTurnUp = turnUp;
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight){
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			mCenterY = getHeight() / 2;
			mCenterX = getWidth() / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t){
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final int derection = mTurnUp ? 1 : -1;
			final Matrix matrix = t.getMatrix();
			camera.save();
			if (mTurnIn) {
				camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
			} else {
				camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
			}
			camera.rotateX(degrees);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
}