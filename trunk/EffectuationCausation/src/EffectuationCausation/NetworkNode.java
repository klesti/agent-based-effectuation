/**
 * 
 */
package EffectuationCausation;

/**
 * @author klesti
 *
 */
public class NetworkNode {
	protected String label;
	protected int graphicsSize;
	
	public NetworkNode() {
		
	}
	
	public NetworkNode(String label) {
		this.label = label;
	}
	
	public NetworkNode(String label, int graphicsSize) {
		this.label = label;
		this.graphicsSize = graphicsSize;
	}

	/**
	 * @return the graphicsSize
	 */
	public int getGraphicsSize() {
		return graphicsSize;
	}

	/**
	 * @param graphicsSize the graphicsSize to set
	 */
	public void setGraphicsSize(int graphicsSize) {
		this.graphicsSize = graphicsSize;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
