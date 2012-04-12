/**
 * 
 */
package EffectuationCausation;

import repast.simphony.space.graph.EdgeCreator;

/**
 * @author klesti
 *
 */
public class CustomEdgeCreator implements EdgeCreator<CustomNetworkEdge, Object> {
	
	public CustomEdgeCreator() {
		
	}
	
	public Class<CustomNetworkEdge> getEdgeType() {
		return CustomNetworkEdge.class;
	}

	@Override
	public CustomNetworkEdge createEdge(Object source, Object target,
			boolean isDirected, double weight) {
		return new CustomNetworkEdge(source, target, true, 0);
	}	

}
