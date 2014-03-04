package utils;

import java.util.HashMap;
import java.util.Map;

public class ColumnInfo {
	
	public static Map<String,Double> ColumnStartDoubleRanges = new HashMap<String,Double>();
	public static Map<String,Double> ColumnEndDoubleRanges = new HashMap<String,Double>();
	public static Map<String,String> ColumnDataType = new HashMap<String,String>();
	
	public static boolean shouldGetRange(String colType){
		if (colType.equalsIgnoreCase("integer") || colType.equalsIgnoreCase("int") || colType.equalsIgnoreCase("double") || colType.equalsIgnoreCase("float"))
			return true;
		return false;
	}
}
