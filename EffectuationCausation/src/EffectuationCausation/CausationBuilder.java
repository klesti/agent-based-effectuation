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

	//Parameters, later will be prodived by the user interface
	public int vectorSpaceSize = 10;
	public int numberOfCustomers = 100;
	public int sampleSize = 5; //Sample size in percentage 

	@Override
	public Context build(Context<Object> context) {
		
		
		context.setId("causation");
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("network",
				context, true);
		DirectedJungNetwork<Object> network = (DirectedJungNetwork<Object>) netBuilder.buildNetwork();
		

		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(network, "Causator", this);
		Goal initialGoal = new Goal(vectorSpaceSize, true);
		
		causator.setGoal(initialGoal);
		network.addEdge(causator, initialGoal);		
		
		context.add(causator);
		context.add(initialGoal);
		
		//Add the required means to the network
		
		for (Means m: initialGoal.getRequiredMeans()) {
			context.add(m);
			network.addEdge(initialGoal, m);
		}
		
		//Add the customers to the network	
		
		for (int i = 0; i < numberOfCustomers; i++) {
			Customer c = new Customer(network, "Customer" + String.valueOf(i), vectorSpaceSize);
			context.add(c);
		}
	
		return context;
	}
	
}
