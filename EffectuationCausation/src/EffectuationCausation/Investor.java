package EffectuationCausation;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Investor extends Entrepreneur {
	
	private double availableMoney;
	
	public Investor(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
		goal = new Goal(context, network);
		goal.generateRandomProductVector();
		generateAvailableMeans();
		generateAvailableMoney();
		goal.setRequiredMeans(availableMeans);
	}

	/**
	 * @return the availableMoney
	 */
	public double getAvailableMoney() {
		return availableMoney;
	}

	/**
	 * @param availableMoney the availableMoney to set
	 */
	public void setAvailableMoney(double availableMoney) {
		this.availableMoney = availableMoney;
	}
	
	public void generateAvailableMoney() {
		availableMoney = RandomHelper.nextDoubleFromTo(1000, 10000);
	}

}
