/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class CopyingModelNetworkGenerator extends EntrepreneurialNetworkGenerator implements NetworkGenerator<Object> {	
	

	public CopyingModelNetworkGenerator(Context<Object> context) {
		super(context);
	}

	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		this.network = network;
		
		initializeNetwork(0.3);	
		
		// Evolve network using a "copy model"
		
		while (totalCustomers > 0 || totalEntrepreneuers > 0 || totalInvestors > 0) {
			if (totalCustomers > 0) {
				Customer c = new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt()));
				context.add(c);
				rewireNode(c);				
				totalCustomers--;
			}
			
			if (totalEntrepreneuers > 0) {
				Entrepreneur e = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
				e.generateGoal();
				context.add(e);
				rewireNode(e);
				totalEntrepreneuers--;
			}
			
			if (totalInvestors > 0) {
				Investor i = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt())); 
				rewireNode(i);
				totalInvestors--;
			}			
		}
		
		return network;
	}
	
	public void rewireNode(Agent n) {
		int attached = 0;
		ArrayList<Object> alreadyWired = new ArrayList<Object>();
		
		while (attached != getEdgesPerStep()) {
			Object randomNode = getRandomNode();
			
			if (alreadyWired.indexOf(randomNode) != -1) {
				continue;
			}
			
						
			ArrayList<Object> adjacentNodes = new ArrayList<Object>();
			
			for (Object o: network.getAdjacent(randomNode)) {
				adjacentNodes.add(o);
			}
			
			Collections.shuffle(adjacentNodes);
			
			int i = 0;
			
			while (i < attached && attached != getEdgesPerStep()) {
				network.addEdge(n, adjacentNodes.get(i));
								
				i++;
				attached++;
			}
		}		
	}
	
	public Object getRandomNode() {
		ArrayList<Object> nodes = new ArrayList<Object>();		
		
		for (Object o: network.getNodes()) {
			if (o instanceof Customer || o instanceof Entrepreneur || o instanceof Investor
					|| o instanceof Effectuator) {
				nodes.add(o);
			}
		}
		
		return nodes.get(RandomHelper.nextIntFromTo(0, nodes.size() - 1));		
	}

}
