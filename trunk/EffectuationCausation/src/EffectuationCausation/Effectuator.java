/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Effectuator extends Entrepreneur {
	
	private List<Goal> goals;

	public Effectuator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		goals = new ArrayList<Goal>();
	}

	/**
	 * @return the goals
	 */
	public List<Goal> getGoals() {
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(List<Goal> goals) {
		for (Goal g: this.goals) {
			context.remove(g);
		}
		for (Goal g: goals) {
			context.add(g);
			network.addEdge(this, g);
		}		
		this.goals = goals;
	}
	
	/**
	 * @param g the goal to be added to the goals list
	 */
	public void addGoal(Goal g) {
		goals.add(g);
		context.add(g);
		network.addEdge(this, g);		
	}
	
}
