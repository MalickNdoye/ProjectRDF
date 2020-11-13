package rdf;

import rdfio.CSVFileIO;
import tools.DefaultParameter;
import java.util.Map;
import java.util.Set;

public class DictionaryNode {
    private static String dictionaryPath = "unkown";
    private final Map<String,Integer> dictionaryBN ;
    /*
    private Map<String, String> prefixs;
    private Map<String, String> blanknodes;
    private List<String> vars1;
    private List<String> vars2;
    */


    /** Constructeur privé */
    private DictionaryNode() {
        if (dictionaryPath.equals("unknown")){
            dictionaryPath = DefaultParameter.dictionaryPath ;
        }
        CSVFileIO csvIO = new CSVFileIO(dictionaryPath) ;
        dictionaryBN = csvIO.load();
    }

    public Integer get(String toString) {
        return dictionaryBN.get(toString);
    }

    public Set<String> keySet() {
        return dictionaryBN.keySet();
    }

    public void save() {
        CSVFileIO csvFileIO = new CSVFileIO();
        csvFileIO.save(this.getDictionaryPath());
    }

    /** Holder */
    private static class DictionaryNodeHolder {
        /** Instance unique non préinitialisée */
        private final static DictionaryNode instance = new DictionaryNode();
    }

    /** Point d'accès pour l'instance unique du singleton */
    public static DictionaryNode getInstance() {
        return DictionaryNodeHolder.instance;
    }

    public static DictionaryNode getInstance(String filepath){
        DictionaryNode.dictionaryPath = filepath ;
        return DictionaryNodeHolder.instance;
    }

    public String getDictionaryPath(){
        return DictionaryNode.dictionaryPath ;
    }

    public synchronized Boolean update(String uri){
        if (!dictionaryBN.containsKey(uri)) {
            dictionaryBN.put(uri, dictionaryBN.size()+1);
            return true;
        }
        return false ;
    }


}
