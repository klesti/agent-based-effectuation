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
	@ScheduledMethod(start=2,interval=2,priority=1)
	@Override	
	public void offer() {
		Entrepreneur e = null;
		
		if (isOffering() || SimulationBuilder.allEntrepreneursOffering) {
			return;
		}
		
		e = (Entrepreneur)meet(Entrepreneur.class);
		
		if (e != null && !(e instanceof Causator) && !(e instanceof Effectuator) && !e.isOffering()) {			
			
			setNegotiating(true);
			SimulationBuilder.printMessage("Deal offered from effectuator to entrepreneur.");
			
			if (!e.processOffer(getGoal().getProductVector())) {				
				int[] diff = SimulationBuilder.diff(goal.getProductVector(), e.getGoal().getProductVector());
				
				double changeCost = 0;
				
				for (int i = 0; i < diff.length; i++) {
					if (diff[i] == 1) {
						changeCost += SimulationBuilder.productElementCost[i];
					}
				}
				
				if (changeCost <=  (availableMeans.get(0).getMoney() * Parameters.affordableLoss) / 100.0) {
					if (e.processOffer(e.getGoal().getProductVector())) {
						goal = e.getGoal();
						availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() - changeCost);
						Commitment c = new Commitment(e);
						c.setGoal(e.getGoal());
						commitmentList.add(c);						
						SimulationBuilder.effectuationNetwork.addEdge(this, e);
						productRefinedOnce = true;
						setOffering(true);
						SimulationBuilder.printMessage("Effectual commitment estabilished!");
					}
				} else {
					Means m = e.askCommitment(diff);
					if (m != null) {
						goal = e.getGoal();				
						availableMeans.add(m);
						Commitment c = new Commitment(e);
						c.setGoal(e.getGoal());
						c.setMeans(m);
						commitmentList.add(c);
						SimulationBuilder.effectuationNetwork.addEdge(this, e);		
						productRefinedOnce = true;
						setOffering(true);
						SimulationBuilder.printMessage("Effectual commitment estabilished!");
					}
				}
			} else {
				Commitment c = new Commitment(e);
				c.setGoal(e.getGoal());
				commitmentList.add(c);				
				SimulationBuilder.effectuationNetwork.addEdge(this, e);
				setOffering(true);
				SimulationBuilder.printMessage("Effectuators deal accepted by entrepreneur!");
			}
			
			e.setNegotiating(false);
			setNegotiating(false);
		}
	}
	
	/**
	 * Processes a possible offered deal from another entrepreneur
	 * @param productVector
	 * @param m
	 */
	public boolean processOfferedDeal(Entrepreneur e, int[] productVector, int[] productChanges, Means m) {
		boolean allAgreed = true;
		int [] generalKnowHow = new int[Parameters.vectorSpaceSize];
		for (int i = 0; i < generalKnowHow.length; i++) {
			generalKnowHow[i] = 0;
		}
		
		if (isOffering()) {			
			for (Commitment c: commitmentList) {
				if (!c.getSecondParty().processDeal(productVector)) {
					allAgreed = false;
				}
				for (int j = 0; j < generalKnowHow.length; j++) {			
					if (c.getMeans() != null && c.getMeans().getKnowHow()[j] == 1 
							|| availableMeans.get(0).getKnowHow()[j] == 1) {
						generalKnowHow[j] = 1;
					}					
				}
			}			
		}
		
		if (allAgreed) {
		
			double changeCost = 0.0;
			
			for (int i = 0; i < productChanges.length; i++) {
				if (productChanges[i] == 1) {
					 if(generalKnowHow[i] != 1) {
						 changeCost += SimulationBuilder.productElementCost[i];
					 }
				}
			}
			
			if (changeCost <= m.getMoney() + availableMeans.get(0).getMoney()) {
				Commitment c = new Commitment(e);
				c.setMeans(m);
				availableMeans.add(m);
				goal.setProductVector(productVector);
				c.setGoal(getGoal());				
				changeCost -= m.getMoney();				
				availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() - changeCost);
				SimulationBuilder.effectuationNetwork.addEdge(this, e);
				for (Commitment cm: commitmentList) {
					cm.getSecondParty().getGoal().setProductVector(productVector);
				}
				
				commitmentList.add(c);
				setOffering(true);
				return true;
			}
		}		
		
		return false;
	}
}
