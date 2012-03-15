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
	
	public void generateRequiredMeans() {
		requiredMeans = new Means();
		
		double requiredMoney = 0;
		
		for (int i = 0; i < productVector.length; i++) {
			requiredMoney += SimulationBuilder.productElementCost[i];
		}
		
		requiredMeans.setKnowHow(productVector);
		requiredMeans.setMoney(requiredMoney);		
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
		this.generateRequiredMeans();
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
