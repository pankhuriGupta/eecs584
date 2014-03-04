package workload;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import com.relationalcloud.tsqlparser.loader.SchemaTable;

import frame.Frame;
import frame.FrameGenerator;
import utils.Constants;
import utils.TableLoader;
import sampleCandidates.*;
import statisticsCollector.CollectorQueryStats;

/**
 * The class is responsible for generating 
 * a) Sample Candidates
 * b) Queries across different time windows
 * @author pankhuri
 *
 */
public class SimpleWorkloadGenerator {

	int distance;
	public void setDistanceMeasure(int dist){
		this.distance = dist;
	}
	
	public static void main(String[] args) {
		
		File directory;
		// Check if the number of arguments are legal. 
		if(args.length < 1 ){
			System.out.println("ILLEGAL NUMBER OF ARGUMENTS");
			System.exit(0);
		}
	
		// -------------------------------------------------------------------
		// --------------------------  Loading Table -------------------------
		// -------------------------------------------------------------------
		TableLoader tableLoaderObj = new TableLoader();
		SchemaTable table = tableLoaderObj.loadTable(Constants.basePath + "/table_description.txt");
		
		// -------------------------------------------------------------------
		// ----------------  Generating Sample Candidates --------------------
		// -------------------------------------------------------------------
		if(args[0].equals("-samples")){
			if(args.length < 2){
				System.out.println("FRAME NUMBER FOR CANDIDATE GENERATION NOT SPECIFIED");
				System.exit(0);
			}
			Integer frameNumber = Integer.parseInt(args[1]);
			
			// Get the samples (populate in files)
			String filePath = Constants.basePath + "candidateSet/frame_" + frameNumber.toString();
			directory = new File(filePath);
			directory.mkdir();
			(new SampleGenerator()).GenerateSamples(table, (frameNumber));
			System.out.println("Successfully generated Queries for candidate generation");
		}
		
		// -------------------------------------------------------------------
		// -----------------------  Generating Queries -----------------------
		// -------------------------------------------------------------------
		else if(args[0].equals("-queries")){
			// For each distance measure
			for(int dist = 0 ; dist < Constants.distanceFractions.size() ; dist++){
				try{
					double fraction = Constants.distanceFractions.get(dist); 
					// Create directory for the queries
					directory = new File(Constants.basePath,"querySet/querySet" + dist); 
					directory.mkdir();
					
					SimpleWorkloadGenerator generator = new SimpleWorkloadGenerator();
					generator.setDistanceMeasure(dist);
					
					// Traverse over the time line - 10 windows
					Frame currentFrame = null;
					FrameGenerator frameGen = new FrameGenerator();
					for(int timeFrameNumber = 0; timeFrameNumber < Constants.NumberOfTimeFrames ; timeFrameNumber++){						
						directory = new File(Constants.basePath,"querySet/querySet" + dist + "/frame_" + timeFrameNumber); 
						directory.mkdir();
						
						if(timeFrameNumber == 0)	
							// first time frame. Get a new window
							currentFrame = frameGen.GenerateNewTimeFrame(Constants.distributionType, table);
						else
							// slide the existing window with the distance to generate 
							// window for the next time frame
							currentFrame = frameGen.SlideTimeFrame(currentFrame,Constants.distributionType, fraction);
						
						// Extract all queries stored in the current time frame
						String queries = currentFrame.getAllSqlQueries();
						String qcs = currentFrame.getAllQcs();
						String freqs = currentFrame.getAllFreq();
						
						// Writing those queries to the file
						File writeFileQueries = new File(directory, "/workload.queries");
						File writeFileQcs = new File(directory, "/workload_qcs.txt");
						File writeFileFreqs = new File(directory, "/workload_frequencies.txt");
						try{
							BufferedWriter outputQueries = new BufferedWriter(new FileWriter(writeFileQueries));
							BufferedWriter outputQcs = new BufferedWriter(new FileWriter(writeFileQcs));
							BufferedWriter outputFreq = new BufferedWriter(new FileWriter(writeFileFreqs));

							outputQueries.write(queries);
							outputQueries.write("\nquit;");
							
							outputQcs.write(qcs);
							outputFreq.write(freqs);
							
							outputQueries.close();
							outputQcs.close();
							outputFreq.close();

						}catch(IOException ex){
							ex.printStackTrace();
						}
						
						// Generate queries for collecting statistics about queries and write them in files
						CollectorQueryStats queryStatsCollector = new CollectorQueryStats();
						queryStatsCollector.GenerateStatsCollectorSqlQueries(timeFrameNumber, dist, table.getTableName());
					}
					}catch(Exception ex){
						System.out.println("Exception caused : " + ex.getMessage());
						ex.printStackTrace();
					}
			}
		// ---------------------------------------------------------------
		System.out.println("Successfully generated Queries");
		}
	}
}
