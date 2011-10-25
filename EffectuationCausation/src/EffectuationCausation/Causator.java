package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.util.ContextUtils;

public class Causator extends Entrepreneur {
	
	protected List<MarketResearcher> marketResearchers;
	protected Goal goal;
	protected int[] aggregatedSurveyResults;
	
	

	
	public Causator(JungNetwork<Object> network, String label) {
		super(network, label);
		
		marketResearchers = new ArrayList<MarketResearcher>();
		//Initialize aggregatedSurveyResults
		aggregatedSurveyResults = new int[CausationBuilder.vectorSpaceSize];
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {
			aggregatedSurveyResults[i] = 0;
		}		
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public List<MarketResearcher> getMarketResearchers() {
		return marketResearchers;
	}

	public void setMarketResearchers(List<MarketResearcher> marketResearchers) {
		this.marketResearchers = marketResearchers;
	}
	
	public void hireMarketResearchers() {
		int numberOfMarketResearchers = RandomHelper.nextIntFromTo(1, 5);
		Context<Object> context = ContextUtils.getContext(this);
		
		for (int i = 0; i < numberOfMarketResearchers; i++) {
			MarketResearcher m = new MarketResearcher(network, "MarketResearcher" + String.valueOf(i));			
			marketResearchers.add(m);
			context.add(m);			
			network.addEdge(this, m);
		}
	}
	
	public void selectAndSpreadMarketSample() {		
		int sampleSize = (int) Math.ceil((CausationBuilder.sampleSize / 100) * CausationBuilder.numberOfCustomers);
		Context<Object> context = ContextUtils.getContext(this);
		
		List<Customer> customers = new ArrayList<Customer>();
		
		
		for (Object c: context.getRandomObjects(Customer.class, sampleSize)) {
			customers.add((Customer)c);
		}
		
		//Spread the customers sample among the hired market researchers 
		
		for (int i = 0; i < marketResearchers.size(); i++) {
			MarketResearcher m = marketResearchers.get(i);
			
			int spread;
			
			List<Customer> workingSample = new ArrayList<Customer>();
			
			if (i == marketResearchers.size() - 1) {
				spread = marketResearchers.size();
			} else {
				int start = (customers.size() > 5) ? 5 : 1;				
				spread = RandomHelper.nextIntFromTo(start, customers.size());
			}
			
			for (int j = 0; j < spread; j++) {
				workingSample.add(customers.remove(0));
			}
			
			m.setWorkingSample(workingSample);
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
			if (productVector[i] != aggregatedSurveyResults[i] && 
					(aggregatedSurveyResults[i] / CausationBuilder.sampleSize) * 100 
					> CausationBuilder.productElementChangeThreshold) {				
				productVector[i] = aggregatedSurveyResults[i];
			}
		}
		
		goal.setProductVector(productVector);
	}
}
