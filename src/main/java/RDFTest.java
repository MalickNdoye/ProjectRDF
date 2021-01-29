import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.impl.JenaParameters;
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
        //LggGraphs lggGraphs = new RDFModelFactory().load("/home/noctis/Projet 3A/Ressource/Examples/G1.n3",
        //                                               "/home/noctis/Projet 3A/Ressource/Examples/G2.n3");
        //DictionaryNode dico = DictionaryNode.getInstance();
        //Model prod = lggGraphs.productGraph();
        //System.out.println(prod);
        //System.out.println(lggGraphs);
        //lggGraphs.writelgg();

        System.out.println(System.getProperty("user.home"));
        //Model model = ModelFactory.createDefaultModel() ;
        //model.read("/home/noctis/Projet 3A/Ressource/Examples/G2.n3") ;

        Model graph = RDFDataMgr.loadModel(
                System.getProperty("user.home")+"/Projet 3A/Ressource/Examples/G2_syntax.n3",
                Lang.N3);
        StmtIterator it = graph.listStatements();
        while(it.hasNext()){
            Statement st = it.next();
            RDFNode sub = st.getSubject();
            RDFNode obj = st.getObject();
            if (sub.isAnon() || obj.isAnon()){
                System.out.println(st);
                /*
                if (sub.isAnon()){
                    System.out.println(sub.asNode().getBlankNodeId().getLabelString());
                }else{
                    System.out.println(obj.asNode().getBlankNodeId().getLabelString());
                }*/
            }
        }
        System.out.println(graph);
    }
}
