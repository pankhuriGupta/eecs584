package distributions;

import java.util.Collections;
import java.util.List;

public class ExtractCdf {

	public String getCdf(List<Integer> elements, double breakPoint){
		Integer returnValue = 0;
		Collections.sort(elements);
		Integer i ;
		// Calculating cdf from the distribution
		for(i = 1 ; i < elements.size() ; i++){
			Integer sum = elements.get(i) + elements.get(i-1);
			elements.set((i), sum) ;
		}
		for( i = 0 ; i < elements.size() ; i++){
			double fraction = (double)(elements.get(i))/(double)(elements.get(elements.size()-1)); 
			if( fraction >= breakPoint){
				returnValue = elements.get(i);
				break;
			}
		}
		return returnValue.toString();
	}
/*
	public String getCdf(List<Double> elements, double breakPoint){
		double returnValue = 0.0;
		Collections.sort(elements);
		Integer i ;
		// Calculating cdf from the distribution
		for(i = 1 ; i < elements.size() ; i++){
			double sum = elements.get(i) + elements.get(i-1);
			elements.set(i, sum) ;
		}
		for( i = 0 ; i < elements.size() ; i++){
			double fraction = (double)(elements.get(i))/(double)(elements.get(elements.size()-1)); 
			if( fraction >= breakPoint){
				returnValue = elements.get(i);
				break;
			}
		}
		return returnValue;
	}
	*/
}
