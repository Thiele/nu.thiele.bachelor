package nu.thiele.bachelor.util;

/**
 * Class to contain default values for most setup 
 * 
 * Note: Use ONLY native types compatible with Android's
 * preferences editor
 * 
 * @author Andreas
 *
 */
public class DefaultValues {
	public static final String PREFERRED_HAND = "RIGHT";
	
	//Interaction method
	public static final boolean STACK_DISCRETE_FLIP = false;
	public static final String STACK_INTERACTION_METHOD = "SURFACE_SIZE_STEADY";
	public static final boolean STACK_LINEAR_CONTINUOUS_FLIP = false; //Use the non-linear flip
}
