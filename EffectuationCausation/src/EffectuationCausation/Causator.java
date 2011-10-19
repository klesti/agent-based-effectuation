package EffectuationCausation;

import java.util.List;

import repast.simphony.space.graph.Network;

public class Causator extends Entrepreneur {
	
	protected List<MarketResearcher> marketResearcher;
	protected Goal goal;

	
	public Causator(Network<Object> network) {
		super(network);
		// TODO Auto-generated constructor stub
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public List<MarketResearcher> getMarketResearcher() {
		return marketResearcher;
	}

	public void setMarketResearcher(List<MarketResearcher> marketResearcher) {
		this.marketResearcher = marketResearcher;
	}
	
	

}
