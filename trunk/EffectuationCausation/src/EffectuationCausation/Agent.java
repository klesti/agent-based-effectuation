/**
 * 
 */
package EffectuationCausation;

import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Agent {
	protected String label;
	protected Network<Object> network;
	
	public Agent(Network<Object> network, String label) {
		this.network = network;
		this.label = label;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
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

	public void step() {
				
	}
}
