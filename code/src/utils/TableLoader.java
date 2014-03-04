package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import com.relationalcloud.tsqlparser.loader.SchemaTable;


/**
 * Reads  the description from a file and :
 * 1. creates the table to be used by the rest of the package
 * 2. Loads information about column types of the table
 * @author pankhuri
 *
 */
public class TableLoader {
	
	public Map<String,String> colDataTypeMap = new HashMap<String,String>();
	
	public SchemaTable loadTable(String filePath){
	//public SchemaTable loadTable(String filePath){
		SchemaTable table = new SchemaTable();
		try{
			BufferedReader bufRead = new BufferedReader(new FileReader(filePath));
			String row = bufRead.readLine();
			Vector<String> columns = new Vector<String>();
			Vector<String> columnTypes = new Vector<String>();
      
			while(row != null){
				// Tokenize the incoming string
				ArrayList<String> tokens = Helpers.tokenize(row);
				String columnName = tokens.get(0).trim();
				String columnType = tokens.get(1).trim();
				ColumnInfo.ColumnDataType.put(columnName, columnType);
				if(ColumnInfo.shouldGetRange(columnType)){
					String startRange = tokens.get(2).trim();
					String endRange = tokens.get(3).trim();
					ColumnInfo.ColumnStartDoubleRanges.put(columnName, Double.parseDouble(startRange));
					ColumnInfo.ColumnEndDoubleRanges.put(columnName, Double.parseDouble(endRange));
				}
				columns.add(columnName);
				columnTypes.add(columnType);
				colDataTypeMap.put(columnName, columnType);
            
				// Read next column from file
				row = bufRead.readLine();
			}
			table.setColumns(columns);
			table.setColTypes(columnTypes);
			table.setTableName("students");
        
			bufRead.close();
		}catch(Exception ex){
				System.out.println("Exception Caused : " + ex.getMessage());
				ex.printStackTrace();
		}
		return table;
	}
}
