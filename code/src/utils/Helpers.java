package utils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Helpers {

	private static double sum(double[] values){
		double result  = 0.0 ;
		for (int i = 0 ; i < values.length; i++){
			result += values[i];
		}
		return result;
	}
	
	public static double[] Normalize(double[] values) {
		if (values == null)
			return null;
		double s = sum(values);
		double[] ret = new double[values.length];
		for (int i = 0; i < values.length; i++)
			ret[i] = values[i] / s;
		return ret;
	}
	
	public static ArrayList<String> tokenize(String row){
		StringTokenizer StrTkn = new StringTokenizer(row, " ");
    	ArrayList<String> tokens = new ArrayList<String>(row.length());
        while(StrTkn.hasMoreTokens())
        	tokens.add(StrTkn.nextToken());
        return tokens;
	}
}
