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
		
		while (totalCustomers > 0 || totalEntrepreneuers > 0 || totalInvestors > 0) {
			if (totalCustomers > 0) {
				attachNode(new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt())));
				totalCustomers--;
			}
			
			if (totalEntrepreneuers > 0) {
				Entrepreneur e = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
				e.generateGoal();
				attachNode(e);
				totalEntrepreneuers--;
			}
			
			if (totalInvestors > 0) {
				attachNode(new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt())));
				totalInvestors--;
			}			
		}
		
		return network;
	}
	
	
	/**
	 * Preferential attachment
	 * @param n Node to be attached
	 */
	public void attachNode(Object n) {
		context.add(n);
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = network.getDegree();
			
			for (Object o: context) {
				
				//Attach only to subclasses of Agent!
				
				if (!o.getClass().isAssignableFrom(Agent.class)) 
				{
					continue;
				}
				
				double prob = network.getDegree(o) / totalDegree;
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) >= prob) {				
					network.addEdge(n, o);
					break;				
				}			
			}
		}
	}
}
