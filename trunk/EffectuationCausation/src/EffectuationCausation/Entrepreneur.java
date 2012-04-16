package EffectuationCausation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;

/**
 * @author klesti
 *
 */
public class Entrepreneur extends Agent {
	protected List<Means> availableMeans;
	protected Goal goal;
	protected boolean offering;
	protected boolean productRefinedOnce;	
	
	public Entrepreneur(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		availableMeans = new ArrayList<Means>();
		generateAvailableMeans();
		setOffering(false);
		productRefinedOnce = false;
	}

	public List<Means> getAvailableMeans() {
		return availableMeans;
	}

	public void setAvailableMeans(List<Means> availableMeans) {
		this.availableMeans = availableMeans;
	}
	
	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Goal goal) {		
		this.goal = goal;
	}
	
	
	
	/**
	 * @return the offering
	 */
	public boolean isOffering() {
		return offering;
	}

	/**
	 * @param offering the offering to set
	 */
	public void setOffering(boolean offering) {
		this.offering = offering;
	}

	public void addMeans(Means m) {
		availableMeans.add(m);
	}
	
	
	/**
	 *  Generates the available means
	 */
	public void generateAvailableMeans() {
		availableMeans.clear();
		
		Means m = new Means();
		
		//Random know-how
		
		int[] knowHow = new int[Parameters.vectorSpaceSize];
		
		for (int i = 0; i < knowHow.length; i++) {
			knowHow[i] = RandomHelper.nextIntFromTo(0, 1); 
		}
		
		m.setKnowHow(knowHow);
		
		m.setMoney(RandomHelper.nextDoubleFromTo(Parameters.minAvailableMoney, 
				Parameters.maxAvailableMoney));
		
		availableMeans.add(m);
	}
	
	public void generateGoal() {
		goal = new Goal();
		goal.generateRequiredMeans();
	}
		
	
	/**
	 * Effectuation scenario:
	 * Aggregate goal product vector based on the demand of the surrounding customers
	 */
	public void aggregateGoalProductVector() {
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		List<Customer> customers = new ArrayList<Customer>(); 
		
		SimulationBuilder.getCustomerAcquiantances(this, depth, customers);		
		
		if (customers.size() > 0) {
			Collections.shuffle(customers);
			
			int sampleTotal = RandomHelper.nextIntFromTo(1, customers.size());
			
			//Survey random connected customers sample
			
			int[] surveyResults = new int[Parameters.vectorSpaceSize];
			
			for (int i = 0; i < surveyResults.length; i++) {
				surveyResults[i] = 0;
			}			
			
			for (int i = 0; i < sampleTotal; i++) {
				int[] demandVector = customers.get(i).demandVector;
				
				for (int j = 0; j < demandVector.length; j++) {
					surveyResults[j] += demandVector[j];
				}
			}
			
			int[] productVector = goal.getProductVector();
			
			for (int i = 0; i < surveyResults.length; i++) {				
				if ( ((double)surveyResults[i] / (double)sampleTotal) * 100 
						>= Parameters.productElementChangeThreshold) {					
					productVector[i] = (productVector[i] + 1) % 2;				
				}
			}
		}
	}
	
	/**
	 * Meet an entity of an specified type (entrepreneur or customer)
	 * @param Class<?> Entity type 
	 * @return Object An acquaintance
	 */
	public Object meet(Class<?> type) {		
		int depth = RandomHelper.nextIntFromTo(1, Parameters.maxDepthForMeeting);
		
		int i = 0;
		Object o = this, acquaintance = this;
		
		while (i < depth && o != null) {
			o = network.getRandomAdjacent(acquaintance);

			if (o != null && o.getClass() == type && !((Agent)o).isNegotiating()) {
				acquaintance = o;
			}
			i++;
		}
		
		if (acquaintance == this) {
			acquaintance = null;			
		} 
		
		return acquaintance;		
	}		
	
	/**
	 * Offers the product to customers
	 */
	@ScheduledMethod(start=1,priority=2,interval=3)
	public void offer() {
		if (isOffering()) {
			Customer c;	
			
			c = (Customer) meet(Customer.class);
			
			if (c!=null && c instanceof Customer) {			
				c.processOffer(goal.getProductVector());
			}
		}
	}
	
	/**
	 * Offers a deal to the effectuator if meets him
	 */
	@ScheduledMethod(start=4,priority=3,interval=4)
	public void offerDeal() {
		if (isNegotiating() || isOffering() || (this instanceof Effectuator) 
				|| (this instanceof Causator)) {
			return;
		}
		
		Entrepreneur o = (Entrepreneur)meet(Effectuator.class);
		
		if (o != null && o instanceof Effectuator && !((Effectuator)o).isNegotiating()) {
			SimulationBuilder.printMessage("Deal offered from entrepreneur to effectuator.");
			setNegotiating(true);
			Effectuator e = (Effectuator)o;
			e.setNegotiating(true);
			int[] diff = SimulationBuilder.diff(e.getGoal().getProductVector(), getGoal().getProductVector());
			
			double changeCost = 0;
			boolean hasKnowHow = true;
			
			for (int i = 0; i < diff.length; i++) {
				if (diff[i] == 1) {
					changeCost += SimulationBuilder.productElementCost[i];
					if (hasKnowHow && availableMeans.get(0).getKnowHow()[i] != 1) {
						hasKnowHow = false;
					}
				}
			}
			
			Means m = new Means();
			
			m.setKnowHow(availableMeans.get(0).getKnowHow());
			
			if (availableMeans.get(0).getMoney() <= changeCost) {
				m.setMoney(changeCost);
				availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() - changeCost);
			} else {
				m.setMoney(availableMeans.get(0).getMoney());
				availableMeans.get(0).setMoney(0.0);
			}
			
			if (e.processOfferedDeal(this, goal.getProductVector(), diff, m)) {
				SimulationBuilder.printMessage("Deal from entrepreneur to effectuator accepted!");
			} else {
				SimulationBuilder.printMessage("Deal from entrepreneur to effectuator rejected!");
			}
			e.setNegotiating(false);
		}
	}
	
	/**
	 * Process the offer from an effectuator or causator
	 * @param productVector
	 * @return boolean accepted the offer ?
	 */
	public boolean processOffer(int[] productVector) {	
		setNegotiating(true);
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		if (Arrays.equals(goal.getProductVector(), productVector) && r <= 0.5) {
			setOffering(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Replies to the request for commitment of resources
	 * @param diffVector
	 * @return Means means
	 */
	public Means askCommitment(int[] diffVector) {
		double requiredFunds = goal.getRequiredMeans().getMoney();
		
		//Can provide know how ?
		for (int i = 0; i < diffVector.length; i++) {
			if (availableMeans.get(0).getKnowHow()[i] == 1) {
				requiredFunds -= SimulationBuilder.productElementCost[i];
			}
		}
		
		
		double r = RandomHelper.nextDoubleFromTo(0, 1);
				
		if (requiredFunds <= availableMeans.get(0).getMoney() && r <= 0.5) {
			setOffering(true);
			availableMeans.get(0).setMoney(availableMeans.get(0).getMoney() - requiredFunds);
			Means m = new Means();
			m.setKnowHow(availableMeans.get(0).getKnowHow());
			m.setMoney(requiredFunds);
			return m;
		}
		
		return null;
	}
	
	/**
	 * Process a deal about changing the product vector
	 * @param productVector
	 * @return 
	 */
	public boolean processDeal(int productVector[]) {
		double r = RandomHelper.nextDoubleFromTo(0, 1);
		
		if (r <= 0.5 &&
				SimulationBuilder.hammingDistance(productVector, goal.getProductVector()) < 3) {			
			return true;
		}
		return false;
	}
}
