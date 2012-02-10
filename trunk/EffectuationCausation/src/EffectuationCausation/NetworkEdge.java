/**
 * 
 */
package EffectuationCausation;

import repast.simphony.space.graph.RepastEdge;

/**
 * @author klesti
 *
 */
public class NetworkEdge extends RepastEdge<Object> {
	
	private double thickness;

	public NetworkEdge() {
		super();
		setThickness(1.0);
	}

	public NetworkEdge(Object source, Object target, boolean directed,
			double weight) {
		super(source, target, directed, weight);
		setThickness(1.0);
	}

	public NetworkEdge(Object source, Object target, boolean directed) {
		super(source, target, directed);
		setThickness(1.0);
	}

	/**
	 * @return the thickness
	 */
	public double getThickness() {
		return thickness;
	}

	/**
	 * @param thickness the thickness to set
	 */
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
}
