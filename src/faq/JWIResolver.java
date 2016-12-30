package faq;

import java.util.List;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import java.io.IOException;
import java.net.URL;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import static faq.ResolveSimilarWords.STOP_WORDS;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * @author Pranav Prakash Jha
 */
/**
 * SAUMYA:: Do the JWI processing Here New Project-> Java Project with exsisting
 * Source->SourcePackage Folder->add JWI folder (extracted one) now come to this
 * project, Libraries->Right Click-> Add Projects -> add the above project
 *
 * Things to do here: 1. As I have done here for verbs and
 * nouns, think what others can or should be included or excluded, change
 * POS, also check //IWord word.getRelatedWords()
 * 2. Solve this Bug: 
 *  Bug: those with no verb are not displayed even though there are nouns of that word
 *      words not showing: it will display 'doing' but when 'do' is typed it sends empty, heroism, etc..
 * 3. Understand it completely and properly document this page and ResolveSimilarWords, and make the error tags and check for possible Exceptions and handle it                         
 *      
 */

public class JWIResolver {

    private List<List<List<String>>> unresolvedWordIDs;
    private List<List<List<String>>> unresolvedGloss;
    private IDictionary dict;

    public JWIResolver() throws MalformedURLException, IOException {
        //Dictionary initializing 
        String path = "C:\\Program Files (x86)\\WordNet\\2.1\\dict";
        URL url = new URL("file", null, path);
        // construct the dictionary object and open it
        dict = new Dictionary(url);
        dict.open();
      
    }
    JWIResolver(List<List<String>> faqWords) throws MalformedURLException, IOException {
        this();
        System.out.println("JWIResolver start");
        unresolvedGloss= new ArrayList<>(faqWords.size());
        unresolvedWordIDs= new ArrayList<>(faqWords.size());
      
        for (List<String> line : faqWords) {
            List<List<String>> lineGloss= new LinkedList<>();
            List<List<String>> lineWordId= new LinkedList<>();
        
            //System.out.println("faqWords: " + faqWords);
            for (String wrd : line) {
                System.out.println(wrd+": ");
                //Extracts the meanings and similar words of 'wrd' here
                List<String> tempGloss = new LinkedList<>();
                List<String> tempId = new LinkedList<>();

                WordnetStemmer stemmer = new WordnetStemmer(dict);
                List<String> inputWords= new ArrayList<>();
                    
                try{
                    inputWords = stemmer.findStems(wrd, null);
                }catch(IllegalArgumentException e){
                    System.err.println("0 size string: "+e);
                }
                for (String input : inputWords) {
       //             System.out.println("\t" + input);
                    boolean canAdjProcess = true;
                    boolean canAdverbProcess = true;
                    boolean canNounProcess = true;
                    boolean canVerbProcess = true;
                    
                    //For ADJECTIVe
                    IIndexWord idxAdj = dict.getIndexWord(input, POS.ADJECTIVE);
                    try {/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                        it gives nullPointerException*/
                        if (idxAdj.getWordIDs().isEmpty()) {
                            canAdjProcess = false;
                        }
                    } catch (NullPointerException e) {
                        canAdjProcess = false;
                    }
                    if (canAdjProcess == true) {
           //             System.out.println("\n\nNumber of Nouns " + idxNoun.getWordIDs().size());
                        for (IWordID ids : idxAdj.getWordIDs()) {
                            IWord word = dict.getWord(ids);
                            tempId.add(refineWordId(ids.toString()));
                            tempGloss.add(word.getLemma() + "(Adjective):<br>" + word.getSynset().getGloss());
                        }
                    }
                    System.out.println("1: "+tempGloss);
                    //For verbs
                    IIndexWord idxVerb = dict.getIndexWord(input, POS.VERB);
                    
                    try {/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                        it gives nullPointerException*/
                        if (idxVerb.getWordIDs().isEmpty()) 
                            canVerbProcess = false;
                    } catch (NullPointerException e) {
                        canVerbProcess = false;
                    }

                    if (canVerbProcess == true) {
         //               System.out.println("Number of Verbs: " + idxVerb.getWordIDs().size());
                        for (IWordID ids : idxVerb.getWordIDs()) {
                            IWord word = dict.getWord(ids);
                            //Adding the gloss and id in the list
                            tempId.add(refineWordId(ids.toString()));
                            tempGloss.add(word.getLemma() + "(verb):<br>" + word.getSynset().getGloss());
                        }
                    }
                    System.out.println("2: "+tempGloss);
                     
                    //For Noun
                    IIndexWord idxNoun = dict.getIndexWord(input, POS.NOUN);
                    try {/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                        it gives nullPointerException*/
                        if (idxNoun.getWordIDs().isEmpty()) {
                            canNounProcess = false;
                        }
                    } catch (NullPointerException e) {
                        canNounProcess = false;
                    }
                    if (canNounProcess == true) {
           //             System.out.println("\n\nNumber of Nouns " + idxNoun.getWordIDs().size());
                        for (IWordID ids : idxNoun.getWordIDs()) {
                            IWord word = dict.getWord(ids);
                            tempId.add(refineWordId(ids.toString()));
                            tempGloss.add(word.getLemma() + "(Noun):<br>" + word.getSynset().getGloss());
                        }
                    }
                    
                    
                    System.out.println("3: "+tempGloss);
                    
                     //For Adverb
                    IIndexWord idxAdverb = dict.getIndexWord(input, POS.ADVERB);
                    try {/*Doing this because for wordnets below wordnet 3.0, if any derived word is countered, instead of sending null
                        it gives nullPointerException*/
                        if (idxAdverb.getWordIDs().isEmpty()) {
                            canAdverbProcess = false;
                        }
                    } catch (NullPointerException e) {
                        canAdverbProcess = false;
                    }
                    if (canAdverbProcess == true) {
           //             System.out.println("\n\nNumber of Nouns " + idxNoun.getWordIDs().size());
                        for (IWordID ids : idxAdverb.getWordIDs()) {
                            IWord word = dict.getWord(ids);
                            tempId.add(refineWordId(ids.toString()));
                            tempGloss.add(word.getLemma() + "(Adverb):<br>" + word.getSynset().getGloss());
                        }
                    }
                
                    System.out.println("4: "+tempGloss);
                }//JWI processing for a word ends here
                lineGloss.add(tempGloss);
                lineWordId.add(tempId);
            }
            unresolvedGloss.add(lineGloss);
            unresolvedWordIDs.add(lineWordId);
        }
        System.out.println("JWIResolver end");
    }
    private String refineWordId(String strId){
        char[] chr= new char[strId.length()];
        strId.getChars(4, 12, chr, 0);
        String test= new String(chr, 0, 8);
        return test;
    }
    List<List<List<String>>> getWordIds() {
        return unresolvedWordIDs;
    }

    List<List<List<String>>> getGloss() {
        return unresolvedGloss;
    }
    
    private List<List<String>> wordToId( String query ) {
            
        
            List<List<String>> queryList = new ArrayList<>();
        
            StringTokenizer s;
            s = new StringTokenizer(query);
            List<String> tempList = new ArrayList<>();
            while (s.hasMoreTokens()) {
                String temp = s.nextToken().replaceAll("[^a-zA-Z]", "");//removes symbols
                System.out.println(temp);
                temp.trim();
                //Removing stop words
                if (!STOP_WORDS.contains(temp) && !temp.isEmpty()) {
                    tempList.add(temp);
                }
            }
            if(!tempList.isEmpty())
                queryList.add(tempList);
            //System.out.println("temp List"+ tempList);
        
        System.out.println("faqWords: " + queryList);
        
        return queryList;
        
    }

}


            