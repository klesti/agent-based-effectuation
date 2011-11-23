package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.graph.Network;

public class Entrepreneur extends Agent {
	protected List<Means> availableMeans;
	protected List<Customer> customers;
	protected Goal goal;
	
	public Entrepreneur(Network<Object> network, String label) {
		super(network, label);
		availableMeans = new ArrayList<Means>();
		customers = new ArrayList<Customer>();
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	
	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
		this.availableMeans = availableMeans;
	}
	
	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}	

}
