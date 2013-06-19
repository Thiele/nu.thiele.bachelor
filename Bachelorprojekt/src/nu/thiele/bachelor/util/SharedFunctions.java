package nu.thiele.bachelor.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class SharedFunctions {
	public static AppInfo getAppInfoById(int id){
		for(AppInfo app : Shared.APPLICATIONLIST){
			if(id == app.getId()) return app;
		}
		return null;
	}
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();
	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);
	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
}