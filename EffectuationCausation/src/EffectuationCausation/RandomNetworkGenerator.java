/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class RandomNetworkGenerator extends EntrepreneurialNetworkGenerator {

	/**
	 * @param context
	 */
	public RandomNetworkGenerator(Context<Object> context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see EffectuationCausation.EntrepreneurialNetworkGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		this.network = network;
		
		// Evolve network using a "copy model"
		
		while (totalCustomers > 0 || totalEntrepreneuers > 0 || totalInvestors > 0) {
			if (totalCustomers > 0) {
				Customer c = new Customer(context, network, EffectuationBuilder.nextId("C"));
				attachNode(c);
				totalCustomers--;
			}
			
			if (totalEntrepreneuers > 0) {
				Entrepreneur e = new Entrepreneur(context, network, EffectuationBuilder.nextId("E"));
				attachNode(e);
				e.generateGoal();
				totalEntrepreneuers--;
			}
			
			if (totalInvestors > 0) {
				Investor i = new Investor(context, network, EffectuationBuilder.nextId("I"));
				attachNode(i);
				i.generateGoal();
				totalInvestors--;
			}
		}
		
		randomWire(0.3);
		
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
