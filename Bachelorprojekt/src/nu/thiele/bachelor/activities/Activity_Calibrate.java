package nu.thiele.bachelor.activities;

import java.util.LinkedList;
import nu.thiele.bachelor.R;
import nu.thiele.bachelor.util.Shared;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Calibrate extends Activity{
	private boolean touchedPopup = false;
	private boolean minEvaluating = true;
	private Button button, reset;
	private ImageView touchArea;
	private TextView status,text;
	private LinkedList<Float> mins, maxs;
	private int touchesTotal = 400;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//Set content view
		this.setContentView(R.layout.activity_calibrate);
		
		//Initialize
		this.button = (Button) findViewById(R.id.activity_calibrate_button);
		this.reset = (Button) findViewById(R.id.activity_calibrate_reset);
		this.status = (TextView) findViewById(R.id.activity_calibrate_text_status);
		this.text = (TextView) findViewById(R.id.activity_calibrate_text);
		this.touchArea = (ImageView) findViewById(R.id.activity_calibrate_toucharea);
		
		initialize();
	}
	
	private void initialize(){
		this.reset();
	}
	
	private void reset(){
		//Set/reset maxs+mins
		this.maxs = new LinkedList<Float>();
		this.mins = new LinkedList<Float>();
		
		
		//Update information
		this.button.setText("Save");
		this.button.setEnabled(false);
		this.minEvaluating = true;
		this.reset.setText("Reset");
		this.text.setText("Move finger with low contact size");
		this.touchArea.setEnabled(true);
		this.touchedPopup = false;
		this.status.setText("");
		
		this.touchArea.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(minEvaluating) mins.add(event.getTouchMajor());
				else if(touchedPopup) maxs.add(event.getTouchMajor());
				status.setText(event.getTouchMajor()+"");
				if(mins.size() >= touchesTotal && minEvaluating){
					//Mins found, find maxs
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setCancelable(false);
					builder.setMessage("Now move finger with a large contact size as you feel comfortable with");
					builder.setNeutralButton("Ok", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							touchedPopup = true;
						}
					});
					builder.create().show();
					text.setText("Move finger with large contact size");
					minEvaluating = !minEvaluating;
				}
				else if(maxs.size() >= touchesTotal && !minEvaluating){
					touchArea.setEnabled(false);
					button.setEnabled(true);
					text.setText("Done calibrating");
				}
				return true;
			}
		});
		
		//Setting listeners
		this.button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(button.isEnabled()){
					save();
				}
			}
		});
		this.reset.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				reset();	
			}
		});
	}
	
	private void save(){
		//Parse touch information
		//Sum up both min and max
		float summax = 0;
		float summin = 0;
		
		for(float f : this.maxs) summax = summax+f;
		for(float f : this.mins) summin = summin+f;
		
		
		//And calculate the average
		float min = summin/((float)this.mins.size());
		float max = summax/((float)this.maxs.size());
		
		
		if(Shared.SURFACEINFORMATION.saveTouchInformation(this.getApplicationContext(), min, max)) Toast.makeText(this, "Calibration completed", Toast.LENGTH_SHORT).show();
		else Toast.makeText(this, "An error happened during calibration", Toast.LENGTH_SHORT).show();
		this.setResult(Activity.RESULT_OK);
		this.finish();
	}
}