package rdf;

import org.apache.jena.rdf.model.Model;
import rdfio.CSVFileIO;
import rdfio.RDFGraphIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryNode {
    private Map<String,String> dictionaryBN ;
    /*
    private Map<String, String> prefixs;
    private Map<String, String> blanknodes;
    private List<String> vars1;
    private List<String> vars2;
    */



    public DictionaryNode(String filename){
        CSVFileIO csvIO = new CSVFileIO(filename) ;
        dictionaryBN = csvIO.load();
        /*
        prefixs = new HashMap<String, String>();
        blanknodes = new HashMap<String, String>() ;
        vars1 = new ArrayList<String>();
        vars2 = new ArrayList<String>();
        */

    }



}
