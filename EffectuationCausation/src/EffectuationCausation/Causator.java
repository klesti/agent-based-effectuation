package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Causator extends Entrepreneur {
	
	protected int[] aggregatedSurveyResults;
	private int sampleSize;

	public Causator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);	
		
		aggregatedSurveyResults = new int[Parameters.vectorSpaceSize];
		generateGoal();
	}

	@Override
	public void generateGoal() {
		super.generateGoal();
		
		Means m = new Means();
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
	public void performInitialMarketResearch() {
		clearAggregatedSurveyResults();
		
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		for (Object c: context.getObjects(Customer.class)) {
			customers.add((Customer)c);
		}
		
		Collections.shuffle(customers);
		
		
		sampleSize = (int)Math.ceil((Parameters.sampleSizePercentage / 100.0) * customers.size());
		
		for (int i = 0; i < sampleSize; i++) {
			for (int j = 0; j < Parameters.vectorSpaceSize; j++) {
				aggregatedSurveyResults[j] += customers.get(i).getDemandVector()[j];
			}
		}
		
	}
	
	/**
	 * Performs market research regarding a specific product vector
	 * @param productVector
	 * @return meetDemand
	 */
	public double performMarketResarch(int[] productVector) {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		for (Object c: context.getObjects(Customer.class)) {
			customers.add((Customer)c);
		}
		
		Collections.shuffle(customers);		
		
		sampleSize = (int)Math.ceil((Parameters.sampleSizePercentage / 100.0) * customers.size());
		
		int meetDemand = 0;
		
		for (int i = 0; i < sampleSize; i++) {
			if (SimulationBuilder.hammingDistance(goal.getProductVector(), productVector) < 2) {
				meetDemand++;
			}
		}
		
		return (meetDemand / (double)sampleSize) * 100;
	}
	

	/**
	 *  Refine product based on the results of the initial market research
	 */
	public void refineProduct() {	
		
		int[] productVector = goal.getProductVector();		
		
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {		
			
			if ( ((double)aggregatedSurveyResults[i] / (double)sampleSize) * 100 
					>= Parameters.productElementChangeThreshold) {
				
				productVector[i] = (productVector[i] + 1) % 2;				
			}
		}
		System.out.println("Causator's product refined");
		goal.setProductVector(productVector);
	}
	
	/* 
	 *  Makes a product offer to an entrepreneur
	 */
	@ScheduledMethod(start=1,interval=2,priority=1)
	@Override	
	public void offer() {		
		if ( SimulationBuilder.allEntrepreneursOffering) {
			return;
		}
		
		Entrepreneur e = null;		
		
		do {
			e = getEntrepreneur();
		} while (e.isNegotiating() || e.isOffering());
				
		//React in case of offer refusal
		if (!productRefinedOnce && e != null && !e.processOffer(goal.getProductVector())) {
			
			if (performMarketResarch(e.getGoal().getProductVector()) >= Parameters.productElementChangeThreshold) {
				goal.setProductVector(e.getGoal().getProductVector());
				e.processOffer(goal.getProductVector());
				productRefinedOnce = true;
				System.out.println("Causator commitment estabilished!");
			}			
			e.setNegotiating(false);
		} 
	}
	
	/**
	 * Returns an entrepreneur based on the selected "utility function"
	 * @return entrepreneur
	 */
	public Entrepreneur getEntrepreneur() {		
		ArrayList<Entrepreneur> entrepreneurs = new  ArrayList<Entrepreneur>();
		
		for (Object o: context.getObjects(Entrepreneur.class)) {
			if (!(o instanceof Effectuator) && !(o instanceof Causator)) {
				entrepreneurs.add((Entrepreneur)o);
			}
		}		
		
		return (Entrepreneur)Collections.max(entrepreneurs);
	}
}
