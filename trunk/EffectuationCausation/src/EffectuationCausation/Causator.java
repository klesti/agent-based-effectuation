package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

public class Causator extends Entrepreneur {
	
	protected List<MarketResearcher> marketResearchers;
	protected int[] aggregatedSurveyResults;

	public Causator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		marketResearchers = new ArrayList<MarketResearcher>();
		availableMeans = new ArrayList<Means>();
		//Initialize aggregatedSurveyResults
		aggregatedSurveyResults = new int[Parameters.vectorSpaceSize];
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {
			aggregatedSurveyResults[i] = 0;
		}		
	}

	public List<MarketResearcher> getMarketResearchers() {
		return marketResearchers;
	}

	public void setMarketResearchers(List<MarketResearcher> marketResearchers) {
		for (MarketResearcher m: this.marketResearchers) {
			context.remove(m);
		}
		
		for (MarketResearcher m: marketResearchers) {
			if (!context.contains(m)) {
				context.add(m);
			}
			network.addEdge(this, m);
		}
		this.marketResearchers = marketResearchers;
	}
	
	public void hireMarketResearchers() {		
		int numberOfMarketResearchers = RandomHelper.nextIntFromTo(1, 5);
		
		for (int i = 0; i < numberOfMarketResearchers; i++) {
			MarketResearcher m = new MarketResearcher(context, network, "MarketResearcher" + String.valueOf(i));			
			marketResearchers.add(m);
			context.add(m);			
			network.addEdge(this, m);
		}
		System.out.println("Hired market researchers");
	}
	
	public void selectAndSpreadMarketSample() {		
		int sampleSize = (int) Math.ceil((Parameters.sampleSizePercentage * Parameters.numberOfCustomers / 100));
		
		CausationBuilder.sampleSize = sampleSize;
		
		List<Customer> customers = new ArrayList<Customer>();		
		
		for (Object c: context.getRandomObjects(Customer.class, sampleSize)) {
			customers.add((Customer)c);
		}		
		
		
		//Spread the customers sample among the hired market researchers 
		int stop = customers.size();
		for (int i = 0; i < stop; i++) {			
			int index = RandomHelper.nextIntFromTo(0, customers.size()-1);			
			marketResearchers.get(i % marketResearchers.size()).getWorkingSample()
									.add(customers.remove(index));
		}		
	}
	
	@Watch(watcheeClassName = "EffectuationCausation.MarketResearcher", watcheeFieldNames = "finishedMarketResearch", 
			query = "linked_to", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)	
	public void aggregateMarketResearchResults(MarketResearcher m) {
		for (int i = 0; i < m.getSurveyResults().length; i++) {
			aggregatedSurveyResults[i] += m.getSurveyResults()[i];
		}
		
		boolean finishedMarketResearch = true;
		
		for (MarketResearcher m2: marketResearchers) {
			if (!m2.isFinishedMarketResearch()) {
				finishedMarketResearch = false;
				break;
			}
		}		
		
		if (finishedMarketResearch) {
			refineProduct();
		}
	}
	
	/**
	 *  Refine product based on the results of the initial market research
	 */
	public void refineProduct() {	
		
		int[] productVector = goal.getProductVector();
		
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {		
			
			if ( ((double)aggregatedSurveyResults[i] / (double)CausationBuilder.sampleSize) * 100 
					>= Parameters.productElementChangeThreshold) {
				
				productVector[i] = (productVector[i] + 1) % 2;				
			}
		}
		System.out.println("Product refined");
		goal.setProductVectorCausation(productVector);
		acquireMeans();
	}
	
	/**
	 *  Acquire the means that are needed to achieve the desired goal
	 */
	
	public void acquireMeans() {
					
		for (Means m: goal.getRequiredMeans()) {
			double minWeight = 1000 * Parameters.meansOfferedWeightMax;
			Provider provider = new Provider(context, network, "p");
			//Get the means from the provider that offers it with lowest "weight"
			for (Object o: network.getAdjacent(m)) {
				if (o instanceof Provider) {
					Provider p = (Provider)o;
					RepastEdge<Object> edge = network.getEdge(m, p);
					if (edge.getWeight() < minWeight) {
						minWeight = edge.getWeight();
						provider = p;
					}
				}
			}
			
			availableMeans.add(m);
			network.addEdge(this, m);
			ProvidesTo providesTo = new ProvidesTo(this, m);
			provider.getProvidesToList().add(providesTo);
		}		
	}
	
	public int getNumberOfMarketResearchers() {
		return getMarketResearchers().size();
	}
}
