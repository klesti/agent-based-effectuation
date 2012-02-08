/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
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
				attachNode(e);
				e.generateGoal();				
				totalEntrepreneuers--;
			}
			
			if (totalInvestors > 0) {
				Investor i = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt()));
				attachNode(i);
				i.generateGoal();
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
		//When checking the network degree, look only at the "entreprenurial network",
		// i.e at the network without means and goals
		JungNetwork<Object> entrepreneurialNetwork = EffectuationBuilder.getEntrepreneurialNetwork();
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = entrepreneurialNetwork.getDegree();
			
			for (Object o: context) {
				
				//Attach only to subclasses of Agent!
				
				if (o instanceof Means || o instanceof Goal) 
				{
					continue;
				}
				
				double prob = entrepreneurialNetwork.getDegree(o) / totalDegree;
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) >= prob) {
					entrepreneurialNetwork.addEdge(n, o);
					network.addEdge(n, o);
					break;				
				}			
			}
		}
	}
}