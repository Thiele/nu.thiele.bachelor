package nu.thiele.bachelor.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;

public class TouchSurfaceHelper {
	//Keys used for the preferences only in here
	private static final String KEY_MAX = "TOUCH_MAX", KEY_MIN = "TOUCH_MIN";
	
	private boolean isCalibrated = false;
	private float min, max; //To store size of major axis of touch
	
	/**
	 * Note: Should be initialized with application context, not some activty context
	 * @param context
	 */
	public TouchSurfaceHelper(Context context){
		this.loadTouchInformation(context);
	}
	
	/**
	 * Method to get touch ratio normalized according to calibrated values
	 * @param touch
	 * @return
	 */
	private float getComparedTouchRatio(float touch){
		if(touch > this.max) return 1.0f;
		if(touch < this.min) return 0.0f;
		return (touch-this.min)/(this.max-this.min);
	}
	
	/**
	 * Simply implemented for niceness
	 * @param event Some motionevent
	 * @return
	 */
	public float getComparedTouchRatio(MotionEvent event){
		return this.getComparedTouchRatio(event.getTouchMajor());
	}
	
	/**
	 * Loads touch surface area information from preferences
	 * @param context
	 */
	private void loadTouchInformation(Context context){
		SharedPreferences preferences = context.getSharedPreferences("TouchSurfaceAreaProject", Activity.MODE_PRIVATE);
		this.min = preferences.getFloat(KEY_MIN, -1f);
		this.max = preferences.getFloat(KEY_MAX, -1f);
		this.isCalibrated = (this.min != -1f && this.max != -1f);
	}
	
	public boolean IsCalibrated(){
		return this.isCalibrated;
	}
	
	public float getMaximumTouch(){
		return this.max;
	}
	
	public float getMinimumTouch(){
		return this.min;
	}
	
	public void reset(Context context){
		this.loadTouchInformation(context);
	}
	
	/**
	 * Method to save information
	 * @param context
	 * @param min
	 * @param max
	 */
	public boolean saveTouchInformation(Context context, float min, float max){
		SharedPreferences preferences = context.getSharedPreferences("TouchSurfaceAreaProject", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat(KEY_MAX, max);
		editor.putFloat(KEY_MIN, min);
		return editor.commit();
	}
}