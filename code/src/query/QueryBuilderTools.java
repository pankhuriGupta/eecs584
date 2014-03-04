package query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.relationalcloud.tsqlparser.loader.SchemaTable;
import com.relationalcloud.tsqlparser.schema.Column;
import com.relationalcloud.tsqlparser.schema.Table;

public class QueryBuilderTools {

	
	/**
	 * Choose upto "k" columns from the input table
	 * The columns are to be selected randomly (uniform random sampling)
	 * @param table
	 * @param upperLimit
	 * @return
	 */
	public List<Column> chooseUptoKColumns(SchemaTable table, int upperLimit) {
		int nColsTotal = upperLimit <= 0 ? table.getNumColumns() : upperLimit;
		Random randomGenerator = new Random();
		int nColsWhere = randomGenerator.nextInt(nColsTotal) + 1;
		List<String> cols = PickWithoutReplacement(table.getColumns(), nColsWhere);
		List<Column> columnsList = new ArrayList<Column>();
		for (String c : cols){
			columnsList.add(new Column(new Table(table),c));
		}
		return columnsList;
	}
	
	/**
	 * Pick "n" values from the input list of elements
	 * @param elements
	 * @param n
	 * @return
	 */
	private <T> List<T> PickWithoutReplacement(List<T> elements, int n) {
		List<T> pickedElements = new ArrayList<T>();
		List<T> tempElementsList = new ArrayList<T>();
		for(T temp: elements)
			tempElementsList.add(temp);
		Random rand = new Random();
		for(int i = 0 ; i < n  ; i++){
			int numberColumns = tempElementsList.size();
			// Pick a random index for selecting element
			int index = rand.nextInt(numberColumns);
			// Add the element to the list to be returned.
			pickedElements.add(tempElementsList.get(index));	
			// Remove the selected element from the input to avoid replacement
			tempElementsList.remove(index);			
		}
		return pickedElements;
	}
}
