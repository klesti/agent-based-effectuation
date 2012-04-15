/**
 * 
 */
package EffectuationCausation;

/**
 * @author klesti
 *
 */
public class Commitment {
	private Entrepreneur secondParty;
	private Goal goal;
	private Means means;
	
	public Commitment(Entrepreneur secondParty) {
		this.secondParty = secondParty;
		goal = secondParty.getGoal();
		means = null;
	}


	/**
	 * @return the secondParty
	 */
	public Entrepreneur getSecondParty() {
		return secondParty;
	}

	/**
	 * @param secondParty the secondParty to set
	 */
	public void setSecondParty(Entrepreneur secondParty) {
		this.secondParty = secondParty;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the means
	 */
	public Means getMeans() {
		return means;
	}

	/**
	 * @param means the means to set
	 */
	public void setMeans(Means means) {
		this.means = means;
	}
}
