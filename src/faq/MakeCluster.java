/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faq;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author Pranav Prakash Jha
 */
public class MakeCluster {
    String soln_id;
    List<Cluster> clstrList;
    final float SIMILARTIY_THRESHOLD = (float) 0.6;      // threshold for accepting results
    final List<List<String>> wordIDs;                     // id of all the final words selected from WordNet
    String[] solutionIdList;                                  // array containing timestamp(id of faq saved  in db) of all faqs

    private class Sentences {
        String id;
        List<String> sentence;

        public Sentences(String id, List<String> sentence) {
            this.id = id;
            for(String str: sentence){
                if(!sentence.contains(str))
                    this.sentence.add(str);
            }
        }

    }
    List<Sentences> sentenceList;
    /**
     *
     * @param soln_id the value of soln_id
     * @param wordIDs list<list<int>> to MakeCluster.java
     *                    |_________|-This part represents the int array of the ids of the words of one line
     *               |______________|-This part means the array of the lists of word IDs for a solution 
     */
    MakeCluster(String soln_id, List<List<String>> wordIDs, String[] all_faq_id) {
        System.out.println("MakeCluster start");
        this.clstrList = new LinkedList<>();
        this.sentenceList = new ArrayList<>();
        this.soln_id = soln_id;
        this.wordIDs = wordIDs;
        this.solutionIdList = all_faq_id;
    }
    public void condenseCluster(){
        //First storing all the lines as form of clusters
        System.out.println("\n\nCLuster:\n");
        for (int i = 0; i < wordIDs.size(); i++) {
            //wordIDs.get(i)- list of words of one line
            //all_faq_id[i]- solution_id
            clstrList.add(new Cluster(soln_id, wordIDs.get(i), solutionIdList[i]));
            clstrList.get(i).display();
            sentenceList.add(new Sentences(solutionIdList[i], wordIDs.get(i)));           
        }
        //Now Condensing the size of the clstrList
        {
            int size, i, ii = 0, jj = 0;
            Cluster temp;
            double sim, max_similarity = 1;
            boolean flag = true;
            while (max_similarity >= SIMILARTIY_THRESHOLD) {
                i = 0;
                flag = false;
                max_similarity = 0;
                do {
                    size = clstrList.size();
                    //System.out.println("size " + size);
                    for (int j = (size - 1); j > i; j--) {
                        sim = similarity(clstrList.get(i), clstrList.get(j));
                        if (sim >= max_similarity) {
                            ii = i;
                            jj = j;
                            max_similarity = sim;
                            flag = true;
                        }
                    }
                    i++;
                } while (i < size);
                if (flag == true && max_similarity >= SIMILARTIY_THRESHOLD) {
                    temp = addCluster(clstrList.get(ii), clstrList.get(jj));
                    clstrList.remove(jj);
                    clstrList.remove(ii);
                    clstrList.add(temp);
                } else {
                    break;
                }

            }
            for (i = 0; i < clstrList.size(); i++) {
                if (clstrList.get(i) == null) {
                    clstrList.remove(i);
                }
            }
            System.out.println("\n\nFinal Cluster List: \nSize: "+ clstrList.size()+"\n" );
            for (Cluster cl : clstrList) {
                cl.display();
            }
        }//end of clstrList condensing

        //For saving the cluster into database
//        for (Cluster cl : clstrList) {
//            try {
//                /**
//                 * ***************************
//                 * ABHIGYAN************************************** * Make the
//                 * cluster table according to this statement, and change
//                 * accordingly
//                 * **************************************************************************
//                 * *
//                 */
//                String sql = "insert into clusters values(" + soln_id + ", '" + cl.c_id + "', '" + cl.ub + "', '" + cl.lb + "')";
//                boolean flag = UpdateDatabase.update(sql);
//                if (!flag) {
//                    System.err.println("Error in cluster: " + cl.c_id);
//                } else {
//                    System.out.println("Added cluster ub: " + cl.ub + " lb: " + cl.lb);
//                }
//            } catch (Exception e) {
//                System.err.println("Error in: " + cl.c_id + " Error>> " + e);
//            }
//        }//end of saving the cluster into database
//        */
//        return clstrList;
}
    class Elements{
        String element;
        Double membershipValue;
        Elements(String element, double val){
            this.element=element;
            this.membershipValue=val;
        }
    }
    // v0.4 PRANAV------------- NEW CLUSTERING-------------------------------------------------------------//
    private double similarity(Cluster cl1, Cluster cl2){
        if(cl1.wordList.isEmpty() || cl2.wordList.isEmpty())
            return 0.0f;
        List<Elements> elementsList1=new ArrayList<>();
        List<Elements> elementsList2=new ArrayList<>();
        for(int i=0; i<cl1.wordList.size(); i++)
            elementsList1.add(new Elements(cl1.wordList.get(i), cl1.membershipValue.get(i)));
        for(int i=0; i<cl2.wordList.size(); i++)
            elementsList2.add(new Elements(cl2.wordList.get(i), cl2.membershipValue.get(i)));
        for(Elements el: elementsList1)
            System.out.println(el.element+": "+el.membershipValue);
        for(Elements el: elementsList2)
            System.out.println(el.element+": "+el.membershipValue);
        int count=0;
        double sum=0.0;
        for(Elements el1: elementsList1){
            boolean elementFound=false;
            for(Elements el2: elementsList2){
                if(el1.element.equalsIgnoreCase(el2.element)){
                    double diff;
                    if(el1.membershipValue>el2.membershipValue)
                        diff= el1.membershipValue-el2.membershipValue;
                    else
                        diff= el2.membershipValue-el1.membershipValue;
                    sum+=diff;
                    count++;
                    elementFound=true;
                    break;
                }
            }
            if(elementFound==false){//If element of elementList1 is not in elementList2
                count++;
                sum+=el1.membershipValue;
            }
        }
        for(Elements el2: elementsList2){
            boolean elementFound=false;
            for(Elements el1: elementsList1){
                if(el2.element.equalsIgnoreCase(el1.element)){
                   elementFound=true;
                   break;
                }        
            }
            if(elementFound==false){
                sum+=el2.membershipValue;
                count++;
            }
        }
        System.out.println("Comparing "+cl1.wordList+ " and "+ cl2.wordList+ ":  "+(1-(sum/count))+"  sum: "+sum+" count: "+ count);
        
        return 1-(sum/count);
    }
//END- change
    private Cluster addCluster(Cluster cl1, Cluster cl2){
        Cluster sumCluster= new Cluster();
        List<String> allWords= new ArrayList<>();
        for(List<String> lst: cl1.lines){
            sumCluster.lines.add(lst);
            if(!lst.isEmpty())
                allWords.addAll(lst);
        }
        for(List<String> lst: cl2.lines){
            sumCluster.lines.add(lst);
            if(!lst.isEmpty())
                allWords.addAll(lst);
        }
        
        for(String str: cl1.query_id)
            sumCluster.query_id.add(str);
        for(String str: cl2.query_id)
            sumCluster.query_id.add(str);   
    
        sumCluster.soln_id=cl1.soln_id;
        
        sumCluster.numOfLines=cl1.numOfLines+cl2.numOfLines;
        
        for(int i=0; i<cl1.wordList.size(); i++){
            String word1= cl1.wordList.get(i);
            boolean elementFound= false;
            for(int j=0; j<cl2.wordList.size(); j++){
                String word2= cl2.wordList.get(j);
                if(word1.equals(word2)){
                    elementFound=true;
                    double vl1=( (cl1.membershipValue.get(i) * cl1.numOfLines) + ( cl2.membershipValue.get(j)*cl2.numOfLines) );
                    vl1=vl1/(sumCluster.numOfLines);
                    sumCluster.wordList.add(word2);
                    sumCluster.membershipValue.add(vl1);
                    break;
                }           
            }
            if(elementFound==false){
                sumCluster.wordList.add(word1);
                sumCluster.membershipValue.add((cl1.membershipValue.get(i))/sumCluster.numOfLines);
            }        
        }
        for(int j=0; j<cl2.wordList.size(); j++){
            boolean elementFound=false;
            String word2= cl2.wordList.get(j);
            for(int i=0; i<cl1.wordList.size(); i++){
                String word1= cl1.wordList.get(i);
                if(word1.equals(word2)){
                    elementFound=true;
                    break;
                }
            }
            if(elementFound==false){
                sumCluster.wordList.add(word2);
                sumCluster.membershipValue.add((cl2.membershipValue.get(j))/sumCluster.numOfLines);
            }        
        }
        /*HashMap<String, Integer> counters = new HashMap<>();
        for (String word : allWords) {
            if (counters.containsKey(word)) {
                counters.put(word, counters.get(word) + 1);
            }else {
                counters.put(word, 1);
            }
        }
        int sizeOfCounter= counters.size();
        for (Entry<String, Integer> entry : counters.entrySet()) {
            String key = entry.getKey();
            sumCluster.wordList.add(key);
            Double val= (double)entry.getValue()/sizeOfCounter;
            sumCluster.membershipValue.add(val);
        }
        
        */
        System.out.println("Adding \n"+cl1.wordList+"\n"+cl2.wordList+"\nReturning: \n"+sumCluster.wordList+" :-> ");
        sumCluster.display();
        
       return sumCluster;
    }
    
    
    
    
    
    
}//end of MakeCluster class



