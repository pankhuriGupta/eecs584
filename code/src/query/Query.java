package query;

import java.util.List;

import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;

public class Query {
	
	List<Column> SelectClause;
	List<SchemaTable> FromClause;
	List<Column> WhereClause;
	List<Column> GroupByClause;
	List<Column> OrderByClause;

	public
	
	// Constructor
	Query(List<Column> select, List<SchemaTable> from, List<Column> where, List<Column> groupBy, List<Column> orderBy){
		SelectClause = select;
		FromClause = from;
		WhereClause = where;
		GroupByClause = groupBy;
		OrderByClause = orderBy;
	}
	
	// Getters
	public List<Column> getSelectClause(){
		return this.SelectClause;
	}
	
	public List<SchemaTable> getFromClause(){
		return this.FromClause;
	}
	
	public List<Column> getWhereClause(){
		return this.WhereClause;
	}
	
	public List<Column> getGroupByClause(){
		return this.GroupByClause;
	}

	public List<Column> getOrderByClause(){
		return this.OrderByClause;
	}
}
