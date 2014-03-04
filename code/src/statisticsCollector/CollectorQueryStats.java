package statisticsCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Constants;
import utils.Helpers;
import utils.Mergers;

public class CollectorQueryStats {

	Map<String, Integer> qcsNameToNumberMap = new HashMap<String,Integer>();
	Map<Integer, Integer> qcsFreq = new HashMap<Integer,Integer>();
	
	/**
	 * Updates the frequency of any qcs of the query in the given query set
	 * @param qcs
	 * @return
	 */
	private void updateQueryFrequency(String qcs){
		Integer queryNumber = qcsNameToNumberMap.get(qcs);
		if(qcsFreq.containsKey(queryNumber))
			qcsFreq.put(queryNumber, qcsFreq.get(queryNumber) + 1);
		else
			qcsFreq.put(queryNumber, 1);
	}
	
	// ---------- PUBLIC MEMER FUNCTIONS ---------------
	/**
	 * Main function that collects the statistics of queries
	 * and writes them out to a file.
	 * This will then be fed as input to GLPK solver 
	 * @param oldFrameNumber
	 * @param distance
	 */
	public void GenerateStatsCollectorSqlQueries(Integer timeFrameNumber, Integer distance, String queryTableName) {
		Integer qcsNumber = 1;
		
		// Step : Open the file from the Queries folder to start reading qcs of queries
		String workingDirectory = Constants.basePath+"/querySet/querySet" + distance+"/frame_"+ timeFrameNumber ;
		String readFileName = workingDirectory +"/workload_qcs.txt";
		String dqcsQueries = "";
        String sparsityDeltaQueries = "";
        
	    try {
    		// Step : Opening the file from which queries are to be read
	    	BufferedReader bufRead = new BufferedReader(new FileReader(readFileName));
	        String qcs = bufRead.readLine();
	        
	        while (qcs != null) {					// for each qcs
	    		
	    		if(!qcsNameToNumberMap.containsKey(qcs)){
	    			// If we are seeing the qcs for the first time
	    			List<String> qcsList = Helpers.tokenize(qcs);
	    			
	    			// Query for calculating Sparsity delta of the query
	    			String sparsityQuery = "SELECT COUNT(*) FROM " +
	    					" ( SELECT COUNT(*) groups FROM " + queryTableName + " GROUP BY " + Mergers.JoinStrings(qcsList, ",") +  
	    					") temp WHERE temp.groups <= " + Constants.M  + ";";
	    			sparsityDeltaQueries = sparsityDeltaQueries + sparsityQuery + "\n";
		            
		            // Query for calculating dqcs of the query
		            dqcsQueries = dqcsQueries + "SELECT COUNT(*) FROM (SELECT 1 FROM " + queryTableName + " GROUP BY " + Mergers.JoinStrings(qcsList, ",") + ") temp ;\n" ;

	    			qcsNameToNumberMap.put(qcs, qcsNumber);
	    			// Increase the number of unique query types
	    			qcsNumber++;
	    		}
	    		
	    		// Step 7: Frequencies - needs to be done every time  
	    		updateQueryFrequency(qcs);

	            // Read the next qcs
	            qcs = bufRead.readLine();
	        }
	        
	        // Closing the read file
	        bufRead.close();
	    } catch(Exception ex) {
	    	System.out.println(ex.getMessage());
	    }
	    
	    // Step 8: Writing statistics to file
	    try {
	    	
    		// Step 8a: opening files in which data is to be written
    		BufferedWriter bufWriteSparsityDelta = new BufferedWriter(new FileWriter(new File(workingDirectory + "/workload_stats_sparsity.queries")));
    		BufferedWriter bufWriteDqcs = new BufferedWriter(new FileWriter(new File(workingDirectory + "/workload_stats_dqcs.queries")));
    		
    		bufWriteSparsityDelta.write(sparsityDeltaQueries);
    		bufWriteSparsityDelta.write("\nquit;");
    		bufWriteDqcs.write(dqcsQueries);
    		bufWriteDqcs.write("\nquit;");
    		
    		// Step 8c: Closing all open files
	        bufWriteSparsityDelta.close();
	        bufWriteDqcs.close();
	        
	    } catch(Exception ex) {
	    	System.out.println("ERROR : " + ex.getMessage());
	    	ex.printStackTrace();
	    }
	}

}