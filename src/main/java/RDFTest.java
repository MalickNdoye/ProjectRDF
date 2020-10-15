import org.apache.jena.rdf.model.Model;
import rdfio.RDFGraphIO;

public class RDFTest {
    public static void main(String[] args) {
        RDFGraphIO rdfio = new RDFGraphIO("/home/tndoye/ProjetRDF/G1.n3");
        Model model = rdfio.read();
        System.out.println(model);
    }
}
