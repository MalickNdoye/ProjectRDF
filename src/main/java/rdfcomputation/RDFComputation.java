package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * RDFComputation est la classe qui généralise toutes les classes de traitement et calcul.
 *
 * <p>
 *  Les attributs nécessaires aux traitements sont :
 * <ul>
 * <li>Deux graphes RDF.</li>
 * <li>Ensembles regroupant les entêtes de requêtes.</li>
 * <li>Ensembles des noeuds anonymes.</li>
 * <li>Ensemble des préfixes d'URI.</li>
 * </ul>
 * </p>
 * @version 1.0.0
 */
public class RDFComputation {
    /**
     * Object Model du graphe 1.
     */
    protected Model query1;
    /**
     * Object Model du graphe 2.
     */
    protected Model query2;
    /**
     * Object Model du graphe des points communs.
     */
    protected Model resultProd;
    /**
     * Liste des entêtes de requêtes du graphe 1.
     */
    protected ArrayList<String> vars1; // should contain head variables of query1 but query1 is a graph
    /**
     * Liste des entêtes de requêtes du graphe 2.
     */
    protected ArrayList<String> vars2; // should contain head variables of query2 but query2 is a graph
    /**
     * Dictionnaire des noeuds anonymes.
     */
    protected HashMap<String, String> blankNodes;
    /**
     * Dictionnaires des préfixes.
     */
    protected HashMap<String, String> prefixes;


    /**
     * Constructeur par défaut.
     * Elle initialise tous les attributs et les graphes sont vides.
     * @see rdf.RDFModelFactory
     */
    public RDFComputation() {
        query1 = ModelFactory.createDefaultModel();
        query2 = ModelFactory.createDefaultModel();
        resultProd = null;
        vars1 = new ArrayList<>();
        vars2 = new ArrayList<>();
        prefixes = new HashMap<>();
        blankNodes = new HashMap<>();
    }

    /**
     * Determine si un URI n'est pas une entête de requête ou non.
     * @param s URI.
     * @return True si c'est une entête, False sinon.
     */
    public boolean isNotVars(String s) {
        return s.charAt(0) != 'y' && s.charAt(0) != '?';
    }

    /**
     * Retourne la liste des entêtes du graphe 1.
     * @return Liste des entêtes du graphe 1.
     * @see #vars1
     */
    public ArrayList<String> getVars1() {
        return vars1;
    }
    
    /**
     * Retourne la liste des entêtes du graphe 2.
     * @return Liste des entêtes du graphe 2.
     * @see #vars2
     */
    public ArrayList<String> getVars2() {
        return vars2;
    }

    /**
     * Retourne l'objet Model du graphe 1.
     * @return Objet Model du graphe 1.
     * @see #query1
     */
    public Model getQuery1() {
        return query1;
    }

    /**
     * Retourne l'objet Model du graphe 2.
     * @return Objet Model du graphe 2.
     * @see #query2
     */
    public Model getQuery2() {
        return query2;
    }

    /**
     * Retourne le dictionnaire des préfixes.
     * @return Dictionnaire des préfixes.
     * @see #prefixes
     */
    public HashMap<String, String> getPrefixes() {
        return prefixes;
    }

    /**
     * Retourne le dictionnaire des noeuds anonymes.
     * @return Dictionnaire des noeuds anonymes.
     * @see #blankNodes
     */
    public HashMap<String, String> getBlankNodes() {
        return blankNodes;
    }

    /**
     * @return Renvoie le graphe contenant les résultats de traitement
     * @see #resultProd
     */
    public Model getResultProd(){ return resultProd; }

    public void writeLGG() {}

}
