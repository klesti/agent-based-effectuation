package EffectuationCausation;

public class Means  {

	private int[] knowHow;
	private double money;
	
	public Means() {
		knowHow = new int[Parameters.vectorSpaceSize];
		for (int i = 0; i < knowHow.length; i++) {
			knowHow[i] = 0;
		}
		money = 0.0;
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
	
	
	public void print() {
		System.out.print("Money: " + String.valueOf(money) + ", Know-How: ");
		for (int i = 0; i < knowHow.length; i++) {
			System.out.print(knowHow[i]);
		}
		System.out.println();
	}
	
}
