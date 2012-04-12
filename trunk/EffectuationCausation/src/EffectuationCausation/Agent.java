/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ShortestPath;

/**
 * @author klesti
 *
 */
public class Agent {
	protected Context<Object> context;
	protected Network<Object> network;
	protected boolean negotiating;
	protected String label;
	protected double graphicsSize;	
	protected double betweennessCentrality;

	
	public Agent(Context<Object> context, Network<Object> network, String label) {		
		this.context = context;
		this.network = network;
		this.negotiating = false;		
		this.graphicsSize = 0;
		this.label = label;
		this.betweennessCentrality = 0;
	}
	

	/**
	 * @return the context
	 */
	public Context<Object> getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context<Object> context) {
		this.context = context;
	}

	/**
	 * @return the network
	 */
	public Network<Object> getNetwork() {
		return network;
	}


	/**
	 * @param network the network to set
	 */
	public void setNetwork(Network<Object> network) {
		this.network = network;
	}
	
	
	
	/**
	 * @return the negotiating
	 */
	public boolean isNegotiating() {
		return negotiating;
	}


	/**
	 * @param negotiating the negotiating to set
	 */
	public void setNegotiating(boolean negotiating) {
		this.negotiating = negotiating;
	}


	/**
	 * @return the graphicsSize
	 */
	public double getGraphicsSize() {
		return graphicsSize;
	}

	/**
	 * @param graphicsSize the graphicsSize to set
	 */
	public void setGraphicsSize(double graphicsSize) {
		this.graphicsSize = graphicsSize;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}	

	/**
	 * @param betweennessCentrality the betweennessCentrality to set
	 */
	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}

	/**
	 * @return the betweennessCentrality
	 */
	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}

	/**
	 * Returns the "degree centrality" of the agent
	 * @return centrality
	 */
	public double getDegreeCentralityUtility() {	
		return network.getDegree(this);
	}	

	
	/**
	 * Returns the "connections utility" as defined by "Jackson and Wolinsky, 1996"
	 * @return utility
	 */
	public double getConnectionsUtility() {
		double sigma = 0.5;
		
		double utility = 0;
		
		
		ShortestPath<Object> sp = new ShortestPath<Object>(network);
				
		for(Object o: network.getNodes()) {
			if (!this.equals(o)) {
				utility += Math.pow(sigma, sp.getPathLength(this, o));
			}
		}	
		
		return utility;
	}	
}
