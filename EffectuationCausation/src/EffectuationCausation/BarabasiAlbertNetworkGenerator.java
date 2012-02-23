/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;


/**
 * @author klesti
 *
 */
public class BarabasiAlbertNetworkGenerator extends EntrepreneurialNetworkGenerator implements NetworkGenerator<Object> {
	
	public BarabasiAlbertNetworkGenerator(Context<Object> context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see repast.simphony.context.space.graph.RandomDensityGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		
		this.network = network;
		
		initializeNetwork(0.3);		

		// Evolve network using preferential attachment
		
		evolveNetwork();
		
		return network;
	}

	/**
	 * Preferential attachment
	 * @param n Node to be attached
	 */
	public void attachNode(Object n) {
		context.add(n);
		//When checking the network degree, look only at the "entreprenurial network",
		// i.e at the network without means and goals
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = network.getDegree();
			
			boolean attached = false;
			
			while (!attached) {
				Object o = context.getRandomObjects(Agent.class, 1).iterator().next();
				
				double prob = (network.getDegree(o) + 1) /
								(totalDegree 
										+ network.size() - 1);
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) >= prob) {
					network.addEdge(n, o);
					attached = true;
				}			
			}
		}
	}
}
