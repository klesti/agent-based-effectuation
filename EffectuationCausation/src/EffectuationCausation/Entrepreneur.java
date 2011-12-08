package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
/**
 * @author klesti
 *
 */
public class Entrepreneur extends Agent {
	protected List<Means> availableMeans;
	protected List<Customer> customers;
	protected Goal goal;
	
	public Entrepreneur(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
		customers = new ArrayList<Customer>();
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		for (Customer c: this.customers) {
			context.remove(c);
		}
		for (Customer c: customers) {
			context.add(c);
			network.addEdge(this, c);
		}
		this.customers = customers;
	}
	
	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
		for (Means m: this.availableMeans) {
			context.remove(m);
		}
		for (Means m: availableMeans) {
			context.add(m);
			network.addEdge(this, m);
		}
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
		if (this.goal != null) {
			context.remove(this.goal);
		}
		if (context.getObjects(Goal.class).size() == 0) {
			context.add(goal);
		}
		network.addEdge(this, goal);
		this.goal = goal;
	}
	
	public void addMeans(Means m) {
		context.add(m);
		network.addEdge(this, m);
		availableMeans.add(m);
	}
	
	
	/**
	 * To be called only in the effectuation scenario 
	 */
	public void generateAvailableMeans() {
		int totalMeans = RandomHelper.nextIntFromTo(1, EffectuationBuilder.maxInitalMeans);
		
		for (int i = 0; i < totalMeans; i++) {
			Means m = new Means("Means" + RandomHelper.nextInt());
			availableMeans.add(m);
			context.add(m);
			network.addEdge(this, m);
		}
	}
	
	
	
	/**
	 *  To be called only in the effectuation scenario
	 *  @description Generate goal based on the available means (require all of them)
	 */
	public void generateGoal() {
		if (availableMeans.size() == 0) {
			generateAvailableMeans();
		}
		goal = new Goal(context, network);
		goal.generateRandomProductVector();
		goal.setRequiredMeans(availableMeans);
	}
}
