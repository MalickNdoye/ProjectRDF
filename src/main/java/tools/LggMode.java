package tools;

/**
 * LggMode est un type énuméré qui définie le mode d'exécution de l'application.
 *
 * @author tndoye
 * @version 1.0.0
 */
public enum LggMode {
    /**
     * Calcul de LGG à partir de requêtes.
     */
    LGG_QUERY_MODE ,
    /**
     * Calcul de LGG à partir de graphes RDF.
     */
    LGG_GRAPH_MODE ,
    /**
     * Erreur sur les paramètres d'entrées.
     */
    INPUT_ERROR,
    /**
     * Mode par défaut.
     */
    DEFAULT

}
