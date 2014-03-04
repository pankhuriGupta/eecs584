package distributions;

import java.util.ArrayList;
import java.util.List;
import utils.Helpers;
import java.util.Random;
import utils.Constants;

/**
 * The class generates frequency distributions
 * This frequency dictates how many instances of a query type should be in a time frame.
 * @author pankhuri
 *
 */
public class GenerateDistributions {

	String distributionType;
	
	public String getDistributionType(){
		return this.distributionType;
	}
	
	public void setDistributionType(String type){
		this.distributionType = type;
	}
	
	/**
	 * Create frequency of occurrence of each type of query in a frame,
	 * according to power law.
	 * @return
	 */
	public static List<Integer> createPowerLawFrequencies(int numberQueryTypes, int totalNumberOfQueries) {
		double[] values = new double[numberQueryTypes];
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.random() * 100 + Math.exp(0.5 * i);		
		}
		// normalize values to compute frequencies
		values = Helpers.Normalize(values);

		List<Integer> freqs = new ArrayList<Integer>();
		for (int i = 0; i < values.length; i++)  
			freqs.add((int) Math.ceil(values[i] * totalNumberOfQueries));
		
		/*
		int sum = 0 ;
		for(Integer i : freqs){
			System.out.println(i);
			sum += i;
		}
		System.out.println("total = " + sum);
		*/
		
		return freqs;
	}

	/**
	 * Create frequency of occurrence of each type of query in a frame,
	 * according to normal distribution (mean = 0,standard variance = 1).
	 * @return
	 */
	public static List<Integer> createNormalDistributionFrequencies(int numberQueryTypes, int totalNumberOfQueries) {
		Random random = new Random();
		double[] values = new double[numberQueryTypes];
		for (int i = 0; i < values.length; i++) {
			values[i] = random.nextGaussian();		
		}
		
		// normalize values to compute frequencies
		values = Helpers.Normalize(values);
		
		List<Integer> freqs = new ArrayList<Integer>();
		for (int i = 0; i < values.length; i++)  
			freqs.add((int) Math.ceil(values[i] * totalNumberOfQueries));
		return freqs;
	}

}
