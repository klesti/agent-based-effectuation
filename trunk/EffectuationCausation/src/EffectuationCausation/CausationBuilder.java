/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.graph.DirectedJungNetwork;

/**
 * @author klesti
 *
 */
public class CausationBuilder implements ContextBuilder<Object>  {

	@Override
	public Context build(Context<Object> context) {
		context.setId("causation");
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("network",
				context, true);
		DirectedJungNetwork<Object> network = (DirectedJungNetwork<Object>) netBuilder.buildNetwork();
		
		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(network, "Causator");
		Goal initialGoal = new Goal(true);
		
		causator.goal = initialGoal;
		network.addEdge(causator, initialGoal);		
		
		context.add(causator);
		context.add(initialGoal);
		
		//Add the required means to the network
		
		for (Means m: initialGoal.getRequiredMeans()) {
			context.add(m);
			network.addEdge(initialGoal, m);
		}
		
		//Add the customers to the network
		
		int numberOfCustomers = 100;
		
		for (int i = 0; i < numberOfCustomers; i++) {
			Customer c = new Customer(network, "Customer" + String.valueOf(i));
			context.add(c);
		}
	
		return context;
	}
	
}
