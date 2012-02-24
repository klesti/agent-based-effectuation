/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
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
		this.network = network;
		
		evolveNetwork();
		
		//Wire network using Watts beta-model
		
		double beta = 0.3;
		int meanDegree = 4;
		
		WattsBetaSmallWorldGenerator<Object> ng = 
			new WattsBetaSmallWorldGenerator<Object>(beta, meanDegree, true);
		
		network = ng.createNetwork(network);
		
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
