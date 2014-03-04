package sampleCandidates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import query.Query;
import utils.Constants;
import utils.Mergers;

import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;

/**
 * From all the columns of the table, randomly select qcs
 * of size 1 or 2 (without replacement)
 * @author pankhuri
 *
 */
public class SampleGenerator {
	
	String sampleCandidateBasePath = Constants.basePath + "/candidateSet/";
	
	// for each qcs, what is the name of the sample table that will be created
	//static Map<String, String> sampleQcsSampleTableNameMap = new HashMap<String, String>();

	// Stores all the qcs generated up till now for the table. This saves from repeatedly generating samples with same qcs
 	public static List<String> sampleQcsAll = new ArrayList<String>();
 	// for each table, how many samples have already been created
 	public static Map<String, Map<String,Integer> > tableQcsSampleNumberMap = new HashMap<String, Map<String,Integer> >();
 	public static Map<String, Integer> tableSampleNumberMap = new HashMap<String, Integer>();
 	
 	
 	private void updateSampleNumbers(String tableName, String qcs){
 		Integer sampleNumber = 0;
 		if(tableSampleNumberMap.keySet().contains(tableName))
 			sampleNumber = tableSampleNumberMap.get(tableName);
 		else
 			sampleNumber = 0;
 		tableSampleNumberMap.put(tableName, ++sampleNumber);
 		
 		Map<String, Integer> temp;
 		if(tableQcsSampleNumberMap.keySet().contains(tableName)){
 			temp = tableQcsSampleNumberMap.get(tableName);
 		}else{
 			temp = new HashMap<String, Integer>();
 		}
		temp.put(qcs, sampleNumber);
		tableQcsSampleNumberMap.put(tableName, temp);
		
 	}
 	
 	private void resetSampleNumbers(){
 		for(String table : tableSampleNumberMap.keySet())
 			tableSampleNumberMap.put(table, 0);
 	}
 	
 	/*
	 * Gets a list of qcs that are to be built 
	 * (this means the number of samples that would be generated)
	 */
	public List<String> GetSampleQcsToBuild(SchemaTable table, Integer frameNumber, SampleQueryGenerator sampleQueryGen){
		List<String> sampleQcsToBuild = new ArrayList<String>();
		if(frameNumber == 0){
			// first frame. Generate random candidates
			for(int sampleNo = 0; sampleNo < Constants.NumberOfSamples ; sampleNo++){
				// for each sample, generate a random query
				List<String> sampleQcsList ;
				String currentQcs;
				do{
					sampleQcsList = new ArrayList<String>();
					Query sampleQuery = sampleQueryGen.GenerateRandomSampleQuery(table);
					List<Column> qcsColumns = sampleQuery.getWhereClause();
					sampleQcsList = Mergers.DeDuplicate(qcsColumns);
					Collections.sort(sampleQcsList);
					currentQcs = Mergers.JoinStrings(sampleQcsList, " ");
				}while(sampleQcsAll.contains(currentQcs));
				
				sampleQcsAll.add(currentQcs);
				updateSampleNumbers(table.getTableName(), currentQcs);
				sampleQcsToBuild.add(currentQcs);
			}
			
			// Write to the QCS file
			String fileName = sampleCandidateBasePath + "frame_" + frameNumber.toString() + "/qcs";		
			try{
				BufferedWriter bufWrite = new BufferedWriter(new FileWriter(fileName));
				bufWrite.write(Mergers.JoinStrings(sampleQcsToBuild,"\n"));
				bufWrite.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return sampleQcsToBuild;
		}else{
			// It's a different time frame number.
			// All the previous samples have to be cleared and so, initialize the number of samples to be 0.
			resetSampleNumbers();
		}
		
		// Read the QCS from the already present file ( generated either by GLPK wrapper)
		String fileName = sampleCandidateBasePath + "frame_" + frameNumber.toString() + "/qcs";
		try{
			BufferedReader bufRead = new BufferedReader(new FileReader(fileName));
			String row = bufRead.readLine();
			while(row != null){
				sampleQcsToBuild.add(row.trim());
				updateSampleNumbers(table.getTableName(), row.trim());
				row = bufRead.readLine();
			}
			bufRead.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sampleQcsToBuild;
	}
	
	public void GenerateSamples(SchemaTable table, Integer frameNumber){
		
		// Make the directory
		String filePath = Constants.basePath + "/candidateSet/frame_" + frameNumber.toString();
		SampleQueryGenerator sampleQueryGen = new SampleQueryGenerator();
		BufferedWriter bufWrite;

		try{
			List<String> sampleQcsList = GetSampleQcsToBuild(table, frameNumber, sampleQueryGen);
			// Getting Create queries for samples
			String allCreateQueries = sampleQueryGen.GenerateSampleSqlCreateQueries(sampleQcsList, table.getTableName());
			bufWrite = new BufferedWriter(new FileWriter(filePath + "/candidate_create.queries"));
			bufWrite.write(allCreateQueries);
			bufWrite.write("\nquit;");
			bufWrite.close();
			
			// Getting DROP queries for the samples
			String allDropQueries = sampleQueryGen.GenerateSampleSqlDropQueries(sampleQcsList, table.getTableName());
			bufWrite = new BufferedWriter(new FileWriter(filePath + "/candidate_drop.queries"));
			bufWrite.write(allDropQueries);
			bufWrite.write("\nquit;");
			bufWrite.close();
			
			// Getting COUNT queries for the samples 
			// This is for collecting the statistics for the samples - DQCS;
			String dqcsStatsCollectorQueries = sampleQueryGen.GenerateSampleSqlDqcsQueries(sampleQcsList, table.getTableName());
			bufWrite = new BufferedWriter(new FileWriter(filePath + "/candidate_stats_dqcs.queries"));
			bufWrite.write(dqcsStatsCollectorQueries);
			bufWrite.write("\nquit;");
			bufWrite.close();

			// Getting COUNT queries for the samples 
			// This is for collecting the total storage capacity for the samples
			String storageStatsCollectorQueries = sampleQueryGen.GenerateSampleSqlStorageCountQueries(sampleQcsList, table.getTableName());
			bufWrite = new BufferedWriter(new FileWriter(filePath + "/candidate_stats_storage.queries"));
			bufWrite.write(storageStatsCollectorQueries);
			bufWrite.write("\nquit;");
			bufWrite.close();
			
		}catch(Exception ex){
			System.out.println("Exception : " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
