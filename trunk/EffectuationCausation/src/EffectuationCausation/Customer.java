package EffectuationCausation;

import repast.simphony.space.graph.JungNetwork;

public class Customer extends Agent {
	
	protected int[] demandVector;
	
	public Customer(JungNetwork<Object> network, String label) {
		super(network, label);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the demandVector
	 */
	public int[] getDemandVector() {
		return demandVector;
	}

	/**
	 * @param demandVector the demandVector to set
	 */
	public void setDemandVector(int[] demandVector) {
		this.demandVector = demandVector;
	}

}
