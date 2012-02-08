/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Effectuator extends Entrepreneur {
	
	private List<Goal> goals;
	private List<Commitment> possibleCommitments;
	private Commitment actualCommitment;
	private int totalRemainingMeetings;
	private boolean finishedExpandingResources;

	public Effectuator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		goals = new ArrayList<Goal>();
		totalRemainingMeetings = RandomHelper.nextIntFromTo(Parameters.minMeetings, 
				Parameters.maxMeetings);
		
		possibleCommitments = new ArrayList<Commitment>();
		setFinishedExpandingResources(false);
		setActualCommitment(null);		
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
			if (!context.contains(g)) {
				context.add(g);
			}
			network.addEdge(this, g);
		}		
		this.goals = goals;
	}
	
	
	/**
	 * @return the possibleCommitments
	 */
	public List<Commitment> getPossibleCommitments() {
		return possibleCommitments;
	}

	/**
	 * @param possibleCommitments the possibleCommitments to set
	 */
	public void setPossibleCommitments(List<Commitment> possibleCommitments) {
		this.possibleCommitments = possibleCommitments;
	}
	
	

	/**
	 * @return the actualCommitment
	 */
	public Commitment getActualCommitment() {
		return actualCommitment;
	}

	/**
	 * @param actualCommitment the actualCommitment to set
	 */
	public void setActualCommitment(Commitment actualCommitment) {
		this.actualCommitment = actualCommitment;
	}
	
	

	/**
	 * @return the totalMeetings
	 */
	public int getTotalRemainingMeetings() {
		return totalRemainingMeetings;
	}

	/**
	 * @param totalMeetings the totalMeetings to set
	 */
	public void setTotalRemainingMeetings(int totalMeetings) {
		this.totalRemainingMeetings = totalMeetings;
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
	 * @param g the goal to be added to the goals list
	 */
	public void addGoal(Goal g) {
		goals.add(g);
		if (!context.contains(g)) {
			context.add(g);
		}
		network.addEdge(this, g);
	}
	
	
	/**
	 * Meet an entity (entrepreneur, investor, etc) and "negotiate" a commitment
	 * @return Object An acquaintance
	 */
	public Object meet() {		
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		JungNetwork<Object> entrepreneurialNetwork = EffectuationBuilder.getEntrepreneurialNetwork();
		
		int i = 0;
		Object o = this, acquaintance = this;
		
		while (i < depth && o != null) {
			o = entrepreneurialNetwork.getRandomAdjacent(acquaintance);
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
	

	/**
	 *  Expanding cycle of resources
	 */
	@ScheduledMethod(start=1 , interval=1.0)
	public void expandResources() {
		if (totalRemainingMeetings == 0 && !finishedExpandingResources) {
			setFinishedExpandingResources(true);
			return;
		}
		
		Object m = meet();
		if (m != null) {
			Commitment c = new Commitment(this, (Agent)m);
			possibleCommitments.add(c);
			totalRemainingMeetings--;			
		}
	}
	
	
	/**
	 *  Choose the actual goal from the set of the possible goals (including expansion)
	 */
	@Watch(watcheeClassName = "EffectuationCausation.Effectuator", watcheeFieldNames = "finishedExpandingResources", 
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)	
	public void chooseGoal() {	
		actualCommitment = Collections.max(possibleCommitments);
		System.out.println("Max utility:" + actualCommitment.getBetweennessCentralityUtility());
		System.out.println("Possible commitments:" + possibleCommitments.size());
		
		if (actualCommitment.getMeans() != null) {
			for  (Means m: actualCommitment.getMeans()) {
				addMeans(m);					
			}
		}
		
		setGoal(actualCommitment.getGoal());
		// Create a direct connection between the involved parties if it is 
		// not already there
		if (!network.isAdjacent(this, actualCommitment.getSecondParty())) {
			network.addEdge(this, actualCommitment.getSecondParty());
		}
		System.out.println("The goal has been chosen.");
	}
	
}
