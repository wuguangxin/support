package com.wuguangxin.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuguangxin.R;

/**
 * 耗时操作时显示的对话框
 *
 * <p>Created by wuguangxin on 14/8/29 </p>
 */
public class LoadingDialog extends Dialog {
	private Context context;
	private View mView;
	private ImageView mLoadingImage;
	private TextView mLoadingMessage;
	private AnimationDrawable anim;
	private AlphaAnimation alphaAnimation;
	private Window window;
	private WindowManager.LayoutParams lp;
	private String message = "处理中...";

	public LoadingDialog(Context context){
		super(context, R.style.xin_loading_dialog);
		this.context = context;
		initView();
	}

	public LoadingDialog(Context context, String msg) {
		super(context, R.style.xin_loading_dialog);
		this.context = context;
		this.message = msg;
		initView();
	}
	
	// 初始化
	@SuppressLint("InflateParams")
	private void initView() {
		mView = LayoutInflater.from(context).inflate(R.layout.xin_dialog_loading, null);
		mLoadingMessage = (TextView) mView.findViewById(R.id.loading_message);
		mLoadingImage = (ImageView) mView.findViewById(R.id.loading_image);
		mLoadingImage.setBackgroundResource(R.drawable.xin_loading_anim);
		mLoadingMessage.setText(getMessage());
		anim = (AnimationDrawable) mLoadingImage.getBackground();
		alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(500);
		setContentView(mView);
	}
	
	public String getMessage(){
		return message; 
	}

	public void setMessage(String message){
		this.message = message;
		if(mLoadingMessage != null){
			mLoadingMessage.setText(message);
		}
	}

	private void startAnim() {
		// 如果正在转动，先停止，再转动
		if(anim != null && !anim.isRunning()){
			anim.start();
		}
	}

	private void stopAnim() {
		if (anim != null && anim.isRunning()) {
			anim.stop(); // 如果正在播放 停止
			mView.clearAnimation();
		}
	}

	@Override
	public void show() {
		super.show();
		mView.startAnimation(alphaAnimation);
		setAlpha();
		startAnim();
	}

	private void setAlpha() {
		window = getWindow();
		lp = window.getAttributes();
		lp.alpha = 0.8f;
		window.setAttributes(lp);
	}

	@Override
	public void hide() {
		stopAnim();
		super.hide();
	}

	@Override
	public void dismiss() {
		stopAnim();
		super.dismiss();
	}

	@Override
	public void cancel() {
		stopAnim();
		super.cancel();
	}
}
