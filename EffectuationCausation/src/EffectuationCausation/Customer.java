package EffectuationCausation;

import repast.simphony.space.graph.Network;

public class Customer extends Agent {
	
	protected int[] demandVector;
	
	public Customer(Network<Object> network) {
		super(network);
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
