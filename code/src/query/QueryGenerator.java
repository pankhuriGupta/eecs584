package query;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import utils.ColumnInfo;
import utils.Mergers;

import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;

import frame.Frame;

public class QueryGenerator {

	/**
	 * pick the number of predicates to go in the where clause
	 * sample table uniformly at random
	 * @return
	 */
	public Query GenerateRandomQueryOfNewType(SchemaTable table, Frame frame, int freq){
		
		QueryBuilderTools queryBuilder = new QueryBuilderTools();
		List<Column> selectCols = queryBuilder.chooseUptoKColumns(table, 1);
		List<SchemaTable> fromCols = new ArrayList<SchemaTable>();
		fromCols.add(table);	
		
		List<Column> whereCols = new ArrayList<Column>();
		List<Column> groupByCols = new ArrayList<Column>();
		
		String thisQcs = "";
		do{
			whereCols = queryBuilder.chooseUptoKColumns(table, 1);
			groupByCols = queryBuilder.chooseUptoKColumns(table, 2);
			
			// Get the qcs and add it to the frame
			List<Column> columns = Mergers.MergeColumns(whereCols,groupByCols);
			List<String> qcs = Mergers.DeDuplicate(columns);
			thisQcs = Mergers.JoinStrings(qcs, " ");
		}while(frame.qcs.contains(thisQcs));
		
		frame.AddQcs(thisQcs);
		frame.AddFreq(thisQcs,freq);
		
		List<Column> emptyCols = Collections.emptyList();
		return new Query(Mergers.MergeColumns(selectCols,groupByCols), fromCols, whereCols, groupByCols, emptyCols);
	}

	public String GenerateSqlInstanceSameQueryType(Query query){
		
		// Add SELECT projection
		List<String> SelectProjection = new ArrayList<String>();
		Set<Column> groupByKeys = new HashSet<Column>(query.getGroupByClause());
		for (Column c : query.getSelectClause()) {
			if (groupByKeys.contains(c))
				// If the column is in GroupBy clause, project it as it is
				SelectProjection.add(c.getColumnName());
			else
				// Project the minimum of that column (which is not in group by)
				// NOTE: This can also be max.. Not average (as the columns can be names)
				SelectProjection.add("min(" + c.getColumnName() + ")");
		}
		
		// Add the FROM predicate
		List<String> FromPredicate = new ArrayList<String>();
		for (SchemaTable t : query.getFromClause()) {
			FromPredicate.add(t.getTableName());
		}

		List<String> WherePredicate = new ArrayList<String>();
		try{
			// Add WHERE Predicate only if integer or double type field
			for(Column c: query.getWhereClause()){
				Random rand = new Random();
				Double rangeStart = ColumnInfo.ColumnStartDoubleRanges.get(c.getColumnName());
				Double rangeEnd = ColumnInfo.ColumnEndDoubleRanges.get(c.getColumnName());
				Double value = rangeStart + (rangeEnd - rangeStart) * rand.nextDouble();
			
				if( ColumnInfo.ColumnDataType.get(c.getColumnName()).equalsIgnoreCase("Integer")){ 
					WherePredicate.add(c.getColumnName() + " <= " + ((Long)Math.round(value)).toString() );
				}else{
					DecimalFormat twoDForm = new DecimalFormat("#.##");
					value = Double.valueOf(twoDForm.format(value));
					WherePredicate.add(c.getColumnName() + " <= " + value.toString() );
				}
			}
		}catch(Exception ex){
			// else don't add anything to the where predicate
			// Don't do anything
		}
		
		
		// Add GROUP BY predicate
		List<String> GroupByPredicate = new ArrayList<String>();
		for (Column c : query.getGroupByClause()) {
			GroupByPredicate.add(c.getColumnName());
		}
	
		// Add ORDER BY predicate		
		List<String> OrderByPredicate = new ArrayList<String>();
		for (Column c : query.getOrderByClause()) {
			OrderByPredicate.add(c.getColumnName());
		}
		
		// Now building the entire query
		StringBuilder sqlQuery = new StringBuilder();
		
		// SELECT
		sqlQuery.append(" SELECT ");
		if (SelectProjection.isEmpty())
			sqlQuery.append(" 1 ");
		else
			sqlQuery.append(Mergers.JoinStrings(SelectProjection, ", "));
		
		// FROM
		sqlQuery.append(" FROM ");
		sqlQuery.append(Mergers.JoinStrings(FromPredicate, ", "));
		
		// WHERE
		if (!WherePredicate.isEmpty()) {
			sqlQuery.append(" WHERE ");
			sqlQuery.append(Mergers.JoinStrings(WherePredicate, " AND "));
		}

		// GROUP BY
		if (!GroupByPredicate.isEmpty()) {
			sqlQuery.append(" GROUP BY ");	
			sqlQuery.append(Mergers.JoinStrings(GroupByPredicate, ", "));
		}

		// ORDER BY
		if (!OrderByPredicate.isEmpty()) {
			sqlQuery.append(" ORDER BY ");	
			sqlQuery.append(Mergers.JoinStrings(OrderByPredicate, ", "));
		}
		
		// LIMIT
		sqlQuery.append(" LIMIT 10;");		
		return sqlQuery.toString();
	}
}
