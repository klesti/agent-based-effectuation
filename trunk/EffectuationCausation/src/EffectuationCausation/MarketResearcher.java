/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class MarketResearcher extends Agent {
	
	protected List<Customer> workingSample;
	protected int[] surveyResults;
	protected boolean finishedMarketResearch = false;

	public MarketResearcher(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		workingSample = new ArrayList<Customer>();
		surveyResults = new int[Parameters.vectorSpaceSize];
	}

	
	/**
	 * @return the workingSample
	 */
	public List<Customer> getWorkingSample() {
		return workingSample;
	}


	/**
	 * @param workingSample the workingSample to set
	 */
	public void setWorkingSample(List<Customer> workingSample) {
		this.workingSample = workingSample;		
		for (int i = 0; i < surveyResults.length; i++) {
			surveyResults[i] = 0;
		}
	}
	

	/**
	 * @return the surveyResults
	 */
	public int[] getSurveyResults() {
		return surveyResults;
	}


	
	/**
	 * @return the finishedMarketResearch
	 */
	public boolean isFinishedMarketResearch() {
		return finishedMarketResearch;
	}


	/* (non-Javadoc)
	 * @see EffectuationCausation.Agent#step()
	 * Performs an "interview" with the customers regarding their demand
	 */
	@Override
	@ScheduledMethod(start=3,interval=1)	
	public void step() {
		if (!finishedMarketResearch && workingSample.size() > 0) {
			Customer c = workingSample.remove(0);			
			for (int i = 0; i < c.demandVector.length; i++ ) {
				surveyResults[i] += c.demandVector[i];			
			}	
			if (workingSample.size() == 0) {
				finishedMarketResearch = true;
			}
		}
	}
	
	

}
