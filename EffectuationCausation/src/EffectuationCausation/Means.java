package EffectuationCausation;

public class Means  {

	private String label;
	private int[] knowHow;
	private double money;
	
	public Means(String label) {
		this.label = label;
		knowHow = new int[Parameters.vectorSpaceSize];
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the knowHow
	 */
	public int[] getKnowHow() {
		return knowHow;
	}

	/**
	 * @param knowHow the knowHow to set
	 */
	public void setKnowHow(int[] knowHow) {
		this.knowHow = knowHow;
	}

	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}
	
	
}
