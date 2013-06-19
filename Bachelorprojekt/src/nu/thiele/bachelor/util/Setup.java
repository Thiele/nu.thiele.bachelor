package nu.thiele.bachelor.util;

import android.graphics.Color;
import android.view.View.OnClickListener;
import nu.thiele.bachelor.advancedLayouts.Attracter;
import nu.thiele.bachelor.advancedLayouts.Stack;
import nu.thiele.bachelor.layouts.Rectangle;
import nu.thiele.bachelor.layouts.RotatedSquare;
import nu.thiele.bachelor.selectionListeners.AlertImageDisplayer;
import nu.thiele.bachelor.selectionListeners.SimpleSelectionListener;

@SuppressWarnings("rawtypes")
public class Setup {
	public static final boolean INCLUDE_SYSTEM_APPS = true;
	public static int LAYOUT_BACKGROUND_COLOR = Color.parseColor("#1e1e1e");
	public static int LAYOUT_TEXT_COLOR = Color.LTGRAY;
	
	/*
	 * Various listeners
	 */
	//Selection listener
	public static final OnClickListener ON_SELECTION_LISTENER = new AlertImageDisplayer();		
}