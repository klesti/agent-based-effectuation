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
	
	//Common parameters
	
	public static int vectorSpaceSize;
	
	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static int productElementChangeThreshold;
	public static int numberOfCustomers;
	
	//Effectuation scenario
	public static int numberOfEntrepreneurs;
	public static int numberOfInvestors;
	public static int maxInitialGoals;
	public static int maxInitalMeans;
	public static int maxDepthForMeeting;
	public static int minMeetings;
	public static int maxMeetings;
	public static String networkGenerator;
	public static int edgesPerStep;	
	public static String utilityFunction;
	public static boolean aggregateProductVector;

	//Causation scenario

	public static int sampleSizePercentage; //Sample size in percentage
	public static int maxProviders; // Maximum number of providers	
	public static int meansOfferedWeightMin; // Means offered weight min 
	public static int meansOfferedWeightMax; // Means offered weight max
	public static int maxMarketResearchers;
	
	public static void initializeCausation() {
		commonInitialize();
		sampleSizePercentage = (Integer)params.getValue("sampleSizePercentage");
		maxProviders = (Integer)params.getValue("maxProviders");
		meansOfferedWeightMin = (Integer)params.getValue("meansOfferedWeightMin");
		meansOfferedWeightMax = (Integer)params.getValue("meansOfferedWeightMax");
		maxMarketResearchers = (Integer)params.getValue("maxMarketResearchers");
	}
	
	public static void initializeEffectuation() {
		commonInitialize();
		numberOfEntrepreneurs = (Integer)params.getValue("numberOfEntrepreneurs");
		numberOfInvestors = (Integer)params.getValue("numberOfInvestors");		
		maxInitialGoals = (Integer)params.getValue("maxInitialGoals");
		maxInitalMeans = (Integer)params.getValue("maxInitialMeans");
		maxDepthForMeeting = (Integer)params.getValue("maxDepthForMeeting");
		minMeetings = (Integer)params.getValue("minMeetings");
		maxMeetings = (Integer)params.getValue("maxMeetings");
		networkGenerator = (String)params.getValue("networkGenerator");
		edgesPerStep = (Integer)params.getValue("edgesPerStep");
		utilityFunction = (String)params.getValue("utilityFunction");
		aggregateProductVector = (Boolean)params.getValue("aggregateProductVector");
	}
	
	public static void commonInitialize() {				
		vectorSpaceSize = (Integer)params.getValue("vectorSpaceSize");
		productElementChangeThreshold = (Integer)params.getValue("productElementChangeThreshold");		
		numberOfCustomers = (Integer)params.getValue("numberOfCustomers");
	}
	
}
