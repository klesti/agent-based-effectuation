package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

public class EntrepreneurialNetworkGenerator {

	protected int edgesPerStep;
	protected int totalCustomers;
	protected int totalEntrepreneuers;
	protected int totalInvestors;
	protected Context<Object> context;
	protected Network<Object> network;

	public EntrepreneurialNetworkGenerator(Context<Object> context) {
		this.context = context;
				
		edgesPerStep = 0;
		totalCustomers = 0;
		totalEntrepreneuers = 0;
		totalInvestors = 0;
	}

	
	/**
	 * Initializes the network
	 * @param p initial wiring probability
	 */
	protected void initializeNetwork(double p) {
		if (totalCustomers == 0 && totalEntrepreneuers == 0 && totalInvestors == 0) {
			initializeParametersRandomly();
		}
		
		context.add(new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt())));
		totalCustomers--;
		context.add(new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt())));
		totalCustomers--;
	
		Entrepreneur e1 = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
		context.add(e1);
		e1.generateGoal();
		totalEntrepreneuers--;
		
		Entrepreneur e2 = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
		context.add(e2);
		e2.generateGoal();
	
		totalEntrepreneuers--;
		
		Investor i1 = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt()));
		context.add(i1);
		i1.generateGoal();		
		totalInvestors--;
		
		Investor i2 = new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt()));
		context.add(i2);
		i2.generateGoal();
		totalInvestors--;
		
		//Initial wiring using a random network
		for (Object i: network.getNodes()) {
			for (Object j: network.getNodes()) {
				double random = RandomHelper.nextDoubleFromTo(0, 1);
				if ( (i instanceof Customer || i instanceof Investor || i instanceof Entrepreneur) &&
						(j instanceof Customer || j instanceof Investor || j instanceof Entrepreneur) &&
							random >= p) {
					network.addEdge(i, j);					
				}
			}
		}
	}
	
	public void initializeParametersRandomly() {
		edgesPerStep = RandomHelper.nextIntFromTo(2, 5);
		totalCustomers = RandomHelper.nextIntFromTo(5, 50);
		totalEntrepreneuers = RandomHelper.nextIntFromTo(2, 10);
		totalInvestors = RandomHelper.nextIntFromTo(2, 5);
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
	 * @return the totalInvestors
	 */
	public int getTotalInvestors() {
		return totalInvestors;
	}

	/**
	 * @param totalInvestors the totalInvestors to set
	 */
	public void setTotalInvestors(int totalInvestors) {
		this.totalInvestors = totalInvestors;
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

}