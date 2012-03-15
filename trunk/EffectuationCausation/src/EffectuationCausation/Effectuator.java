/**
 * 
 */
package EffectuationCausation;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Effectuator extends Entrepreneur {
	
	private ArrayList<Commitment> commitmentList;
	
	public Effectuator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		commitmentList = new ArrayList<Commitment>();
		generateGoal();
	}

	/**
	 *  @description Generate goal based on the available means (require all of them)
	 */	
	@Override
	public void generateGoal() {		
		super.generateGoal();
		
		if (availableMeans.size() == 0) {
			generateAvailableMeans();
		}
		
		goal.setProductVector(availableMeans.get(0).getKnowHow());
		
		if (availableMeans.get(0).getMoney() < goal.getRequiredMeans().getMoney()) {
			availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() 
					+ goal.getRequiredMeans().getMoney());
		}
	}
	
	/* 
	 * Offers the product to an entrepreneur 
	 */
	@Override
	@ScheduledMethod(start=1,interval=2,priority=1)	
	public void offer() {
		Entrepreneur e = null;
		
		if (SimulationBuilder.allEntrepreneursOffering) {
			return;
		}
		
		e = (Entrepreneur)meet(Entrepreneur.class);
		
		if (!productRefinedOnce && e != null && !e.isNegotiating()) {

			int[] diff = SimulationBuilder.diff(goal.getProductVector(), e.getGoal().getProductVector());
			
			double changeCost = 0;
			
			for (int i = 0; i < diff.length; i++) {
				if (diff[i] == 1) {
					changeCost += SimulationBuilder.productElementCost[i];
				}
			}
			
			if (changeCost <=  (availableMeans.get(0).getMoney() * Parameters.affordableLoss) / 100.0) {
				if (e.processOffer(e.getGoal().getProductVector())) {
					goal.setProductVector(e.getGoal().getProductVector());
					availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() - changeCost);
					SimulationBuilder.effectuationNetwork.addEdge(this, e);
					productRefinedOnce = true;
				}
			} else {
				Means m = e.askCommitment(diff);
				if (m != null) {
					goal.setProductVector(e.getGoal().getProductVector());				
					availableMeans.add(m);
					Commitment c = new Commitment(e);
					c.setGoal(e.getGoal());
					c.setMeans(m);
					commitmentList.add(c);
					SimulationBuilder.effectuationNetwork.addEdge(this, e);		
					productRefinedOnce = true;
				}
			}
			
			e.setNegotiating(false);
		}
	}
}
