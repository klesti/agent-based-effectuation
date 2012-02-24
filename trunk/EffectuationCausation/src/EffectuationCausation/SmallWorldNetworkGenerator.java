/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class SmallWorldNetworkGenerator extends EntrepreneurialNetworkGenerator {

	/**
	 * @param context
	 */
	public SmallWorldNetworkGenerator(Context<Object> context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see EffectuationCausation.EntrepreneurialNetworkGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		
		evolveNetwork();
		
		//Wire network using Watts beta-model
		
		double beta = 0.3;
		int meanDegree = 4;
		
		int K = RandomHelper.nextIntFromTo(1, meanDegree / 2);		
		
		ArrayList<Agent> nodes = new ArrayList<Agent>();		
		for (Object o: network.getNodes()) {
			nodes.add((Agent)o);
		}
		
		for (int i = 0; i < nodes.size() - 1; i++) {
			for (int j = 0; j < nodes.size() - 1; j++) {
				if (((Integer)Math.abs(i-j)) % nodes.size() == K) {
					network.addEdge(nodes.get(i), nodes.get(j));
				}
			}
		}
		
		for (int i = 0; i < nodes.size() - 1; i++) { 
			for (int j = 0; j < nodes.size() - 1; j++) {
				
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				
				if (i < j & network.getEdge(nodes.get(i), nodes.get(j)) != null && random >= beta) {					
					
					int k;
					do {
						k = RandomHelper.nextIntFromTo(0, nodes.size() - 1);
					} while (k != i && network.getEdge(nodes.get(i), nodes.get(k)) != null);					
					
					network.addEdge(nodes.get(i), nodes.get(k));
				}
			}
		}
		
		return network;		
	}

	/* (non-Javadoc)
	 * @see EffectuationCausation.EntrepreneurialNetworkGenerator#attachNode(java.lang.Object)
	 */
	@Override
	public void attachNode(Object n) {
		context.add(n);
	}
}
