package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

public class Query {
	
	private Connection connection;	
	
	public Query(){
		
		//connection à la DB
    	this.connection = DBConnect.getInstance();     	    	
	}
	
	/**
	 * fonction qui exécute une requete de type SELECT à la BD
	 * 
	 * @param requete
	 * @return
	 */
	public ResultSet select(String requete){
		Statement stmt=null;
		ResultSet result = null;
		
		try {
			
			stmt = this.getConnection().createStatement();		
			result = stmt.executeQuery(requete);
		} 
		catch (SQLException e) {			
			e.printStackTrace();
		}
				
		return result;
	}
	

	/**
	 * fonction qui insère des tuples dans la BD
	 * renvoie le nombre de tuples inséré effectivement
	 * 
	 * @param requete
	 * @return
	 */
	public int insert(String value){
		PreparedStatement stmt=null;
		
		int nbMaj = 0;
		
		String requete = "INSERT INTO tweets_elague(tweet_text) VALUES(?)";
		
		try {
			
			this.getConnection().setAutoCommit(false);
			stmt = this.getConnection().prepareStatement(requete);
			stmt.setString(1,value);
			nbMaj = stmt.executeUpdate();		
			System.out.println("nb mise a jour = "+nbMaj);
			this.getConnection().commit();
		} 
		catch (SQLException e) {
			try {
				
				this.getConnection().rollback();
			}
			catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();		
		}
		
		return nbMaj;
	}
	
	
	public int update(String nomTable, String nomAttributAMettreAJour, Long id){
		PreparedStatement stmt=null;
		
		int nbMaj = 0;
		
		String requete = "UPDATE `"+nomTable+"` SET `"+nomAttributAMettreAJour+ "` = 'oui' WHERE id= ?";		
		
		try {
			
			this.getConnection().setAutoCommit(false);
			stmt = this.getConnection().prepareStatement(requete);
			stmt.setLong(1,id);
			nbMaj = stmt.executeUpdate();		
			System.out.println("nb mise a jour = "+nbMaj);
			this.getConnection().commit();
		} 
		catch (SQLException e) {
			try {
				
				this.getConnection().rollback();
			}
			catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();		
		}
		
		return nbMaj;
	}
	
	/**
	 * insère une ligne dans la table du modèle booléen
	 * 
	 * @param nomTable nom de la table
	 * @param numLigne indice de la ligne à insérer
	 * @return renvoie le nombre de ligne insérées
	 */
	public int insertRowDefault(String nomTable, int numLigne){
		PreparedStatement stmt=null;
		
		int nbMaj = 0;
		
		String requete = "INSERT INTO `"+nomTable+"`(id) VALUES(?)";
		
		try {
			
			this.getConnection().setAutoCommit(false);
			stmt = this.getConnection().prepareStatement(requete);
			stmt.setLong(1,numLigne);
			nbMaj = stmt.executeUpdate();		
			System.out.println("nb mise a jour = "+nbMaj);
			this.getConnection().commit();
		} 
		catch (SQLException e) {
			try {
				
				this.getConnection().rollback();
			}
			catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();		
		}
		
		return nbMaj;
	}
	
	/**
	 * fonction qui crée une table à partir d'une requete string
	 * @param requete
	 */
	public void createTable(String requete){
		
		Statement stmt = null;
		System.out.println("Creating table in given database...");
		
		try{
			this.getConnection().setAutoCommit(false);
			stmt = this.getConnection().createStatement();		    
		    stmt.executeUpdate(requete);
		    System.out.println("Created table in given database...");
		    this.getConnection().commit();
	    }
		catch(SQLException e){	
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
	      e.printStackTrace();
	    }		
	}
	
	/**
	 * fonction qui crée une table à partir d'une requete string
	 * @param requete
	 */
	public void createTable(String nomTable, Set<String> ensAttributs ){
		
		Statement stmt = null;
		System.out.println("Creating the creation table query...");		
		
		String requete =   "CREATE TABLE IF NOT EXISTS `"+ nomTable +
			    		   "` (`id` bigint(20) NOT NULL AUTO_INCREMENT ";
		
		//builder plus rapide
		StringBuilder sbRequete = new StringBuilder();
		sbRequete.append(requete);
		
		Iterator<String> it = ensAttributs.iterator();
		while(it.hasNext()){
			
			sbRequete.append(", `"+it.next().trim()+"` varchar(64) default 'non' \n");
		}
		sbRequete.append(", PRIMARY KEY (`id`)");
		sbRequete.append(" ) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;");	
		
		System.out.println(sbRequete.toString());
			    		  
		try{
			this.getConnection().setAutoCommit(false);
			stmt = this.getConnection().createStatement();
			System.out.println("Creating table in given database...");
		    stmt.executeUpdate(sbRequete.toString());		    
		    this.getConnection().commit();
		    System.out.println("Created table in given database...");
	    }
		catch(SQLException e){	
			try {
				this.getConnection().rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
	      e.printStackTrace();
	    }		
	}
	
	/*******  GETTERS AND SETTERS **************/
		
	public final Connection getConnection() {
		return connection;
	}

	public final void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/*public static void main(String[] args){
		
		Query q = new Query();
		
		Set<String> att = new HashSet<String>();
		att.add("dan");
		att.add("dali");
		att.add("dave");
		att.add("dan");
		q.createTable("mbouta",att);
	}*/
	
}
