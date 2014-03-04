package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.relationalcloud.tsqlparser.schema.Column;

public class Mergers {
	public static List<Column> MergeColumns(List<Column> first, List<Column> second){
		Set<Column> mergedSet = new HashSet<Column>();
		mergedSet.addAll(first);
		mergedSet.addAll(second);
		return new ArrayList<Column>(mergedSet);
	}
	
	public static String JoinStrings(List<String> list, String conjunction){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : list){
			if (first)
				first = false;
		    else
		    	sb.append(conjunction);
		      	sb.append(item);
		}
		return sb.toString();
	}
	
	public static List<String> DeDuplicate(List<Column> cols){
		List<String> deduped = new ArrayList<String>();
		for(Column c: cols){
			if(!deduped.contains(c.getColumnName()))
				deduped.add(c.getColumnName());
		}
		return deduped;
	}
}
