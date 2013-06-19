package nu.thiele.bachelor.inputListeners;

import java.util.Calendar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import nu.thiele.bachelor.layouts.MyLayout;

public class TapListener extends MyTouchListener{
	/*
	 * Settings
	 */
	private static final long MAX_ACTIVATION_TIME = 300; //Only allow click if less than 300ms have passed since touch down and up
	/*
	 * 
	 */
	private float touchX = Float.MAX_VALUE, touchY = Float.MAX_VALUE;
	private long touchTime = 0;
	private MyLayout layout;
	
	public TapListener(MyLayout layout) {
		super(layout);
		this.layout = layout;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//If event is touch, store the touch time and coordinates
		if(event.getAction() == MotionEvent.ACTION_DOWN){ //No need for AND operation since action_down is 0 
			this.touchTime = Calendar.getInstance().getTimeInMillis();
			this.touchX = event.getX();
			this.touchY = event.getY();
		}
		else if((event.getAction() & MotionEvent.ACTION_UP) == MotionEvent.ACTION_UP){
			if(Calendar.getInstance().getTimeInMillis()-this.touchTime < MAX_ACTIVATION_TIME){
				View item = this.layout.getViewAt(touchX, touchY);
				if(item != null){ //If something found, perform tap
					item.performClick();
				}
			}
		}
		return false;
	}
}
