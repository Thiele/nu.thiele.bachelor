package nu.thiele.bachelor.activities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import nu.thiele.bachelor.advancedLayouts.Stack;
import nu.thiele.bachelor.inputListeners.StackFlipListener;
import nu.thiele.bachelor.inputListeners.TapListener;
import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.Setup;
import nu.thiele.bachelor.util.Shared;
import nu.thiele.bachelor.util.TouchSurfaceHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Activity_Selection extends Activity{
	private RelativeLayout layers;
	private static final int MENU_CALIBRATE = 2, MENU_SETTINGS = 3;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CALIBRATE, 0, "Calibrate");
		menu.add(0,MENU_SETTINGS,0,"Settings");
	    return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
			case MENU_CALIBRATE:
				Intent i = new Intent(this, Activity_Calibrate.class);
				this.startActivity(i);
				break;
			case MENU_SETTINGS:
				i = new Intent(this, Activity_Settings.class);
				this.startActivity(i);
				break;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		/*
		 * Do something
		 */
		//Initialize stuff
		this.layers = new RelativeLayout(this);
		
		//Import all listeners
		
		//Find all apps
		LinkedList<AppInfo> apps = getInstalledApps();
		Collections.sort(apps);
		Shared.APPLICATIONLIST = apps;
		/*
		 * Setup
		 */
		//Create layout view
		Shared.LAYOUT = new Stack(this, apps);
		
		
		//Add a transparent layer
		LinearLayout inputLayer = new LinearLayout(this);
		
		/*
		 * Put it all together
		 */
		RelativeLayout.LayoutParams fullscreenParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		this.layers.setLayoutParams(fullscreenParams);
		this.layers.addView(Shared.LAYOUT.getLayout());
		this.layers.addView(inputLayer, fullscreenParams);
		//Listener...
		inputLayer.setOnTouchListener(new MyOnTouchListenerHandler());
		//Background color
		this.layers.setBackgroundColor(Setup.LAYOUT_BACKGROUND_COLOR);
		
		this.setContentView(this.layers);
	}
	
	private LinkedList<AppInfo> getInstalledApps(){
		LinkedList<AppInfo> retval = new LinkedList<AppInfo>();
		List<PackageInfo> packages = this.getPackageManager().getInstalledPackages(0);
		int id = 0;
		for(PackageInfo pck : packages){
			//Filter out system packages, if need be
			if(Setup.INCLUDE_SYSTEM_APPS || (pck.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
				String name = String.valueOf(pck.applicationInfo.loadLabel(getPackageManager()));
				Drawable icon = pck.applicationInfo.loadIcon(getPackageManager());
				AppInfo add = new AppInfo(name,icon, id);
				retval.add(add);
				id++;
			}
		}
		return retval;
	}
	
	private class MyOnTouchListenerHandler implements OnTouchListener{
		private StackFlipListener flip = new StackFlipListener(Shared.LAYOUT, 0f, 1f);
		private TapListener tap = new TapListener(Shared.LAYOUT);
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(Shared.SURFACEINFORMATION == null){
				Shared.SURFACEINFORMATION = new TouchSurfaceHelper(getApplicationContext());
			}
			float size = Shared.SURFACEINFORMATION.getComparedTouchRatio(event);
			System.out.println(size);
			if(size >= 0f && size <= 1){
				if(tap.onTouch(v, event)) return true;
			}
			if(size >= 0f && size <= 1f){
				if(this.flip.onTouch(v, event)) return true;
			}
			return true;
		}
	}
}