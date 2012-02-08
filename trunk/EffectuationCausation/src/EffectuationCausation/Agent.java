/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Agent {
	protected String label;
	protected Context<Object> context;
	protected Network<Object> network;
	protected double betweennessCentrality;
	
	
	public Agent(Context<Object> context, Network<Object> network, String label) {
		this.context = context;
		this.network = network;
		this.label = label;
		this.betweennessCentrality = 0;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}


	/**
	 * @return the context
	 */
	public Context<Object> getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context<Object> context) {
		this.context = context;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the network
	 */
	public Network<Object> getNetwork() {
		return network;
	}


	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network<Object> network) {
		this.network = network;
	}
	

	/**
	 * @return the betweennessCentrality
	 */
	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}

	/**
	 * @param betweennessCentrality the betweennessCentrality to set
	 */
	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}
	
	public void step() {
		
	}
}