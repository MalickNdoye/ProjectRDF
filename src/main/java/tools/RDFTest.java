package tools;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;

public class RDFTest {
    public static void main(String[] args) {
        RDFModelFactory RDFModelFactory = new RDFModelFactory("/home/tndoye/ProjetRDF/Ressource/Examples/G7.n3");
        Model query1 = RDFModelFactory.read();
        Model query2 = RDFModelFactory.read("/home/tndoye/ProjetRDF/Ressource/Examples/G2.n3");
        //Model query2 = ModelFactory.createDefaultModel();

        LggGraphs lggGraphs = new LggGraphs(query1,query2);
        System.out.println(lggGraphs);
        //lggGraphs.writelgg();
    }
}
