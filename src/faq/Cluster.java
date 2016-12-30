/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faq;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pranav Prakash Jha
 */
public class Cluster {
    List<String> wordList;           //list to store all query_id of clusters
    List<List<String>> lines;        //List of lines that made that cluster
    List<String> query_id;              //list of ids of query
    List<Double> membershipValue;       //list to store membership value of each element in list element 
    int numOfLines;
   // String c_id, ub;
    String soln_id;

    Cluster() {
        this.query_id = new ArrayList<>(0);
        this.wordList = new ArrayList<>(0);
        this.membershipValue = new ArrayList<>();
        this.lines= new ArrayList<>();
        
    }
////v0.4 PRANAV-- params update--------------------------------------------
    /**
     *
     * @param lst the list of words in the line to make an initial one line cluster
     * @param i the query_id of that line
     */
    Cluster(String soln_id, List<String> lst, String i) {
        this();
        numOfLines=1;
        this.wordList = new ArrayList<>(lst);
        this.soln_id=soln_id;
        lines.add(lst);
        for (int j = 0; j < lst.size(); j++) {
            membershipValue.add(1.0);
        }
        query_id.add(i);
    }

    public void display() {
        System.out.println("Elements: " + this.query_id);
//        System.out.println("Words: "+this.wordList);
        System.out.println("u: " + this.membershipValue);
        System.out.println("Words: " + this.wordList);
        //      System.out.println("Lower Bound: "+this.lower_bound);
        System.out.println("\n");
    }
}
