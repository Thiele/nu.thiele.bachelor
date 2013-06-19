package nu.thiele.bachelor.inputListeners;

import android.view.View.OnTouchListener;
import nu.thiele.bachelor.layouts.MyLayout;

public abstract class MyTouchListener implements OnTouchListener{
	protected MyLayout layout;

	/**
	 * Use this constructor if you do not need to pass
	 * contact size information to listener
	 * @param layout
	 */
	public MyTouchListener(MyLayout layout){
		this.layout = layout;
	}
	
	/**
	 * Use this constructor if you need to pass the min
	 * and max touch sizes to the listener
	 * @param layout
	 * @param min
	 * @param max
	 */
	public MyTouchListener(MyLayout layout, float min, float max){
		this.layout = layout;
	}
	
	/**
	 * Called when the listener is destroyed
	 */
	public void onDestroy(){
		
	}
}
