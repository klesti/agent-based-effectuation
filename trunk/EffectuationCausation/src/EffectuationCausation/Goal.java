package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.util.ContextUtils;

public class Goal {
	private List<Means> requiredMeans;
	private int[] productVector;
	
	public Goal(int productVectorSize, boolean generateRequiredMeans) {
		if (generateRequiredMeans) {
			generateRequiredMeans();
		}
		requiredMeans = new ArrayList<Means>();
		productVector = new int[productVectorSize];
		generateRandomProductVector();
		
	}
	
	public Goal(int productVectorSize) {
		this(productVectorSize, false);
	}

	public void generateRequiredMeans() {
		clearRequiredMeans();
		Context<Object> context = ContextUtils.getContext(this);
		JungNetwork<Object> network = (JungNetwork<Object>)context.getProjection("network");
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
		Context<Object> context = ContextUtils.getContext(this);
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
		this.requiredMeans = requiredMeans;
	}

	/**
	 * @return the productVector
	 */
	public int[] getProductVector() {
		return productVector;
	}

	/**
	 * @param productVector the productVector to set
	 */
	public void setProductVector(int[] productVector) {
	
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
	
	public void generateRandomProductVector() {
		for (int i = 0; i < productVector.length; i++) {
			productVector[i] = RandomHelper.nextIntFromTo(0, 1);
		}
	}

}
