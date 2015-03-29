package pretraitement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * class test pour la lémmatisation avec Stanford nlp
 * @author Daniel
 *
 */
public class StanfordLemmatizer {
	
	private StanfordCoreNLP pipeline;
	
	public StanfordLemmatizer(){
		
		//StanfordCoreNLP.set_model('pos.model', 'english-left3words-distsim.tagger');
	
		//MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/french/french-tagger");
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger");
	    
	    props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger");
	    //props.put("pos.model","F:\\workspace_eclipse\\j2ee\\Tweets\\taggers\\french.tagger");
	   // props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    
	    
	    this.pipeline = new StanfordCoreNLP(props);    
	    
	}
	
	@SuppressWarnings("unused")
	public List<String> lemmatize(String documentText){
		
		List<String> lemmas = new LinkedList<String>();
		// create an empty Annotation just with the given text
	    Annotation document = new Annotation(documentText);
	    
	    // run all Annotators on this text
	    this.pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	        // traversing the words in the current sentence
	        // a CoreLabel is a CoreMap with additional token-specific methods
	        for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	          // this is the text of the token
	          String word = token.get(TextAnnotation.class);
	          // this is the POS tag of the token
	          String pos = token.get(PartOfSpeechAnnotation.class);
	          // this is the NER label of the token
	          String ne = token.get(NamedEntityTagAnnotation.class); 	          
	          
	          // Retrieve and add the lemma for each word into the
              // list of lemmas
              lemmas.add(token.get(LemmaAnnotation.class));
	        }
	        
	        // this is the parse tree of the current sentence
	        Tree tree = sentence.get(TreeAnnotation.class);

	        // this is the Stanford dependency graph of the current sentence
	        SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	        
	        // This is the coreference link graph
	        // Each chain stores a set of mentions that link to each other,
	        // along with a method for getting the most representative mention
	        // Both sentence and token offsets start at 1!
	        Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);	        
	        
	    }
	    
	    return lemmas;
	}
	
	public static void main(String[] args) {
		
        System.out.println("Starting Stanford Lemmatizer");
        String text = "my birthday is taking place? \n"+
                "i'm sharing all with you \n"+
                "venez vous rejouir avec moi \n"+
                "merci de votre precieux temps";
        
        StanfordLemmatizer slem = new StanfordLemmatizer();
        System.out.println(slem.lemmatize(text));
    }
}
