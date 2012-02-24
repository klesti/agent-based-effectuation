/**
 * 
 */
package EffectuationCausation;

import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.graph.EdgeCreator;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class SimulationBuilder extends DefaultContext<Object> implements ContextBuilder<Object> {

	public static Context<Object> context;
	public static Network<Object> entrepreneurialNetwork;
	public static Network<Object> effectuationNetwork;	
	private static EntrepreneurialNetworkGenerator networkGenerator;
	private static HashMap<String, Integer> lastIds;
	
	@Override
	public Context<Object> build(Context<Object> context) {
		Parameters.initializeEffectuation();
		
		lastIds = new HashMap<String, Integer>();
		
		context.setId("effectuation-causation");
				
		SimulationBuilder.context = context;
		
		buildNetworks();
		
		//Add the effectuator entrepreneur 
		Effectuator effectuator = new Effectuator(context, entrepreneurialNetwork, "Effectuator");
		context.add(effectuator);
		effectuator.generateAvailableMeans();
		
		//Add the causator entrepreneur and it's initial goal
		Causator causator = new Causator(context, entrepreneurialNetwork, "Causator");
		context.add(causator);
		
		Goal initialGoal = new Goal();		
		causator.setGoal(initialGoal);		
		
		context.add(causator);

		
		//Network generation
		
		if (Parameters.networkGenerator.equals("BarabasiAlbert")) {
			networkGenerator = new BarabasiAlbertNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("CopyModel")) {
			networkGenerator = new CopyingModelNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("RandomNetwork")) {
			networkGenerator = new RandomNetworkGenerator(context);
		} else if (Parameters.networkGenerator.equals("SmallWorld")) {
			networkGenerator = new SmallWorldNetworkGenerator(context);
		}
		
		networkGenerator.setTotalCustomers(Parameters.numberOfCustomers);
		networkGenerator.setTotalEntrepreneuers(Parameters.numberOfEntrepreneurs);
		networkGenerator.seEdgesPerStep(Parameters.edgesPerStep);
		
		entrepreneurialNetwork = networkGenerator.createNetwork(entrepreneurialNetwork);
		
		System.out.println(entrepreneurialNetwork.numEdges());
		
		for (Object o: entrepreneurialNetwork.getNodes()) {
			if (o instanceof Means || o instanceof Goal) {
				System.out.println("Test");
			}
		}
		
		return context;
	}

	/**
	 * @param context
	 */
	private void buildNetworks() {
		EdgeCreator<NetworkEdge, Object> edgeCreator = new EdgeCreator<NetworkEdge, Object>(
				
		) {
			public Class<NetworkEdge> getEdgeType() {
				return NetworkEdge.class;
			}

			@Override
			public NetworkEdge createEdge(Object source, Object target,
					boolean isDirected, double weight) {
				return new NetworkEdge(source, target, true, 0);
			}
		};
		
		
		//Build Entrepreneurial network
		
		NetworkBuilder<Object> netBuilder2 = new NetworkBuilder<Object>("entrepreneurial network",
				context, false);
		
		netBuilder2.setEdgeCreator(edgeCreator);		
		
		SimulationBuilder.entrepreneurialNetwork = netBuilder2.buildNetwork();
		
		//Build Effectuation network
		
		NetworkBuilder<Object> netBuilder3 = new NetworkBuilder<Object>("effectuation network",
				context, false);
		
		netBuilder3.setEdgeCreator(edgeCreator);	
		
		SimulationBuilder.effectuationNetwork = netBuilder3.buildNetwork();
	}

	/**
	 * Returns the next Id of a node, also by specifying a certain prefix
	 * @param prefix
	 * @return nextId
	 */
	public static String nextId(String prefix) {
		int nextId;
		
		if (lastIds.containsKey(prefix)) {
			nextId = lastIds.get(prefix) + 1;
			lastIds.put(prefix, nextId);
		} else {
			nextId = 1;
			lastIds.put(prefix, nextId);
		}
		
		return prefix + String.valueOf(nextId);
	}
}
