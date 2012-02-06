/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.UndirectedJungNetwork;

/**
 * @author klesti
 *
 */
public class EffectuationBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	//Parameters, later will be prodived by the user interface
	public static final int numberOfCustomers = 100;	
	public static final int maxInitialGoals = 3;
	public static final int maxInitalMeans = 3;
	public static final int maxDepthForMeeting = 3;
	public static final int minMeetings = 3;
	public static final int maxMeetings = 10;
	public static Context<Object> context;
	public static Network<Object> network;	
	
	//Entrepreneurial network caching
	
	private static int cachedTotalENNodes = 0;
	private static int cachedTotalENEdges = 0;
	private static JungNetwork<Object> entrepreneurialNetwork;
	
	
	@Override
	public Context<Object> build(Context<Object> context) {
		
		context.setId("effectuation");
				
		EffectuationBuilder.context = context;
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("effectuation network",
				context, true);
		
		EffectuationBuilder.network  = netBuilder.buildNetwork();	
		 
		
		//Add the effectuator entrepreneur and it's initial available means
		Effectuator effectuator = new Effectuator(context, network, "Effectuator");
		context.add(effectuator);
		effectuator.generateAvailableMeans();
		
		// Generate initial goal(s) based on the available means	
			
		int totalInitialGoals = RandomHelper.nextIntFromTo(1, maxInitialGoals);
		
		for (int i = 0; i < totalInitialGoals; i++) {
			
			Goal g = new Goal(context, network);
			context.add(g);
			//First goal requires all available means
			if (i == 0) {
				g.setRequiredMeans(effectuator.getAvailableMeans());				
			} else {
				//Require a random number of the available means
				Collections.shuffle(effectuator.getAvailableMeans());
				int randomNumber = RandomHelper.nextIntFromTo(1, effectuator.getAvailableMeans().size());
				
				List<Means> requiredMeans = new ArrayList<Means>();
				
				for (int j = 0; j < randomNumber; j++) {
					requiredMeans.add(effectuator.getAvailableMeans().get(j));					
				}				
				g.setRequiredMeans(requiredMeans);
			}			
			effectuator.addGoal(g);
		}
		
						
		//Network generation
		
		BarabasiAlbertNetworkGenerator ng = new BarabasiAlbertNetworkGenerator(context);
		EffectuationBuilder.network = ng.createNetwork(EffectuationBuilder.network);
		
		// Refine the product vector of the entrepreneurs based on the
		// connected customers (randomly)
		
		double prob = RandomHelper.nextDoubleFromTo(0, 1);
		
		for (Object o: context.getObjects(Entrepreneur.class)) {
			double r = RandomHelper.nextDoubleFromTo(0, 1);
			
			if (r >= prob) {
				Entrepreneur e = (Entrepreneur)o;
				e.aggregateGoalProductVector();
			}
		}
		
		return context;
	}
	
	/**
	 * Calculates the betweenness centrality for each node, using the JUNG implemented
	 * betweenness centrality calculator algorithm 
	 */
	public static void calculateBetweennesCentralities() {
		JungNetwork<Object> N = EffectuationBuilder.getEntrepreneurialNetwork();
		
		Graph<Object, RepastEdge<Object>> G = N.getGraph();
		
		BetweennessCentrality<Object, RepastEdge<Object>> ranker = 
			new BetweennessCentrality<Object, RepastEdge<Object>>(G);
		
		ranker.evaluate();
		
		for (Object n: N.getNodes()) {
			Agent a = (Agent)n;
			a.setBetweennessCentrality(ranker.getVertexRankScore(n));
		}	
	}
	
	/**
	 * Returns the "entrepreneurial network density"
	 * density = 2 * number of edges / N * (N-1)
	 * @return networkDensity
	 */
	public static double getNetworkDensity() {
		Network<Object> n = getEntrepreneurialNetwork();
		return ( 2 * n.getDegree() ) / ( n.size() * (n.size()-1) );
	}
	
	/**
	 * Returns the network without means and goals
	 * @return Network
	 */
	public static JungNetwork<Object> getEntrepreneurialNetwork() {
		
		//Update cache if needed
		if (cachedTotalENNodes != network.size() || cachedTotalENEdges != network.numEdges()) {			
			entrepreneurialNetwork = new UndirectedJungNetwork<Object>("entrepreneurial network");
			for (Object o: network.getNodes()) {
				if (!(o instanceof Means) && !(o instanceof Goal)) {
					entrepreneurialNetwork.addVertex(o);
				}
			}
			
			for (RepastEdge<Object> e: network.getEdges()) {
				if (e.getTarget() instanceof Goal ||  e.getTarget() instanceof Means 
						|| e.getSource() instanceof Goal || e.getTarget() instanceof Means) {					
					continue;				
				}
				entrepreneurialNetwork.addEdge(e);			
			}
		}
		
		return entrepreneurialNetwork;
	}
	
	/**
	 *  Evolves network during the simulation (adding new nodes randomly)
	 */
	@ScheduledMethod(start=2)
	public static void evolveNetwork() {
		double prob = RandomHelper.nextDoubleFromTo(0, 1);
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r >= prob) {
			BarabasiAlbertNetworkGenerator ng = new BarabasiAlbertNetworkGenerator(context);
			ng.setNetwork(network);			
			
			int random = RandomHelper.nextIntFromTo(1, 3);
			
			switch (random) {
				default:
					Customer c = new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt()));
					ng.attachNode(c);	
					break;
				case 2:
					Entrepreneur e = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));					
					ng.attachNode(e);	
					e.generateGoal();
					break;
				case 3:
					Investor i = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt()));
					ng.attachNode(i);
					i.generateGoal();
					break;
			}
		}
	}

}