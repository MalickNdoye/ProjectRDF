package tools;

import org.apache.jena.rdf.model.*;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;

public class RDFTest {
    public static void main(String[] args) {
        RDFModelFactory RDFModelFactory = new RDFModelFactory("/home/tndoye/ProjetRDF/Ressource/Examples/G1.n3");
        Model query1 = RDFModelFactory.read();
        Model query2 = RDFModelFactory.read("/home/tndoye/ProjetRDF/Ressource/Examples/G2.n3");
        //Model query2 = ModelFactory.createDefaultModel();

//         list the statements in the Model
        //StmtIterator iter = query1.listStatements();


//// print out the predicate, subject and object of each statement
//        while (iter.hasNext()) {
//            Statement stmt      = iter.nextStatement();  // get next statement
//            Resource subject   = stmt.getSubject();     // get the subject
//            Property  predicate = stmt.getPredicate();   // get the predicate
//            RDFNode   object    = stmt.getObject();      // get the object
//
//            String objectType ;
//
////            System.out.print(" " + predicate.toString() + " ");
//            if (object instanceof Resource) {
//                objectType = object.toString();
//            } else {
//                // object is a literal
//                objectType = " \"" + object.toString() + "\"";
//            }
//            String ligne = String.format("<%s> <%s> %s",
//                    subject.toString(),
//                    predicate.toString(),
//                    objectType);
//            System.out.print(ligne);
//
//            System.out.println(" .");
//            //i=1;
//        }
        LggGraphs lggGraphs = new LggGraphs(query1,query2);
        System.out.println(lggGraphs);
//        //lggGraphs.writelgg();
    }
}
