package com.relationalcloud.tsqlparser.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;



public class SchemaLoader {

  /**
   * Given a connection to the information_schema and a schemaName, loads the metadata for that schema and create an internal "Schema" representation for it.
   * @param conn
   * @param schemaName
   * @return
   * @throws SQLException
   */
  public static Schema loadSchemaFromDB(Connection conn,String schemaName) throws SQLException
  {
	  String driver=conn.getMetaData().getDriverName();
	  if(driver.contains("PostgreSQL"))
	  {
		  Statement stmt = conn.createStatement();
		  Statement stmt2 = conn.createStatement();
		  Statement stmt3 = conn.createStatement();
		  String columnPreparedSql;
		  String schemaSql = "select * from information_schema.SCHEMATA where CATALOG_NAME='"+schemaName + "' AND SCHEMA_NAME='public'";
		  String tablesSql = "select TABLE_NAME from information_schema.TABLES where TABLE_CATALOG='"+schemaName+"' AND TABLE_SCHEMA='public'";
		  columnPreparedSql= "select COLUMN_NAME,DATA_TYPE from information_schema.COLUMNS where TABLE_CATALOG='"+schemaName+"' AND TABLE_SCHEMA='public' AND TABLE_NAME=?";
		  ResultSet schemaRes = stmt.executeQuery(schemaSql);
		  schemaRes.next();
		  String collationName=null;
		  Schema schema = new Schema(schemaRes.getString("CATALOG_NAME"),
				  schemaName,
				  schemaRes.getString("DEFAULT_CHARACTER_SET_NAME"),
				  collationName,
				  schemaRes.getString("SQL_PATH"),
				  driver);
		  ResultSet tableList = stmt2.executeQuery(tablesSql);;
		  PreparedStatement ps = conn.prepareStatement(columnPreparedSql);
		  String tableName = null;
		  while(tableList.next()){
			  tableName = tableList.getString("table_name");
			  ps.setString(1, tableName);
			  ResultSet columnList = ps.executeQuery();
			  SchemaTable tt = new SchemaTable(tableName);
			  String columnName = null;
			  String columnType = null;
			  while(columnList.next()){
				  columnName = columnList.getString("COLUMN_NAME");
				  columnType = columnList.getString("DATA_TYPE");       
				  tt.addColumn(columnName,columnType);        
			  }
			  schema.addTable(tt);


			  // ---- load primary keys ----
			  String primarykeyquery =" SELECT c.TABLE_NAME,COLUMN_NAME,c.CONSTRAINT_NAME "+
			  " FROM  information_schema.KEY_COLUMN_USAGE k, information_schema.TABLE_CONSTRAINTS c" +
			  " WHERE k.TABLE_NAME = c.TABLE_NAME" +
			  " AND k.TABLE_SCHEMA = c.TABLE_SCHEMA" +
			  " AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME" +
			  " AND CONSTRAINT_TYPE = 'PRIMARY KEY'" +     // Maybe changing this to an or of primary keys and unique keys would work
			  " AND k.TABLE_CATALOG = '" + schemaName +"' "+
			  " AND k.TABLE_SCHEMA = 'public' "+ 
			  " AND c.TABLE_CATALOG = '" + schemaName +"' "+
			  " AND c.TABLE_SCHEMA = 'public' "+ 
			  " AND c.TABLE_NAME = '" + tableName + "'" +
			  " ORDER BY COLUMN_NAME;"; 


			  //System.out.println(primarykeyquery);
			  ResultSet constraintslist = stmt3.executeQuery(primarykeyquery);       

			  Vector<String> colinkey = new Vector<String>();
			  String consname = null;
			  while(constraintslist.next()){
				  consname = constraintslist.getString("CONSTRAINT_NAME");
				  colinkey.add(constraintslist.getString("COLUMN_NAME"));
			  }
			  if(consname!= null && colinkey.size()>0)
				  try {
					  tt.addConstraint(new PrimaryKey(consname,(Vector<String>)colinkey.clone()));
				  } catch (IntegrityConstraintsExistsException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }

		  }
		  return schema;
	  }
	  else	if(driver.contains("MySQL"))
	  {
		  Statement stmt = conn.createStatement();
		  Statement stmt2 = conn.createStatement();
		  Statement stmt3 = conn.createStatement();
		  String columnPreparedSql;
		  String schemaSql = "select * from information_schema.SCHEMATA where SCHEMA_NAME='"+schemaName + "'";
		  String tablesSql = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA='"+schemaName+"'";

		  columnPreparedSql= "select COLUMN_NAME,COLUMN_TYPE from information_schema.COLUMNS where TABLE_SCHEMA='"+schemaName+"' AND TABLE_NAME=?";

		  ResultSet schemaRes = stmt.executeQuery(schemaSql);
		  schemaRes.next();
		  Schema schema = new Schema(schemaRes.getString("CATALOG_NAME"),
				  schemaName,
				  schemaRes.getString("DEFAULT_CHARACTER_SET_NAME"),
				  "DEFAULT_COLLATION_NAME",
				  schemaRes.getString("SQL_PATH"),
				  driver);
		  ResultSet tableList = stmt2.executeQuery(tablesSql);;
		  PreparedStatement ps = conn.prepareStatement(columnPreparedSql);
		  String tableName = null;
		  while(tableList.next()){
			  tableName = tableList.getString("table_name");
			  ps.setString(1, tableName);
			  ResultSet columnList = ps.executeQuery();
			  SchemaTable tt = new SchemaTable(tableName);
			  String columnName = null;
			  String columnType = null;
			  while(columnList.next()){
				  columnName = columnList.getString("COLUMN_NAME");
				  columnType = columnList.getString("COLUMN_TYPE");       
				  tt.addColumn(columnName,columnType);        
			  }
			  schema.addTable(tt);       
			  // ---- load primary keys ----
			  String primarykeyquery =" SELECT c.TABLE_NAME,COLUMN_NAME,c.CONSTRAINT_NAME "+
			  " FROM information_schema.KEY_COLUMN_USAGE k, information_schema.TABLE_CONSTRAINTS c" +
			  " WHERE k.TABLE_NAME = c.TABLE_NAME" +
			  " AND k.TABLE_SCHEMA = c.TABLE_SCHEMA" +
			  " AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME" +
			  " AND CONSTRAINT_TYPE = 'PRIMARY KEY'" +     // Maybe changing this to an or of primary keys and unique keys would work
			  " AND k.TABLE_SCHEMA = '" + schemaName +"' "+ 
			  " AND c.TABLE_SCHEMA = '" + schemaName +"' "+ 
			  " AND c.TABLE_NAME = '" + tableName + "'" +
			  " ORDER BY COLUMN_NAME;"; 
			  //System.out.println(primarykeyquery);
			  ResultSet constraintslist = stmt3.executeQuery(primarykeyquery);       

			  Vector<String> colinkey = new Vector<String>();
			  String consname = null;
			  while(constraintslist.next())
			  {
				  consname = constraintslist.getString("CONSTRAINT_NAME");
				  colinkey.add(constraintslist.getString("COLUMN_NAME"));
			  }
			  if(consname!= null && colinkey.size()>0)
			  {
				  try 
				  {
					  tt.addConstraint(new PrimaryKey(consname,(Vector<String>)colinkey.clone()));
				  } 
				  catch (IntegrityConstraintsExistsException e) 
				  {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }   
			  }
		  }
		  return schema;
	  }
	  throw(DriverNotSupportedException("[ Driver not supported ]"));
  }

private static SQLException DriverNotSupportedException(String string) throws SQLException {
	// TODO Auto-generated method stub
	throw new SQLException();
}
}   
    
    
    
    
    
