package EffectuationCausation;

import repast.simphony.random.RandomHelper;

public class Goal  {
	private Means requiredMeans;
	private int[] productVector;
	
	public Goal() {
		productVector = new int[Parameters.vectorSpaceSize];
		
		generateRandomProductVector();	
	}
	

	/**
	 * @return the requiredMeans
	 */
	public Means getRequiredMeans() {
		return requiredMeans;
	}

	/**
	 * @param requiredMeans the requiredMeans to set
	 */
	public void setRequiredMeans(Means requiredMeans) {
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
