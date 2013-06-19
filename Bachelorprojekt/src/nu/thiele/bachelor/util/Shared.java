package nu.thiele.bachelor.util;

import java.util.LinkedList;

import nu.thiele.bachelor.layouts.MyLayout;

public class Shared {
	public static LinkedList<AppInfo> APPLICATIONLIST;
	public static Hand PREFERREDHAND = Hand.valueOf(DefaultValues.PREFERRED_HAND);
	public static MyLayout LAYOUT = null;
	public static TouchSurfaceHelper SURFACEINFORMATION;
	
	/**
	 * A simple enum to contain information on which hand
	 * the user is preferring to interact with.
	 * 
	 * @author Andreas
	 *
	 */
	public enum Hand{
		LEFT, RIGHT;
	}
}
