/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		
		return context;
	}

}