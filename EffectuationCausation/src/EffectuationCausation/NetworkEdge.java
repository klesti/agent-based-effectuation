/**
 * 
 */
package EffectuationCausation;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;

/**
 * @author klesti
 *
 */
public class NetworkEdge extends RepastEdge<Object> {
	
	private double thickness;
	private Color color;

	public NetworkEdge() {
		super();
		setThickness(1.0);
		setColor(Color.BLACK);
	}

	public NetworkEdge(Object source, Object target, boolean directed,
			double weight) {
		super(source, target, directed, weight);
		setThickness(1.0);
		setColor(Color.BLACK);
	}

	public NetworkEdge(Object source, Object target, boolean directed) {
		super(source, target, directed);
		setThickness(1.0);
		setColor(Color.BLACK);
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

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getRed() {
		return color.getRed() / 255;
	}
	
	public double getGreen() {
		return color.getGreen() / 255;
	}
	
	public double getBlue() {
		return color.getBlue() / 255;
	}
}
