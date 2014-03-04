import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;
public class ModelGenerator {

    /**
     * Class Members that hold the input data
     */

    //public:

    String basePath = "/home/ec2-user/eecs584/GLPK_Files/input/";
    /*
     * This function reads the input data from files
     */
    Vector<Double> queryFrequencies = new Vector<Double>();
    Vector<Integer> sparsityDelta = new Vector<Integer>();
    Vector<Integer> stratifiedSampleSize = new Vector<Integer>();
    Vector<Integer> totalStorageCapacity = new Vector<Integer>();
    Vector<Integer> dqcsi = new Vector<Integer>();
    Vector<Integer> dqcsj = new Vector<Integer>();
    Vector<String> qcs = new Vector<String>();
    Vector<String> queries = new Vector<String>();
    Vector<String> samples = new Vector<String>();
    
    void loadData() throws FileNotFoundException{
        System.out.println("Loading the data");
        try {
            System.out.println("Loading query frequencies");
            queryFrequencies = readDoubleData(basePath+"probQueries");
            System.out.println("Loading sparsity delta");
            sparsityDelta = readIntData(basePath+"sparsityDelta");
            System.out.println("Loading stratified sample size");
            stratifiedSampleSize = readIntData(basePath+"stratifiedSampleSize");
            System.out.println("Loading total storage capacity");
            totalStorageCapacity = readIntData(basePath+"totalStorageCapacity");
            System.out.println("Loading dqcsi");
            dqcsi = readIntData(basePath+"dqcs_candidates");
            System.out.println("Loading dqcsj");
            dqcsj = readIntData(basePath+"dqcs_queries");
            System.out.println("Loading candidates");
            qcs = readStringData(basePath+"candidates");
            System.out.println("Loading queries");
            queries = readStringData(basePath+"queries");
        }   
        catch (Exception e){ 
        }   
    }   
    Vector<Integer> readIntData(String fileName) throws FileNotFoundException{
        //System.out.println("Read Int Data");
        Vector<Integer> data = new Vector<Integer>();
        int i = 0;
        try{
	        BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));
            String line;
            line = br.readLine();
            while (line != null){
                data.add(i++, Integer.parseInt(line));
                line = br.readLine();
            }   
            br.close();
        }   
        catch (Exception e){ 
            System.out.println(e);
        }   
        return data;
    }   

    Vector<Double> readDoubleData(String fileName) throws FileNotFoundException{
        //System.out.println("Read Int Data");
        Vector<Double> data = new Vector<Double>();
        int i = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));
            String line;
            line = br.readLine();
            while (line != null){
                data.add(i++, Double.parseDouble(line));
                line = br.readLine();
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        return data;
    }

    Vector<String> readStringData(String fileName) throws FileNotFoundException{
        //ystem.out.println("Read String Data");
        BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));
        Vector<String> data = new Vector<String>();
        String line;
        int i = 0;
        try {
        	line = br.readLine();
            while (line != null){
                data.add(i++, line);
                line = br.readLine();
            }   
            br.close();
        }   
        catch (Exception e){ 
            System.out.println(e);
        }   
        return data;
    }   
    /** 
     * This function generates the GLPK model (blinkDB.mod)
     */
    void generateModel(){
        System.out.println("Generating the model");
        
        try {
		    PrintWriter writer = new PrintWriter("blinkdb.mod", "UTF-8");
		    writer.println("param numQcs integer, >=0;");
		    writer.println("param numQueries integer, >= 0;");
		    writer.println("set Queries := {1..numQueries};");
		    writer.println("set Qcs := {1..numQcs}; ");
		    writer.println("param sparsityDelta{q in Queries} >=0;");
		    writer.println("param probQ{q in Queries} >=0, <=1;");
		    writer.println("param storageCapacity >=0;");
		    writer.println("param stratifiedSampleSize{i in Qcs} >=0;");
		    writer.println("param DqcsI{i in Qcs} >= 0;");
		    writer.println("param DqcsJ{j in Queries} >=0;");
		    writer.println("param domainQueries{j in Queries, i in Qcs}, binary,  >=0;");
		    writer.println("param fraction{j in Queries,i in Qcs} := min(1,(DqcsI[i] / DqcsJ[j])) * domainQueries[j,i];");
		    //writer.println("param maxedItem{j in Queries};");
		    //writer.println("param maxFraction{j in Queries} := max{i in Qcs}fraction[j,i];");
		    writer.println("var z{i in Qcs}, binary;");
		    writer.println("var y{j in Queries} >=0, <=1;");
		    //writer.println("var allCov{j in Queries,i in Qcs} >=0, <=1;");
		    //writer.println("var d{j in Queries, i in Qcs}, binary;");
		    writer.println("maximize obj: sum{q in Queries} probQ[q]*y[q]*sparsityDelta[q];");
		    writer.println("subject to storage: 0 <= sum{i in Qcs} stratifiedSampleSize[i]*z[i] <= storageCapacity;");
		    //writer.println("subject to allCoverage{j in Queries,i in Qcs}: allCov[j,i] = fraction[j,i]*z[i];");
		    //writer.println("subject to mipFormulation{j in Queries} : sum{i in Qcs}d[j,i] = 1;");
		    for (int counter = 1; counter < queries.size() + 1; counter++){
		    	writer.println("subject to coverage"+counter+": y["+counter+"] <= sum{i in Qcs}fraction["+counter+",i]*z[i];");
        	}
		    //writer.println("display {j in Queries, i in Qcs}fraction[j,i]");
		    //writer.println("display z;");
		    //writer.println("display y;");
		    //writer.println("display obj;");
		    //writer.println("display{j in Queries, i in Qcs} allCov[j,i");
            writer.println("solve;");
            writer.println("display z;");
		    writer.println("end;");
		    writer.close();
        }
        catch (Exception e){
        	System.out.println(e);
        }
    }   

    /** 
     * This function generates the GLPK data file (blinkDB.dat)
     */
    void generateData(){
        System.out.println("Generating the data file for the model");
        try {
		    PrintWriter writer = new PrintWriter("blinkdb.dat", "UTF-8");
		    writer.println("param numQcs := "+qcs.size()+";");    
		    writer.println("param numQueries := "+queries.size()+";");
		    writer.println("param sparsityDelta :=");
		    for (int counter = 1; counter < queries.size() + 1; counter++)
		    {
		    	writer.println(counter+ " " +sparsityDelta.get(counter-1));
		    }
		    writer.println(";");
		    writer.println("param probQ :=");
		    for (int counter = 1; counter < queries.size() + 1; counter++)
		    {
		    	writer.println(counter+ " " +queryFrequencies.get(counter-1));
		    }
		    writer.println(";");
		    writer.println("param storageCapacity :=" +totalStorageCapacity.get(0)+";");
		    writer.println("param stratifiedSampleSize :=");
		    for (int counter = 1; counter < qcs.size() + 1; counter++)
		    {
		    	writer.println(counter+ " " +stratifiedSampleSize.get(counter-1));
		    }
		    writer.println(";");
		    //TODO: replace the 1s with real values
		    writer.println("param DqcsI :=");
		    for (int counter = 1; counter < qcs.size() + 1; counter++)
		    {
		    	//writer.println(counter+ " " +"1");
		    	writer.println(counter+ " " +dqcsi.get(counter-1));
		    }
		    writer.println(";");
		    //TODO: replace the 1s with real values
		    writer.println("param DqcsJ :=");
		    for (int counter = 1; counter < queries.size() + 1; counter++)
		    {
		    	//writer.println(counter+ " " +"1");
		    	writer.println(counter+ " "+dqcsj.get(counter-1));
		    }
		    writer.println(";");
		    
		    writer.println("param domainQueries default 0 :=");
		    writer.print(":");
		    for (int i = 1; i < qcs.size() + 1; i++){
		    	writer.print(" " + i);
		    }
		    writer.println(":=");    	
		    //writer.println("param domainQueries default 0 :=");
		    for (int i = 0; i < queries.size(); i++){
		    	String[] candidate;
		    	Boolean isOverlap = false;
		    	writer.print(i+1);
		    	for (int j = 0; j < qcs.size(); j++){
			    	candidate = qcs.elementAt(j).split(" ");
                    
		    		for (int k = 0; k < candidate.length; k++){
		    			if (queries.elementAt(i).indexOf(candidate[k]) > -1){
		    		        isOverlap = true;
		    			}	
		    			else{
		    				isOverlap = false;
		    			}
		    		}
		    		if (isOverlap){
		    			writer.print(" 1");
		    		}
		    		else {
		    			writer.print(" 0");
		    		}
		    	}
		    	writer.println();
		    }
    		writer.println(";");		    
		    writer.println("end;");
		    writer.close();
        }
        catch (Exception e){
        	System.out.println(e);
        }
    
    }   

    /** 
     * @param args
     */
    public static void main(String[] args){
        System.out.println("Hi");
        ModelGenerator modelGenerator = new ModelGenerator();
        
        try{
            modelGenerator.loadData();
        }   
        catch (FileNotFoundException e){ 
        }   
        modelGenerator.generateModel();
        modelGenerator.generateData();
        //loadData();
        //generateModel();
        //generateData();
    }   

}
