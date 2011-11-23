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
	public static final int maxInitialGoals = 5;
	public static Network<Object> network;
	
	@Override
	public Context build(Context<Object> context) {
		
		context.setId("effectuation");
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("effectuation network",
				context, true);
		
		network  = netBuilder.buildNetwork();	
		
		//Add the effectuator entrepreneur and it's initial goal(s)
		Effectuator effectuator = new Effectuator(network, "Effectuator");
		context.add(effectuator);
		
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