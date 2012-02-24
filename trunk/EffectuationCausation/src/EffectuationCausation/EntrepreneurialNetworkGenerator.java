package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public abstract class EntrepreneurialNetworkGenerator  implements NetworkGenerator<Object> {

	protected int edgesPerStep;
	protected int totalCustomers;
	protected int totalEntrepreneuers;
	protected Context<Object> context;
	protected Network<Object> network;

	public EntrepreneurialNetworkGenerator(Context<Object> context) {
		this.context = context;
				
		edgesPerStep = 0;
		totalCustomers = 0;
		totalEntrepreneuers = 0;
	}

	
	/**
	 * Initializes the network
	 * @param p initial wiring probability
	 */
	protected void initializeNetwork(double p) {
		for (int i = 0; i < 10 && i < totalCustomers; i++) {		
			context.add(new Customer(context, network, SimulationBuilder.nextId("C")));
			totalCustomers--;
		}
		
		randomWire(p);
	}


	/**
	 * @param p
	 */
	public void randomWire(double p) {
		//Initial wiring using a random network
		for (Object i: network.getNodes()) {
			for (Object j: network.getNodes()) {
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				if (random >= p) {
					network.addEdge(i, j);
				}
			}
		}
	}
	
	/**
	 * @return the edgesPerStep
	 */
	public int getEdgesPerStep() {
		return edgesPerStep;
	}

	/**
	 * @param edgesPerStep the edgesPerStep to set
	 */
	public void seEdgesPerStep(int edgesPerStep) {
		this.edgesPerStep = edgesPerStep;
	}

	/**
	 * @return the totalCustomers
	 */
	public int getTotalCustomers() {
		return totalCustomers;
	}

	/**
	 * @param totalCustomers the totalCustomers to set
	 */
	public void setTotalCustomers(int totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	/**
	 * @return the totalEntrepreneuers
	 */
	public int getTotalEntrepreneuers() {
		return totalEntrepreneuers;
	}

	/**
	 * @param totalEntrepreneuers the totalEntrepreneuers to set
	 */
	public void setTotalEntrepreneuers(int totalEntrepreneuers) {
		this.totalEntrepreneuers = totalEntrepreneuers;
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
	
	
	public void evolveNetwork() {
		while (totalCustomers > 0 || totalEntrepreneuers > 0) {
			
			double random = RandomHelper.nextDoubleFromTo(0, 1);
			
			if (totalEntrepreneuers > 0 && random >= 0.8) {
				Entrepreneur e = new Entrepreneur(context, network, SimulationBuilder.nextId("E"));
				attachNode(e);
				e.generateGoal();				
				totalEntrepreneuers--;				
			} else if (totalCustomers > 0) {
				attachNode(new Customer(context, network, SimulationBuilder.nextId("C")));
				totalCustomers--;
			}
		}
	}	


	@Override
	public Network<Object> createNetwork(Network<Object> network) {
		return network;
	}
	
	public void attachNode(Object n) {
		
	}
}