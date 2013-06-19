package nu.thiele.bachelor.layouts;

import java.util.List;
import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.SharedFunctions;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class MyLayout {
	protected Context context;
	protected float zoomScale = 1.0f;
	protected List<AppInfo> appInfo;
	public MyLayout(Context context, List<AppInfo> appInfo){}
	
	/**
	 * A method to enforce a rule on how much an item may take up on screen 
	 * 
	 * This is needed to make the stack layout have a "nice" feel.
	 * 
	 * @param size This number may vary depending on the layout. Just pass the value returned from getItemSize on a a populated layer.
	 */
	public abstract void ensureItemSize(int size);
	/**
	 * Just return -1 as default
	 * @return
	 */
	public int getItemSize(){
		return -1;
	}
	
	/**
	 * Is called when the application is ready to have the
	 * applications shown
	 * @return
	 */
	public abstract ViewGroup getLayout();
	
	
	/**
	 * Method to get the view from content layer at some coordinate
	 * 
	 * Can be overwritten if wanted, since some views may not
	 * be this simply implemented. Such an example is the Stack
	 * layout
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @return
	 */
	public View getViewAt(float x, float y){
		int count = this.getLayout().getChildCount();
		for(int i = 0; i < count; i++){
			View v = this.getLayout().getChildAt(i);
			if(isPointInsideView(x,y,v)) return v;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return Current zoom level
	 */
	public float getZoomLevel(){
		return this.zoomScale;
	}
	
	
	/**
	 * This method is used to decide whether or not a point
	 * is inside view. This is called when trying to decide
	 * which view was selected
	 * @param x x-coordinate for point
	 * @param y y-coordinate for point
	 * @param v view to test
	 * @return true if inside, false otherwise
	 */
	protected boolean isPointInsideView(float x, float y, View v){
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		
		return (x > location[0] && x < location[0]+v.getWidth() && y > location[1] && y < location[1]+v.getHeight());
	}
	
	/**
	 * Method to move through stack
	 * @param howmuch
	 * @return
	 */
	public boolean moveThroughStack(float howmuch){
		Log.d("Moving through stack", "Moving through stack is not supported by this view");
		return false;
	}
	
	/**
	 * Method to pan the layout
	 * 
	 * @param x How much to pan x
	 * @param y How much to pan y
	 * @return true if method is supported by layout, false otherwise
	 */
	public boolean pan(float x, float y){
		Log.d("Panning", "Panning not supported by layout");
		return false;
	}	
	
	/**
	 * Reset alpha and zoom for layout
	 */
	public void reset(){
		//Reset alpha and zoom
		this.resetAlpha();
		this.resetZoom();
	}
	
	/**
	 * Reset alpha
	 * @return
	 */
	public boolean resetAlpha(){
		return this.setAlpha(1.0f);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean resetZoom(){
		return this.setZoom(1.0f);
	}
	
	/**
	 * Set alpha value of layout
	 * @param alpha
	 * @return
	 */
	public boolean setAlpha(float alpha){
		Log.d("MyLayout.setAlpha", "Alpha not supported by view");
		return false;
	}
	
	/**
	 * Set zoom to some specified scale
	 * @param scale The scale at which zooming should be set
	 * @return
	 */
	public boolean setZoom(float scale){
		Log.d("MyLayout.setZoom", "Zooming not supported by view");
		return false;
	}
	
	/**
	 * For zooming with current pivot point
	 * @param scale How much should be zoomed
	 * @return zoom(scale, -1, -1)
	 */
	public boolean zoom(float scale){
		return zoom(scale, -1f, -1f);
	}
	
	/**
	 * Method to zoom layout
	 * 
	 * @param scale How much should be zoomed
	 * @param x x-coordinate to zoom around
	 * @param y y-coordinate to zoom around
	 * @return true if method is supported by layout, false otherwise
	 */
	public boolean zoom(float scale, float x, float y){
		Log.d("Zooming", "Zooming not supported by layout");
		return false;
	}
}