/**
 * 
 */
package EffectuationCausation;

import java.util.List;

import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Provider extends Agent {
	
	private List<Means> offeredMeans;

	public Provider(Network<Object> network) {
		super(network);
		// TODO Auto-generated constructor stub
	}

	public List<Means> getOfferedMeans() {
		return offeredMeans;
	}

	public void setOfferedMeans(List<Means> offeredMeans) {
		this.offeredMeans = offeredMeans;
	}
	
}
