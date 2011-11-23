/**
 * 
 */
package EffectuationCausation;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Effectuator extends Entrepreneur {

	public Effectuator(Network<Object> network, String label) {
		super(network, label);
		// TODO Auto-generated constructor stub
	}

	public void generateAvailableMeans() {		
		int totalMeans = RandomHelper.nextIntFromTo(1, Parameters.maxInitalMeans);
		
		for (int i = 0; i < totalMeans; i++) {
			Means m = new Means("Means" + String.valueOf(i));
			availableMeans.add(m);
			EffectuationBuilder.context.add(m);
			EffectuationBuilder.network.addEdge(this, m);
		}		
	}
}
