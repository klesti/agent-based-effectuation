/**
 * 
 */
package EffectuationCausation;

/**
 * @author klesti
 * Association class that specifies what means does a provider provide to an entrepreneur
 */

public class ProvidesTo {
	private Entrepreneur entrepreneur;
	private Means means;
	
	public ProvidesTo(Entrepreneur entrepreneur, Means means) {		
		this.entrepreneur = entrepreneur;
		this.means = means;
	}
	
	/**
	 * @return the entrepreneur
	 */
	public Entrepreneur getEntrepreneur() {
		return entrepreneur;
	}
	/**
	 * @param entrepreneur the entrepreneur to set
	 */
	public void setEntrepreneur(Entrepreneur entrepreneur) {
		this.entrepreneur = entrepreneur;
	}
	/**
	 * @return the means
	 */
	public Means getMeans() {
		return means;
	}
	/**
	 * @param means the means to set
	 */
	public void setMeans(Means means) {
		this.means = means;
	}
	
	
}