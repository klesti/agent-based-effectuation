package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.util.ContextUtils;

public class Causator extends Entrepreneur {
	
	protected List<MarketResearcher> marketResearchers;
	protected Goal goal;
	private CausationBuilder causationBuilder;

	
	public Causator(JungNetwork<Object> network, String label, 
			CausationBuilder causationBuilder) {
		super(network, label);
		this.causationBuilder = causationBuilder;
		marketResearchers = new ArrayList<MarketResearcher>();
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
	
	@ScheduledMethod(priority=1,start=1,duration=1)
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
	
	@ScheduledMethod(priority=1,start=2,duration=1)
	public void selectMarketSample() {		
		int sampleSize = (int) Math.ceil((causationBuilder.sampleSize / 100) * causationBuilder.numberOfCustomers);
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
	
	public void aggregateMarketResearchResults() {
		
	}
}
