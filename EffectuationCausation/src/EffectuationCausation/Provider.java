/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.JungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;

/**
 * @author klesti
 *
 */
public class Provider extends Agent {
	
	private List<Means> offeredMeans;

	public Provider(JungNetwork<Object> network, String label) {
		super(network, label);
		offeredMeans = new ArrayList<Means>();
		generateOfferedMeans();
	}

	public List<Means> getOfferedMeans() {
		return offeredMeans;
	}

	public void setOfferedMeans(List<Means> offeredMeans) {
		this.offeredMeans = offeredMeans;
	}
	
	@Watch(watcheeClassName = "EffectuationCausation.Goal", watcheeFieldNames = "requiredMeans", 
			 whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)	
	public void generateOfferedMeans() {
		clearOfferedMeans();
		Context<Object> context = ContextUtils.getContext(this);		
		Network<Object> network = (Network<Object>)context.getProjection("network");
		
		int totalRequiredMeans = context.getObjects(Means.class).size();
		
		for (Object o: context.getRandomObjects(Means.class, RandomHelper.nextIntFromTo(1, totalRequiredMeans))) {
			Means m = (Means)o;
			offeredMeans.add(m);
			CausationBuilder.offeredMeans.add(m);
			network.addEdge(this, o);			
		}		
		assureAllMeansAreOffered(context, network);
	}
	
	public void clearOfferedMeans() {
		JungNetwork<Object> network = (JungNetwork<Object>)context.getProjection("network");
		for (Means m: offeredMeans) {
			RepastEdge<Object> edge = network.getEdge(this, m);
			network.removeEdge(edge);
		}
		offeredMeans.clear();		
	}
	
	public void assureAllMeansAreOffered(Context<Object> context, Network<Object> network) {		

		List<Provider> providers = new ArrayList<Provider>();
		
		for (Object o: context.getObjects(Provider.class)) {
			Provider p = (Provider)o;
			//if not all the providers have "declared" their offered means, exit
			if (p.getOfferedMeans().size() == 0) {
				return;
			}
			providers.add((Provider)o);
		}
		
		Goal g = (Goal)context.getObjects(Goal.class).get(0);
		List<Means> requiredMeans = new ArrayList<Means>(g.getRequiredMeans());
		requiredMeans.removeAll(CausationBuilder.offeredMeans);
		
		//Spread the missing means randomly among the providers
		
		for (Means m: requiredMeans) {
			int n = RandomHelper.nextIntFromTo(1, providers.size());
			for (int i = 0; i < n; i++) {
				int j = RandomHelper.nextIntFromTo(0, providers.size()-1);
				providers.get(j).getOfferedMeans().add(m);
				network.addEdge(providers.get(j), m);
			}
		}
		
	}
}
