package nu.thiele.bachelor.advancedLayouts;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import nu.thiele.bachelor.R;
import nu.thiele.bachelor.layouts.MyLayout;
import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.Setup;
import nu.thiele.bachelor.util.SharedFunctions;

public class Attracter extends MyLayout{
	private ArrayList<ArrayList<LinearLayout>> appHolderViews;
	private Context context;
	private int bottom, left, right, top, IMAGINARY_ID = 5<<12; //Random enough
	private List<AppInfo> appInfo;
	private RelativeLayout layout;
	
	public static int TEXT_LINES = 2;
	
	public Attracter(Context context, List<AppInfo> appInfo) {
		super(context, appInfo);
		this.appInfo = appInfo;
		this.context = context;
		
		this.initialize();
	}
	
	/**
	 * Initialize
	 */
	private void initialize(){

	}

	@Override
	public ViewGroup getLayout() {
		if(this.layout == null){
			this.layout = new RelativeLayout(this.context);
			
			//Make full screen
			RelativeLayout.LayoutParams fullScreenParams = new RelativeLayout.LayoutParams(1000,LayoutParams.MATCH_PARENT);
			this.layout.setLayoutParams(fullScreenParams);
			
			this.appHolderViews = new ArrayList<ArrayList<LinearLayout>>();
			//Add 8 lists
			for(int i = 0; i < 8; i++) this.appHolderViews.add(new ArrayList<LinearLayout>());
			int c = 0;
			
			DisplayMetrics display = this.context.getResources().getDisplayMetrics();
			int iconWidth = display.widthPixels/5; //Display has to hold 5 in width at once
			for(AppInfo app : this.appInfo){
				ArrayList<LinearLayout> workingList = this.appHolderViews.get(c%this.appHolderViews.size());			
				
				//View to add
				//Get view and add to container
				LayoutInflater inflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout v = (LinearLayout) inflater.inflate(R.layout.appholder_rectangle, null);
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
				vText.setLines(Attracter.TEXT_LINES); //For a start
				
				//Add to the current list
				workingList.add(v);

				//Remember to increment
				c++;
			}
			
			//And while list 1-7 does not have same length as 0, add empty appholder (null) to the list
			while(this.appHolderViews.get(7).size() < this.appHolderViews.get(0).size()){
				for(int i = 1; i < this.appHolderViews.size(); i++){
					if(this.appHolderViews.get(i).size() < this.appHolderViews.get(0).size()) this.appHolderViews.get(i).add(null);
				}
			}
			
			
			//First the imaginary center appholder
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout imaginary = (LinearLayout) inflater.inflate(R.layout.appholder_rectangle, null);
			Bitmap imaginaryLogoBmp = Bitmap.createBitmap(iconWidth, iconWidth, Bitmap.Config.ARGB_8888);
			ImageView imaginaryLogo = (ImageView) imaginary.findViewById(R.id.appholder_rectangle_logo);
			TextView imaginaryText = (TextView) imaginary.findViewById(R.id.appholder_rectangle_text);
			
			imaginary.setId(this.IMAGINARY_ID);
			
			imaginaryText.setText("");
			imaginaryText.setLines(Attracter.TEXT_LINES);
			imaginaryText.setBackgroundColor(Color.WHITE); //Just transparent
			
			Canvas canvas = new Canvas(imaginaryLogoBmp);
			canvas.drawColor(Color.WHITE);
			//Update logo
			imaginaryLogo.setImageBitmap(imaginaryLogoBmp);
						
			//Figure out corners of center imaginary appholder
			RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			this.layout.addView(imaginary, centerParams);
			imaginary.setVisibility(View.INVISIBLE);
			
			//Figure out some width height
			int iconTotalHeight = (int) (canvas.getHeight()+(Attracter.TEXT_LINES+0.3)*imaginaryText.getTextSize());
			
			//And position the views accordingly
			for(int i = 0; i < this.appHolderViews.size(); i++){
				int num = 0;
				for(LinearLayout appholder : this.appHolderViews.get(i)){
					if(appholder == null) break;
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					switch(i){
					case 0:
						params.rightMargin = num*iconWidth;
						params.bottomMargin = num*iconTotalHeight;
						params.addRule(RelativeLayout.LEFT_OF, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.ABOVE, this.IMAGINARY_ID);
						break;
					case 1:
						params.bottomMargin = num*iconTotalHeight;
						params.addRule(RelativeLayout.ABOVE, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						break;
					case 2:
						params.leftMargin = num*iconWidth;
						params.bottomMargin = num*iconTotalHeight;
						params.addRule(RelativeLayout.ABOVE, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.RIGHT_OF, this.IMAGINARY_ID);
						break;
					case 3:
						params.rightMargin = num*iconWidth;
						params.addRule(RelativeLayout.LEFT_OF, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.CENTER_VERTICAL);
						break;
					case 4:
						params.leftMargin = num*iconWidth;
						params.addRule(RelativeLayout.RIGHT_OF, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.CENTER_VERTICAL);
						break;
					case 5:
						params.topMargin = num*iconTotalHeight;
						params.rightMargin = num*iconWidth;
						params.addRule(RelativeLayout.BELOW, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.LEFT_OF, this.IMAGINARY_ID);
						break;
					case 6:
						params.topMargin = num*iconTotalHeight;
						params.addRule(RelativeLayout.BELOW, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						break;
					case 7:
						params.topMargin = num*iconTotalHeight;
						params.leftMargin = num*iconWidth;
						params.addRule(RelativeLayout.BELOW, this.IMAGINARY_ID);
						params.addRule(RelativeLayout.RIGHT_OF, this.IMAGINARY_ID);
						break;
					}
					num++;
					this.layout.addView(appholder, params);
				}
			}
		}
		return this.layout;
	}

	@Override
	public void ensureItemSize(int size) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getItemSize() {
		return -1;
	}
}