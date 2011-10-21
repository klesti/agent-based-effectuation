/**
 * 
 */
package EffectuationCausation;

import repast.simphony.space.graph.JungNetwork;

/**
 * @author klesti
 *
 */
public class Agent {
	protected String label;
	protected JungNetwork<Object> network;
	
	public Agent(JungNetwork<Object> network, String label) {
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
	public JungNetwork<Object> getNetwork() {
		return network;
	}


	/**
	 * @param network the network to set
	 */
	public void setNetwork(JungNetwork<Object> network) {
		this.network = network;
	}

	public void step() {
				
	}
}
