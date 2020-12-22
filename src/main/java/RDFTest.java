import org.apache.jena.rdf.model.Model;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;


/**
 * RDFTest est la classe de test des objets de l'application.
 *
 * @author tndoye
 * @version 1.0.0
 */
public class RDFTest {
    public static void main(String[] args) {
        LggGraphs lggGraphs = new RDFModelFactory().loadlgg("/home/noctis/Projet 3A/Ressource/Examples/G1.n3",
                                                        "/home/noctis/Projet 3A/Ressource/Examples/G2.n3");
        //DictionaryNode dico = DictionaryNode.getInstance();
        Model prod = lggGraphs.productGraph();
        System.out.println(prod);
        System.out.println(lggGraphs);
//        //lggGraphs.writelgg();
    }
}
