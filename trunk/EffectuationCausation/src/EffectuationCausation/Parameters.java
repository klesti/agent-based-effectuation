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
	public static int marketSplit; // The percentage of the population that "like" a product element  
		
	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	public static int numberOfCustomers;
	
	public static int numberOfEntrepreneurs;
	public static int maxInitialGoals;
	public static int maxInitalMeans;
	public static int maxDepthForMeeting;
	public static String networkGenerator;
	public static int edgesPerStep;	
	public static String utilityFunction;
	public static boolean aggregateProductVector;
	public static boolean evolveNetwork;

	//Causation scenario
	public static int sampleSizePercentage; //Sample size in percentage
	
	public static void initialize() {
		vectorSpaceSize = (Integer)params.getValue("vectorSpaceSize");
		marketSplit = (Integer)params.getValue("marketSplit");
		
		productElementChangeThreshold = (Integer)params.getValue("productElementChangeThreshold");		
		numberOfCustomers = (Integer)params.getValue("numberOfCustomers");
		
		sampleSizePercentage = (Integer)params.getValue("sampleSizePercentage");

		numberOfEntrepreneurs = (Integer)params.getValue("numberOfEntrepreneurs");
		maxInitialGoals = (Integer)params.getValue("maxInitialGoals");
		maxInitalMeans = (Integer)params.getValue("maxInitialMeans");

		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		networkGenerator = (String)params.getValue("networkGenerator");
		edgesPerStep = (Integer)params.getValue("edgesPerStep");
		utilityFunction = (String)params.getValue("utilityFunction");
		aggregateProductVector = (Boolean)params.getValue("aggregateProductVector");
		evolveNetwork = (Boolean)params.getValue("evolveNetwork");
	}	
}
