package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.graph.Network;

public class Investor extends Agent {
	
	private List<Means> availableMeans;
	private double availableMoney;
	private Goal goal;
	
	public Investor(Network<Object> network, String label) {
		super(network, label);
		availableMeans = new ArrayList<Means>();
		goal = new Goal();
		goal.generateRandomProductVector();
	}

	/**
	 * @return the availableMeans
	 */
	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	/**
	 * @param availableMeans the availableMeans to set
	 */
	public void setAvailableMeans(List<Means> availableMeans) {
		this.availableMeans = availableMeans;
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
}
