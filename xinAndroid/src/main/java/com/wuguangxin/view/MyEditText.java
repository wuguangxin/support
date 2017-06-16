package com.wuguangxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

@SuppressLint("NewApi")
public class MyEditText extends EditText{
	/**
	 * This is a replacement method for the base TextView class' method of the same name. This method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup appears when triggered from the text insertion handle. Returning false forces this window to never appear.
	 * 
	 * @return false
	 */
	boolean canPaste(){
		return false;
	}

	/**
	 * This is a replacement method for the base TextView class' method of the same name. This method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup appears when triggered from the text insertion handle. Returning false forces this window to never appear.
	 * 
	 * @return false
	 */
	@Override
	public boolean isSuggestionsEnabled(){
		return false;
	}

	public MyEditText(Context context){
		super(context);
		init();
	}

	public MyEditText(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}

	private void init(){
		this.setCustomSelectionActionModeCallback(new ActionModeCallbackInterceptor());
		this.setLongClickable(false);
	}

	/**
	 * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing by intercepting the callback that would cause it to be created, and returning false.
	 */
	private class ActionModeCallbackInterceptor implements ActionMode.Callback{
		public boolean onCreateActionMode(ActionMode mode, Menu menu){
			return false;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu){
			return false;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item){
			return false;
		}

		public void onDestroyActionMode(ActionMode mode){}
	}
}