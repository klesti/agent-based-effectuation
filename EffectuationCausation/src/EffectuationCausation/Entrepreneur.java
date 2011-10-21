package EffectuationCausation;

import java.util.List;

import repast.simphony.space.graph.Network;

public class Entrepreneur extends Agent {
	protected List<Means> availableMeans;
	protected List<Customer> customers;
	
	public Entrepreneur(Network<Object> network, String label) {
		super(network, label);
		// TODO Auto-generated constructor stub
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

}
