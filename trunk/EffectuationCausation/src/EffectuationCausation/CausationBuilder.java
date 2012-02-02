/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class CausationBuilder extends DefaultContext<Object> implements ContextBuilder<Object>	 {

	//Parameters, later will be prodived by the user interface
	public static final int numberOfCustomers = 100;
	public static final int sampleSizePercentage = 5; //Sample size in percentage
	public static int sampleSize = 0; //Sample size in percentage

	public static final int maxProviders = 10; // Maximum number of providers	
	public static final int[] meansOfferedWeightRange = {1,10}; // Means offered weight range 
	
	public static Context<Object> context;
	public static Network<Object> network;
	public static List<Means> offeredMeans; // List of all offered means (by all providers) 

	@Override
	public Context<Object> build(Context<Object> context) {		
		context.setId("causation");
		
		CausationBuilder.context = context;
		
		offeredMeans = new ArrayList<Means>();

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("causation network",
				context, true);
		
		network  = netBuilder.buildNetwork();
		
		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(context, network, "Causator");
		context.add(causator);
		
		Goal initialGoal = new Goal(context,network);		
		
		context.add(initialGoal);
		initialGoal.generateRequiredMeans();
		
		context.add(causator);
		causator.setGoal(initialGoal);		
		
		//Add the customers to the network	
		
		for (int i = 0; i < numberOfCustomers; i++) {
			Customer c = new Customer(context, network, "Customer" + String.valueOf(i));
			context.add(c);
		}
	
		
		//Add the providers to the network
		
		int numberOfProviders = RandomHelper.nextIntFromTo(3, maxProviders);
		
		for (int i = 0; i < numberOfProviders; i++) {
			Provider p = new Provider(context, network, "Provider" + String.valueOf(i));
			context.add(p);
			p.generateOfferedMeans();
		}
				
		//Schedule actions
		
		scheduleActions();		
				
		return context;
	}
	
	public void scheduleActions() {
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment.getInstance()
        .getCurrentSchedule();
		
		//Hire market researchers		
		Causator causator = (Causator)CausationBuilder.context.getObjects(Causator.class).get(0);
		ScheduleParameters parameters = ScheduleParameters.createOneTime(1);
		schedule.schedule(parameters, causator, "hireMarketResearchers");
				
		//Spread the customers sample among the hired market researchers
		
		ScheduleParameters parameters2 = ScheduleParameters.createOneTime(2);
		schedule.schedule(parameters2, causator, "selectAndSpreadMarketSample");		
	}
}
