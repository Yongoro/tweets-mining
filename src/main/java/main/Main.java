package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import pretraitement.LemmatizerLine;
import pretraitement.Tagger;
import pretraitement.Traitement;
import database.Query;
import fpGrowth.AlgoFPGrowth_Strings;

public class Main {
	
	/* variables globales contenant la fenetre de données à extraire de la BD*/
	
	public static int start = 0;
	public static int offset;
	
	public static Map<String,Integer> mapOfWords;
	
	/**
	 *  notifications
	 */
    private static JTextArea information;    
	
	public  Main(int offset,int start,JTextArea information) {		
		
		Main.information=information;		
		
		if(start < 0)
			start=0;		
		
		run(start, offset);		
	}
	
	public static void run(int start,int offset){
		
		//pour la distribution
		mapOfWords = new HashMap<String,Integer>();
		
		double minSupp = 0.01; //support minimum pour l'algorithme FPGrowth
		int minSeuil = 4 ; //pour la distribution des attributs à éliminer
		
		String input ="./output_"+start+".csv";
		String output ="./output_"+start+".arff";
		AlgoFPGrowth_Strings fp = new AlgoFPGrowth_Strings();
		
		//tagger   	
    	Tagger tagger = new Tagger();    	
    	
    	//Lemmatizer
    	LemmatizerLine lemmatizer = new LemmatizerLine("french");
    	
    	//objet filtre    	
    	//Filtre filtre = new Filtre("french"); 
    	
    	//fichiers de sortie      	
    	final String outputCSV = "output_"+start+".csv";   	
    	
    	BufferedWriter fichierCSV = null;     	
    	//BufferedReader fichierCSVOpen = null;
    	
    	//objet pour requete
    	Query query = new Query();
  	
    	//requetes
    	String reqSelectTweets = "select tweet_text from tweets where getIsoLanguageCode = 'fr'"
    							+ " order by created_at limit "
    							+ start +", " +offset+" ;";
    	
    	//conteneur de reponse de la BD
    	ResultSet resultats = null; 
    	
    	//conteneur de la chaine concatenée renvoyée par le filtre
    	//il doit donc à chaque fois ajouter un "\n" pour aller à la ligne
    	
    	StringBuilder sb = new StringBuilder(); //plus rapide dans les boucles et occupe moins d'espace
    	
    	try {			
    		
    		information.append("\nOpening file to write data from DB...\n");
			System.out.println("\nOpening file to write data from DB...\n");			
			
			fichierCSV = new BufferedWriter(new FileWriter(outputCSV));				
			
			resultats = query.select(reqSelectTweets);
			
			boolean again = resultats.next();
			
			String tweet="", tweet_tagged="", tweet_final="";
			information.append("\n analyzing... \n");
			System.out.println("analyzing...");
			
			while(again){//pour chaque tweet								
				
				//1, correspond à l'attribut tweet_text obtenu de la table tweets		
				tweet = resultats.getString(1);
				
				//elimination texte inultiles et taggage			
				tweet_tagged = tagger.taggedSentence(Traitement.removeDirtyBeforeTagging(tweet));	
				
				//System.out.println(tweet_tagged);				
				
				//elimination texte inutile apres taggage
				tweet_final = lemmatizer.lemmatise(Traitement.removeDirtyAfterTagging(tweet_tagged));
				
				//				
				sb.append(tweet_final).append("\n");				
				
				//on passe au prochain tweet
				again= resultats.next();
			}//fin du filtrage
			
			//System.out.println("apres tag: "+sb.toString());
			
			information.append("\n Creation du fichier csv... \n");
			System.out.println("Creation du fichier csv...");
			
			information.append(" size before distribution elimination: "+mapOfWords.size()+ "\n");
			System.out.println("size before distribution elimination: "+mapOfWords.size());
			
			//elimination des attibuts peu fréquents du map
			mapOfWords = Traitement.removeAttribute(minSeuil, mapOfWords);
			
			information.append(" size after distribution elimination: "+mapOfWords.size()+ "\n");
			System.out.println("size after distribution elimination: "+mapOfWords.size());
			
			//System.out.println("sb apres tag: "+sb.toString());
			fichierCSV.write(sb.toString()); //on ferme pour permettre à la procedure d'acceder a son tour au fichier			
			
			/*******/
			fichierCSV.close();
			BufferedReader buf = new BufferedReader(new FileReader(outputCSV));
			
			@SuppressWarnings("unused")
			String tmp;
			while((tmp = buf.readLine())!= null){
				//System.out.println("MAIN:"+tmp);
			}
			
			buf.close();
			//on recrée le fichier pour éliminer les attributs inutiles
			Traitement.cleanFileFromUselessWords(outputCSV, mapOfWords.keySet());
			
			//on ouvre de nouveau
			//fichierCSV = new BufferedWriter(new FileWriter(outputCSV));	
			fichierCSV.close();
			
			/** enrichissement avec les itemsets frequents precedents **/
			if(start == 0){ //un moyen de s'assurer qu'il existe bien des itemsets de la phase precedente				
				//fichierCSV.write(sb.toString());	
			}
			else{				
				Traitement.enrichItemSets("output_"+(start-offset)+".arff",outputCSV);
			}			
					
			//fichierCSV.close();				
		}		
		catch(Exception e)  {
			e.printStackTrace();
		}
		finally{
			try {				
				fichierCSV.close();				
			}
			catch (IOException e2) {				
				e2.printStackTrace();
			}			
		}
    	
    	/******    Execution de FPGrowth   ******************/    	
    	
		try {
			fp.runAlgorithm(input, output, minSupp);
			fp.printStats();
			
			//information.append("\n"+fp.toString());			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/***************** GETTERS et SETTERS  *****************/
	
	public static int getOffset() {
		return offset;
	}

	public static void setOffset(int offset) {
		Main.offset = offset;
	}

}
