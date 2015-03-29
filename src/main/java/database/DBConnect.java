package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JTextArea;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.DatabaseMetaData;

/**
 * The synchronized Singleton class that manages the connection instance
 * 
 * @author Daniel
 *
 */

  
public class DBConnect {
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	
	/**
	 * Database Connection URL
	 */
	private  static String DB_URL ;

	//  Database credentials
	
	/**
	 * User name
	 */
			
	private static  String USER;
	
	

	/**
	 * Connexion test
	 */
	private static boolean testeconnexion=false;
	
	/**
	 *  notifications
	 */
    private static JTextArea information;
    
    
	/**
	 * user password
	 */
	private static  String PASS ;
	

	/**
	 * connection object
	 */
	private static Connection connect;
	
	/**
	 * private constructor
	 */
	public DBConnect(String DB_URL,String USER,String PASS,JTextArea mytext)
	{
		
		information=mytext;
		setUSER(USER);
		setDB_URL(DB_URL);
		setPASS(PASS);
		
		effectuerSync();
		
	}
	
	/**
	 * manages the synchronization
	 */
	private synchronized static void effectuerSync(){
		
		if(connect == null){
			try {
				Class.forName(JDBC_DRIVER);
				System.out.println("Connecting to database...");
				connect = (Connection) DriverManager.getConnection(getDB_URL(),getUSER(),getPASS());
				System.out.println("Connected database successfully...");
				
				//obtenir des infos sur la Base
				DatabaseMetaData metaData = (DatabaseMetaData) connect.getMetaData();
				System.out.println(metaData.getDatabaseProductName());
				System.out.println(metaData.getDatabaseProductVersion());
				testeconnexion=true;
				
			} 
			catch( ClassNotFoundException e) { 
				information.append("\n failed to load driver : " + e+"\n ");
				System.err.println("failed to load driver : " + e);				
			}
			catch(SQLException sqle) {
				information.append("\n SQL Error: " + sqle+"\n");
				System.err.print("SQL Error: " + sqle);				
			}
			catch(Exception e){
				information.append("\n"+e.getMessage()+"\n");
				System.err.println(e.getMessage());				
			}
		}		
	}
	
	/**
	 * Returns the connection instance or create it if it doesn't exist
	 * @return
	 */
	public static Connection getInstance(){
		
		if(connect == null){
			effectuerSync();
		}		
		return connect;	
	}
	public static String getDB_URL() {
		return DB_URL;
	}
	
	public static void setDB_URL(String dB_URL) {
		DB_URL = dB_URL;
	}

	public static String getUSER() {
		return USER;
	}

	public static void setUSER(String uSER) {
		USER = uSER;
	}

	public static String getPASS() {
		return PASS;
	}

	public static void setPASS(String pASS) {
		PASS = pASS;
	}
	
	public boolean connexionsucced()
	{
		return testeconnexion;
	}
	
}
