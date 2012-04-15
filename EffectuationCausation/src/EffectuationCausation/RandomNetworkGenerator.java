/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.RandomDensityGenerator;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class RandomNetworkGenerator extends EntrepreneurialNetworkGenerator {
	
	private double density;

	/**
	 * @param context
	 */
	public RandomNetworkGenerator(Context<Object> context, double density) {
		super(context);
		this.density = density;
	}

	/* (non-Javadoc)
	 * @see EffectuationCausation.EntrepreneurialNetworkGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		this.network = network;
		
		// Evolve network 
		
		evolveNetwork();		
		
		RandomDensityGenerator<Object> ng = new RandomDensityGenerator<Object>(density, false, true);
		
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

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}
}
