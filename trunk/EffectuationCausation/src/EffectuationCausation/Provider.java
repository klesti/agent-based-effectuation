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
public class Provider extends Agent {
	
	private List<Means> offeredMeans;

	public Provider(JungNetwork<Object> network, String label) {
		super(network, label);
		offeredMeans = new ArrayList<Means>();
	}

	public List<Means> getOfferedMeans() {
		return offeredMeans;
	}

	public void setOfferedMeans(List<Means> offeredMeans) {
		this.offeredMeans = offeredMeans;
	}
	
}
