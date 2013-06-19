package nu.thiele.bachelor.util;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo>{
	private Drawable icon;
	private int id;
	private String name;
	
	/**
	 * 
	 * @param name
	 * @param icon
	 * @param id Should be unique. Used to uniquely identify single 
	 */
	public AppInfo(String name, Drawable icon, int id){
		this.icon = icon;
		this.id = id;
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}

	@Override
	public int compareTo(AppInfo another) {
		//To avoid confusing users, compare lower case words
		return this.name.toLowerCase().compareTo(another.name.toLowerCase());
	}
}
