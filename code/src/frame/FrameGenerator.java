package frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;

import query.QueryGenerator;
import utils.Constants;
import utils.Mergers;
import distributions.GenerateDistributions;
import query.Query;


public class FrameGenerator {

	public Frame GenerateNewTimeFrame(String distributionType, SchemaTable table){
		Frame newFrame = new Frame(table);
		
		// Get the frequency distribution
		List<Integer> frequencies;
		if(distributionType.equalsIgnoreCase("PowerLaw"))
			frequencies = GenerateDistributions.createPowerLawFrequencies(Constants.NumOfTypesOfQueriesPerWindow, Constants.NumOfQueriesPerWindow);
		else
			frequencies = GenerateDistributions.createNormalDistributionFrequencies(Constants.NumOfTypesOfQueriesPerWindow, Constants.NumOfQueriesPerWindow);
		
		assert(Constants.NumOfTypesOfQueriesPerWindow == frequencies.size());
		QueryGenerator queryGen = new QueryGenerator();
		for(int queryTypeNum = 0 ; queryTypeNum < frequencies.size() ; queryTypeNum++){
			// Get a new type of query and add it to the time frame
			Query newTypeOfQuery = queryGen.GenerateRandomQueryOfNewType(table, newFrame, frequencies.get(queryTypeNum));
			newFrame.AddQuery(newTypeOfQuery);
			
			// Populate the frame with one or more SQL instances of the same query type
			// The number of SQL instances = frequency of that query type in the frame. 
			for(int times = 0 ; times < frequencies.get(queryTypeNum); times++){
				String sqlQuery = queryGen.GenerateSqlInstanceSameQueryType(newTypeOfQuery);
				newFrame.AddSqlQuery(sqlQuery);
			}			
		}
		return newFrame;
	}
	
	/**
	 * Take a fraction and kill that many types of queries from the time frame
	 * Generate new type of query for them.
	 * Keep the rest queries same but change their sql instances. 
	 * @param oldFrame
	 * @param distributionType
	 * @return
	 * @throws Exception
	 */
	public Frame SlideTimeFrame(Frame oldFrame, String distributionType, double fraction) throws Exception{
		
		if (oldFrame == null)
			throw new Exception("We cannot forecast the next window from a null window!");
		SchemaTable table = oldFrame.getTable();
		Frame newFrame = new Frame(table);
		
		// Populating Ids of all query types
		List<Integer> allQueryIds = new ArrayList<Integer>();
		for(int i = 0 ; i < oldFrame.queries.size() ; i++)
			allQueryIds.add(i);
		
		// Number of query types that are to be kept and that are to be newly generated
		int numberOfQueryTypesChanged = (int) Math.round(fraction * allQueryIds.size());
		int numberOfQueryTypesRetained = allQueryIds.size() - numberOfQueryTypesChanged;

		// Picking at random which queries should be retained
		List<Integer> retainedQueryTypeIds = pickQueryTypesRandomlyWithoutReplacement(numberOfQueryTypesRetained, allQueryIds.size());
		
		// Populating the Ids of the queries that should be re-generated 
		List<Integer> changedQueryTypeIds = new ArrayList<Integer>();
		changedQueryTypeIds.addAll(allQueryIds);		// adding all query Ids first
		changedQueryTypeIds.removeAll(retainedQueryTypeIds);  // removing the query Ids that are to be retained.
			
		List<Integer> newFrequencies = new ArrayList<Integer>();
		
		// Calculating new frequencies for each query type
		if (distributionType.equals("PowerLaw"))
			newFrequencies = GenerateDistributions.createPowerLawFrequencies(oldFrame.queries.size(), oldFrame.sqlQueries.size());
		else
			newFrequencies = GenerateDistributions.createNormalDistributionFrequencies(oldFrame.queries.size(), oldFrame.sqlQueries.size());
		
		// Updating the query types that are to be retained
		for(Integer queryId : retainedQueryTypeIds){
			Query queryType = oldFrame.queries.get(queryId);	// get the query type

			// Check if the new frequency is even 1 for the same query type
			if(newFrequencies.get(queryId) >= 1){
				newFrame.AddQuery(queryType);
				
				// Regenerate the SQL instances of the query type
				for(int i = 0 ; i < newFrequencies.get(queryId) ; i++){
					QueryGenerator querygen = new QueryGenerator();
					String query = querygen.GenerateSqlInstanceSameQueryType(queryType);
					newFrame.AddSqlQuery(query);
				}
				
				// Get the Qcs and the frequency
				List<Column> columns = Mergers.MergeColumns(queryType.getWhereClause(),queryType.getGroupByClause());
				List<String> qcs = Mergers.DeDuplicate(columns);
				String thisQcs = Mergers.JoinStrings(qcs, " ");
				newFrame.AddQcs(thisQcs);
				newFrame.AddFreq(thisQcs, newFrequencies.get(queryId));
			}
		}
		
		// Generating new query types
		for(Integer queryId : changedQueryTypeIds){
			QueryGenerator querygen = new QueryGenerator();
			// get the query type
			Query queryType = querygen.GenerateRandomQueryOfNewType(table, newFrame, newFrequencies.get(queryId));	
			// Check if the new frequency is even 1 for the same query type
			if(newFrequencies.get(queryId) >= 1){
				newFrame.AddQuery(queryType);
				// Regenerate the SQL instances of the query type
				for(int i = 0 ; i < newFrequencies.get(queryId) ; i++){
					String query = querygen.GenerateSqlInstanceSameQueryType(queryType);
					newFrame.AddSqlQuery(query);
				}
			}
		}
		return newFrame;
	}
	
	/**
	 * From all the queries, randomly pick which queries should be retained
	 * @param numberRetained
	 * @param total
	 * @return
	 */
	 List<Integer> pickQueryTypesRandomlyWithoutReplacement(int numberRetained, int total){
		Random rand = new Random();
		List<Integer> ids = new ArrayList<Integer>();
		for(int i = 0 ; i <  numberRetained ; i++){
			int index = 0;
			do{
				index = rand.nextInt(total);
			}while(ids.contains(index));
			ids.add(index);
		}
		assert (ids.size() == numberRetained);
		return ids;
	}
}

