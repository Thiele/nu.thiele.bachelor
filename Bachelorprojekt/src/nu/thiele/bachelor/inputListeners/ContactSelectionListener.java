package nu.thiele.bachelor.inputListeners;

import android.view.MotionEvent;
import android.view.View;
import nu.thiele.bachelor.layouts.MyLayout;

public class ContactSelectionListener extends MyTouchListener{
	private MyLayout layout;
	private boolean clicked = false;
	
	public ContactSelectionListener(MyLayout layout) {
		super(layout);
		this.layout = layout;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		View item = this.layout.getViewAt(event.getX(), event.getY());
		if(item != null && !this.clicked){ //If something found, perform selection, if no selection is already made
			item.performClick();
			this.clicked = true;
		}
		return true;
	}

}
