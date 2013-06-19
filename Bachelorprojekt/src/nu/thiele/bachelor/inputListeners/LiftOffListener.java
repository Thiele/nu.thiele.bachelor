package nu.thiele.bachelor.inputListeners;

import android.view.MotionEvent;
import android.view.View;
import nu.thiele.bachelor.layouts.MyLayout;

public class LiftOffListener extends MyTouchListener{
	private MyLayout layout;
	public LiftOffListener(MyLayout layout) {
		super(layout);
		this.layout = layout;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if((arg1.getAction() & 1) == MotionEvent.ACTION_UP){
			View item = this.layout.getViewAt(arg1.getX(), arg1.getY());
			if(item != null){ //If something found, perform click
				item.performClick();
				return true;
			}
		}
		return false;
	}
}
