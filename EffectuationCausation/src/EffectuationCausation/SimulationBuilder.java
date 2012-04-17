/**
 * 
 */
package EffectuationCausation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
/**
 * @author klesti
 *
 */
public class SimulationBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> network;
	public static Network<Object> effectuationNetwork;	
	private static EntrepreneurialNetworkGenerator networkGenerator;
	private static HashMap<String, Integer> lastIds;
	public static double[]  productElementCost;
	public static boolean allEntrepreneursOffering;
	public static Effectuator effectuator;
	public static Causator causator;
	public static ArrayList<Customer> customers;
	private static ArrayList<int[]> oldDemand;
	public static int staticDemandSteps;
	
	
	@Override
	public Context<Object> build(Context<Object> context) {
		Parameters.initialize();
		
		customers = new ArrayList<Customer>();
		oldDemand = new ArrayList<int[]>();
		staticDemandSteps = 0;
		
		generateProductElementCosts();
		
		lastIds = new HashMap<String, Integer>();
		
		context.setId("effectuation-causation");
				
		SimulationBuilder.context = context;
		
		buildNetworks();
		
		if (!Parameters.observedEntrepreneur.equals("Causator")) {
			//Create the effectuator entrepreneur
			effectuator = new Effectuator(context, network, "Effectuator");
		} else {
			effectuator = null;
		}
		
		if (!Parameters.observedEntrepreneur.equals("Effectuator")) {
			//Create causator entrepreneur and it's initial goal
			causator = new Causator(context, network, "Causator");			
			Goal initialGoal = new Goal();		
			causator.setGoal(initialGoal);		
		} else {
			causator = null;
		}
		
		//Network generation
		
		if (Parameters.networkGenerator.equals("BarabasiAlbert")) {
			networkGenerator = new BarabasiAlbertNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("CopyModel")) {
			networkGenerator = new CopyingModelNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("RandomNetwork")) {
			networkGenerator = new RandomNetworkGenerator(context,Parameters.randomNetworkDensity);
		} else if (Parameters.networkGenerator.equals("SmallWorld")) {
			networkGenerator = new SmallWorldNetworkGenerator(context, Parameters.meanDegree);
		}
		
		networkGenerator.setTotalCustomers(Parameters.numberOfCustomers);
		networkGenerator.setTotalEntrepreneuers(Parameters.numberOfEntrepreneurs);
		networkGenerator.seEdgesPerStep(Parameters.edgesPerStep);
		networkGenerator.setEdgeProbability(Parameters.edgeProbability);
		
		network = networkGenerator.createNetwork(network);
		
		initializeDemandVectors();
		aggregateProductVectors();
		
		if (!Parameters.observedEntrepreneur.equals("Effectuator")) {		
			//Define the causator's product based on preliminary market research 
			causator.performInitialMarketResearch();
			causator.refineProduct();
		}
		
		calculateBetweennesCentralities();
		
		allEntrepreneursOffering = false;
		
		scheduleActions();	
		
		return context;
	}

	/**
	 * @param context
	 */
	private void buildNetworks() {
		EdgeCreator<CustomNetworkEdge, Object> edgeCreator = new CustomEdgeCreator();		
		
		//Build Entrepreneurial network
		
		NetworkBuilder<Object> netBuilder2 = new NetworkBuilder<Object>("full network",
				context, false);
		
		netBuilder2.setEdgeCreator(edgeCreator);		
		
		network = netBuilder2.buildNetwork();
		
		//Build Effectuation network
		
		NetworkBuilder<Object> netBuilder3 = new NetworkBuilder<Object>("effectual network",
				context, false);
		
		netBuilder3.setEdgeCreator(edgeCreator);	
		
		effectuationNetwork = netBuilder3.buildNetwork();
	}

	/**
	 * Returns the next Id of a node, also by specifying a certain prefix
	 * @param prefix
	 * @return nextId
	 */
	public static String nextId(String prefix) {
		int nextId;
		
		if (lastIds.containsKey(prefix)) {
			nextId = lastIds.get(prefix) + 1;
			lastIds.put(prefix, nextId);
		} else {
			nextId = 1;
			lastIds.put(prefix, nextId);
		}
		
		return prefix + String.valueOf(nextId);
	}
	
	/**
	 * Returns the the bits that are different in two equal length 0-1 vectors
	 * @param p1
	 * @param p2
	 * @return s
	 */
	public static int[] diff(int[] p1, int[] p2) {
		int[] diff = new int[p1.length];
		
		for (int i = 0; i < p1.length; i++) {
			diff[i] = p1[i] ^ p2[i];
		}
		
		return diff;
	}
	
	/**
	 * Returns the "Hamming distance" between two equal length 0-1 vectors
	 * @param p1
	 * @param p2
	 * @return int
	 */
	public static int hammingDistance(int[] p1, int[] p2) {
		int d = 0;
		
		for (int i = 0; i < p1.length; i++) {
			d += p1[i] ^ p2[i];
		}
		
		return d;		
	}
	
	/**
	 * Recursively get all customer acquaintances of a node using a specified network depth.
	 * @param n node
	 * @param depth
	 * @param List of customers acquaintances
	 */
	public static void getCustomerAcquiantances(Object n, int depth, List<Customer> customers) {		
		for (Object o: network.getAdjacent(n)) {
			if (o instanceof Customer) {
				customers.add((Customer)o);
			}
			if (depth > 1) {
				getCustomerAcquiantances(o, depth-1, customers);
			}
		}
	}
	
	public static void printMessage(String m) {
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance()
        .getCurrentSchedule();
		
		System.out.println(m + " - " + String.valueOf(schedule.getTickCount()));
	}
	
	/**
	 * Randomly generate product element costs
	 */
	private void generateProductElementCosts() {
		productElementCost = new double[Parameters.vectorSpaceSize];
		double avgAvailableMoney = (Parameters.minAvailableMoney 
				+ Parameters.maxAvailableMoney) / 2.0;
		
		for (int i = 0; i < productElementCost.length; i++) {
			productElementCost[i] = RandomHelper.nextIntFromTo(1, 3) 
										* Parameters.minAvailableMoney;
			productElementCost[i] = productElementCost[i] > avgAvailableMoney 
										? avgAvailableMoney : productElementCost[i];
		}
	}
	

	/**
	 *  Initialize customer demand vectors using the define "Market split", i.e
	 *  the percentage of customers that "like" a certain product element
	 */
	private void initializeDemandVectors() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		for (Object c: context.getObjects(Customer.class)) {
			customers.add((Customer)c);
		}
		
		int shouldLikeProductElement = (int)Math.ceil(((double)Parameters.marketSplit / 100) * customers.size());
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			ArrayList<Customer> copy = new ArrayList<Customer>(customers);
			
			for (int j = 0; j < shouldLikeProductElement; j++) {
				Customer c = copy.remove(RandomHelper.nextIntFromTo(0, copy.size() - 1));
				c.getDemandVector()[i] = 1;
			}
		}
	}
	
	/**
	 * Refine the product vector of the entrepreneurs based on the
	 * connected customers (randomly)
	 */
	public void aggregateProductVectors() {
		
		if (!Parameters.aggregateProductVector) {
			return;
		}
		
		double prob = RandomHelper.nextDoubleFromTo(0, 1);
		
		for (Object o: context.getObjects(Entrepreneur.class)) {
			
			//Skip causator and effectuator
			
			if (o instanceof Causator || o instanceof Effectuator) {
				continue;
			}
			
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			if (r >= prob) {
				Entrepreneur e = (Entrepreneur)o;
				e.aggregateGoalProductVector();
			}
		}		
	}
	
	/**
	 * Caches the old customers demand
	 */
	@ScheduledMethod(priority=1,interval=2,start=1)
	public void cacheOldCustomersDemand() {
		oldDemand.clear();
		
		for (Customer c: customers) {		
			oldDemand.add(c.getDemandVector().clone());
		}
	}
	
	/**
	 * Checks if all entrepreneurs are offering and update the relevant flag
	 */
	@Watch(watcheeClassName="EffectuationCausation.Entrepreneur",
			watcheeFieldNames="offering",whenToTrigger=WatcherTriggerSchedule.IMMEDIATE)
	public void checkAllEntrepreneursOffering() {
		for (Object o: context.getObjects(Entrepreneur.class)) {
			if (!((Entrepreneur)o).isOffering()) {
				allEntrepreneursOffering = false;
			}
			return;
		}
		allEntrepreneursOffering = true;
	}
	
	/**
	 * Calculates the betweenness centrality for each node, using the JUNG implemented
	 * betweenness centrality calculator algorithm 
	 */
	
	public void calculateBetweennesCentralities() {			
		
		ContextJungNetwork<Object> N = (ContextJungNetwork<Object>)network;
		
		Graph<Object, RepastEdge<Object>> G = N.getGraph();
		
		BetweennessCentrality<Object, RepastEdge<Object>> ranker = 
			new BetweennessCentrality<Object, RepastEdge<Object>>(G);
		
		ranker.setRemoveRankScoresOnFinalize(false);
		ranker.evaluate();
		
		for (Object n: network.getNodes()) {
			Agent a = (Agent)n;
			// Normalize by (n-1)(n-2)/2
			a.setBetweennessCentrality(ranker.getVertexRankScore(n) 
					/ (((N.size()-1) * (N.size()-2)) / 2.0) );
		}	
	}
	
	/**
	 * Returns the "network density"
	 * density = 2 * number of edges / N * (N-1)
	 * @return networkDensity
	 */
	public double getNetworkDensity() {
		
		return ( 2.0 * network.numEdges() ) / ( network.size() * (network.size()-1) );
	}
	
	/**
	 * Returns the average clustering coefficient of the network 
	 * (as calculated by Watts and Strogatz)
	 * @return C
	 */
	public double getClusteringCoefficient() {
		double C = 0;
		
		
		Map<Object, Double> cc = Metrics.clusteringCoefficients(((ContextJungNetwork<Object>)network).getGraph());
		
		for (Object n: network.getNodes()) {
			C += cc.get(n) / network.size();
		}
		
		return C;
	}
	
	/**
	 * Returns the percentage of customers that have changed their demand
	 * @return demandChange
	 */
	public double getDemandChange() {
		int changed = 0;
		
		for (int i = 0; i < oldDemand.size(); i++) {
			if (!Arrays.equals(oldDemand.get(i), customers.get(i).getDemandVector())) {
				changed++;
			}
		}
		
		return ((double)changed / oldDemand.size()) * 100.0;
	}
	
	/**
	 * Returns the effectuator's betweenness centrality in the network
	 * @return betweennessCentrality
	 */
	public double getEffectuatorsBetweennessCentrality() {		 
		return effectuator.getBetweennessCentrality();
	}

	/**
	 * Returns the effectuator's degree centrality in the entrepreneurial network
	 * @return degreeCentrality
	 */	
	public double getEffectuatorsDegreeCentrality() {
		//Return the normalized effectuators degree centrality
		return network.getDegree(effectuator) / (double)(network.size() - 1);		
	}	

	public String getEffectuatorsGoal() {
		return effectuator.getGoal().printProductVector();
	}
	
	public String getCausatorsGoal() {
		return causator.getGoal().printProductVector();
	}
	
	public double getEffectuatorsMarketFit() {
		int meetDemand = 0;		
		
		for (int i = 0; i < customers.size(); i++) {
			if (hammingDistance(effectuator.getGoal().getProductVector(), 
					customers.get(i).getDemandVector()) < Math.ceil(Parameters.vectorSpaceSize / 4.0)) {
				meetDemand++;
			}
		}
		
		return (meetDemand / (double)customers.size()) * 100.0;
	}
	
	public double getCausatorsMarketFit() {
		int meetDemand = 0;		
		
		for (int i = 0; i < customers.size(); i++) {
			if (hammingDistance(causator.getGoal().getProductVector(), 
					customers.get(i).getDemandVector()) < Math.ceil(Parameters.vectorSpaceSize / 4.0)) {
				meetDemand++;
			}
		}
		
		return (meetDemand / (double)customers.size()) * 100.0;
	}
	
	/**
	 *  Evolves network (if set) during the simulation (adding new nodes randomly)
	 */
	@ScheduledMethod(start=1,interval=2)
	public void evolveNetwork() {
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);	
		
		if (r < Parameters.newConnectionsProbability) {
			int random = RandomHelper.nextIntFromTo(1, 5);
			
			Object attached;
			
			switch (random) {
				default:
					Customer c = new Customer(context, network, nextId("C"));
					networkGenerator.attachNode(c);
					attached = c;
					break;
				case 2:
					Entrepreneur e = new Entrepreneur(context, network, nextId("E"));	
					e.generateGoal();
					networkGenerator.attachNode(e);					
					attached = e;				
					break;
			}
			
			for (RepastEdge<Object> edge: network.getEdges(attached)) {
				((CustomNetworkEdge) edge).setThickness(2.0); 
				((CustomNetworkEdge) edge).setColor(Color.red);
			}		
			
			calculateBetweennesCentralities();
		}	
	}	
	
	@ScheduledMethod(start=5,interval=1)
	public void stopSimulation() {
		if (repast.simphony.engine.environment.RunEnvironment.
				getInstance().getCurrentSchedule().getTickCount() >= 500) {
			repast.simphony.engine.environment.RunEnvironment.getInstance().endRun();
		}
		double demandChange = getDemandChange();
		
		if (demandChange == 0.0) {
			staticDemandSteps++;
		} else {
			staticDemandSteps = 0;
		}
		
		boolean observedEntrepreneursOffering = false;
		
		if (effectuator == null) {
			observedEntrepreneursOffering = causator.isOffering();
		} else if (causator == null) {
			observedEntrepreneursOffering = effectuator.isOffering();
		} else {
			observedEntrepreneursOffering = causator.isOffering() && effectuator.isOffering();
		}
		
		if (observedEntrepreneursOffering && staticDemandSteps >= 50) {
			repast.simphony.engine.environment.RunEnvironment.getInstance().endRun();
		}
	}
	
	public void scheduleActions() {
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance()
								.getCurrentSchedule();
		
		
		//Schedule adaptProductVector for each Customer
		
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		for (Object c: context.getObjects(Customer.class)) {
			customers.add((Customer)c);
		}
		
		ScheduleParameters parameters = 
			ScheduleParameters.createRepeating(1, 6 - Parameters.adaptationSpeed, 1);		
		
		schedule.scheduleIterable(parameters, customers, "adaptProductVector", true);		
	}

}
