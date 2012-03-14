/**
 * 
 */
package EffectuationCausation;

import java.util.List;

/**
 * @author klesti
 *
 */
/**
 * @author klesti
 *
 */
public class Commitment {
	private Entrepreneur firstParty;
	private Agent secondParty;
	private Goal goal;
	private List<Means> means;
	
	public Commitment(Entrepreneur firstParty, Agent secondParty) {
		this.firstParty = firstParty;
		this.secondParty = secondParty;
	}

	/**
	 * @return the firstParty
	 */
	public Entrepreneur getFirstParty() {
		return firstParty;
	}

	/**
	 * @param firstParty the firstParty to set
	 */
	public void setFirstParty(Entrepreneur firstParty) {
		this.firstParty = firstParty;
	}

	/**
	 * @return the secondParty
	 */
	public Agent getSecondParty() {
		return secondParty;
	}

	/**
	 * @param secondParty the secondParty to set
	 */
	public void setSecondParty(Agent secondParty) {
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
	public List<Means> getMeans() {
		return means;
	}

	/**
	 * @param means the means to set
	 */
	public void setMeans(List<Means> means) {
		this.means = means;
	}
}
