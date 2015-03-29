package pretraitement;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.danishStemmer;
import org.tartarus.snowball.ext.dutchStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.finnishStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.hungarianStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import org.tartarus.snowball.ext.norwegianStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;
import org.tartarus.snowball.ext.romanianStemmer;
import org.tartarus.snowball.ext.russianStemmer;
import org.tartarus.snowball.ext.spanishStemmer;
import org.tartarus.snowball.ext.swedishStemmer;
import org.tartarus.snowball.ext.turkishStemmer;

public class Constants {

	public static SnowballStemmer newStemmer(String language) throws RuntimeException {
		  if (language.equals("english"))   return new englishStemmer();
		  if (language.equals("french"))   return new frenchStemmer();
		  if (language.equals("german"))   return new germanStemmer();
		  if (language.equals("spanish"))   return new spanishStemmer();
		  if (language.equals("portuguese"))   return new portugueseStemmer();
		  if (language.equals("russian"))   return new russianStemmer();
		  if (language.equals("danish"))   return new danishStemmer();
		  if (language.equals("romanian"))   return new romanianStemmer();
		  if (language.equals("hungarian"))   return new hungarianStemmer();
		  if (language.equals("turkish"))   return new turkishStemmer();
		  if (language.equals("finnish"))   return new finnishStemmer();
		  if (language.equals("dutch"))   return new dutchStemmer();
		  if (language.equals("italian"))   return new italianStemmer();
		  if (language.equals("norwegian"))   return new norwegianStemmer();
		  if (language.equals("swedish"))   return new swedishStemmer();
		  throw new RuntimeException("No stemmer for language (" + language + ")");
		}
}
