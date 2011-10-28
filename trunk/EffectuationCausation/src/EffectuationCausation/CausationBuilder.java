/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.DirectedJungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.util.ContextUtils;

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
	public static final int maxProviders = 10; // Maximum number of providers	
	public static final int[] meansOfferedWeightRange = {1,10}; // Means offered weight range 
	
	protected Schedule schedule;
	protected Context<Object> context;
	protected Network<Object> network;
	public static List<Means> offeredMeans; // List of all offered means (by all providers) 

	@Override
	public Context<Object> build(Context<Object> context) {
		
		this.context = context;		
		
		context.setId("causation");
		
		offeredMeans = new ArrayList<Means>();
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("network",
				context, true);
		
		this.network  = (DirectedJungNetwork<Object>) netBuilder.buildNetwork();
		
		

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
	
		
		//Add the providers to the network
		
		int numberOfProviders = RandomHelper.nextIntFromTo(3, maxProviders);
		
		for (int i = 0; i < numberOfProviders; i++) {
			Provider p = new Provider(network, "Provider" + String.valueOf(i));
			context.add(p);
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
