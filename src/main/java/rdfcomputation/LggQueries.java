package rdfcomputation;

import org.apache.jena.rdf.model.*;

/**
 * LggQueries est la classe qui effectue le calcul de LGG sur les rêquetes.
 *
 * @see RDFComputation
 * @version 1.0.0
 */
public class LggQueries extends RDFComputation{

    /**
     * @see RDFComputation#RDFComputation()
     */
    public LggQueries(){
        super();
    }


    /**
     * @see RDFComputation#RDFComputation(Model, Model)
     */
    public LggQueries(Model query1,Model query2){
        super(query1, query2);
    }

    /**
     * @param dictionaryname Chemin vers le fichier du dictionnaire
     * @return Objet Model
     * @see RDFComputation#productGraph()
     */
    public Model productGraph(String dictionaryname) {
        return null;
    }


}
