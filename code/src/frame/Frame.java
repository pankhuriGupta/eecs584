package frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import query.Query;

import com.relationalcloud.tsqlparser.loader.SchemaTable;

public class Frame {

	private SchemaTable table ;
	public List<Query> queries;
	public List<String> sqlQueries;
	public List<String> qcs;
	public Map<String,Integer> queryTypeFraction;
	
	Frame(SchemaTable tab){
		queries = new ArrayList<Query>();
		sqlQueries = new ArrayList<String>();
		qcs = new ArrayList<String>();
		queryTypeFraction = new HashMap<String,Integer>();
		this.table = tab;
	}
	
	public String getAllSqlQueries(){
		StringBuilder allQueries= new StringBuilder();
		for(String que : sqlQueries){
			allQueries.append(que);
			allQueries.append("\n");
		}
		return allQueries.toString();
	}
	
	public String getAllQcs(){
		StringBuilder allQcs = new StringBuilder();
		for(String q : qcs){
			allQcs.append(q);
			allQcs.append("\n");
		}
		return allQcs.toString();
	}
	
	public String getAllFreq(){
		StringBuilder allFreq = new StringBuilder();
		for(Integer f : queryTypeFraction.values()){
			allFreq.append(f.toString());
			allFreq.append("\n");
		}
		return allFreq.toString();
	}

	public void AddQuery(Query que){
		queries.add(que);
	}
	
	public void AddSqlQuery(String que){
		sqlQueries.add(que);
	}
	
	public void AddQcs(String q){
		this.qcs.add(q);
	}
	
	public void AddFreq(String qcs, Integer freq){
		this.queryTypeFraction.put(qcs, freq);
	}
	
	public SchemaTable getTable(){
		return this.table;
	}
}
