package sampleCandidates;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import query.Query;
import query.QueryBuilderTools;
import utils.Constants;
import utils.Mergers;
import utils.Helpers;
import utils.ColumnInfo;

import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;

public class SampleQueryGenerator {
	
	private void prune(List<Column> groupby, List<Column> where){
		for (Column c: where){
			if (groupby.contains(c)){
				groupby.remove(c);
			}
		}
	}
	
	/**
	 * 
	 * @param table
	 * @return
	 */
	public Query GenerateRandomSampleQuery(SchemaTable table){
		QueryBuilderTools queryBuilder = new QueryBuilderTools();
		List<SchemaTable> fromCols = new ArrayList<SchemaTable>();
		fromCols.add(table);
		// Pick randomly size of the qcs to be generated
		// Size can be 1 or 2 or 3
		Random rand = new Random();
		int sizeWhereClause = rand.nextInt(3)+1;
		int sizeQcs = (rand.nextInt(20)%3) + 1;		// this is done simply to attain more randomness
		assert (sizeQcs >= 1 && sizeQcs <= 3);
		assert (sizeWhereClause >= 1 && sizeWhereClause <= 3);
		sizeQcs = Math.max(sizeQcs, sizeWhereClause);
		
		List<Column> whereCols = queryBuilder.chooseUptoKColumns(table, sizeWhereClause);
		List<Column> groupByCols = new ArrayList<Column>();
		Integer diff = Math.abs(sizeQcs-sizeWhereClause);  
		if( diff > 0){
			groupByCols = queryBuilder.chooseUptoKColumns(table, diff);
			prune(groupByCols, whereCols);
		}
		
		List<Column> emptyCols = Collections.emptyList();
		// The select clause for the sample does not matter as it is not a part of the qcs
		// Where and group by clauses form the Qcs. that is the information we want to store
		return new Query(emptyCols, fromCols, Mergers.MergeColumns(whereCols,groupByCols),emptyCols, emptyCols);
	}
	
	private List<String> getGroupByPredicate(String qcs){
		List<String> GroupByPredicate = new ArrayList<String>();
		List<String> columns = Helpers.tokenize(qcs);
		for(String c : columns){
			if(!GroupByPredicate.contains(c))
				GroupByPredicate.add(c);
		}
		return GroupByPredicate;
	}
	
	private List<String> getWherePredicate(String qcs){
		List<String> WherePredicate = new ArrayList<String>();
		// Add WHERE Predicate only if integer or double type field
		List<String> columns = Helpers.tokenize(qcs);
		for(String col: columns){
			if(ColumnInfo.shouldGetRange(ColumnInfo.ColumnDataType.get(col))){
				Random rand = new Random();
				Double rangeStart = ColumnInfo.ColumnStartDoubleRanges.get(col);
				Double rangeEnd = ColumnInfo.ColumnEndDoubleRanges.get(col);
				Double value = rangeStart + (rangeEnd - rangeStart) * rand.nextDouble();
				if( ColumnInfo.ColumnDataType.get(col).equalsIgnoreCase("integer") || ColumnInfo.ColumnDataType.get(col).equalsIgnoreCase("int")  ){ 
					WherePredicate.add(col + " <= " + ((Long)Math.round(value)).toString() );
				}else{
					DecimalFormat twoDForm = new DecimalFormat("#.##");
					value = Double.valueOf(twoDForm.format(value));
					WherePredicate.add(col + " <= " + value.toString() );
				}
			}
		}
		return WherePredicate;
	}
	
	public String GenerateSampleSqlCreateQueries(List<String> qcsList, String tableName){
		List<String> queryList = new ArrayList<String>();
		for(String qcs : qcsList){
			List<String> tokenizedQcs = Helpers.tokenize(qcs);
			String joinedQcs = Mergers.JoinStrings(tokenizedQcs, ",");
			// The query will be of the type:
			// CREATE TABLE sample AS 
			// SELECT table.* FROM table JOIN
			// 		(SELECT gpa, count(1) / T ratio FROM table WHERE <> GROUP BY gpa)
			//	temp ON ( table.gpa = temp.gpa) WHERE rand() < ratio;
			String sampleTableName = tableName + "_sample_" + (SampleGenerator.tableQcsSampleNumberMap.get(tableName)).get(qcs);
			String query = "CREATE TABLE " + sampleTableName + " AS ";
			// Generating from underlyng table
			query = query + "SELECT " + tableName + ".* FROM " + tableName + " JOIN (" ;
			// adding join query
			query = query + " SELECT " + joinedQcs + ", count(1)/" + Constants.C + " ratio FROM " + tableName; 
			// adding where and group by clause to the join (innermost) query
			List<String> where = getWherePredicate(qcs);
			if(!where.isEmpty())
				query = query + " WHERE " + Mergers.JoinStrings(where, " AND ");
			query = query + " GROUP BY "+ joinedQcs + ") temp ";
			query = query + "ON (" + tableName + "." +tokenizedQcs.get(0) + " = temp." +  tokenizedQcs.get(0) + ") " ;
			query = query + " WHERE rand() < ratio;";
			
			//System.out.println(query);
			queryList.add(query);
		}
		return Mergers.JoinStrings(queryList, "\n");
	}
	
	public String GenerateSampleSqlDropQueries(List<String> qcsList, String tableName){
		List<String> queryList = new ArrayList<String>();
		for(String qcs : qcsList){
			String sampleTableName = tableName + "_sample_" + (SampleGenerator.tableQcsSampleNumberMap.get(tableName)).get(qcs);
			String query = "DROP TABLE " + sampleTableName + ";";
			queryList.add(query);
		}
		return Mergers.JoinStrings(queryList, "\n");
	}

	public String GenerateSampleSqlDqcsQueries(List<String> qcsList, String tableName){
		List<String> queryList = new ArrayList<String>();
		for(String qcs : qcsList){
			ArrayList<String> cols = Helpers.tokenize(qcs);
			
			String sampleTableName = tableName + "_sample_" + (SampleGenerator.tableQcsSampleNumberMap.get(tableName)).get(qcs);
			String query = "SELECT COUNT(*) FROM (SELECT 1 FROM " + sampleTableName + " GROUP BY " + Mergers.JoinStrings(cols, ",") + ") temp ;";
			queryList.add(query);
		}
		return Mergers.JoinStrings(queryList, "\n");
	}
	
	public String GenerateSampleSqlStorageCountQueries(List<String> qcsList, String tableName){
		List<String> queryList = new ArrayList<String>();
		for(String qcs : qcsList){
			String sampleTableName = tableName + "_sample_" + (SampleGenerator.tableQcsSampleNumberMap.get(tableName)).get(qcs);
			String query = "SELECT COUNT(*) FROM " + sampleTableName + ";";
			queryList.add(query);
		}
		return Mergers.JoinStrings(queryList, "\n");
	}
	
}
