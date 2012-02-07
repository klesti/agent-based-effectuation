/**
 * 
 */
package EffectuationCausation;

import repast.simphony.engine.environment.RunEnvironment;

/**
 * @author klesti
 */
public class Parameters {
	public static repast.simphony.parameter.Parameters params 
					= RunEnvironment.getInstance().getParameters();	
	
	public static int vectorSpaceSize;
	
	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	
	//Effectuation scenario
	public static int numberOfCustomers;	
	public static int maxInitialGoals;
	public static int maxInitalMeans;
	public static int maxDepthForMeeting;
	public static int minMeetings;
	public static int maxMeetings;
	public static String networkGenerator;
	
	public static void initialize() {
		vectorSpaceSize = (Integer)params.getValue("vectorSpaceSize");
		productElementChangeThreshold = (Integer)params.getValue("productElementChangeThreshold");
		numberOfCustomers = (Integer)params.getValue("numberOfCustomers");
		maxInitialGoals = (Integer)params.getValue("maxInitialGoals");
		maxInitalMeans = (Integer)params.getValue("maxInitialMeans");
		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		minMeetings = (Integer)params.getValue("minMeetings");
		maxMeetings = (Integer)params.getValue("maxMeetings");
		networkGenerator = (String)params.getValue("networkGenerator");
	}
	
}
