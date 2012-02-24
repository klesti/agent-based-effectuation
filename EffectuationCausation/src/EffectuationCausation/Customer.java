package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class Customer extends Agent {
	
	protected int[] demandVector;
	
	public Customer(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		demandVector = new int[Parameters.vectorSpaceSize];
		
		initializeDemandVector();
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
	
	/**
	 * Initialize demand vector with all element set as 0s
	 */
	public void initializeDemandVector() {
		for (int i = 0; i < demandVector.length; i++) {
			demandVector[i] = 0;
		}
	}

}
