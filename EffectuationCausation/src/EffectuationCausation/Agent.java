/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.ShortestPath;

/**
 * @author klesti
 *
 */
public class Agent implements Comparable<Object> {
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
				utility += Math.pow(sigma, (double)sp.getPath(this, o).size());
			}
		}	
		
		return utility;
	}


	/* 
	 * Allows for comparing agents based on the selected utility function
	 */
	@Override
	public int compareTo(Object a) {
		
		if (!(a instanceof Entrepreneur)) {
			return 0;
		}
		
		double ownUtility, othersUtility;
		
		if (Parameters.utilityFunction.equals("ConnectionsUtility")) {
			ownUtility = getConnectionsUtility();
			othersUtility = ((Agent)a).getConnectionsUtility();
		} else if (Parameters.utilityFunction.equals("DegreeCentrality")) {
			ownUtility = getDegreeCentralityUtility();
			othersUtility = ((Agent)a).getDegreeCentralityUtility();
		} else if (Parameters.utilityFunction.equals("BetweennessCentrality")) {
			ownUtility = getBetweennessCentrality();
			othersUtility = ((Agent)a).getBetweennessCentrality();
		} else {
			ownUtility = RandomHelper.nextDouble();
			othersUtility = RandomHelper.nextDouble();
		}
		
		return ((Double)ownUtility).compareTo((Double)othersUtility);
	}	
}
