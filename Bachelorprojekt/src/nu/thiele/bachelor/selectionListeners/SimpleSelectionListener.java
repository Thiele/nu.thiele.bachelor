package nu.thiele.bachelor.selectionListeners;

import nu.thiele.bachelor.util.AppInfo;
import nu.thiele.bachelor.util.SharedFunctions;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleSelectionListener implements OnClickListener{
	@Override
	public void onClick(View v) {
		int id = Integer.valueOf(v.getTag().toString());
		AppInfo app = SharedFunctions.getAppInfoById(id);
		Toast.makeText(v.getContext(), app.getName(), Toast.LENGTH_SHORT).show();
	}
}