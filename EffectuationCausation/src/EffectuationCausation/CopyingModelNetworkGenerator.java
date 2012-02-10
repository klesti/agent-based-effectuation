/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class CopyingModelNetworkGenerator extends EntrepreneurialNetworkGenerator {	
	

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
				Customer c = new Customer(context, network, "Customer" + 
						UUID.randomUUID().toString().subSequence(0, 7));
				attachNode(c);
				totalCustomers--;
			}
			
			if (totalEntrepreneuers > 0) {
				Entrepreneur e = new Entrepreneur(context, network, "Entrepreneur" + 
						UUID.randomUUID().toString().subSequence(0, 7));
				attachNode(e);
				e.generateGoal();
				totalEntrepreneuers--;
			}
			
			if (totalInvestors > 0) {
				Investor i = new Investor(context, network, "Investor" + 
						UUID.randomUUID().toString().subSequence(0, 7));
				attachNode(i);
				i.generateGoal();
				totalInvestors--;
			}
		}
		
		return network;
	}
	
	public void rewireNode(Object n) {
		JungNetwork<Object> entrepreneurialNetwork = EffectuationBuilder.getEntrepreneurialNetwork();
		
		int attached = 0;
		ArrayList<Object> alreadyWired = new ArrayList<Object>();
		
		while (attached != getEdgesPerStep()) {
			
			Object randomNode = getRandomNode();
			
			if (alreadyWired.indexOf(randomNode) != -1) {
				continue;
			}
			
			ArrayList<Object> adjacentNodes = new ArrayList<Object>();
			
			for (Object o: entrepreneurialNetwork.getAdjacent(randomNode)) {
				adjacentNodes.add(o);
			}
			
			Collections.shuffle(adjacentNodes);
			
			int i = 0;
						
			while (i < adjacentNodes.size() && attached != getEdgesPerStep()) {
				entrepreneurialNetwork.addEdge(n, adjacentNodes.get(i));
				network.addEdge(n, adjacentNodes.get(i));								
				i++;
				attached++;
			}
		}		
	}
	
	public void attachNode(Object n) {
		context.add(n);
		rewireNode(n);
	}
	
	/**
	 * Returns a random node from the entrepreneurial network (customer, entrepreneur, investor)
	 * @return node
	 */
	public Object getRandomNode() {		
		int random = RandomHelper.nextIntFromTo(1, 3);
		
		Object o;
		
		switch(random) {
			default:
				o = context.getRandomObjects(Customer.class, 1).iterator().next();
				break;
			case 2:
				o = context.getRandomObjects(Entrepreneur.class, 1).iterator().next();
				break;
			case 3:
				o = context.getRandomObjects(Investor.class, 1).iterator().next();
				break;			
		}
		
		return o;		
	}

}
