package nu.thiele.bachelor.selectionListeners;

import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.SharedFunctions;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AlertImageDisplayer implements OnClickListener{
	@Override
	public void onClick(View v) {
		int id = Integer.valueOf(v.getTag().toString());
		AppInfo app = SharedFunctions.getAppInfoById(id);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		ImageView img = new ImageView(v.getContext());
		img.setImageDrawable(app.getIcon());
		builder.setView(img);
		builder.setTitle(app.getName());
		builder.create().show();
	}
}
