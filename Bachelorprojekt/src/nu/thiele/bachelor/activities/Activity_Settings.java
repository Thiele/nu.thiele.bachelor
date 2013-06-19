package nu.thiele.bachelor.activities;

import java.util.LinkedList;

import nu.thiele.bachelor.R;
import nu.thiele.bachelor.advancedLayouts.Stack;
import nu.thiele.bachelor.inputListeners.StackFlipListener;
import nu.thiele.bachelor.inputListeners.StackFlipListener.InteractionMethod;
import nu.thiele.bachelor.util.DefaultValues;
import nu.thiele.bachelor.util.PreferencesStrings;
import nu.thiele.bachelor.util.Shared;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Activity_Settings extends Activity{
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private LinearLayout layoutSettings;
	private Spinner handSpinner, layoutSpinner;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Aaaand, set content view
		this.setContentView(R.layout.activity_settings);
		
		//Init
		this.handSpinner = (Spinner) findViewById(nu.thiele.bachelor.R.id.activity_settings_handspinner);
		this.layoutSettings = (LinearLayout) findViewById(nu.thiele.bachelor.R.id.activity_settings_layout_settings);
		this.layoutSettings.setOrientation(LinearLayout.VERTICAL);
		this.layoutSpinner = (Spinner) findViewById(nu.thiele.bachelor.R.id.activity_settings_spinner);
		this.preferences = this.getSharedPreferences("TouchSurfaceAreaProject", Activity.MODE_PRIVATE);
		this.editor = this.preferences.edit();
		
		//Add the values to the hand-spinner
		final String[] hands = new String[2];
		String currentHand = this.preferences.getString(PreferencesStrings.PREFERRED_HAND, DefaultValues.PREFERRED_HAND);
		hands[0] = Shared.Hand.LEFT.toString();
		hands[1] = Shared.Hand.RIGHT.toString();
		int current = (currentHand.equals(Shared.Hand.LEFT.toString()) ? 0 : 1);
		
		ArrayAdapter<String> handAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,hands);
		handAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.handSpinner.setAdapter(handAdapter);
		this.handSpinner.setSelection(current);
		this.handSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//Update the preferred hand and save prefs
				Shared.PREFERREDHAND = Shared.Hand.valueOf(hands[arg2]);
				editor.putString(PreferencesStrings.PREFERRED_HAND, hands[arg2]);
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nothing to do		
			}
		});
		
		//Add all the possible layouts to spinner.
		final String[] layouts = {"Rectangle","RotatedSquare", "Stack"};
		current = 2;
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,layouts);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.layoutSpinner.setAdapter(spinnerAdapter);
		this.layoutSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
					changeLayoutSettings(layouts[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {//Do nothing
				}
		});
		this.layoutSpinner.setSelection(current);
	}
	
	private void changeLayoutSettings(String current){
		//Empty the layout navigation view
		this.layoutSettings.removeAllViews();
		//A fill all width params
		LayoutParams fillWidth = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
		//If stack layout, add a subsetting
		if(current.toLowerCase().equals("stack"))
		{
			//Init something
			final CheckBox linear = new CheckBox(this);
			final CheckBox discrete = new CheckBox(this);
			discrete.setText("Discrete flipping?");
			discrete.setChecked(this.preferences.getBoolean(PreferencesStrings.STACK_DISCRETE_FLIP, DefaultValues.STACK_DISCRETE_FLIP));
			//Add listener
			discrete.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					//Update
					editor.putBoolean(PreferencesStrings.STACK_DISCRETE_FLIP, isChecked);
					editor.commit();
					
					Stack.DISCRETE_FLIPPING = isChecked;
					//Reset the stacklayer
					if(Shared.LAYOUT instanceof Stack) Shared.LAYOUT.reset();
					linear.setEnabled(!Stack.DISCRETE_FLIPPING);
				}
			});
			this.layoutSettings.addView(discrete, fillWidth);
			
			//Linear or not?
			linear.setText("Linear transition?");
			linear.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					editor.putBoolean(PreferencesStrings.STACK_LINEAR_CONTINUOUS_FLIP, isChecked);
					editor.commit();
					
					Stack.LINEAR_CONTINUOUS_FLIPPING = isChecked;	
				}
			});
			linear.setChecked(this.preferences.getBoolean(PreferencesStrings.STACK_LINEAR_CONTINUOUS_FLIP, DefaultValues.STACK_LINEAR_CONTINUOUS_FLIP));
			
			this.layoutSettings.addView(linear, fillWidth);
			
			//Set up spinner and text
			TextView txt = new TextView(this);
			txt.setText("Interaction method");
			this.layoutSettings.addView(txt, fillWidth);
			
			//Add all the possible layouts to spinner.
			LinkedList<String> vals = new LinkedList<String>();
			for(InteractionMethod method : StackFlipListener.InteractionMethod.values()){
				vals.add(method.toString());
			}
			final String[] methods = new String[vals.size()];
			int c = 0;
			int now = 0;
			for(String s : vals){
				if(StackFlipListener.METHOD.toString().equals(s)) now = c;
				methods[c] = s;
				c++;
			}
			
			//Set up spinner
			Spinner interactionMethodSpinner = new Spinner(this);
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,methods);
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			interactionMethodSpinner.setAdapter(spinnerAdapter);
			interactionMethodSpinner.setSelection(now);
			
			//Set up listener
			interactionMethodSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					editor.putString(PreferencesStrings.STACK_INTERACTION_METHOD, methods[arg2]);
					editor.commit();
					StackFlipListener.METHOD = StackFlipListener.InteractionMethod.valueOf(methods[arg2]);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {// Do nothing	
				}
			});
			
			//And add
			this.layoutSettings.addView(interactionMethodSpinner, fillWidth);
		}
	}
}
