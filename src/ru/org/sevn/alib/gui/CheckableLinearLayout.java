/*******************************************************************************
 * Copyright 2016 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ru.org.sevn.alib.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CheckableLinearLayout extends LinearLayout implements Checkable, android.widget.CompoundButton.OnCheckedChangeListener {

    /**
     * Interface definition for a callback to be invoked when the checked state of this View is
     * changed.
     */
    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkableView The view whose state has changed.
         * @param isChecked     The new checked state of checkableView.
         */
        void onCheckedChanged(View checkableView, boolean isChecked);
    }
    
    public boolean performClick() {
    	boolean ret = super.performClick();
   		performClick4ListView();
    	return ret;
    }
    
    private void performClick4ListView() {
    	CheckableLinearLayout v = this;
    	if (v.getParent() != null && v.getParent() instanceof ListView) {
			ListView parent = (ListView)v.getParent();
			int position = parent.getPositionForView(this);
			if (position >= 0) {
				parent.performItemClick(v, position, parent.getAdapter().getItemId(position));
			}
    	}    	
    }

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean mChecked = false;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        CheckBox checkBox = new CheckBox(context);
        checkBox.setChecked(isChecked());
        checkBox.setDuplicateParentStateEnabled(true);
        addView(checkBox);
        CheckBox.OnCheckedChangeListener l = new CheckBox.OnCheckedChangeListener() {
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		setChecked(isChecked);
        	}
        };
        checkBox.setOnCheckedChangeListener(l);
    }
    
    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		setChecked(isChecked);
	}

}
