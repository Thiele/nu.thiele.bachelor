package nu.thiele.bachelor.inputListeners;

import java.util.Calendar;
import java.util.LinkedList;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import nu.thiele.bachelor.advancedLayouts.Stack;
import nu.thiele.bachelor.layouts.MyLayout;
import nu.thiele.bachelor.util.DefaultValues;
import nu.thiele.bachelor.util.Shared;

public class StackFlipListener extends MyTouchListener{
	public static InteractionMethod METHOD = InteractionMethod.valueOf(DefaultValues.STACK_INTERACTION_METHOD); //Default interaction method
	private MyLayout layout;
	private int orientation = 0; //-1 = decreasing touch size, 0 = same, 1 = increasing touch size
	private int touchesToStore = 50; //How many to store in list
	private float latestAngle = Float.MAX_VALUE, latestSize = Float.MAX_VALUE, latestX = Float.MAX_VALUE, latestY = Float.MIN_VALUE;
	private float flipSpeed = 0.001f; //How much of the layer to flip through at once
	private float min, max;
	private float size = 0;
	private long lastTouchTime = Long.MAX_VALUE;
	
	public StackFlipListener(MyLayout layout, float min, float max) {
		super(layout, min, max);
		this.layout = layout;
		this.max = max;
		this.min = min;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		long now = Calendar.getInstance().getTimeInMillis();
		this.size = Shared.SURFACEINFORMATION.getComparedTouchRatio(event);
		float x = event.getX();
		float y = event.getY();
				
		//Delta values
		float deltaSize = size-this.latestSize;
		float deltaX = x-this.latestX;
		float deltaY = y-this.latestY;

		//If first, store as first point, return true
		if(this.latestX == Float.MAX_VALUE){
			this.lastTouchTime = now;
			this.orientation = 1;
			this.latestSize = size;
			this.latestX = x;
			this.latestY = y;
			return true;
		}

		switch(METHOD){
			case ANGULAR:
				this.flipSpeed = 3f;

				//Set suitable flipspeed
				if(Stack.DISCRETE_FLIPPING) this.flipSpeed = 1.5f;
				else if(!Stack.LINEAR_CONTINUOUS_FLIPPING) this.flipSpeed = 5f;
				else this.flipSpeed = 0.8f;

				
				//Set origin for touch
				DisplayMetrics display = this.layout.getLayout().getResources().getDisplayMetrics();
				float originX = 0, originY = 0;
				if(Shared.PREFERREDHAND.equals(Shared.Hand.LEFT)){
					originY = display.heightPixels;
				}
				else if(Shared.PREFERREDHAND.equals(Shared.Hand.RIGHT)){
					originX = display.widthPixels;
					originY = display.heightPixels;
				}
				
				//Now, calculate angle from origin, if the finger has moved
				//This is done using law of cosines
				float difX = Math.abs(x-originX);
				float difY = Math.abs(y-originY);
				
				float a = difX;
				float b = difY;
				float c = (float) Math.sqrt(a*a+b*b);
				float currentAngle = (float) Math.acos((b*b+c*c-a*a)/(2*b*c));
				
				//If first touch, save angle and break
				if(this.latestAngle == Float.MAX_VALUE){
					this.latestAngle = currentAngle;
					break;
				}
				
				//Angle difference. Find absolute and multiply by some direction variable
				float angleDifference = currentAngle-this.latestAngle;
				
				//Calculate and update latest angle
				this.latestAngle = currentAngle;
				
				//And move through stack
				this.layout.moveThroughStack(-angleDifference*this.flipSpeed);
				
				break;
			case SURFACE_SIZE:
				if(Stack.DISCRETE_FLIPPING) this.flipSpeed = 0.05f;
				else if(!Stack.LINEAR_CONTINUOUS_FLIPPING) this.flipSpeed = 0.15f;
				else this.flipSpeed = 0.025f;
				
				float absDeltaSize = Math.abs(deltaSize);
				float activationThreshold = 0.001f; //Seems like good threshold
				//And orientation
				int nowOrientation = 0;
				if(deltaSize > 0) nowOrientation = 1;
				else if(deltaSize < 0) nowOrientation = -1;
				
				if(absDeltaSize >= activationThreshold && nowOrientation == this.orientation && (event.getAction() & MotionEvent.ACTION_UP) != MotionEvent.ACTION_UP){
					this.layout.moveThroughStack(this.flipSpeed * (deltaSize > 0 ? 1 : -1));
				}
				break;
			case SURFACE_SIZE_STEADY:
				//Only move if event is movement
				if((event.getAction() & MotionEvent.ACTION_MOVE) == MotionEvent.ACTION_MOVE){
					float flipSpeed = 1;
					if(Stack.DISCRETE_FLIPPING) flipSpeed = 0.025f; //If discrete
					else if(Stack.LINEAR_CONTINUOUS_FLIPPING) flipSpeed = 0.02f; //If linear
					else flipSpeed = 0.13f; //If non-linear
					//And move through stack
					layout.moveThroughStack(flipSpeed * (size > (this.max+this.min)*0.5 ? -1 : 1));
				}
				
				break;
			case X_AXIS:
				if(Stack.DISCRETE_FLIPPING) this.flipSpeed = 0.02f;
				else if(Stack.LINEAR_CONTINUOUS_FLIPPING) this.flipSpeed = 0.02f;
				else this.flipSpeed = 0.04f;
				if(Math.abs(deltaX) >= 0.02){//If at least 2%-point increase
					this.layout.moveThroughStack(this.flipSpeed * (deltaX > 0 ? 1 : -1));
				}
				break;
		}
		
		
		//and update latest info to current
		//Find orientation
		if(size < this.latestSize) orientation = -1;
		else if(size > this.latestSize) orientation = 1;
		else orientation = 0;
		
		this.latestSize = size;
		this.latestX = x;
		this.latestY = y;
		return true;
	}
	
	/*
	 *	Enum used to decide which way to interact 
	 */
	public enum InteractionMethod{
		ANGULAR, //Moving in a circular motion with thumb origin as center
		SURFACE_SIZE, //
		SURFACE_SIZE_STEADY,
		X_AXIS //Moving along x-axis
	}
}