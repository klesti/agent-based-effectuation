/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class SimulationBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> network;
	public static Network<Object> effectuationNetwork;	
	private EntrepreneurialNetworkGenerator networkGenerator;
	private static HashMap<String, Integer> lastIds;
	
	@Override
	public Context<Object> build(Context<Object> context) {
		Parameters.initialize();
		
		lastIds = new HashMap<String, Integer>();
		
		context.setId("effectuation-causation");
				
		SimulationBuilder.context = context;
		
		buildNetworks();
		
		//Add the effectuator entrepreneur 
		Effectuator effectuator = new Effectuator(context, network, "Effectuator");
		context.add(effectuator);
		effectuator.generateAvailableMeans();
		
		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(context, network, "Causator");
		context.add(causator);
		
		Goal initialGoal = new Goal();		
		causator.setGoal(initialGoal);		
		
		context.add(causator);

		
		//Network generation
		
		if (Parameters.networkGenerator.equals("BarabasiAlbert")) {
			networkGenerator = new BarabasiAlbertNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("CopyModel")) {
			networkGenerator = new CopyingModelNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("RandomNetwork")) {
			networkGenerator = new RandomNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("SmallWorld")) {
			networkGenerator = new SmallWorldNetworkGenerator(context);
		}
		
		networkGenerator.setTotalCustomers(Parameters.numberOfCustomers);
		networkGenerator.setTotalEntrepreneuers(Parameters.numberOfEntrepreneurs);
		networkGenerator.seEdgesPerStep(Parameters.edgesPerStep);
		
		network = networkGenerator.createNetwork(network);
		
		initializeDemandVectors();
		aggregateProductVectors();
		
		scheduleActions();		
		
		return context;
	}

	/**
	 * @param context
	 */
	private void buildNetworks() {
		EdgeCreator<NetworkEdge, Object> edgeCreator = new EdgeCreator<NetworkEdge, Object>(
				
		) {
			public Class<NetworkEdge> getEdgeType() {
				return NetworkEdge.class;
			}

			@Override
			public NetworkEdge createEdge(Object source, Object target,
					boolean isDirected, double weight) {
				return new NetworkEdge(source, target, true, 0);
			}
		};
		
		
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
	 * Returns the the bits that are different in two equal 0-1 strings
	 * @param s1
	 * @param s2
	 * @return s
	 */
	public static String diff(String s1, String s2) {
		String s = "";
		
		for (int i = 0; i < s1.length(); i++) {
			s += String.valueOf(
					Integer.parseInt(String.valueOf(s1.charAt(i))) 
							^ Integer.parseInt(String.valueOf(s2.charAt(i)))
							);
		}
		
		return s;
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
			
			//Skip causator 
			
			if (o instanceof Causator) {
				continue;
			}
			
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			if (r >= prob) {
				Entrepreneur e = (Entrepreneur)o;
				e.aggregateGoalProductVector();
			}
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
	
	@ScheduledMethod(start=1,priority=2,interval=5)
	public void printTest() {
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			int count = 0;
			for (Object o: context.getObjects(Customer.class)) {
				Customer c = (Customer)o;
				if (c.getDemandVector()[i] == 1) 
					count++;
			}
			System.out.print(String.valueOf(count) + " ");
		}
		System.out.println("");
	}
}
