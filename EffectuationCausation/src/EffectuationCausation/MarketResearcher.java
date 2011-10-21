/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.graph.JungNetwork;

/**
 * @author klesti
 *
 */
public class MarketResearcher extends Agent {
	
	protected List<Customer> workingSample;
	protected int[] surveyResults;

	public MarketResearcher(JungNetwork<Object> network, String label) {
		super(network, label);
		workingSample = new ArrayList<Customer>();
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
		surveyResults = new int[this.workingSample.get(0).demandVector.length];
	}
	

	/**
	 * @return the surveyResults
	 */
	public int[] getSurveyResults() {
		return surveyResults;
	}


	/* (non-Javadoc)
	 * @see EffectuationCausation.Agent#step()
	 */
	@Override
	public void step() {
		// TODO Auto-generated method stub
		super.step();
	}
	
	

}
