/**
 * 
 */
package EffectuationCausation;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.RandomDensityGenerator;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;


/**
 * @author klesti
 *
 */
public class BarabasiAlbertNetworkGenerator extends RandomDensityGenerator<Object> {
	
	private int edgesPerStep;
	private int totalCustomers;
	private int totalEntrepreneuers;
	private int totalInvestors;
	private Context<Object> context;
	private Network<Object> network;

	public BarabasiAlbertNetworkGenerator() {
		super(0.2, false, false);
		
		context = new DefaultContext<Object>("effectual network");
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("network",
				context, true);
		
		network = netBuilder.buildNetwork();
		
		edgesPerStep = 0;
		totalCustomers = 0;
		totalEntrepreneuers = 0;
		totalInvestors = 0;
	}

	/* (non-Javadoc)
	 * @see repast.simphony.context.space.graph.RandomDensityGenerator#createNetwork(repast.simphony.space.graph.Network)
	 */
	@Override
	public Network<Object> createNetwork(Network<Object> network) {		
		initializeNetworkNodes();
		Network<Object> n = super.createNetwork(network);
		
		this.network = n;

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
		
		return n;
	}
	
	/**
	 * Preferential attachment
	 * @param n Node to be attached
	 */
	public void attachNode(Agent n) {
		context.add(n);
		for (int i = 0; i < edgesPerStep; i++) {
			
			double totalDegree = network.getDegree();
			
			for (Object o: context) {				
				double prob = network.getDegree(o) / totalDegree;
				
				if (prob > 0.0 && RandomHelper.nextDoubleFromTo(0,1) >= prob) {				
					network.addEdge(n, o);
					break;				
				}			
			}
		}
	}	
	
	
	private void initializeNetworkNodes() {
		if (totalCustomers == 0 && totalEntrepreneuers == 0 && totalInvestors == 0) {
			initializeParametersRandomly();
		}
		
		context.add(new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt())));
		totalCustomers--;
		context.add(new Customer(context, network, "Customer" + String.valueOf(RandomHelper.nextInt())));
		totalCustomers--;

		Entrepreneur e1 = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
		e1.generateGoal();
		totalEntrepreneuers--;
		
		Entrepreneur e2 = new Entrepreneur(context, network, "Entrepreneur" + String.valueOf(RandomHelper.nextInt()));
		e2.generateGoal();
		context.add(e2);		
		totalEntrepreneuers--;
		
		context.add(new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt())));
		totalInvestors--;
		context.add(new Investor(context, network, "Investor" + String.valueOf(RandomHelper.nextInt())));
		totalInvestors--;	
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
