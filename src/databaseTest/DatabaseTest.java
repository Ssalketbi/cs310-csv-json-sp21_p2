package databasetest;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {
    
    public JSONArray getJSONData(){
        
        
        JSONArray jsArr = new JSONArray();
            
        Connection conx = null;
        PreparedStatement sctStmt = null, updStmt = null;
        ResultSet rs = null;
        ResultSetMetaData metadata = null;
        
        String query;
         
        boolean stmtResult;
        int rezCount, colCount;
        
        try {
            
           
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "";
            System.out.println("Connecting to " + server + "...");
            
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conx = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conx.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                sctStmt = conx.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                stmtResult = sctStmt.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( stmtResult || sctStmt.getUpdateCount() != -1 ) {

                    if ( stmtResult ) {
                        
                        /* Get ResultSet Metadata */
                        
                        rs = sctStmt.getResultSet();
                        metadata = rs.getMetaData();
                        colCount = metadata.getColumnCount();
                        
                       
                        int currRez = 0;
                        
                        while(rs.next()) {
                            JSONObject jsObj = new JSONObject();
                            for (int i = 2; i <= colCount; i++){
                                jsObj.put(metadata.getColumnLabel(i), rs.getString(i));
                            }
                            jsArr.add(jsObj);
                        }
                        
                    }

                    else {

                        rezCount = sctStmt.getUpdateCount();  

                        if ( rezCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    stmtResult = sctStmt.getMoreResults();

                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            conx.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (rs != null) { try { rs.close(); rs = null; } catch (Exception e) {} }
            
            if (sctStmt != null) { try { sctStmt.close(); sctStmt = null; } catch (Exception e) {} }
            
            if (updStmt != null) { try { updStmt.close(); updStmt = null; } catch (Exception e) {} }
            
        }

        return jsArr;
    }
    
}