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
	protected Network<Object> network;
	
	public Agent(Network<Object> network) {
		this.network = network;
	}
	

	public Network<Object> getNetwork() {
		return network;
	}


	public void setNetwork(Network<Object> network) {
		this.network = network;
	}
	
	public void step() {
		
	}
}
