package pretraitement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

/**
 * Classe qui effectue les différents traitements
 * 
 * @author Daniel WONDEU
 *
 */
public class Traitement {
	
	/**
	 * Fonction qui effectue l'épuration du tweet en éliminant des chaines non désirées
	 * citées ex: RT ou les caractères [?$&^§£!%°]
	 * 
	 * @param sentence le tweet à épurer
	 * @return le tweet épurer ou " " si tout à été éliminer
	 */
	public static String removeDirtyBeforeTagging(String sentence){
		
		/* élimine l'apostrophe; RT: retweet et les mots commençant par l'un des caractères [?$&^§£!%°]*/
		Pattern pattern = Pattern.compile("[a-z]['’]|^RT$|[?$&^§£!%°]", Pattern.CASE_INSENSITIVE);
		
		/* remplace 75 et les différents arrondissements par "PARIS" */
		Pattern pattern2 = Pattern.compile("75|1er|[2-9][eéè]me|1[0-9][eéè]me|20[eèé]me");
		
		Matcher matcher, matcher2;
		StringBuilder sb = new StringBuilder(); //contain the result
		StringTokenizer st = new StringTokenizer(sentence);
		
		while(st.hasMoreTokens()){
			/* en présence de mot commentcant par @|# on les élimine directement*/			
			String mot = st.nextToken();
			
			if(mot.startsWith("http")|| mot.startsWith("www")||mot.startsWith("@")||mot.startsWith("#")){
				//on passe
			}
			else{
				matcher = pattern.matcher(mot);
				matcher2 = pattern2.matcher(matcher.replaceFirst(" "));
				sb.append(matcher2.replaceFirst("paris")).append(" ");
			}						 
		}		
		return 	sb.toString();	
	}
	
	/**
	 * élimine les mots de nature non souhaités, et les renvoie triés dans l'ordre lexicographique
	 * 
	 * @param sentence texte taggués (avec Stanford NLP) à analyser 
	 * @return texte lemmatisé et trié
	 */
	public static String removeDirtyAfterTagging(String sentence){
		//contains the result
		StringBuilder sb = new StringBuilder();
		
		//tokenizer
		StringTokenizer st = new StringTokenizer(sentence);		
		
		while(st.hasMoreTokens()){
			
			String[] words = st.nextToken().split("_");	
			
			if(words[1].matches("P|PUNC|DET|CC|CLS|PREF|I|CS|PRO|PROREL|CLO|CL|V")){
				//on l élague
			}
			else{
				//System.out.println("AFTERTAGGING: words"+words[0]);
				sb.append(words[0]).append(" ");	
			}										 
		}		
		return 	sb.toString();	
	}

	/**
	 * Enrichit le texte de l'étape courrante avec le fichier contenant les itemsets 
	 * fréquents de l'étape précédente, on injecte ces itemsets fréquents comme des tweets
	 * 
	 * @param cur_sb stringBuilder contenant le texte de la fenetre courante
	 * @param filenameOfPreviousItemSets fichier csv (itemSet+fréquence) contenant les itemsets de la phase précédente
	 * @return 
	 */
	public static void enrichItemSets(String filenameOfPreviousItemSets, String filenameTarget){
		
		StringBuilder sb = new StringBuilder();		
		BufferedReader br=null;				
		BufferedReader br1 =null;		
		BufferedWriter bw = null;
		
		try {
			 br1 = new BufferedReader(new FileReader(filenameTarget));
			 String ligne1;
			 
			 //System.out.println("test avant +++++++++++++++++++++");
			 while((ligne1 = br1.readLine()) != null){				
					sb.append(ligne1).append("\n");	
					//System.out.println("ligne1:::::"+ligne1);
			 }			 
			 //System.out.println("test apres +++++++++++++++++++++");
			 
			br1.close();
			
			String ligne;
			int freqItems; //freqItems: est la frequence de l'items- nombre de fois qu'il apparait
			br = new BufferedReader(new FileReader(filenameOfPreviousItemSets));
			//System.out.println("enrich ++++++");
			while((ligne = br.readLine()) != null){
				String words[] = ligne.split(":");
				
				//System.out.println("words: "+words[0]+"-"+words[1]);
				
				freqItems = Integer.parseInt(words[1]);
				for(int count=0; count<freqItems;count++){
					//System.out.println("ENRICH"+words[0]);
					sb.append(words[0]).append("\n");
				}				
			}
			//System.out.println("sb dans enrich "+sb.toString());
			br.close();
			
			//ecriture
			bw = new BufferedWriter(new FileWriter(filenameTarget));
			bw.append(sb.toString());			
			
		}
		catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
		catch (IOException e) {			
			e.printStackTrace();
		}
		finally{
			try {
				//br1.close();
				br.close();
				bw.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * élimine des mots qui ont une distribution inférieure à celle indiquée en paramètre
	 * 
	 * @param seuilMin distirbution cible en dessous de laquelle le mot est éliminé
	 * @param mapOfWords map contenant le mot comme clé et ayant pour valeur son nombre d'occurence 
	 * @return le map après élimination des mots peu représentés
	 */
	public static Map<String,Integer> removeAttribute(int seuilMin, Map<String, Integer> mapOfWords){
		
		Map<String,Integer> setOfWords = mapOfWords;
		//int nbWords = setOfWords.size();
		//System.out.println("size before distribution: "+nbWords);
		
		Iterator<Map.Entry<String,Integer>> it = setOfWords.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,Integer> entry = it.next();
			
			//System.out.printf("Key : %s and Value: %d \n", entry.getKey(), entry.getValue());
			if(entry.getValue() < seuilMin){
				
				//System.out.println(entry.getKey()+" removed!");
				it.remove();
			}			   
		}
		return setOfWords;
	}
	
	/**
	 * recrée un fichier du meme nom en éliminant tous les mots qui ne sont pas map<String,Integer>
	 * 
	 * @param filename fichier source et cible
	 * @param map conteneur de mots qui do
	 */
	public static void cleanFileFromUselessWords(String filename, Set<String> set){
		
		StringBuilder sb = new StringBuilder();
		BufferedReader fichierIn=null;
		BufferedWriter fichierOut =null;
		try {
			
			//System.out.println("size of set ds procedure: "+set.size());
			//System.out.println("filename: "+filename);
			String ligne;
			fichierIn = new BufferedReader(new FileReader(filename));
			//fichierIn.
			while((ligne = fichierIn.readLine()) != null){
				
				//System.out.println("je suis ds fichier ds procedure clean");
				StringTokenizer st = new StringTokenizer(ligne);
				
				while(st.hasMoreTokens()){
					String word = st.nextToken().trim();
					if(set.contains(word)){
						//System.out.println("yes! found!");
						sb.append(word).append(" ");
					}
				}
				//System.out.println("CLEAN sb"+sb.toString());
				sb.append("\n");
			}
			
			fichierIn.close();
			
			//création d'un fichier
			fichierOut = new BufferedWriter(new FileWriter(filename));
			//fichierOut.close();
			
			//fichierOut = new BufferedWriter(new FileWriter(filename));
			fichierOut.write(sb.toString());
			//System.out.println("valeur sb "+sb.toString());
			//fichierIn.close();
			fichierOut.close();			
		}
		catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
		catch (IOException e) {			
			e.printStackTrace();
		}
		finally{
			try {
				//fichierIn.close();
				fichierOut.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		//String res = Util.removeDirtyAfterTagging("George_NPP a_V dit_VPP que_CS les_DET sont_V des_DET idiots_NC école_N des_P hotel_NC ");
		//System.out.println(res);		
	}
}
