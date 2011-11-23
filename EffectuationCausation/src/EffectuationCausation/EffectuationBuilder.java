/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class EffectuationBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	//Parameters, later will be prodived by the user interface
	public static final int numberOfCustomers = 100;	
	public static final int maxInitialGoals = 3;
	public static final int maxInitalMeans = 3;
	public static Context<Object> context;
	public static Network<Object> network;	
	
	@Override
	public Context<Object> build(Context<Object> context) {
		
		context.setId("effectuation");
				
		EffectuationBuilder.context = context;
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("effectuation network",
				context, true);
		
		network  = netBuilder.buildNetwork();	
		 
		
		//Add the effectuator entrepreneur and it's initial available means
		Effectuator effectuator = new Effectuator(network, "Effectuator");
		context.add(effectuator);
		effectuator.generateAvailableMeans();
		
		// Generate initial goal(s) based on the available means	
		
		
		int totalInitialGoals = RandomHelper.nextIntFromTo(1, maxInitialGoals);
		
		for (int i = 0; i < totalInitialGoals; i++) {
			Goal g = new Goal();
			context.add(g);
			network.addEdge(effectuator, g);
		}
		
		//Network generation
		
		

		return context;
	}

}