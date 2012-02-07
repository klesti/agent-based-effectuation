package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Entrepreneur extends Agent {
	protected List<Means> availableMeans;
	protected List<Customer> customers;
	protected Goal goal;
	
	public Entrepreneur(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
		customers = new ArrayList<Customer>();
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		for (Customer c: this.customers) {
			context.remove(c);
		}
		for (Customer c: customers) {
			if (!context.contains(c)) {
				context.add(c);
			}
			network.addEdge(this, c);
		}
		this.customers = customers;
	}
	
	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
		for (Means m: this.availableMeans) {
			context.remove(m);
		}
		for (Means m: availableMeans) {
			if (!context.contains(m)) {
				context.add(m);
			}
			network.addEdge(this, m);
		}
		this.availableMeans = availableMeans;
	}
	
	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}


	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Goal goal) {		
		if (this.goal != null) {
			context.remove(this.goal);
		}
		if (!context.contains(goal)) {
			context.add(goal);
		}
		network.addEdge(this, goal);
		this.goal = goal;
	}
	
	public void addMeans(Means m) {
		if (!context.contains(m)) {
			context.add(m);
		}
		network.addEdge(this, m);
		availableMeans.add(m);
	}
	
	
	/**
	 * To be called only in the effectuation scenario 
	 */
	public void generateAvailableMeans() {
		int totalMeans = RandomHelper.nextIntFromTo(1, EffectuationBuilder.maxInitalMeans);
		
		for (int i = 0; i < totalMeans; i++) {
			Means m = new Means("Means" + RandomHelper.nextInt());
			availableMeans.add(m);
			context.add(m);
			network.addEdge(this, m);
		}
	}
	
	
	
	/**
	 *  To be called only in the effectuation scenario
	 *  @description Generate goal based on the available means (require all of them)
	 */
	public void generateGoal() {
		goal = new Goal(context, network);
		context.add(goal);
		goal.generateRandomProductVector();

		if (availableMeans.size() == 0) {
			generateAvailableMeans();
		}			
		goal.setRequiredMeans(availableMeans);
		network.addEdge(this, goal);
	}
	
	/**
	 * Effectuation scenario:
	 * Aggregate goal product vector based on the demand of the surrounding customers
	 */
	public void aggregateGoalProductVector() {
		int depth = RandomHelper.nextIntFromTo(1, EffectuationBuilder.maxDepthForMeeting);
		
		List<Customer> customers = new ArrayList<Customer>(); 
		
		EffectuationBuilder.getCustomerAcquiantances(this, depth, customers);		
		
		if (customers.size() > 0) {
			Collections.shuffle(customers);
			
			int sampleTotal = RandomHelper.nextIntFromTo(1, customers.size());
			
			//Survey random connected customers sample
			
			int[] surveyResults = new int[Parameters.vectorSpaceSize];
			
			for (int i = 0; i < surveyResults.length; i++) {
				surveyResults[i] = 0;
			}			
			
			for (int i = 0; i < sampleTotal; i++) {
				int[] demandVector = customers.get(i).demandVector;
				
				for (int j = 0; j < demandVector.length; j++) {
					surveyResults[j] += demandVector[j];
				}
			}
			
			//Refine product vector
			
			if (goal == null) {
				generateGoal();
			}
			
			int[] productVector = goal.getProductVector();
			
			for (int i = 0; i < surveyResults.length; i++) {				
				if ( ((double)surveyResults[i] / (double)sampleTotal) * 100 
						>= Parameters.productElementChangeThreshold) {					
					productVector[i] = (productVector[i] + 1) % 2;				
				}
			}
		}
	}
}
