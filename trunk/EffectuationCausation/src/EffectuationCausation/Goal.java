package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.random.RandomHelper;

public class Goal  {
	private List<Means> requiredMeans;
	private int[] productVector;
	
	public Goal() {
		requiredMeans = new ArrayList<Means>();
		productVector = new int[Parameters.vectorSpaceSize];
		
		generateRandomProductVector();	
	}
	
	public void generateRequiredMeans() {
		requiredMeans.clear();
		
		int possibleMeans = RandomHelper.nextIntFromTo(2, 10);		
		for (int i = 0; i < possibleMeans; i++) {
			Means m = new Means("M" + SimulationBuilder.nextId("M"));
			requiredMeans.add(m);
		}
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
