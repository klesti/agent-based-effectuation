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
		JungNetwork<Object> N = (JungNetwork<Object>)network;
		
		Graph<Object, RepastEdge<Object>> G = N.getGraph();
		
		BetweennessCentrality<Object, RepastEdge<Object>> ranker = 
			new BetweennessCentrality<Object, RepastEdge<Object>>(G);
		
		ranker.evaluate();
		
		for (Object n: network.getNodes()) {
			if (n instanceof Effectuator || n instanceof Customer || 
					n instanceof Investor) {
				Agent a = (Agent)n;
				a.setBetweennessCentrality(ranker.getVertexRankScore(n));
			}
		}		
	}
	
	/**
	 *  Evolves network during the simulation (adding new nodes randomly)
	 */
	@ScheduledMethod(start=1)
	public static void evolveNetwork() {
		double prob = RandomHelper.nextDoubleFromTo(0, 1);
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r >= prob) {
			int i = RandomHelper.nextIntFromTo(1, 3);
			
			Object n;
			
			switch (i) {
				default:
					n = new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt()));
					break;
				case 2:
					Entrepreneur e = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
					e.generateGoal();
					n = e;
					break;
				case 3:
					n = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt()));
					break;
			}
			
			BarabasiAlbertNetworkGenerator ng = new BarabasiAlbertNetworkGenerator(context);
			ng.setNetwork(network);
			ng.attachNode(n);			
		}
	}

}