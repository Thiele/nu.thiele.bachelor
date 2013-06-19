package nu.thiele.bachelor.layouts;

import java.util.List;

import nu.thiele.bachelor.R;
import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.Setup;
import nu.thiele.bachelor.util.SharedFunctions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class RotatedSquare extends MyLayout{
	private int sidelength = Integer.MAX_VALUE;
	private static final int MAX_ROW_SHOW_TEXT_ONE_LINE = 7;
	private static final int MAX_ROW_SHOW_TEXT_TWO_lINES = 3;
	private LinearLayout layoutContainer;
	private RelativeLayout layout;
	public RotatedSquare(Context context, List<AppInfo> appInfo) {
		super(context, appInfo);
		this.context = context;
		this.appInfo = appInfo;
		
		initialize();
	} 
	
	private void initialize(){
		this.sidelength = (int) Math.ceil(Math.sqrt(8*this.appInfo.size()-4)/4.0+1/2.0);
	}

	@Override
	public ViewGroup getLayout() {
		if(this.layout == null){
			this.layout = new RelativeLayout(this.context);
			this.layoutContainer = new LinearLayout(this.context);
			this.layoutContainer.setOrientation(LinearLayout.VERTICAL);
			
			
			
			//Add all the stuff
			//Find sidelength
			int maxrow = 2*sidelength-1;
			int rowsize = 1;
			int sign = 1;
			
			//Size that images will have in width
			DisplayMetrics display = this.context.getResources().getDisplayMetrics();
			int width = display.widthPixels;
			int iconWidth = (int) (((float)width)/((float)maxrow));
			
			LinearLayout container = new LinearLayout(this.context);
			LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			container.setOrientation(LinearLayout.HORIZONTAL);
			int i = 0;
			for(AppInfo app : this.appInfo){
				if(i == rowsize){ //If row has been filled, add the row and create new
					//If at largest row, change sign
					if(rowsize == maxrow){
						sign = sign*-1;
					}
					
					//Change rowsize
					rowsize = rowsize+2*sign;
					//Reset i
					i = 0;
					//Center in parent before adding
					container.setGravity(Gravity.CENTER_HORIZONTAL);
					//And add the container to child-view
					this.layoutContainer.addView(container, containerParams);
					container = new LinearLayout(this.context);
					container.setOrientation(LinearLayout.HORIZONTAL);
				}
				
				//Get view and add to container
				LayoutInflater inflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.appholder_rectangle, null);
				TextView vText = (TextView) v.findViewById(R.id.appholder_rectangle_text);
				ImageView vIcon = (ImageView) v.findViewById(R.id.appholder_rectangle_logo);
				
				//Do various stuff
				v.setTag(app.getId());//Set tag to store id - very important
				v.setOnClickListener(Setup.ON_SELECTION_LISTENER);
				
				//Add resized icon
				Bitmap bIcon = SharedFunctions.getResizedBitmap(((BitmapDrawable) app.getIcon()).getBitmap(), iconWidth, iconWidth);
				vIcon.setImageBitmap(bIcon);
				
				//And text
				vText.setText(app.getName());
				vText.setLines(0); //For a start
				if(maxrow <= MAX_ROW_SHOW_TEXT_ONE_LINE){
					vText.setLines(1);
					if(maxrow <= MAX_ROW_SHOW_TEXT_TWO_lINES){
						vText.setLines(2);
					}
				}
				
				//Simply add
				container.addView(v);				
				//Increment i
				i++;
			}
			//Finally, add the last			
			//Center in parent before adding
			container.setGravity(Gravity.CENTER_HORIZONTAL);
			
			//Set gravity of layout container
			this.layoutContainer.setGravity(Gravity.CENTER_VERTICAL);
			
			//And add the container to child-view
			this.layoutContainer.addView(container, containerParams);			
			
			//Set up params
			RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
			//Set layout params and add view
			this.layout.addView(this.layoutContainer, centerParams);
		}
		return this.layout;
	}
	
	@Override
	public View getViewAt(float x, float y){
		for(int i = 0; i < this.layoutContainer.getChildCount(); i++){
			ViewGroup v = (ViewGroup) this.layoutContainer.getChildAt(i);
			for(int j = 0; j < v.getChildCount(); j++){
				View view = v.getChildAt(j);
				if(isPointInsideView(x,y,view)) return view;
			}
		}
		return null;
	}
	
	/**
	 * This function returns the optimal number of applications
	 * for the layout. Used by Stack layout
	 * @return
	 */
	public static int getPrefferedItemsInLayout(Context context){
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		float width = display.widthPixels/display.xdpi;
		//Since width is now stored in inches, multiply by 25.4
		width = width*25.4f;
		int widestlevel = (int) Math.floor(width/9.6f);
		if(widestlevel%2 == 0) widestlevel--;
		return (widestlevel*widestlevel+1)/2;
	}
	
	@Override
	public boolean setAlpha(float alpha){
		this.layout.setAlpha(alpha);
		return true;
	}
	
	@Override
	public boolean setZoom(float scale){
		this.zoomScale = scale;
		this.layout.setScaleX(scale);
		this.layout.setScaleY(scale);
		return true;
	}
	
	@Override
	public boolean zoom(float scale, float x, float y){
		this.zoomScale += scale;
		if(x >= 0 && y >= 0){
			this.layout.setPivotX(x);
			this.layout.setPivotY(y);
		}
		this.layout.setScaleX(this.zoomScale);
		this.layout.setScaleY(this.zoomScale);
		return true;
	}

	@Override
	public void ensureItemSize(int size) {
		this.sidelength = size;
	}

	@Override
	public int getItemSize() {
		return this.sidelength;
	}
}