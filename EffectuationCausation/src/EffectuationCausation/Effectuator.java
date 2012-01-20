/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
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
		totalRemainingMeetings = RandomHelper.nextIntFromTo(EffectuationBuilder.minMeetings, 
				EffectuationBuilder.maxMeetings);
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
			context.add(g);
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
		context.add(g);
		network.addEdge(this, g);		
	}
	
	
	/**
	 * Meet an entity (entrepreneur, investor, etc) and "negotiate" a commitment
	 * @return Object An acquaintance
	 */
	public Object meet() {
		int depth = RandomHelper.nextIntFromTo(1, EffectuationBuilder.maxDepthForMeeting);
		
		int i = 0;
		Object o = this, acquaintance = this;
		
		while (i < depth && o != null) {
			o = network.getRandomAdjacent(acquaintance);
			if (o != null) {
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
	@ScheduledMethod(start=1 , interval=EffectuationBuilder.maxMeetings, duration=2.0)
	public void expandResources() {
		if (totalRemainingMeetings == 0 && !finishedExpandingResources) {
			setFinishedExpandingResources(true);
			return;
		}
		
		Object m = meet();
		if (m != null) {
			Commitment c = new Commitment(this, (Agent)m);
			possibleCommitments.add(c);
		}
		totalRemainingMeetings--;
	}
	
	
	/**
	 *  Choose the actual goal from the set of the possible goals (including expansion)
	 */
	@Watch(watcheeClassName = "EffectuationCausation.Effectuator", watcheeFieldNames = "finishedExpandingResources", 
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)	
	public void chooseGoal() {
	
		//"Go for commitment" probability
		//TODO: Later defined by an "utility function"
		double prob = 0.5;
		
		for (Commitment c: possibleCommitments) {
			double p = RandomHelper.nextDoubleFromTo(0,1);
			if (p >= prob) {
				for  (Means m: c.getMeans()) {
					addMeans(m);					
				}
				actualCommitment = c;
				setGoal(c.getGoal());
				// Create a direct connection between the involved parties if it is 
				// not already there
				if (!network.isAdjacent(this, c.getSecondParty())) {
					network.addEdge(this, c.getSecondParty());
				}
				break;
			}
		}
		
		if (actualCommitment == null) {
			for (Goal g: getGoals()) {
				double p = RandomHelper.nextDoubleFromTo(0,1);
				if (p >= prob) {
					setGoal(g);
					break;
				}				
			}
		}
		
	}
	
}
