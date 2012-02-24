package EffectuationCausation;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;

public class Causator extends Entrepreneur {
	
	protected int[] aggregatedSurveyResults;

	public Causator(Context<Object> context, Network<Object> network, String label) {
		super(context, network, label);
		
		availableMeans = new ArrayList<Means>();
		//Initialize aggregatedSurveyResults
		aggregatedSurveyResults = new int[Parameters.vectorSpaceSize];
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {
			aggregatedSurveyResults[i] = 0;
		}		
	}	

	/**
	 *  Refine product based on the results of the initial market research
	 */
	public void refineProduct() {	
		
		int[] productVector = goal.getProductVector();
		
		for (int i = 0; i < aggregatedSurveyResults.length; i++) {		
			
			if ( ((double)aggregatedSurveyResults[i] / (double)CausationBuilder.sampleSize) * 100 
					>= Parameters.productElementChangeThreshold) {
				
				productVector[i] = (productVector[i] + 1) % 2;				
			}
		}
		System.out.println("Product refined");
		goal.setProductVector(productVector);
	}
}
