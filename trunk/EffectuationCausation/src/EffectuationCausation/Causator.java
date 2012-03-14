package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Causator extends Entrepreneur {
	
	protected int[] aggregatedSurveyResults;

	public Causator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		availableMeans = new ArrayList<Means>();
		//Initialize aggregatedSurveyResults
		aggregatedSurveyResults = new int[Parameters.vectorSpaceSize];
		generateGoal();
	}

	public void generateGoal() {
		super.generateGoal();
		
		Means m = new Means(SimulationBuilder.nextId("M"));
		m.setKnowHow(goal.getProductVector());
		m.setMoney(RandomHelper.nextDoubleFromTo(Parameters.minAvailableMoney, 
				Parameters.maxAvailableMoney));
		goal.setRequiredMeans(m);
	}
	
	/**
	 *  Clear the aggreagated survey results
	 */
	public void clearAggregatedSurveyResults() {
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {
			aggregatedSurveyResults[i] = 0;
		}
	}	
	
	/**
	 *  Perfoms market research in a random customers sample (ask for their demand)
	 */
	public void performMarketResearch() {
		clearAggregatedSurveyResults();
		
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		for (Object c: context.getObjects(Customer.class)) {
			customers.add((Customer)c);
		}
		
		Collections.shuffle(customers);
		
		
		int sampleSize = (int)Math.ceil((Parameters.sampleSizePercentage / 100.0) * customers.size());
		
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; i < Parameters.vectorSpaceSize; j++) {
				aggregatedSurveyResults[j] += customers.get(i).getDemandVector()[j];
			}
		}
		
	}
	

	/**
	 *  Refine product based on the results of the initial market research
	 */
	public void refineProduct() {	
		
		int[] productVector = goal.getProductVector();
		
		int sampleSize = 0;
		
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {		
			
			if ( ((double)aggregatedSurveyResults[i] / (double)sampleSize) * 100 
					>= Parameters.productElementChangeThreshold) {
				
				productVector[i] = (productVector[i] + 1) % 2;				
			}
		}
		System.out.println("Causator's product refined");
		goal.setProductVector(productVector);
	}
}
