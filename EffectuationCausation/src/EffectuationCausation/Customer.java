package EffectuationCausation;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;

public class Customer extends Agent {
	
	protected int[] demandVector;
	
	public Customer(JungNetwork<Object> network, String label) {
		super(network, label);
		demandVector = new int[CausationBuilder.vectorSpaceSize];
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
