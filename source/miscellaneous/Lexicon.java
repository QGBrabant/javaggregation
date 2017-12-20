/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.quote;

/**
 * A tool for parsing lines and making lists (not good).
 * @author qgbrabant
 */
public class Lexicon<T> extends HashMap<String,T>{
    private Set<String> separators;
    private String regexSeperator;
    
    public Lexicon(){
        super();
        this.separators = new HashSet<>();
        this.regexSeperator = "";
    }
    
    public void addSeparator(String s){
        String sep = quote(s);
        if(separators.add(sep)){
            if(this.separators.size()==1){
                this.regexSeperator = sep;
            }else{
                this.regexSeperator +="|"+sep;
            }
        }
    }
    
    public List<T> lineToList(String line) throws NotInLexiconException{
        String[] tokens = line.split(this.regexSeperator);
        List<T> l= new ArrayList<>();
        T object;
        for(String s : tokens){
            object = this.get(s);
            if(object == null){
                throw new NotInLexiconException();
            }else{
                l.add(object);
            }
        }
        return l;
    }

}
