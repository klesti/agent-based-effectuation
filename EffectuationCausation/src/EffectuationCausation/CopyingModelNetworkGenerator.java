/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.Collections;

import repast.simphony.context.Context;
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
		evolveNetwork();
		
		return network;
	}
	
	public void rewireNode(Object n) {
		
		int attached = 0;
		ArrayList<Object> alreadyWired = new ArrayList<Object>();
		
		while (attached != getEdgesPerStep()) {
			
			Object randomNode = context.getRandomObject();
			
			if (alreadyWired.indexOf(randomNode) != -1) {
				continue;
			}
			
			ArrayList<Object> adjacentNodes = new ArrayList<Object>();
			
			for (Object o: network.getAdjacent(randomNode)) {
				adjacentNodes.add(o);
			}
			
			Collections.shuffle(adjacentNodes);
			
			int i = 0;
						
			while (i < adjacentNodes.size() && attached != getEdgesPerStep()) {
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
}
