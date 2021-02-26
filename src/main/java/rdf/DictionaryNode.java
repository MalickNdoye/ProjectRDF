package rdf;

import rdfio.CSVFileIO;
import tools.DefaultParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DictionaryNode est la classe qui gère le dictionnaire de nœuds anonymes et URI.
 * C'est une classe singleton.
 * @version 1.0.0
 */
public class DictionaryNode {
    /**
     * Chemin relatif ou absolu vers le fichier dictionnaire.
     */
    private static String dictionaryPath = "unknown";
    /**
     * Collection qui indexe les URI et nœuds anonymes.
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

    /**
     * Configure le chemin vers le fichier du dictionnaire.
     * @param dictionaryPath path of the dictionary
     */
    public void setDictionaryPath(String dictionaryPath) {
        DictionaryNode.dictionaryPath = dictionaryPath ;
    }

    /**
     *  Singleton Holder.
     */
    private static class DictionaryNodeHolder {
        /** Instance unique non pré-initialisée. */
        private final static DictionaryNode instance = new DictionaryNode();
    }

    /** Point d'accès pour l'instance unique du singleton. */
    public static DictionaryNode getInstance() {
        return DictionaryNodeHolder.instance;
    }

    /**
     * Retourne l'index d'un URI ou nœud anonyme.
     * @param uri désigne l'URI ou le nœud anonyme.
     * @return Index de l'URI.
     */
    public Integer get(String uri) {
        return dictionaryBN.get(uri);
    }

    /**
     * Retourne l'ensemble des URI et nœuds anonymes.
     * @return Ensemble d'URI et nœuds anonymes.
     */
    public Set<String> keySet() {
        return dictionaryBN.keySet();
    }

    /**
     * Sauvegarde la collection dans le fichier situé à dictionaryPath.
     */
    public void save() {
        CSVFileIO csvFileIO = new CSVFileIO();
        csvFileIO.save(this.getDictionaryPath());
    }

    /**
     * Retourne l'objet unique DictionaryNode.
     * @param filepath Chemin vers le fichier du dictionnaire.
     * @return objet DictionaryNode.
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
     * Met à jour le dictionnaire en ajoutant l'URI ou le nœud anonyme s'il n'est pas
     * encore présent dans le dictionnaire.
     * @param uri URI ou nœud anonyme.
     */
    public synchronized void update(String uri){
        if(dictionaryBN == null) dictionaryBN = new HashMap<>();
        if (!dictionaryBN.containsKey(uri)) {
            dictionaryBN.put(uri, dictionaryBN.size()+1);
        }
    }


}
