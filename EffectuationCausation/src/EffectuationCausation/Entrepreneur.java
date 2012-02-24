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
	protected Goal goal;
	
	public Entrepreneur(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
	}

	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
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
		this.goal = goal;
	}
	
	public void addMeans(Means m) {
		availableMeans.add(m);
	}
	
	
	/**
	 * To be called only in the effectuation scenario 
	 */
	public void generateAvailableMeans() {
		int totalMeans = RandomHelper.nextIntFromTo(1, Parameters.maxInitalMeans);
		
		for (int i = 0; i < totalMeans; i++) {
			Means m = new Means("M" + SimulationBuilder.nextId("M"));
			availableMeans.add(m);
		}
	}
		
	/**
	 *  To be called only in the effectuation scenario
	 *  @description Generate goal based on the available means (require all of them)
	 */
	public void generateGoal() {
		goal = new Goal();
		goal.generateRandomProductVector();

		if (availableMeans.size() == 0) {
			generateAvailableMeans();
		}			
		goal.setRequiredMeans(availableMeans);
	}
	
	/**
	 * Effectuation scenario:
	 * Aggregate goal product vector based on the demand of the surrounding customers
	 */
	public void aggregateGoalProductVector() {
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		List<Customer> customers = new ArrayList<Customer>(); 
		
		SimulationBuilder.getCustomerAcquiantances(this, depth, customers);		
		
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
