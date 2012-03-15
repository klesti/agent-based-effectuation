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
	public static int adaptationThreshold;
	public static int adaptationSpeed;
		
	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	//%, the percentage probability that a customers accepts an offer 
	// slightly different from the original demand
	public static int customersPersuadability;  
	
	public static int numberOfCustomers;	
	public static int numberOfEntrepreneurs;
	
	public static double minAvailableMoney; // Thousands €
	public static double maxAvailableMoney; // Thousands €
	public static int affordableLoss; //Percent

	
	public static int maxDepthForMeeting;
	public static String networkGenerator;
	public static int edgesPerStep;	
	public static String utilityFunction;
	public static boolean aggregateProductVector;
	public static double newConnectionsProbability;

	//Causation scenario
	public static int sampleSizePercentage; //Sample size in percentage
	
	public static void initialize() {
		vectorSpaceSize = (Integer)params.getValue("vectorSpaceSize");
		marketSplit = (Integer)params.getValue("marketSplit");
		adaptationThreshold = (Integer)params.getValue("adaptationThreshold");
		adaptationSpeed = (Integer)params.getValue("adaptationSpeed"); //From 1 to 5
		
		productElementChangeThreshold = (Integer)params.getValue("productElementChangeThreshold");
		customersPersuadability = (Integer)params.getValue("customersPersuadability");
		
		numberOfCustomers = (Integer)params.getValue("numberOfCustomers");
		sampleSizePercentage = (Integer)params.getValue("sampleSizePercentage");		

		numberOfEntrepreneurs = (Integer)params.getValue("numberOfEntrepreneurs");
		
		
		minAvailableMoney = (Double)params.getValue("minAvailableMoney");
		maxAvailableMoney = (Double)params.getValue("maxAvailableMoney");
		affordableLoss = (Integer)params.getValue("affordableLoss");

		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		networkGenerator = (String)params.getValue("networkGenerator");
		edgesPerStep = (Integer)params.getValue("edgesPerStep");
		utilityFunction = (String)params.getValue("utilityFunction");
		aggregateProductVector = (Boolean)params.getValue("aggregateProductVector");
		newConnectionsProbability = (Double)params.getValue("newConnectionsProbability");		
	}	
}
