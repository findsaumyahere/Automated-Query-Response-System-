
import java.io.IOException;
import java.net.URL;
import edu.mit.jwi.*;
import edu.mit.jwi.item.*;
import java.util.Iterator;
import java.util.List;
import edu.mit.jwi.morph.WordnetStemmer;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pranav Prakash Jha
 */
public class JavaProject1 {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        // construct the URL to the Wordnet dictionary directory

        String path = "C:\\Program Files (x86)\\WordNet\\2.1\\dict";
        //String path = wnhome + File . separator + " dict ";
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        IDictionary dict = new Dictionary(url);
        dict.open();

        String wrd = "does";
        WordnetStemmer stemmer= new WordnetStemmer(dict);
        
        List<String> inputWords= stemmer.findStems(wrd, null);
        
        for(String input: inputWords){
            System.out.println("\t"+input);

            boolean canProcess=true;
            IIndexWord idxVerb = dict.getIndexWord(input, POS.VERB);
            try{/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                it gives nullPointerException*/
                if(idxVerb.getWordIDs().isEmpty() )
                    canProcess=false ;
            }catch(NullPointerException e){
                canProcess=false;
            }
            if (canProcess==true) {
                System.out.println("Number of Verbs: " + idxVerb.getWordIDs().size());
               for (IWordID ids : idxVerb.getWordIDs()) {
                    IWord word = dict.getWord(ids);
                    //word.getRelatedWords()
                    System.out.println("\nId = " + ids);
                    System.out.println(" Lemma = " + word.getLemma());
                    System.out.println(" Gloss = " + word.getSynset().getGloss());

                }
            }

            IIndexWord idxNoun = dict.getIndexWord(input, POS.NOUN);

            try{/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                it gives nullPointerException*/
                if(idxNoun.getWordIDs().isEmpty())
                    canProcess=false;
            }catch(NullPointerException e){
                canProcess=false;
            }
            if (canProcess==true) {
                System.out.println("\n\nNumber of Nouns " + idxNoun.getWordIDs().size());
                for (IWordID ids : idxNoun.getWordIDs()) {
                    IWord word = dict.getWord(ids);
                    //word.getRelatedWords()
                    System.out.println("\nId = " + ids);
                    System.out.println(" Lemma = " + word.getLemma());
                    System.out.println(" Gloss = " + word.getSynset().getGloss());

                }
            }
        }
    }
}

/**
ISynset synset = word.getSynset();
                
// get the hypernyms
List< ISynsetID> hypernyms= synset.getRelatedSynsets(Pointer.HYPERNYM);

// print out each h y p e r n y m s id and synonyms
List<IWord> words;
for (ISynsetID sid : hypernyms) {
    words = dict.getSynset(sid).getWords();
    System.out.print(sid + " {");
    for (Iterator<IWord> i = words.iterator(); i.hasNext();) {
        System.out.print(i.next().getLemma());
        if (i.hasNext()) {
            System.out.print(", ");
        }
    }
    System.out.println("}");
}
 * 
 */