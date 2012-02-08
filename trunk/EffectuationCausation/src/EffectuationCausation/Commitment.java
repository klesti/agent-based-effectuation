/**
 * 
 */
package EffectuationCausation;

import java.util.List;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.ShortestPath;

/**
 * @author klesti
 *
 */
/**
 * @author klesti
 *
 */
public class Commitment implements Comparable<Commitment> {
	private Entrepreneur firstParty;
	private Agent secondParty;
	private Goal goal;
	private List<Means> means;
	
	public Commitment(Entrepreneur firstParty, Agent secondParty) {
		this.firstParty = firstParty;
		this.secondParty = secondParty;
		buildCommitment();
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
	
	/**
	 * Returns a random utility from 0 to 1 for this commitment
	 * @return utility
	 */
	public double getRandomUtility() {		 
		return RandomHelper.nextDoubleFromTo(0,1);
	}
	
	/**
	 * Returns the "connections utility" as defined by "Jackson and Wolinsky, 1996"
	 * @return utility
	 */
	public double getConnectionsUtility() {
		double sigma = 0.5;
		
		double utility = 0;
		
		JungNetwork<Object> network = EffectuationBuilder.getEntrepreneurialNetwork(); 
		
		ShortestPath<Object> sp = new ShortestPath<Object>(network);
				
		for(Object o: network.getNodes()) {
			if (!getSecondParty().equals(o)) {
				utility += Math.pow(sigma, sp.getPathLength(getSecondParty(), o));
			}
		}	
		
		return utility;
	}
	
	/**
	 * Returns the "degree centrality utility", i.e an utility functions that is based on the
	 * degree centrality of the second party 
	 * @return utility
	 */
	public double getDegreeCentralityUtility() {
		JungNetwork<Object> network = EffectuationBuilder.getEntrepreneurialNetwork();
		
		return network.getDegree(getSecondParty());
	}
	
	
	/**
	 * Returns the "betweenness centrality utility", i.e an utility functions that is based on the
	 * betweenness centrality of the second party
	 * @return utility
	 */
	public double getBetweennessCentralityUtility() {
		return getSecondParty().getBetweennessCentrality();
	}
	
	/**
	 *  Builds the commitment based on the involved parties
	 */
	public void buildCommitment() {
	
		if (secondParty instanceof Entrepreneur || secondParty instanceof Investor) {
			Entrepreneur e = (Entrepreneur)secondParty;
			setMeans(e.getAvailableMeans());
			setGoal(e.getGoal());
		}
		
		if (secondParty instanceof Customer) {
			Customer c = (Customer)secondParty;
			setGoal(new Goal(secondParty.getContext(), secondParty.getNetwork()));
			getGoal().setProductVectorEffectuation(c.getDemandVector());			
		}
	}

	@Override
	public int compareTo(Commitment c) {
		double ownUtility;
		double othersUtility;
		
		if (Parameters.utilityFunction.equals("ConnectionsUtility")) {
			ownUtility = this.getConnectionsUtility();
			othersUtility = c.getConnectionsUtility();
		} else if (Parameters.utilityFunction.equals("DegreeCentrality")) {
			ownUtility = this.getDegreeCentralityUtility();
			othersUtility = c.getDegreeCentralityUtility();
		} else if(Parameters.utilityFunction.equals("BetweennessCentrality")) {
			ownUtility = this.getBetweennessCentralityUtility();
			othersUtility = c.getBetweennessCentralityUtility();
		} else { //Random utility
			ownUtility = this.getRandomUtility();
			othersUtility = c.getRandomUtility();
		}
		
		if (ownUtility < othersUtility) {
			return -1;
		} else if (ownUtility > othersUtility) {
			return 1;
		} else {
			return 0;
		}
	}
}
