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
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

/**
 * @author klesti
 *
 */
public class Provider extends Agent {
	
	private List<Means> offeredMeans;
	private List<ProvidesTo> providesToList;

	public Provider(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		offeredMeans = new ArrayList<Means>();
		providesToList = new ArrayList<ProvidesTo>();		
	}


	/**
	 * @return the offeredMeans
	 */
	public List<Means> getOfferedMeans() {
		return offeredMeans;
	}

	/**
	 * @param offeredMeans the offeredMeans to set
	 */
	public void setOfferedMeans(List<Means> offeredMeans) {
		for (Means m: this.offeredMeans) {
			context.remove(m);
		}
		offeredMeans.clear();
		
		for (Means m: offeredMeans) {
			if (!context.contains(m)) {
				context.add(m);
				network.addEdge(this, m);
			}
		}
		
		this.offeredMeans = offeredMeans;
	}


	/**
	 * @return the providesToList
	 */
	public List<ProvidesTo> getProvidesToList() {
		return providesToList;
	}

	/**
	 * @param providesToList the providesToList to set
	 */
	public void setProvidesToList(List<ProvidesTo> providesToList) {
		this.providesToList = providesToList;
	}

	@Watch(watcheeClassName = "EffectuationCausation.Goal", watcheeFieldNames = "requiredMeans", 
			 whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)	
	public void generateOfferedMeans() {
		clearOfferedMeans();
		
		int totalRequiredMeans = CausationBuilder.context.getObjects(Means.class).size();
		
		for (Object o: CausationBuilder.context.getRandomObjects(Means.class, RandomHelper.nextIntFromTo(1, totalRequiredMeans))) {
			Means m = (Means)o;
			offeredMeans.add(m);
			CausationBuilder.offeredMeans.add(m);
			//Add an edge with a random weight (price, time, etc) between the offered means and the 
			// provider
			RepastEdge<Object> edge = new RepastEdge<Object>(m, this, true, 
					RandomHelper.nextIntFromTo(CausationBuilder.meansOfferedWeightRange[0], 
							CausationBuilder.meansOfferedWeightRange[1]));
			network.addEdge(edge);	
		}		
		assureAllMeansAreOffered(CausationBuilder.context, network);
	}
	
	public void clearOfferedMeans() {		
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
				RepastEdge<Object> edge = new RepastEdge<Object>(m, providers.get(j), true, 
						RandomHelper.nextIntFromTo(CausationBuilder.meansOfferedWeightRange[0], 
								CausationBuilder.meansOfferedWeightRange[1]));
				network.addEdge(edge);
			}
		}
		
	}
}
