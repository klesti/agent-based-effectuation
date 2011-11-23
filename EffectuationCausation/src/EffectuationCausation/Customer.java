package EffectuationCausation;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Customer extends Agent {
	
	protected int[] demandVector;
	
	public Customer(Network<Object> network, String label) {
		super(network, label);
		demandVector = new int[Parameters.vectorSpaceSize];
		generateRandomDemandVector();
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
	
	public void generateRandomDemandVector() {
		for (int i = 0; i < demandVector.length; i++) {
			demandVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}

}
