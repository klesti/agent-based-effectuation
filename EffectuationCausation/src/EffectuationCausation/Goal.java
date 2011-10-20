package EffectuationCausation;

import java.util.List;

import repast.simphony.random.RandomHelper;

public class Goal {
	private List<Means> requiredMeans;
	private int[] productVector;
	
	public Goal(boolean generateRequiredMeans) {
		if (generateRequiredMeans) {
			//TODO: Generate required means
		}
	}
	
	public Goal() {
		this(false);
	}

	public void generateRequiredMeans() {
		int possibleMeans = RandomHelper.nextIntFromTo(2, 10);		
		for (int i = 0; i < possibleMeans; i++) {
			Means m = new Means("Means" + String.valueOf(i));
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

}
