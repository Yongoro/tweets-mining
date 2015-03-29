package pretraitement;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Classe qui effectue le taggage
 * 
 * @author Daniel WONDEU
 *
 */
public class Tagger {
	
	
	private MaxentTagger tagger;
	
	public Tagger(){
		
		//utilisation du tagger français
		tagger = new MaxentTagger("taggers/french.tagger");
	} 
	
	
	public String taggedSentence(String sentence){	
					 
		return this.tagger.tagString(sentence);
	}


	public final MaxentTagger getTagger() {
		return tagger;
	}


	public final void setTagger(MaxentTagger tagger) {
		this.tagger = tagger;
	}	
}
