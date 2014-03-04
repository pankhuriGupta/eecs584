package utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public static int NumOfTypesOfQueriesPerWindow = 5;
	public static int NumOfQueriesPerWindow = 20; 
	//public static String basePath = "/Users/pankhuri/Desktop/EECS584_Blink_Files";
	public static String basePath = "/home/ec2-user/eecs584/workloadGen";
	public static String distributionType = "PowerLaw";
	public static int NumberOfSamples = 10;
	public static int NumberOfTimeFrames = 10;
	public static long M = 10000;	// This is for calculating sparsity delta values
	public static long C = 1107756;
	public static List<Double> distanceFractions = Arrays.asList(0.1, 0.3, 0.5, 0.7, 0.9);
}
