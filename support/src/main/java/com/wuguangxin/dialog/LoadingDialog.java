package com.wuguangxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.wuguangxin.R;

/**
 * 耗时操作时显示的对话框
 *
 * <p>Created by wuguangxin on 14/8/29 </p>
 */
public class LoadingDialog extends Dialog {
	private final float DEF_ALPHA = 0.0f;
	private Context context;
	private View mView;
	private ImageView mLoadingImage;
	private AnimationDrawable anim;
	private AlphaAnimation alphaAnimation;
	private WindowManager.LayoutParams lp;
//	private TextView mLoadingMessage;
//	private String message = "处理中...";

	public LoadingDialog(Context context){
		super(context, R.style.xin_loading_dialog);
		this.context = context;
		initView();
	}

//	public LoadingDialog(Context context, String msg) {
//		super(context, R.style.xin_loading_dialog);
//		this.context = context;
//		this.message = msg;
//		initView();
//	}

	private void initView() {
		mView = LayoutInflater.from(context).inflate(R.layout.xin_dialog_loading, null);
		mLoadingImage = mView.findViewById(R.id.loading_image);
		mLoadingImage.setImageResource(R.drawable.loading); //  R.drawable.xin_loading_anim 普通转圈
//		mLoadingMessage = mView.findViewById(R.id.loading_message);
//		mLoadingMessage.setText(getMessage());
		anim = (AnimationDrawable) mLoadingImage.getDrawable();
		alphaAnimation = new AlphaAnimation(0f, 1f);
		alphaAnimation.setDuration(500);
		setCancelable(false);
		setContentView(mView);
	}

//	public String getMessage(){
//		return message;
//	}

//	public void setMessage(String message){
//		this.message = message;
//		if(mLoadingMessage != null){
//			mLoadingMessage.setText(message);
//		}
//	}

	private void startAnim() {
		// 如果未转动，就转动
		if(anim != null && !anim.isRunning()){
			anim.start();
		}
	}

	private void stopAnim() {
		// 如果正在播放 停止
		if (anim != null && anim.isRunning()) {
			anim.stop();
			mView.clearAnimation();
		}
	}

	@Override
	public void show() {
//		mView.startAnimation(alphaAnimation);
		setAlpha(DEF_ALPHA);
		startAnim();
		super.show();
	}

	public void setVisible(boolean visible) {
		if (visible) {
			show();
		} else {
			dismiss();
		}
	}

	/**
	 * 设置窗体透明度（默认0.8f）
	 * @param alpha 透明度
	 */
	public void setAlpha(float alpha) {
		Window window = getWindow();
		if (window != null) {
			lp = window.getAttributes();
			lp.alpha = alpha;
			window.setAttributes(lp);
		}
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
