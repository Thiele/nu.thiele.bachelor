package nu.thiele.bachelor.layouts;

import java.util.List;
import nu.thiele.bachelor.R;
import nu.thiele.bachelor.selectionListeners.SimpleSelectionListener;
import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.Setup;
import nu.thiele.bachelor.util.Shared;
import nu.thiele.bachelor.util.SharedFunctions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Rectangle extends MyLayout{
	private Context context;
	private int appsPrRow = Integer.MAX_VALUE, idOffset = 2<<12; //To avoid collisions with other ids
	private RelativeLayout layout;
	public static int PR_ROW_SHOW_TEXT_ONE_LINE = 8; //Show one line of text, if at most n apps pr row
	public static int PR_ROW_SHOW_TEXT_TWO_LINES = 4; //Show two lines of text, if at most n apps pr row
	
	
	/**
	 * 
	 * @param context
	 * @param appInfo The apps to show
	 */
	public Rectangle(Context context, List<AppInfo> appInfo){
		super(context,appInfo);
		this.context = context;
		this.appInfo = appInfo;
		//Initialize
		initialize();
	}
	
	
	
	@Override
	public ViewGroup getLayout() {
		//Compute layout if not done yet
		if(this.layout == null){
			DisplayMetrics display = this.context.getResources().getDisplayMetrics();
			int width = (display.widthPixels);
			
			
			//New icon width
			int iconWidth = width/appsPrRow;
			
			this.layout = new RelativeLayout(context);
			
			//Initialize something
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			
			//Add all the apps
			int i = 0;
			for(AppInfo app : this.appInfo){
				//Initialize view
				int id = this.idOffset+i;
				View v = inflater.inflate(R.layout.appholder_rectangle, null);
				TextView vText = (TextView) v.findViewById(R.id.appholder_rectangle_text);
				ImageView vIcon = (ImageView) v.findViewById(R.id.appholder_rectangle_logo);
				
				Bitmap bIcon = SharedFunctions.getResizedBitmap(((BitmapDrawable) app.getIcon()).getBitmap(), iconWidth, iconWidth);
				
				//Update stuff
				v.setTag(app.getId());//Set tag to store id - very important
				v.setOnClickListener(Setup.ON_SELECTION_LISTENER);
				v.setId(id);

				//Icon
				vIcon.setMaxWidth(iconWidth);
				vIcon.setImageBitmap(bIcon);
				
				//Text
				vText.setTextColor(Setup.LAYOUT_TEXT_COLOR);
				vText.setText(app.getName());
				vText.setLines(0); //No lines as default
				vText.setMaxLines(0);
				vText.setMaxWidth(iconWidth); //Done no matter what
				
				if(appsPrRow <= Rectangle.PR_ROW_SHOW_TEXT_ONE_LINE){
					//Show one line
					vText.setMaxLines(1);
					vText.setLines(1);
					
					//Even show two lines?
					if(appsPrRow <= Rectangle.PR_ROW_SHOW_TEXT_TWO_LINES){
						vText.setMaxLines(2);
						vText.setLines(2);						
					}
				}
				
				//Determine where to put view and put it
				if(i < this.appsPrRow){ //First row
					if(i == 0){ //First item
						this.layout.addView(v);
					}
					else{ //Else, put to right of latest
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.RIGHT_OF, id-1);
						this.layout.addView(v, params);
					}				
				}
				else{ //Not first row...
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.BELOW, id-this.appsPrRow);
					//If not first item, place to right
					if(i%this.appsPrRow != 0){
						params.addRule(RelativeLayout.RIGHT_OF, id-1);
					}
					
					this.layout.addView(v,params);
				}
				//Update variables
				i++;
			}
		}
		return this.layout;
	}
	
	private void initialize(){
		/*
		 * Start by figuring out the icon sizes
		 */
		DisplayMetrics display = this.context.getResources().getDisplayMetrics();
		int height = (display.heightPixels);
		int width = (display.widthPixels);
		
		float ratio = ((float) height)/((float)width);
		this.appsPrRow = (int) Math.ceil(Math.sqrt(((float) this.appInfo.size())/ratio));
	}
	
	@Override
	public boolean pan(float x, float y){
		this.layout.scrollBy((int)x, (int)y);
		return true;
	}
	
	@Override
	public boolean setAlpha(float alpha){
		//Set alpha for background
		this.layout.setAlpha(alpha);
		//And for all views
		for(int i = 0; i < this.layout.getChildCount(); i++){
			View v = this.layout.getChildAt(i);
			System.out.println(v);
			ImageView img = (ImageView) v.findViewById(R.id.appholder_rectangle_logo);
			img.setAlpha(alpha);
		}
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
		this.appsPrRow = size;
	}



	@Override
	public int getItemSize() {
		return this.appsPrRow;
	}
}