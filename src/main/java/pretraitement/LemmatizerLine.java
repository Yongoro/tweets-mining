package pretraitement;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import main.Main;

import org.tartarus.snowball.SnowballStemmer;

/**
 * classe qui lémmatise 
 * 
 * @author Daniel WONDEU
 *
 */
public class LemmatizerLine {
	
	private SnowballStemmer stemmer;
	
	
	/**
	 * constructor 
	 * @param langue du texte qu'il faut lemmatiser
	 */
	public LemmatizerLine(String langue){
		
		this.stemmer=Constants.newStemmer(langue);
	}
	
	//===========================  GETTERS SETTERS =======================//
	
	public SnowballStemmer getStemmer() {
		return stemmer;
	}

	public void setStemmer(SnowballStemmer stemmer) {
		this.stemmer = stemmer;
	}
	
	//====================         FONCTIONS      =======================//	
	
	/**
	 * fonction qui teste si ma chaine en entrée contien des entiers
	 * @param chaine
	 * @return
	 */
	public static boolean isNumber(String chaine){
		boolean valeur = true; 
		char[] tab = chaine.toCharArray(); 

		for(char carac : tab){ 
			if(!Character.isDigit(carac) && valeur){
				valeur = false; 
			} 
		} 

		return valeur;
	}
	
	/**
	 * lemmatise la phrase en entrée et les renvoie des lemmes triés dans l'ordre lexicographique et séparés par " "
	 * 
	 * @param sentence
	 * @return retourne la chaine lemmatisée et triée
	 */
	public String lemmatise(String sentence){	
		
		 	Set<String> tries = new TreeSet<String>();
		    StringBuilder sb=new StringBuilder();
		    StringTokenizer tok=new StringTokenizer(sentence);
		    String word, tmp;
		    
		    while (tok.hasMoreTokens()) {
		      word=tok.nextToken();
		      if (!isNumber(word)){
		        this.stemmer.setCurrent(word);
		        this.stemmer.stem();
		        word= this.stemmer.getCurrent();
		      }
		      //sb.append(word).append(" ");
		      tmp=word.trim().toLowerCase();
		      
		      tries.add(tmp);
		      
		      
			  //ajoutons à l'ensemble des mots pour le calcul des distributions
			  //si le mot y est dejà on incrémente la valeur, sinon on l'y ajoute
			  //avec une valuer initiale 1
		      
		      try{
		    	  	if(Main.mapOfWords.containsKey(tmp)){
				    	 Main.mapOfWords.put(tmp, Main.mapOfWords.get(tmp) +1);
				    }
				    else{
				    	 Main.mapOfWords.put(tmp,1);				    	 
				    }
		      }
		      catch(UnsupportedOperationException e){
		    	  e.printStackTrace();
		      }
		      catch(NullPointerException e){
		    	  e.printStackTrace();
		      }
		      catch(IllegalArgumentException e){
		    	  e.printStackTrace();
		      }
		      catch(ClassCastException e){
		    	  e.printStackTrace();
		      }//end try-catch
		      
		    }//end while
		    
		    for(String s: tries){
		    	sb.append(s.trim()).append(" ");
		    }
		    //System.out.println(sb.toString());
		    return sb.toString();		  
	}
	
}
