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

	private int meanDegree; //Must be even
	
	/**
	 * @param context
	 */
	public SmallWorldNetworkGenerator(Context<Object> context, int meanDegree) {
		super(context);
		this.meanDegree = meanDegree;
	}

	/* (non-Javadoc)
	 * @see EffectuationCausation.EntrepreneurialNetworkGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		this.network = network;
		
		evolveNetwork();
		
		//Wire network using Watts beta-model
		
		WattsBetaSmallWorldGenerator<Object> ng = 
			new WattsBetaSmallWorldGenerator<Object>(getEdgeProbability(), meanDegree, true);
		
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

	/**
	 * @return the meanDegree
	 */
	public int getMeanDegree() {
		return meanDegree;
	}

	/**
	 * @param meanDegree the meanDegree to set
	 */
	public void setMeanDegree(int meanDegree) {
		this.meanDegree = meanDegree;
	}
}
