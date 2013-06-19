package nu.thiele.bachelor.inputListeners;

import nu.thiele.bachelor.layouts.MyLayout;
import android.view.MotionEvent;
import android.view.View;

public class ZoomListener extends MyTouchListener{
	private float scaleIncrement = 0.04f;
	float firstX = Float.MAX_VALUE, firstY = Float.MAX_VALUE, latestX = Float.MAX_VALUE;
	private MyLayout layout;
	
	public ZoomListener(MyLayout layout){
		super(layout);
		this.layout = layout;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		//If first touch, store this point as the pivot for zooming
		if(this.firstX == Float.MAX_VALUE){
			this.firstX = arg1.getX();
			this.firstY = arg1.getY();
		}
		//If thumb movement
		if((arg1.getAction()&MotionEvent.ACTION_MOVE) == MotionEvent.ACTION_MOVE){
			//If first touch, store and do nothing
			if(this.latestX == Double.MAX_VALUE){
				this.latestX = arg1.getX();
				return true;
			}
			
			//Store new information
			double lX = this.latestX;
			this.latestX = arg1.getX();
			//Otherwise, some movement
			if(arg1.getX()-lX > 0){ //Should increase
				return this.layout.zoom(this.scaleIncrement, this.firstX, this.firstY);
			}
			else if(arg1.getX()-lX < 0){//Should decrease
				//When zooming out this should be around center of screen
				//Otherwise zooming out becomes ugly
				if(this.layout.getZoomLevel() > 1+this.scaleIncrement) return this.layout.zoom(-this.scaleIncrement, this.layout.getLayout().getWidth()/2, this.layout.getLayout().getHeight()/2);
			}
		}
		return true;
	}
}