package nu.thiele.bachelor.activities;

import nu.thiele.bachelor.advancedLayouts.Stack;
import nu.thiele.bachelor.inputListeners.StackFlipListener;
import nu.thiele.bachelor.util.DefaultValues;
import nu.thiele.bachelor.util.PreferencesStrings;
import nu.thiele.bachelor.util.Shared;
import nu.thiele.bachelor.util.TouchSurfaceHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class Activity_Main extends Activity {
	private static final int CALIBRATE = 1;
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	        super.onActivityResult(requestCode, resultCode, data);

	        if(resultCode == RESULT_OK){
	            switch(requestCode){
	            case CALIBRATE:
	            default:
	            	//Reload the contact size helper, as this
	            	//have been recalibrated now
	            	Shared.SURFACEINFORMATION = new TouchSurfaceHelper(this.getApplicationContext());
	            	startSelection();
	            	break;
	            }
	        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		/*
		 * Initialise and load all kinds of stuff
		 */
		
		SharedPreferences preferences = this.getSharedPreferences("TouchSurfaceAreaProject", Activity.MODE_PRIVATE);
			
		StackFlipListener.METHOD = StackFlipListener.InteractionMethod.valueOf(preferences.getString(PreferencesStrings.STACK_INTERACTION_METHOD, DefaultValues.STACK_INTERACTION_METHOD));
		Stack.DISCRETE_FLIPPING = preferences.getBoolean(PreferencesStrings.STACK_DISCRETE_FLIP, DefaultValues.STACK_DISCRETE_FLIP);
		Stack.LINEAR_CONTINUOUS_FLIPPING = preferences.getBoolean(PreferencesStrings.STACK_LINEAR_CONTINUOUS_FLIP, DefaultValues.STACK_LINEAR_CONTINUOUS_FLIP);
		
		//For some reason, this tends to be null? Quick fix
		while(Shared.SURFACEINFORMATION == null){
			Log.i("Activity_Main", "Surfaceinformation still null");
			Shared.SURFACEINFORMATION = new TouchSurfaceHelper(this.getApplicationContext());
		}
		
		/*
		 * And launch correct activty 
		 */
		
		//If not calibrated yet, do that
		if(!Shared.SURFACEINFORMATION.IsCalibrated()){
			Toast.makeText(this, "It seems you have no calibration stored. Please calibrate", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, Activity_Calibrate.class);
			this.startActivityForResult(i, CALIBRATE);
			return;
		}
		
		//If nothing else happens, do the selection
		startSelection();
	}
	
	private void startSelection(){
		Intent selection = new Intent(this, Activity_Selection.class);
		this.startActivity(selection);
		this.finish(); //Cleanup
	}
}