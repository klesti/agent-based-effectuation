package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class Goal {
	private List<Means> requiredMeans;
	private int[] productVector;
	private Context<Object> context;
	private Network<Object> network;
	
	public Goal(Context<Object> context, Network<Object> network) {
		this.context = context;
		this.network = network;
		
		requiredMeans = new ArrayList<Means>();
		productVector = new int[Parameters.vectorSpaceSize];
		
		generateRandomProductVector();		
	}
	
	/**
	 *  This method should be called only in the causation scenario
	 */
	public void generateRequiredMeans() {
		clearRequiredMeans();
		CausationBuilder.offeredMeans.clear();		
		
		int possibleMeans = RandomHelper.nextIntFromTo(2, 10);		
		for (int i = 0; i < possibleMeans; i++) {
			Means m = new Means("Means" + String.valueOf(i));
			requiredMeans.add(m);
			//Add the required means to the network
			context.add(m);
			network.addEdge(this, m);	
		}
	}
	
	public void clearRequiredMeans() {		
		for (Means m: requiredMeans) {
			context.remove(m);
		}
		requiredMeans.clear();
	}
	
	

	/**
	 * @return the requiredMeans
	 */
	public List<Means> getRequiredMeans() {
		return requiredMeans;
	}

	/**
	 * @param requiredMeans the requiredMeans to set
	 */
	public void setRequiredMeans(List<Means> requiredMeans) {
		clearRequiredMeans();
		for (Means m: requiredMeans) {
			context.add(m);
			network.addEdge(this, m);
		}
		this.requiredMeans = requiredMeans;
	}

	/**
	 * @return the productVector
	 */
	public int[] getProductVector() {
		return productVector;
	}
	

	/**
	 * Sets the product vector in the causation scenario
	 * @param productVector the productVector to set
	 */
	public void setProductVectorCausation(int[] productVector) {
	
		boolean changed = false;
		
		for (int i = 0; i < productVector.length; i++) {
			if (productVector[i] != this.productVector[i]) {
				changed = true;
				break;
			}
		}
		
		this.productVector = productVector;
		
		//If the product has changed generate the new required means
		if (changed) {
			generateRequiredMeans();
		}
	}
	
	/**
	 * Sets the product vector in the effectuation scenario
	 * @param productVector the productVector to set
	 */
	public void setProductVectorEffectuation(int[] productVector) {
		this.productVector = productVector;
	}
	
	
	public String printProductVector() {
		String s = "";
		
		for (int i = 0; i < productVector.length; i++) {
			s += String.valueOf(productVector[i]);
		}
		
		return s;
	}
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}

}
