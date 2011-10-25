/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.DirectedJungNetwork;

/**
 * @author klesti
 *
 */
public class CausationBuilder implements ContextBuilder<Object>  {

	//Parameters, later will be prodived by the user interface
	public static final int vectorSpaceSize = 10;
	public static final int numberOfCustomers = 100;
	public static final int sampleSize = 5; //Sample size in percentage
	// The percentage of the customers sample that needs to have a product element as 1
	// in order to change the initial value of the product elements vector
	public static final int productElementChangeThreshold = 50; 
	
	
	protected Schedule schedule;
	protected Context context;

	@Override
	public Context build(Context<Object> context) {
		
		this.context = context;
		
		context.setId("causation");		
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("network",
				context, true);
		DirectedJungNetwork<Object> network = (DirectedJungNetwork<Object>) netBuilder.buildNetwork();
		

		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(network, "Causator");
		Goal initialGoal = new Goal(vectorSpaceSize, true);
		
		causator.setGoal(initialGoal);
		context.add(causator);
		context.add(initialGoal);
		network.addEdge(causator, initialGoal);		
		
		//Add the customers to the network	
		
		for (int i = 0; i < numberOfCustomers; i++) {
			Customer c = new Customer(network, "Customer" + String.valueOf(i));
			context.add(c);
		}
	
		//Schedule actions
		
		scheduleActions();
		
		return context;
	}
	
	public void scheduleActions() {
		schedule = new Schedule();
		
		//Hire market researchers		
		Causator causator = (Causator)context.getObjects(Causator.class).get(0);		
		ScheduleParameters parameters = ScheduleParameters.createOneTime(1);
		schedule.schedule(parameters, causator, "hireMarketResearchers", (Object)null);
		
		//Spread the customers sample among the hired market researchers
		
		ScheduleParameters parameters2 = ScheduleParameters.createOneTime(2);
		schedule.schedule(parameters2, causator, "selectAndSpreadMarketSample", (Object)null);
	}
	
}
