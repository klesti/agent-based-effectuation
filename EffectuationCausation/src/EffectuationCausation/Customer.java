package EffectuationCausation;

import java.util.HashMap;

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
	
	/**
	 *  Adapts product vector based on neighbours demand and simulation parameters 
	 */
	public void adaptProductVector() {		
		HashMap<Integer, Integer> neighboursAdaption = new HashMap<Integer, Integer>();
		
		double threshold = Parameters.adaptationThreshold / 100.0;
		int neighbours = 0;
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			neighboursAdaption.put(i, 0);
		}
		
		for (Object o: network.getAdjacent(this)) {		
			if (o instanceof Customer) {
				Customer c = (Customer)o;
				
				for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
					int count = neighboursAdaption.get(i);
					if (c.getDemandVector()[i] == 1) {
						count++;
					}
					neighboursAdaption.put(i, count);
				}
			}
			neighbours++;
		}
		
		for (int i = 0; i < Parameters.vectorSpaceSize; i++) {
			if ((double)neighboursAdaption.get(i) / neighbours >= threshold ) {
				demandVector[i] = 1;
			}
		}		 
	}
}
