package EffectuationCausation;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.util.ContextUtils;

public class Causator extends Entrepreneur {
	
	protected List<MarketResearcher> marketResearchers;
	protected Goal goal;

	
	public Causator(JungNetwork<Object> network, String label) {
		super(network, label);
		// TODO Auto-generated constructor stub
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
		
		for (int i = 0; i < numberOfMarketResearchers; i++) {
			MarketResearcher m = new MarketResearcher(network, "MarketResearcher" + String.valueOf(i));			
			marketResearchers.add(m);
			Context<Object> context = ContextUtils.getContext(this);
			context.add(m);			
			network.addEdge(this, m);
		}
	}
}
