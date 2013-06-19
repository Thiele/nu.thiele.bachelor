package nu.thiele.bachelor.inputListeners;

import android.view.MotionEvent;
import android.view.View;
import nu.thiele.bachelor.layouts.MyLayout;

public class PanningListener extends MyTouchListener{
	private float fromX, fromY, latestX, latestY;
	private MyLayout layout;
	public PanningListener(MyLayout layout) {
		super(layout);
		this.layout = layout;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			this.fromX = event.getX();
			this.fromY = event.getY();
			this.latestX = this.fromX;
			this.latestY = this.fromY;
		}
		else if((event.getAction() & MotionEvent.ACTION_MOVE) == MotionEvent.ACTION_MOVE){
			float nowX = event.getX();
			float nowY = event.getY();
			float deltaX = (nowX-this.latestX);
			float deltaY = (nowY-this.latestY);
			
			//And update latest information
			this.latestX = nowX;
			this.latestY = nowY;
			
			//And actually pan
			this.layout.pan(-deltaX, -deltaY);
		}
		return true;
	}
}