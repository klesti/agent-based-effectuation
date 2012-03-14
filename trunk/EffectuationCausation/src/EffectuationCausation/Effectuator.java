/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Effectuator extends Entrepreneur {
	
	private boolean finishedExpandingResources;

	public Effectuator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);		
		setFinishedExpandingResources(false);
		generateGoal();
	}

	/**
	 * @return the finishedExpandingResources
	 */
	public boolean isFinishedExpandingResources() {
		return finishedExpandingResources;
	}

	/**
	 * @param finishedExpandingResources the finishedExpandingResources to set
	 */
	public void setFinishedExpandingResources(boolean finishedExpandingResources) {
		this.finishedExpandingResources = finishedExpandingResources;
	}

	/**
	 *  @description Generate goal based on the available means (require all of them)
	 */	
	public void generateGoal() {		
		super.generateGoal();
		
		if (availableMeans.size() == 0) {
			generateAvailableMeans();
		}
		
		goal.setProductVector(availableMeans.get(0).getKnowHow());
		goal.setRequiredMeans(availableMeans.get(0));		
	}
	
	/**
	 * Meet an entity (entrepreneur, investor, etc) and "negotiate" a commitment
	 * @return Object An acquaintance
	 */
	public Object meet() {		
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
				int i = 0;
		Object o = this, acquaintance = this;
		
		while (i < depth && o != null) {
			o = network.getRandomAdjacent(acquaintance);
			//do not meet customers
			if (o != null && !(o instanceof Customer)) {
				acquaintance = o;
			}
			i++;
		}
		
		if (acquaintance == this) {
			acquaintance = null;			
		} 
		
		return acquaintance;		
	}	
}
