package rdf;

import rdfio.CSVFileIO;
import tools.DefaultParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DictinaryNode est la classe qui gère le dictionnaire de noeuds anonymes et URI.
 * C'est une classe singleton.
 *
 * @version 1.0.0
 */
public class DictionaryNode {
    /**
     * Chemin relatif ou absolu vers le fichier dictionnaire.
     */
    private static String dictionaryPath = "unkown";
    /**
     * Collection qui indexe les URI et noeuds anonymes.
     */
    private Map<String,Integer> dictionaryBN ;



    /**
     * Constructeur privé.
     */
    private DictionaryNode() {
        if (dictionaryPath.equals("unknown")){
            dictionaryPath = DefaultParameter.dictionaryPath ;
        }
        CSVFileIO csvIO = new CSVFileIO(dictionaryPath) ;
        dictionaryBN = csvIO.load();
    }

    public int size() {
        return dictionaryBN.size() ;
    }

    /**
     *  Singleton Holder.
     */
    private static class DictionaryNodeHolder {
        /** Instance unique non préinitialisée. */
        private final static DictionaryNode instance = new DictionaryNode();
    }

    /** Point d'accès pour l'instance unique du singleton. */
    public static DictionaryNode getInstance() {
        return DictionaryNodeHolder.instance;
    }

    /**
     * Retourne l'index d'un URI ou noeud anonyme.
     * @param uri désigne l'URI ou le noeud noeud anonyme.
     * @return Index de l'URI.
     */
    public Integer get(String uri) {
        return dictionaryBN.get(uri);
    }

    /**
     * Retourne l'ensemble des URI et noeuds anomymes.
     * @return Ensemble d'URI et noeuds anonymes.
     */
    public Set<String> keySet() {
        return dictionaryBN.keySet();
    }

    /**
     * Sauvegarde la collection dans le fichier de dictionaryPath.
     */
    public void save() {
        CSVFileIO csvFileIO = new CSVFileIO();
        csvFileIO.save(this.getDictionaryPath());
    }

    /**
     * Retourne l'objet unique DictionaryNode.
     * @param filepath Chemin vers le fichier du dictionnaire.
     * @return objet DictionnaryNode.
     */
    public static DictionaryNode getInstance(String filepath){
        DictionaryNode.dictionaryPath = filepath ;
        return DictionaryNodeHolder.instance;
    }

    /**
     * Retourne le chemin vers le fichier du dictionnaire.
     * @return Chemin vers le fichier du dictionnaire.
     */
    public String getDictionaryPath(){
        return DictionaryNode.dictionaryPath ;
    }

    /**
     * Met à jour le dictionaire en ajoutant l'URI ou le noeud anonyme s'il n'est pas
     * encore présent dans le dictionaire.
     * @param uri URI ou noeud anonyme.
     * @return True si le nouvel ajout est fait et False sinon.
     */
    public synchronized Boolean update(String uri){
        if(dictionaryBN == null) dictionaryBN = new HashMap<>();
        if (!dictionaryBN.containsKey(uri)) {
            dictionaryBN.put(uri, dictionaryBN.size()+1);
            return true;
        }
        return false ;
    }


}
