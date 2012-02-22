/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

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
		
		WattsBetaSmallWorldGenerator<Object> ng = new WattsBetaSmallWorldGenerator<Object>(0.3, 6, true);
		
		Network<Object> n = ng.createNetwork(EffectuationBuilder.getEntrepreneurialNetwork());
				
		for (RepastEdge<Object> edge: n.getEdges()) {
			network.addEdge(edge.getSource(), edge.getTarget());
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
